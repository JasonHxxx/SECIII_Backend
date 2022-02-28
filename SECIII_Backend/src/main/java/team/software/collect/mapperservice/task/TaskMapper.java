package team.software.collect.mapperservice.task;

import team.software.collect.po.task.Task;

import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer tid);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer tid);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    List<Task> selectByUid(Integer uid);

    List<Task> selectAll();

}
