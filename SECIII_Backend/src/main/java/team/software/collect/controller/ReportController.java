package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import team.software.collect.po.report.ReportInfo;
import team.software.collect.service.report.ReportService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportInfoVO;

import javax.annotation.Resource;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.controller
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-14:48
 */

@Api(value = "报告controller", tags = {"发布报告", "下载报告"})
@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    private ReportService reportService;

    @PostMapping("/post_report/{uid&tid}")
    @ApiOperation(value = "众包工人：上传测试报告", tags = {"<tag03>--上传测试报告"}, notes = "post请求")
    public ResultVO<ReportInfoVO> postReport(@RequestBody ReportInfoVO reportInfoVO) {
        return reportService.postReport(reportInfoVO);
    }

    @GetMapping("/get_report/{rid}")
    @ApiOperation(value = "众包工人、发包方：查看测试报告", tags = {"<tag04>--获取测试报告"}, notes = "get请求")
    public ResultVO<ReportInfoVO> getReport(@RequestBody Integer rid) {
        return reportService.getReport(rid);
    }
}
