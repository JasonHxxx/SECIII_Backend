package team.software.collect.serviceimpl.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import team.software.collect.CollectApplication;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.mapperservice.task.TaskrecStrategyMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.po.task.TaskrecStrategy;
import team.software.collect.po.user.Portrait;
import team.software.collect.service.task.TaskService;
import team.software.collect.util.Constant;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TaskServiceIml implements TaskService {
    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TaskrecStrategyMapper taskrecStrategyMapper;

    @Resource
    private PortraitMapper portraitMapper;

    @Resource
    private TaskOrderMapper taskOrderMapper;

    // 发包方查看已发布任务
    @Override
    public List<TaskVO> getPostedTasks(Integer uid) {
        List<TaskVO> postedTasks = new ArrayList<>();
        List<Task> tasksList = taskMapper.selectByUid(uid);
        for (Task task : tasksList) {
            postedTasks.add(new TaskVO(task));
        }
        return postedTasks;
    }

    @Override
    public ResultVO<TaskVO> postTask(TaskVO taskVO) {
        for (Task task : taskMapper.selectByUid(taskVO.getUid())) {
            if (task.getName().equals(taskVO.getName()))
                return new ResultVO<>(Constant.REQUEST_FAIL, "已存在同名任务！");
        }
        Task task = new Task(taskVO);
        task.setBeginTime(new Date());
        if (taskMapper.insert(task) > 0) {
            return new ResultVO<TaskVO>(Constant.REQUEST_SUCCESS, "任务发布成功。", new TaskVO(task));
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "服务器错误");
    }

    @Override
    public PageInfo<TaskVO> getHallTasks(Integer currPage, Integer pageSize) {
        if (currPage == null || currPage < 1) currPage = 1;
        PageHelper.startPage(currPage, pageSize);
        List<Task> tasks = taskMapper.selectAll();
        List<Task> tasksRecruiting = new ArrayList<>();
        int len = tasks.size();
        for (int i = 0; i < len; i++) {
            if (!tasks.get(i).getWorkerCnt().equals(tasks.get(i).getMaxWorkers()))
                tasksRecruiting.add(tasks.get(i));
        }
        PageInfo<Task> po = new PageInfo<>(tasksRecruiting);
        PageInfo<TaskVO> pageTasks = PageInfoUtil.convert(po, TaskVO.class);
        return pageTasks;
    }

    /*
    可能需要实现的：权限认证
     */
    @Override
    public TaskVO getTaskDetail(Integer taskId, Integer uid) {
        Task taskFromDB = taskMapper.selectByPrimaryKey(taskId);
        //一般查询结果不会是null
        if (taskFromDB == null) {
            return new TaskVO();
        } else {
            return new TaskVO(taskFromDB);
        }
    }

    @Override
    public PageInfo<TaskVO> getAllTasks(Integer currPage, Integer pageSize) {
        //可以再加一个参数uid验证是否为系统用户
        if (currPage == null || currPage < 1) currPage = 1;
        PageHelper.startPage(currPage, pageSize);
        List<Task> tasks = taskMapper.selectAll();
        PageInfo<Task> po = new PageInfo<>(tasks);
        PageInfo<TaskVO> pageTasks = PageInfoUtil.convert(po, TaskVO.class);
        return pageTasks;
    }

    @Override
    public PageInfo<TaskVO> getRecommendedTasks(Integer currPage, Integer pageSize, Integer uid) {
        // 1.调用推荐算法（按照管理员设定的推荐规则）计算，返回一个pageInfo
        //得到一个recommendedTaskList

        if (currPage == null || currPage < 1) currPage = 1;
        PageHelper.startPage(currPage, pageSize);

        // 1. 首先从 taskrec_strategy 表中获取可行的策略
        List<TaskrecStrategy> taskrecStrategies = taskrecStrategyMapper.selectAll();
        double a = 0.0, b = 0.0, c = 0.0, d = 0.0;
        for (TaskrecStrategy ts : taskrecStrategies) {
            if (ts.getUid() == 1) {
                a = ts.getAbilitypercent().doubleValue();
                b = ts.getActivitypercent().doubleValue();
                c = ts.getPreferpercent().doubleValue();
                d = ts.getDevicepercent().doubleValue();
                break;
            }
        }

        List<TaskOrder> userTasks=taskOrderMapper.selectByUid(uid);
        HashMap<Integer, TaskOrder> tableMap=new HashMap<>();
        for(int i=0;i<userTasks.size();i++){
            tableMap.put(userTasks.get(i).getTid(),userTasks.get(i));
        }
        // 2. 将所有任务挑选出来  过滤已经招募满的用户 过滤用户已经接受过的任务
        List<Task> tasks = new ArrayList<>();
        for (Task tsk : taskMapper.selectAll()) {
            if (!Objects.equals(tsk.getMaxWorkers(), tsk.getWorkerCnt()) && tableMap.get(tsk.getTid())==null) {
                tasks.add(tsk);
            }
        }

        // 3. 从 portrait 表中解析出用户的四个特性
        Portrait portrait = portraitMapper.selectByUid(uid);
        if (portrait == null) {
            portrait = new Portrait(BigDecimal.valueOf(0.25), "1,1,1", BigDecimal.valueOf(0.25), "1,1,1,1,1,1");
        }

        MultiValueMap<Double, Task> mmap = new LinkedMultiValueMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            mmap.add(getScore(tasks.get(i), portrait, a, b, c, d), tasks.get(i));
        }
        List<Double> lst = new ArrayList<>(mmap.keySet());
        Collections.sort(lst, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1>o2 ? -1:1;
            }
        });
        List<Task> recommendedTaskList = new ArrayList<>();
        Iterator<Double> iterator = lst.iterator();
        while ((iterator.hasNext())) {
            Double key = iterator.next();
            List<Task> tsks = mmap.get(key);
            for (Task tsk : tsks) {
                recommendedTaskList.add(tsk);
            }
        }

        List<Task> ansTasks = new ArrayList<>();
        for (int i = (currPage - 1) * pageSize; i < currPage*pageSize && i < recommendedTaskList.size(); i++) {
            ansTasks.add(recommendedTaskList.get(i));
        }
        PageInfo<Task> po = new PageInfo<>(ansTasks);
        PageInfo<TaskVO> pageRecommendedTasks = PageInfoUtil.convert(po, TaskVO.class);
        return pageRecommendedTasks;

    }

    private double getScore(Task task, Portrait portrait, double a, double b, double c, double d) {
        // 计算公式：(1 - |a * (专业能力 - task难度)|) * b * 活跃度  + c * (Nt / Ns) + d * (Mt / Ms)
        HashMap<String, Integer> hsmap1 = new HashMap<>();
        hsmap1.put("FunctionTest", 0);
        hsmap1.put("PerformanceTest", 1);
        hsmap1.put("StabilityTest", 2);
        HashMap<String, Integer> hsmap2 = new HashMap<>();
        hsmap2.put("Android", 0);
        hsmap2.put("Harmony", 1);
        hsmap2.put("IOS", 2);
        hsmap2.put("Linux", 3);
        hsmap2.put("Windows", 4);
        hsmap2.put("MacOs", 5);

        double ans = 0;
        int sum1 = 0;
        int sum2 = 0;
        int[] preferenceFrequency = new int[3];
        int[] deviceFrequency = new int[6];
        String preferences = portrait.getPreference();
        String devices = portrait.getDevice();

        String strs[] = preferences.split(",");
        for (int i = 0; i < 3; i++) {
            preferenceFrequency[i] = Integer.parseInt(strs[i]);
            sum1 += preferenceFrequency[i];
        }
        strs = devices.split(",");
        for (int i = 0; i < 6; i++) {
            deviceFrequency[i] = Integer.parseInt(strs[i]);
            sum2 += deviceFrequency[i];
        }

        //每个部分都是5分为基准，然后进行加权
        ans += a*(5-Math.abs(portrait.getAbility().doubleValue() - task.getDifficulty().doubleValue()));
        //为活跃度高的人推荐截止日期更临近的任务，否则不推荐（某个用户接受几个任务活跃度就挺高了，如果该用户一定时间内未完成任务要进行活跃度惩罚）

        double factor=0;
        if(portrait.getActivity().compareTo(BigDecimal.valueOf(0.5))<0)
            factor=0.1;
        else if(portrait.getActivity().compareTo(BigDecimal.valueOf(1.5))<0)
            factor=0.3;
        else if(portrait.getActivity().compareTo(BigDecimal.valueOf(2))<0)
            factor=0.5;
        else if(portrait.getActivity().compareTo(BigDecimal.valueOf(2.5))<0)
            factor=0.7;
        else
            factor=0.9;
        Date d1=new Date();
        Date d2=task.getEndTime();
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        double activityScore=0;
        if(diffDays<0)
            activityScore=0;
        else if(diffDays<3)
            activityScore=5;
        else if(diffDays<7)
            activityScore=4;
        else if(diffDays<11)
            activityScore=3;
        else if(diffDays<15)
            activityScore=2;
        else
            activityScore=1;

        ans += b * factor*activityScore;
        if(sum1!=0){
            ans += c * preferenceFrequency[hsmap1.get(task.getType())] / sum1*5;
        }
        else {
            ans+=0;
        }
        if(sum2!=0) {
            String[] devs = task.getDevice().split(",");
            for (String dev : devs) {
                ans += d * deviceFrequency[(hsmap2.get(dev))] / sum2 * 5;
            }
        }
        else {
            ans+=0;
        }
        return ans;
    }
}
