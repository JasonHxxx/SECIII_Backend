package team.software.collect.service.task;

import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TaskService {
    // 发包方查看已发布任务
    List<TaskVO> getPostedTasks(Integer uid);
    // 发布任务
    ResultVO<TaskVO> postTask(TaskVO taskVO);
    //根据page获取大厅任务信息
    PageInfo<TaskVO> getHallTasks(Integer currPage, Integer pageSize);
    //系统用户获取所有任务
    PageInfo<TaskVO> getAllTasks(Integer currPage, Integer pageSize);
    // 查询单门课程
    TaskVO getTaskDetail(Integer taskId, Integer uid);
}
