package team.software.collect.serviceimpl.filepicture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.filepicture.FilePictureVO;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @progect: TaskOrderController.java
 * @package: team.software.collect.serviceimpl.filepicture
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-03-05-14:57
 */
class FilepictureServiceImplTest {
    @Mock
    FilePictureMapper filePictureMapper;
    @InjectMocks
    FilepictureServiceImpl filepictureServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddFilePicture() {
//        ResultVO<FilePictureVO> result = filepictureServiceImpl.addFilePicture(new FilePictureVO(new FilePicture(null)));
//        Assertions.assertEquals(new ResultVO<FilePictureVO>(Integer.valueOf(0), "msg", new FilePictureVO()), result);
    }

    @Test
    void testGetFilePicture() {
//        List<FilePictureVO> result = filepictureServiceImpl.getFilePicture(new FilePictureVO(new FilePicture(null)));
//        Assertions.assertEquals(Arrays.<FilePictureVO>asList(new FilePictureVO(new FilePicture(null))), result);
    }
}
