package team.software.collect.serviceimpl.taskorder;


import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.service.taskorder.TaskOrderService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskOrderServiceImpl implements TaskOrderService {
    @Resource
    TaskOrderMapper taskOrderMapper;

    //todo 将对应课程的已招募人数+1
    @Override
    public ResultVO<TaskOrderVO> confirmTask(Integer userId, Integer taskId) {
        return null;
    }

    @Override
    public List<TaskVO> getFinishedTasks(Integer uid) {
        return null;
    }
}
