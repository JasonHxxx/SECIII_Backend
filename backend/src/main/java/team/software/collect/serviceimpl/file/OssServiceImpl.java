package team.software.collect.serviceimpl.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import team.software.collect.config.AliyunOSSConfig;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.service.file.OssService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    //设置允许上传文件格式
    private static final String[] IMAGE_TYPE=new String[]{".bmp",".jpg",".jpeg",".png",".gif",".mp3",".mp4",".mkv",".pdf",".pptx",".rar",".zip"};
    @Autowired
    private OSS ossClient;
    @Autowired
    private AliyunOSSConfig aliyunOSSConfig;
    @Resource
    private FilePictureMapper filePictureMapper;

    @Override
    public ResultVO<String> upload(MultipartFile file,Integer myFileType,Integer id){
        if(file==null)
            return new ResultVO<>(Constant.UpLoadFail,"输入的文件为null");
        String bucketNanme=aliyunOSSConfig.getBucketName();
        String endPoint = aliyunOSSConfig.getEndPoint();
        String accessKeyId = aliyunOSSConfig.getAccessKeyId();
        String accessKeySecret = aliyunOSSConfig.getAccessKeySecret();
        String fileHost = aliyunOSSConfig.getFileHost();
        String imgHost=aliyunOSSConfig.getImgHost();
        //返回的Url
        String returnUrl="";
        //审核上传文件是否符合规定格式
        boolean isLegal=false;
        for (String type:IMAGE_TYPE){
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),type)){
                isLegal=true;
                break;
            }
        }
        if (!isLegal){
//            如果不正确返回错误状态码
            return new ResultVO<>(Constant.UpLoadFail,"您上传的文件不合法！");
        }
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //截取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String size = String.format("%.2f", (file.getSize() * 1.0 / 1024 / 1024)) ;
//        最终保存文件名称
        String newFileName= UUID.randomUUID().toString()+ fileType;
        //构建日期路径  ps ：oss目标文件夹/yyyy/MM/dd文件名称
        String filePath=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
//        文件上传文件的路径
//        String uploadUrl=fileHost+"/"+filePath+"/"+newFileName;
        FilePicture filePicture=new FilePicture();
        filePicture.setFileName(newFileName);
        filePicture.setTitle(originalFilename);
        filePicture.setUploadTime(new Date());
        filePicture.setFileType(fileType);
        filePicture.setFileSize(Double.parseDouble(size));//单位MB

        String uploadUrl="";
        if(myFileType==1) {
            uploadUrl = fileHost + "/" + newFileName;
            filePicture.setTid(id);
        }
        else if(myFileType==2) {
            uploadUrl = imgHost + "/" + newFileName;
            filePicture.setRid(id);
        }
        filePictureMapper.insert(filePicture);
//        获取文件输入流
        InputStream inputStream=null;
        try{
            inputStream=file.getInputStream();

        }catch (IOException e){
            e.printStackTrace();
        }
        //文件上传到阿里云oss
//        ossClient.put
        ossClient.putObject(bucketNanme,uploadUrl,inputStream);//,meta
        returnUrl="http://"+bucketNanme+"."+endPoint+"/"+uploadUrl;
        return new ResultVO<>(Constant.UpLoadSuccess,"文件上传成功",returnUrl);
    }

    @Override
    public void downloadOssFile(OutputStream os, String objectName, Integer myFileType) {
//        FilePicture filePicture= filePictureMapper.selectByFileName(objectName);
        String fileHost = aliyunOSSConfig.getFileHost();
        String imgHost=aliyunOSSConfig.getImgHost();
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        if(myFileType==1)
            objectName=fileHost+"/"+objectName;
        else if(myFileType==2)
            objectName=imgHost+"/"+objectName;
        OSSObject ossObject = ossClient.getObject(aliyunOSSConfig.getBucketName(), objectName);
        // 读取文件内容。
        try {
            BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
            BufferedOutputStream out = new BufferedOutputStream(os);
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<FilePicture> getUrl(Integer myFileType, Integer id){
        List<FilePicture> filePictureList=null;
        if(myFileType==1){
            filePictureList=filePictureMapper.selectByTid(id);
        }else{
            filePictureList=filePictureMapper.selectByRid(id);
        }
        return filePictureList;
    }

    @Override
    public ResultVO<String> deleteFile(String objectName) {
        String fileHost = aliyunOSSConfig.getFileHost();
        String imgHost=aliyunOSSConfig.getImgHost();
        // 根据BucketName,objectName删除文件
        String ossObjectName=fileHost+"/"+objectName;
        try {
            ossClient.deleteObject(aliyunOSSConfig.getBucketName(), ossObjectName);
            filePictureMapper.deleteByFileName(objectName);
            return new ResultVO<>(Constant.DeleteSuccess,"文件删除成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVO<>(Constant.DeleteFail,"文件删除失败！");
        }
    }
}
