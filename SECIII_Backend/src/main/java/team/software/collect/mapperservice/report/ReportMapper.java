package team.software.collect.mapperservice.report;
import team.software.collect.po.report.ReportInfo;

import java.util.List;
/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.mapperservice.report
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-15:57
 */


public interface ReportMapper {

    int insert(ReportInfo record);

    ReportInfo selectByPrimaryKey(Integer rid);

    List<ReportInfo> selectByUid(Integer uid);

//    int updateByPrimaryKey(ReportInfo record);
}