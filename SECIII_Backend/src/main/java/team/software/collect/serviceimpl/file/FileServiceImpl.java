package team.software.collect.serviceimpl.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.exception.MyException;
import team.software.collect.service.file.FileService;
import team.software.collect.util.Constant;
import team.software.collect.util.FileHelper;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.file.FileInfoVO;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${web.file-upload-path}")
    private String path;
    @Value("${web.fileLocation}")
    private String fileLocation;

    @Override
    public ResultVO<FileInfoVO> uploadFile(MultipartFile file, Integer role, Integer taskOrReportId) {
        try {
            FileInfoVO fileInfoVO = FileHelper.save(path, fileLocation, file, role, taskOrReportId);
            return new ResultVO<>(Constant.REQUEST_SUCCESS, "文件上传成功", fileInfoVO);
        } catch (MyException myException){
            logger.error("用户已经上传过该文件！", myException);
        } catch (IOException ioException){
            logger.error("文件复制时出错", ioException);
        }
        return new ResultVO<>(-1, "您已经上传过该文件！");
    }

//    @Override
//    public void downloadFile(String originName, String newName, HttpServletResponse response) {
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//        response.setContentType("application/x-msdownload");
//        try {
//            Resource resource = FileHelper.loadFileAsResource(path, originName);
//            if(resource == null)
//                return;
//            inputStream = resource.getInputStream();
//            //1.设置文件ContentType类型
//            response.setContentType("application/octet-stream;charset=UTF-8");
//            outputStream = response.getOutputStream();
//            //2.转码  UTF_8为传入的newName编码的格式 ISO_8859_1为浏览器默认编码
//            String convertName = new String(newName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
//            //3.设置 header  Content-Disposition
//            response.setHeader("Content-Disposition", "attachment; filename=" + convertName);
//            int b = 0;
//            byte[] buffer = new byte[2048];
//            while (b != -1) {
//                b = inputStream.read(buffer);
//                if (b != -1) {
//                    outputStream.write(buffer, 0, b);
//                }
//            }
//        } catch (IOException | MyException e) {
//            logger.error("文件下载时出错", e);
//        } finally {
//            try {
//                if(inputStream != null)
//                    inputStream.close();
//                if (outputStream != null)
//                    outputStream.close();
//            } catch (IOException e) {
//                logger.error("输入流或输出流关闭时出错！", e);
//            }
//        }
//    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response, Integer uid){
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
            // 获得文件输入流
            File tempFile=new File(realPath, fileName);
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
