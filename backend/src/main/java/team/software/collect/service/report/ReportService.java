package team.software.collect.service.report;

import com.github.pagehelper.PageInfo;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportVO;

import java.util.List;

public interface ReportService {
    // 发布任务
    ResultVO<ReportVO> postReport(ReportVO reportVO);
    //发包方获取测试报告
    List<ReportVO> getReportsByTid(Integer userId, Integer taskId);
    //绘制协作关系图用的，查看一个用户对一个任务的所有报告
    List<ReportVO> getReportsByTidAndUid(Integer uid, Integer tid);
    //发布协同任务
    ResultVO<ReportVO> postCollaborativeReport(Integer parentId, ReportVO reportVO);
    //得到所有父报告
    List<ReportVO> getParentReports(Integer rid);
    //得到report的详细信息
    ReportVO getReportDetails(Integer rid, Integer uid);

    PageInfo<ReportVO> getSimilarReports(Integer currPage, Integer pageSize, Integer  uid, Integer rid);

    List<List<ReportVO>> getClusters(Integer tid);

    PageInfo<ReportVO> getLowQualityReports(Integer crrPage,Integer pageSize,Integer tid);
}
