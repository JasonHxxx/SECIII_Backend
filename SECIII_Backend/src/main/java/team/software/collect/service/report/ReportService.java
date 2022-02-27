package team.software.collect.service.report;

import team.software.collect.po.report.Report;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportVO;
import team.software.collect.vo.task.TaskVO;

import java.util.List;

public interface ReportService {
    // 发布任务
    ResultVO<ReportVO> postReport(ReportVO reportVO);
    //发包方获取测试报告
    List<ReportVO> getReportsByTid(Integer userId, Integer taskId);
}
