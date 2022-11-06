package team.software.collect.serviceimpl.file;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

public class OssServiceImplTest {
    @Mock
    FilePictureMapper filePictureMapper;
    @InjectMocks
    OssServiceImpl ossService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
//mock与oss配合测试会出现问题，这里我们用postman直接调用controller层的接口进行测试，测试结果正确
//    @Test
//    public void testUpload(){
//        when(filePictureMapper.insert(any())).thenReturn(1);
//
//        Path path = Paths.get("src/test/java/team/software/collect/serviceimpl/file/filePicture/test1.png");
//        String name = "test1.png";
//        String originalFileName = "test1.png";
//        String contentType = "text/plain";
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
//        MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);
//        ResultVO<String> result = ossService.upload(multipartFile, Integer.valueOf(2), Integer.valueOf(1));
//
//        Assertions.assertEquals(new ResultVO<String>(Constant.REQUEST_SUCCESS, "文件上传成功"), result);
//    }
}
