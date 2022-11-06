package team.software.collect.mapperservice.task;

import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;

import java.util.List;

public interface TaskOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskOrder record);

    int insertSelective(TaskOrder record);

    TaskOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskOrder record);

    int updateByPrimaryKey(TaskOrder record);

    List<TaskOrder> selectTasksByUserId(Integer uid);

    List<TaskOrder> selectByUid(Integer uid);

    List<TaskOrder> selectByTid(Integer tid);
}
