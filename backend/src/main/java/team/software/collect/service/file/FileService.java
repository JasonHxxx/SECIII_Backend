package team.software.collect.service.file;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.file.FileInfoVO;

import javax.servlet.http.HttpServletResponse;

/*
注：迭代一使用的接口，弃用，使用oss替代
 */
public interface FileService {
    ResultVO<FileInfoVO> uploadFile(MultipartFile file, Integer role, Integer taskOrReportId);
    void downloadFile(String fileName, HttpServletResponse response, Integer uid, @RequestParam Integer role, @RequestParam Integer taskOrReportId);
}
