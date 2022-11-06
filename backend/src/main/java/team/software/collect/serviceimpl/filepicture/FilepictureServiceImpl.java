package team.software.collect.serviceimpl.filepicture;

import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.service.filepicture.FilePictureService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.filepicture.FilePictureVO;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FilepictureServiceImpl implements FilePictureService {
    @Resource
    private FilePictureMapper filePictureMapper;

    @Override
    public ResultVO<FilePictureVO> addFilePicture(FilePictureVO filePictureVO) {
        //先查找
        return null;
    }

    @Override
    public List<FilePictureVO> getFilePicture(FilePictureVO filePictureVO) {
        return null;
    }
}
