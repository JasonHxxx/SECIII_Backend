package team.software.collect.serviceimpl.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.exception.MyException;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.service.file.FileService;
import team.software.collect.util.Constant;
import team.software.collect.util.FileHelper;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.file.FileInfoVO;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/*
注：迭代二弃用的接口，使用oss替代
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${web.file-upload-path}")
    private String path;
    @Value("${web.fileLocation}")
    private String fileLocation;
    @Resource
    private FilePictureMapper filePictureMapper;

    @Override
    public ResultVO<FileInfoVO> uploadFile(MultipartFile file, Integer role, Integer taskOrReportId) {
        try {
            FileInfoVO fileInfoVO = FileHelper.save(path, fileLocation, file, role, taskOrReportId);
            //上传没问题，将文件名和相应的id存入数据库，便于前端取文件
            FilePicture filePicture=new FilePicture();
            filePicture.setFileName(fileInfoVO.getName());
            filePicture.setTitle("title");
            filePicture.setUploadTime(new Date());
            if(role==1){
                filePicture.setTid(taskOrReportId);
            }else{
                filePicture.setRid(taskOrReportId);
            }
            filePictureMapper.insert(filePicture);
            return new ResultVO<>(Constant.REQUEST_SUCCESS, "文件上传成功", fileInfoVO);
        } catch (MyException myException){
            logger.error("用户已经上传过该文件！", myException);
        } catch (IOException ioException){
            logger.error("文件复制时出错", ioException);
        }
        return new ResultVO<>(-1, "您已经上传过该文件！");
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response, Integer uid, @RequestParam Integer role, @RequestParam Integer taskOrReportId){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 获得待下载文件所在文件夹的绝对路径
            String realPath = ResourceUtils.getURL("classpath:").getPath() + fileLocation;
            //解析获得filename x 直接从文件名里可以进行解析
//            String realFileName="";
//            if(role==1){
//                realFileName="file."+taskOrReportId+".(tid)."+fileName;
//            }else{
//                realFileName="pic."+taskOrReportId+".(rid)."+fileName;
//            }
            //从数据库查找文件名
            String realFileName="";
            List<FilePicture> filePictureList=null;
            if(role==1){
                filePictureList=filePictureMapper.selectByTid(taskOrReportId);
            }else{
                filePictureList=filePictureMapper.selectByRid(taskOrReportId);
            }
            //一次性下载多个文件
//            int lstLen=filePictureList.size();
//            for(int i=0;i<lstLen;i++){
//
//            }
            realFileName=filePictureList.get(0).getFileName();
            // 获得文件输入流
            File tempFile=new File(realPath, realFileName);
            inputStream = new FileInputStream(tempFile);
            // 设置响应头、以附件形式打开文件
            response.setHeader("content-disposition", "attachment; fileName=" + fileName);
            outputStream = response.getOutputStream();
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
        }catch (IOException ioException){
            logger.error("文件下载出错", ioException);
        }finally {
            try {
                if(inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                logger.error("输入流或输出流关闭时出错！", e);
            }
        }
    }
}
