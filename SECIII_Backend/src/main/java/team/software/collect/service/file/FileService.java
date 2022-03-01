package team.software.collect.service.file;

import org.springframework.web.multipart.MultipartFile;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.file.FileInfoVO;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    ResultVO<FileInfoVO> uploadFile(MultipartFile file, Integer role, Integer taskOrReportId);
    void downloadFile(String fileName, HttpServletResponse response, Integer uid);
}
