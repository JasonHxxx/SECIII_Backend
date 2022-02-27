package team.software.collect.serviceimpl.report;

import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.po.report.Report;
import team.software.collect.service.report.ReportService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportVO;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper reportMapper;

    @Override
    public ResultVO<ReportVO> postReport(ReportVO reportVO) {
        return null;
    }

    @Override
    public List<ReportVO> getReportsByTid(Integer userId, Integer taskId) {
        return null;
    }
}
