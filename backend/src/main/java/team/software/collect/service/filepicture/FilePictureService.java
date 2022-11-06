package team.software.collect.service.filepicture;

import team.software.collect.vo.ResultVO;
import team.software.collect.vo.filepicture.FilePictureVO;

import java.util.List;

public interface FilePictureService {
    ResultVO<FilePictureVO> addFilePicture(FilePictureVO filePictureVO);
    List<FilePictureVO> getFilePicture(FilePictureVO filePictureVO);
}
