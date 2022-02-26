package team.software.collect.serviceimpl.task;

import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.po.task.Task;
import team.software.collect.service.task.TaskService;
import team.software.collect.util.Constant;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceIml implements TaskService {
    @Resource
    private TaskMapper taskMapper;

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
        if (taskMapper.insert(task) > 0) {
            return new ResultVO<TaskVO>(Constant.REQUEST_SUCCESS, "任务发布成功。", new TaskVO(task));
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "服务器错误");
    }

    @Override
    public PageInfo<TaskVO> getHallTasks(Integer currPage, Integer pageSize) {
        if (currPage == null || currPage < 1) currPage = 1;
        PageHelper.startPage(currPage, pageSize);
        PageInfo<Task> po = new PageInfo<>(taskMapper.selectAll());
        PageInfo<TaskVO> tasks = PageInfoUtil.convert(po, TaskVO.class);
        return tasks;
    }

}
