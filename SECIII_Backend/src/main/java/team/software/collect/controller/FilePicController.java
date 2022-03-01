package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.service.file.FileService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.file.FileInfoVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Api(value="文件、图片controller", tags = "5.文件、图片接口")
@RestController
@RequestMapping("/filepic")
public class FilePicController {
    @Resource
    FileService fileService;

    @ApiOperation(value="√ 用户：上传文件、照片", notes = "发包方传文件：需要uid和taskId；众包工人传图片：需要uid和reportId 需要前端先创建任务再发相应的文件请求、先创建测试报告再发送相应的传图片请求")
    @PostMapping("/upload")
    public ResultVO<FileInfoVO> upload(@RequestParam("file") MultipartFile file, @RequestParam Integer role, @RequestParam Integer taskOrReportId) {//role=1对应于发包方发布的任务的文件，role=2对应于众包工人上传的报告的图片，如果截图需要划分类型，后续可以添加一个int变量用于标记类型
        return fileService.uploadFile(file, role, taskOrReportId);
    }

    //todo 测试报告的中图片的查看，权限认证在report里做    任务的文件下载权限认证在这里做
    @ApiOperation(value="√ 用户：下载文件、照片", notes = "传文件时，后端已经将真实的文件名返回到前端")
    @GetMapping("/download/{fileName:.+}")//notes：路径变量的@PathVariable后面变量名称要一样
    public void download(@PathVariable String fileName, HttpServletResponse response, @RequestParam Integer uid) {//uid留作权限认证
        //另外一种：public void download(@PathVariable String fileName, HttpServletResponse response, @RequestParam Integer role, @RequestParam Integer taskOrReportId, @RequestParam Integer uid)
        //但是图片里已经包含了role和taskOrReportId信息，可以直接解析
        fileService.downloadFile(fileName, response, uid);
    }
}
