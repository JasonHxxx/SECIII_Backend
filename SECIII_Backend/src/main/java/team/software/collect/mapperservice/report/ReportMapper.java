package team.software.collect.mapperservice.report;

import io.swagger.models.auth.In;
import team.software.collect.po.report.Report;

import java.util.List;

public interface ReportMapper {
    int deleteByPrimaryKey(Integer rid);

    int insert(Report record);

    int insertSelective(Report record);

    Report selectByPrimaryKey(Integer rid);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);

    List<Report> selectByTid(Integer tid);

}
