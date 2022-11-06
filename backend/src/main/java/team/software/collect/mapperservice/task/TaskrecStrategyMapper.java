package team.software.collect.mapperservice.task;

import team.software.collect.po.task.TaskrecStrategy;

import java.util.List;

public interface TaskrecStrategyMapper {
    int deleteByPrimaryKey(Integer sid);

    int insert(TaskrecStrategy record);

    int insertSelective(TaskrecStrategy record);

    TaskrecStrategy selectByPrimaryKey(Integer sid);

    int updateByPrimaryKeySelective(TaskrecStrategy record);

    int updateByPrimaryKey(TaskrecStrategy record);

    List<TaskrecStrategy> selectAll();
}
