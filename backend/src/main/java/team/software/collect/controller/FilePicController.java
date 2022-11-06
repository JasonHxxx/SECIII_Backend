package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.service.file.FileService;
import team.software.collect.service.file.OssService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.file.FileInfoVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value="文件、图片controller", tags = "5.文件、图片接口")
@RestController
@RequestMapping("/filepic")
public class FilePicController {
//    @Resource
//    FileService fileService;
//
//    @ApiOperation(value="√ 用户：上传文件、照片", notes = "发包方传文件：需要uid和taskId；众包工人传图片：需要uid和reportId 需要前端先创建任务再发相应的文件请求、先创建测试报告再发送相应的传图片请求")
//    @PostMapping("/upload")
//    public ResultVO<FileInfoVO> upload(@RequestParam("file") MultipartFile file, @RequestParam Integer role, @RequestParam Integer taskOrReportId) {//role=1对应于发包方发布的任务的文件，role=2对应于众包工人上传的报告的图片，如果截图需要划分类型，后续可以添加一个int变量用于标记类型
//        return fileService.uploadFile(file, role, taskOrReportId);
//    }
//
//    //todo 测试报告的中图片的查看，权限认证在report里做    任务的文件下载权限认证在这里做
//    @ApiOperation(value="√ 用户：下载文件、照片", notes = "传文件时，后端已经将真实的文件名返回到前端")
//    @GetMapping("/download/{fileName:.+}")//notes：路径变量的@PathVariable后面变量名称要一样
//    public void download(@PathVariable String fileName, HttpServletResponse response, @RequestParam Integer uid, @RequestParam Integer role, @RequestParam Integer taskOrReportId) {//uid留作权限认证
//        //另外一种：public void download(@PathVariable String fileName, HttpServletResponse response, @RequestParam Integer role, @RequestParam Integer taskOrReportId, @RequestParam Integer uid)
//        //但是图片里已经包含了role和taskOrReportId信息，可以直接解析
//        fileService.downloadFile(fileName, response, uid, role, taskOrReportId);
//    }


    @Resource
    OssService ossService;

    @ApiOperation(value = "Oss文件、图片上传", notes = "发包方上传文件filetype设置为1，id为taskId，同一个任务多个文件则多次调用这个接口；众包工人上传图片，前端filetype设置为2传给后端，id为reportId，同一个报告有多张图片则多次调用这个接口；")
    @PostMapping("/upload")
    public ResultVO<String> upload(@RequestParam("file") MultipartFile file,@RequestParam Integer myFileType, @RequestParam Integer id)  {
        return ossService.upload(file,myFileType,id);
    }

    @ApiOperation(value = "Oss文件、图片下载", notes = "打包很可能出问题，所以分开传——以图片说明，前端先按照rid请求获得一个url的lst，然后对于每个url请求下载图片，展示所有的的图片；发包方上传多个文件也是分开的，前端先通过tid获得一个url的lst，对于每个url请求一次获得一个文件，全部展示")
    @PostMapping("/download/{filename}")
    public void DownLoad(@PathVariable String filename , @RequestParam Integer myFileType, HttpServletResponse response){
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));//这一句需要异常捕获，可以整合到service里
            ossService.downloadOssFile(response.getOutputStream(), filename,myFileType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "Oss文件、图片 资源位置（包括文件名、图片不需要显示名称）", notes = "id是tid或rid，tid时myFileType为1，否则为2")
    @PostMapping("/getURl")
    public List<FilePicture> getUrl(@RequestParam Integer myFileType, @RequestParam Integer id){
        return ossService.getUrl(myFileType,id);
    }

    @ApiOperation(value = "Oss图片删除", notes = "用户上传图片时传错了删除，根据文件名，因为每次上传文件都会返回文件名，把文件名在用户提交报告前暂时按上传顺序存一下")
    @PostMapping("/delete/{filename}")
    @ResponseBody
    public ResultVO<String> delete(@PathVariable String filename) {
        return ossService.deleteFile(filename);
    }

}
