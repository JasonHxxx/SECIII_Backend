package team.software.collect.service.taskorder;

import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;

import java.util.List;

public interface TaskOrderService {
    ResultVO<TaskOrderVO> confirmTask(Integer userId, Integer taskId);
    // 众包工人查看已完成任务
    List<TaskVO> getFinishedTasks(Integer uid);
}
