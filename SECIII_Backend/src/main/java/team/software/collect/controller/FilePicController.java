package team.software.collect.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.software.collect.service.filepicture.FilePictureService;

import javax.annotation.Resource;

@Api(value="文件、图片controller", tags = "5.文件、图片接口")
@RestController
@RequestMapping("/filepic")
public class FilePicController {
    @Resource
    private FilePictureService filePictureService;
}
