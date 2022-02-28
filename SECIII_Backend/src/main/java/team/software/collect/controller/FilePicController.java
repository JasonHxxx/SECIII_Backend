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

    @ApiOperation(value="√ 用户：上传文件、照片")
    @PostMapping("/upload")
    public ResultVO<FileInfoVO> upload(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @ApiOperation(value="√ 用户：下载文件、照片")
    @GetMapping("/download/{originName:.+}")
    public void download(@PathVariable String originName, @RequestParam String newName, HttpServletResponse response) {
        fileService.downloadFile(originName, newName, response);
    }
}
