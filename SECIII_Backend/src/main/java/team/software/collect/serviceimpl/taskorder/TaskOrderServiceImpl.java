package team.software.collect.serviceimpl.taskorder;


import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.service.taskorder.TaskOrderService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskOrderServiceImpl implements TaskOrderService {
    @Resource
    TaskOrderMapper taskOrderMapper;
    @Resource//每一个Mapper对应一个，不然无法注入
    TaskMapper taskMapper;

    //todo 将对应课程的已招募人数+1
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
}
