package team.software.collect.serviceimpl.filepicture;

import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.service.filepicture.FilePictureService;

import javax.annotation.Resource;

@Service
public class FilepictureServiceImpl implements FilePictureService {
    @Resource
    private FilePictureMapper filePictureMapper;
}
