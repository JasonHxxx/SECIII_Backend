package team.software.collect.controller;

import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import team.software.collect.service.report.CommentService;
import team.software.collect.service.report.ReportService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.CommentVO;
import team.software.collect.vo.report.ReportVO;

import javax.annotation.Resource;
import java.util.List;

@Api(value="测试报告controller", tags = "4.测试报告接口")
@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    private ReportService reportService;
    @Resource
    private CommentService commentService;

    @ApiOperationSupport(order = 1)
    @PostMapping("/uploadTest")
    @ApiOperation(value="众包工人：上传测试报告",notes = "ReportVO里的parentId要设置为-1")
    public ResultVO<ReportVO> postReport(@RequestBody ReportVO reportVO) {
        return reportService.postReport(reportVO);
    }

    @ApiOperationSupport(order = 2)
    @GetMapping("/{uid}")
    @ApiOperation(value="发包方：获取已发布任务的测试报告")
    public List<ReportVO> getReportsByTid(@PathVariable Integer uid, @RequestParam Integer tid) {
        return reportService.getReportsByTid(uid,tid);
    }

    @ApiOperationSupport(order = 3)
    @GetMapping("/userReport/{uid}")
    @ApiOperation(value="获取一个用户对一个任务发布的所有测试报告，包括协作报告")
    public List<ReportVO> getReportsByTidAndUid(@PathVariable Integer uid, @RequestParam Integer tid) {
        return reportService.getReportsByTidAndUid(uid,tid);
    }

    @GetMapping("/similarReports/{page}")
    @ApiOperation(value="众包工人：获取相似的报告（提交报告时调用，可以引导众包工人进行协作）；不感兴趣换一批只要更改页码即可")
    @ApiImplicitParam(name = "page", value = "页码", paramType = "path", required = true)
    public PageInfo<ReportVO> getSimilarReports(@PathVariable Integer page, @RequestParam Integer uid, @RequestParam Integer rid) {
        return reportService.getSimilarReports(page,Constant.PAGE_SIZE1,uid,rid);
    }

    @ApiOperationSupport(order = 3)
    @GetMapping("/reportDetail/{rid}")
    @ApiOperation(value="获取测试报告详细信息，包括测试报告的内容、别人的评价")
    public ReportVO getReportByRid(@RequestParam(required = false, defaultValue = "") Integer uid, @PathVariable Integer rid) {
        return reportService.getReportDetails(rid,uid);
    }

    @PostMapping("/uploadCollaborativeTest")
    @ApiOperation(value="众包工人：上传协作测试报告", notes = "reportVO里也写一下父报告id")
    public ResultVO<ReportVO> postCollaborativeReport(@RequestParam Integer parentId, @RequestBody ReportVO reportVO) {
        return reportService.postCollaborativeReport(parentId,reportVO);
    }

    @GetMapping("/parentReport/{rid}")
    @ApiOperation(value="获取一个报告的父报告，直到某个报告没有父报告，第一个是原报告")
    public List<ReportVO> getParentReports(@PathVariable Integer rid) {
        return reportService.getParentReports(rid);
    }

    @PostMapping("/uploadComment")
    @ApiOperation(value="众包工人：评价测试报告")
    public ResultVO<CommentVO> postComment(@RequestBody CommentVO commentVO) {
        return commentService.postComment(commentVO);
    }

    @GetMapping("/getCommentsByRid/{page}")
    @ApiOperation(value="获取测试报告对应的评价、分页展示；之前有一个没有分页、待更改")
    @ApiImplicitParam(name = "page", value = "页码", paramType = "path", required = true)
    public PageInfo<CommentVO> getCommentsByRid(@PathVariable Integer page, @RequestParam Integer rid) {
        return commentService.getCommentsByRid(page, Constant.PAGE_SIZE1,rid);
    }

    @GetMapping("/similarRelationship")
    @ApiOperation(value="获取聚簇得到的相似关系")
    public List<List<ReportVO>> getSimilarRelationship(@RequestParam Integer tid) {
        return reportService.getClusters(tid);
    }

    @GetMapping("/lowQualityReport/{page}")
    @ApiOperation(value="获取低质量报告")
    public PageInfo<ReportVO> getLowQualityReports(@RequestParam Integer tid,@PathVariable Integer page) {
        return reportService.getLowQualityReports(page,Constant.PAGE_SIZE1,tid);
    }
}
