package team.software.collect.mapperservice.user;

import team.software.collect.po.user.Portrait;

import java.util.List;

public interface PortraitMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(Portrait record);

    int insertSelective(Portrait record);

    Portrait selectByPrimaryKey(Integer pid);

    List<Portrait> selectAll();

    Portrait selectByUid(Integer uid);

    int updateByPrimaryKeySelective(Portrait record);

    int updateByPrimaryKey(Portrait record);
}
