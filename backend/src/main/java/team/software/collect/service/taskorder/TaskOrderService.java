package team.software.collect.service.taskorder;

import org.springframework.web.bind.annotation.PathVariable;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;
import team.software.collect.vo.user.UserInfoVO;

import java.util.List;

public interface TaskOrderService {
    ResultVO<TaskOrderVO> confirmTask(Integer userId, Integer taskId);
    // 众包工人查看正在执行的任务
    List<TaskVO> getUnfinishedTasks(Integer uid);
    // 众包工人查看已完成任务
    List<TaskVO> getFinishedTasks(Integer uid);
    // 查看一个任务的协作关系时，返回所有用户信息
    List<UserInfoVO> getUserByTid(Integer tid);
}
