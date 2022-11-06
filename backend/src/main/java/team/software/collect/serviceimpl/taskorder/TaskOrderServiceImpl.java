package team.software.collect.serviceimpl.taskorder;


import org.springframework.stereotype.Service;
import team.software.collect.enums.TestType;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.mapperservice.user.UserInfoMapper;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.po.user.Portrait;
import team.software.collect.po.user.UserInfo;
import team.software.collect.service.taskorder.TaskOrderService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;
import team.software.collect.vo.user.UserInfoVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskOrderServiceImpl implements TaskOrderService {
    @Resource
    TaskOrderMapper taskOrderMapper;
    @Resource//每一个Mapper对应一个，不然无法注入
    TaskMapper taskMapper;
    @Resource
    PortraitMapper portraitMapper;
    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public ResultVO<TaskOrderVO> confirmTask(Integer userId, Integer taskId) {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectTasksByUserId(userId);

        for(TaskOrder taskOrder: taskOrderList) {
            if (taskOrder.getTid().equals(taskId)) {//已经接受过该任务
                return new ResultVO<>(Constant.REQUEST_FAIL, "已接受该任务");
            }
        }

        Task taskFromDB = taskMapper.selectByPrimaryKey(taskId);

//        Task taskFromDB=taskMapper.selectByPrimaryKey(taskId);
        if(taskFromDB.getMaxWorkers().equals(taskFromDB.getWorkerCnt())){
            return new ResultVO<>(Constant.REQUEST_FAIL, "该任务招募人数已达上限！");
        }
        //if(new Date()>taskFromDB.getEndTime())     //可以判断任务是否已经过期

        TaskOrder taskOrder=new TaskOrder(userId,taskId);
        if(taskOrderMapper.insert(taskOrder) != 1){
            return new ResultVO<>(Constant.REQUEST_FAIL,"接受任务失败，数据库错误！");
        }
        // 更新已招募人数
        taskFromDB.setWorkerCnt(taskFromDB.getWorkerCnt()+1);
        taskMapper.updateByPrimaryKey(taskFromDB);
        //更新用户画像：偏爱的任务测试类型
        updatePrefer(userId,taskId);
        //更新活跃度，每次接受任务加0.3
        Portrait portrait=portraitMapper.selectByUid(userId);
        BigDecimal preNum=portrait.getActivity();
        BigDecimal postNum=preNum.add(new BigDecimal(0.3));
        portrait.setActivity(postNum);
        portraitMapper.updateByPrimaryKey(portrait);
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"接受任务成功",new TaskOrderVO(taskOrder));
    }

    @Override
    public List<TaskVO> getUnfinishedTasks(Integer uid) {
        List<TaskOrder> taskOrderList=taskOrderMapper.selectTasksByUserId(uid);
        List<TaskVO> unFinishedTasks=new ArrayList<>();
        for(TaskOrder taskOrder: taskOrderList) {
            if (taskOrder.getFinished()!=1) {//未经完成该任务
                Task task=taskMapper.selectByPrimaryKey(taskOrder.getTid());
                unFinishedTasks.add(new TaskVO(task));
            }
        }
        return unFinishedTasks;
    }

    @Override
    public List<TaskVO> getFinishedTasks(Integer uid) {
        //task_order的finished字段表示任务是否完成（报告是否已经上传），后续可添加paid字段，与task的cost有关
        List<TaskOrder> taskOrderList=taskOrderMapper.selectTasksByUserId(uid);
        List<TaskVO> finishedTasks=new ArrayList<>();
        for(TaskOrder taskOrder: taskOrderList) {
            if (taskOrder.getFinished()==1) {//已经完成该任务
                Task task=taskMapper.selectByPrimaryKey(taskOrder.getTid());
                finishedTasks.add(new TaskVO(task));
            }
        }
        return finishedTasks;
    }

    //数据分析时调用，一个格式化的方法，待更新，抽象到另外一个方法里
    public HashMap<TestType, Integer> formatHandlePrefer(Integer uid){
        Portrait portrait=portraitMapper.selectByUid(uid);
        String[] preferStrValues=portrait.getPreference().split(",");//字符串形式的数字
        HashMap<TestType,Integer> re=new HashMap<>();
        HashMap<Integer,TestType> typeTable=new HashMap<>();
        typeTable.put(0,TestType.FunctionTest);
        typeTable.put(1,TestType.PerformanceTest);
        typeTable.put(2,TestType.StabilityTest);
        for(int i=0;i<preferStrValues.length;i++){
            re.put(typeTable.get(i),Integer.valueOf(preferStrValues[i]));
        }
        return re;
    }

    public HashMap<TestType, Integer> updatePrefer(Integer uid,Integer tid){
        HashMap<TestType, Integer> map=formatHandlePrefer(uid);
        Task task=taskMapper.selectByPrimaryKey(tid);
        for (Map.Entry<TestType, Integer> entry : map.entrySet()) {
            if(entry.getKey().toString().equals(task.getType()))
                map.put(entry.getKey(),entry.getValue()+1);
        }
        String newStr="";
        HashMap<Integer,TestType> typeTable=new HashMap<>();
        typeTable.put(0,TestType.FunctionTest);
        typeTable.put(1,TestType.PerformanceTest);
        typeTable.put(2,TestType.StabilityTest);
        for(int i=0;i<map.size();i++){
            newStr+=map.get(typeTable.get(i))+",";
        }
        newStr=newStr.substring(0,newStr.length()-1);
        Portrait portrait=portraitMapper.selectByUid(uid);
        portrait.setPreference(newStr);
        portraitMapper.updateByPrimaryKey(portrait);
        return map;
    }

    @Override
    public List<UserInfoVO> getUserByTid(Integer tid){
        List<TaskOrder> orders=taskOrderMapper.selectByTid(tid);
        List<UserInfoVO> userInfoVOS=new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            UserInfo userInfo=userInfoMapper.selectByPrimaryKey(orders.get(i).getUid());
            userInfoVOS.add(new UserInfoVO(userInfo));
        }
        return userInfoVOS;
    }
}
