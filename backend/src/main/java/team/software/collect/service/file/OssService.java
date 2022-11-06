package team.software.collect.service.file;

import org.springframework.web.multipart.MultipartFile;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.vo.ResultVO;

import java.io.OutputStream;
import java.util.List;

public interface OssService {
    //图片下载
    ResultVO<String> upload(MultipartFile file,Integer myFileType,Integer id);
    //图片上传
    void downloadOssFile(OutputStream os, String objectName, Integer myFileType);
    List<FilePicture> getUrl(Integer myFileType, Integer id);
    //图片删除、取消上传
    ResultVO<String> deleteFile(String objectName);
}
