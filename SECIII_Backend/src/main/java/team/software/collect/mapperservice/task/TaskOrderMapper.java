package team.software.collect.mapperservice.task;

import team.software.collect.po.task.TaskOrder;

public interface TaskOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskOrder record);

    int insertSelective(TaskOrder record);

    TaskOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskOrder record);

    int updateByPrimaryKey(TaskOrder record);
}