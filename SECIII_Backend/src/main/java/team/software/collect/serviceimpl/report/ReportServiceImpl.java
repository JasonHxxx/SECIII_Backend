package team.software.collect.serviceimpl.report;

import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.po.report.Report;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.service.report.ReportService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;

    @Override
    public ResultVO<ReportVO> postReport(ReportVO reportVO) {
        List<TaskOrder> taskOrderList=taskOrderMapper.selectByUid(reportVO.getUid());
        TaskOrder newTaskOrder = null;
        for(TaskOrder taskOrder : taskOrderList){
            if(taskOrder.getTid().equals(reportVO.getTid()) && taskOrder.getFinished().equals(1))
                return new ResultVO<>(Constant.REQUEST_FAIL, "已经上传过测试报告！");
            else
                newTaskOrder=taskOrder;
        }

        //...增加上传时间字段
        Report report=new Report(reportVO);
        if(reportMapper.insert(report)!=1){
            return new ResultVO<>(Constant.REQUEST_FAIL, "数据库插入错误！");
        }
        newTaskOrder.setFinished(1);
        taskOrderMapper.updateByPrimaryKey(newTaskOrder);
        return new ResultVO<>(Constant.REQUEST_FAIL, "报告上传成功！", new ReportVO(report));
    }

    @Override
    public List<ReportVO> getReportsByTid(Integer userId, Integer taskId) {
        List<Report> reportList=reportMapper.selectByTid(taskId);
        List<ReportVO> reportVOS=new ArrayList<>();
        for(int i=0;i<reportList.size();i++)
            reportVOS.add(new ReportVO(reportList.get(i)));
        return reportVOS;
    }
}
