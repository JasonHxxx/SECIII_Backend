package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import team.software.collect.po.report.Report;
import team.software.collect.service.report.ReportService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportVO;
import team.software.collect.vo.task.TaskVO;

import javax.annotation.Resource;
import java.util.List;

@Api(value="测试报告controller", tags = "4.测试报告接口")
@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    private ReportService reportService;

    @PostMapping("/uploadTest")
    @ApiOperation(value="众包工人：上传测试报告")
    public ResultVO<ReportVO> postReport(@RequestBody ReportVO reportVO) {
        return reportService.postReport(reportVO);
    }

    //todo
    @GetMapping("/{uid}")
    @ApiOperation(value="发包方：获取已发布任务的测试报告",tags={"<tag02>--获取测试报告列表"})
    public List<ReportVO> getReportsByTid(@PathVariable Integer uid, @RequestParam Integer tid) {
        return reportService.getReportsByTid(uid,tid);
    }
}
