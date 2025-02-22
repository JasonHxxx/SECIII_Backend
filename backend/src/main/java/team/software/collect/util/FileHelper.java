package team.software.collect.util;
import org.springframework.util.ResourceUtils;
import team.software.collect.enums.ExceptionType;
import team.software.collect.exception.MyException;
import team.software.collect.vo.file.FileInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class FileHelper {
    /**
     * 保存文件
     * @param directoryPath 目录路径（以 / 结尾）
     * @param file 文件
     * @return 保存成功后的文件名
     */
    public static FileInfoVO save(String directoryPath, String fileLocation, MultipartFile file, Integer role, Integer taskOrReportId) throws IOException{
        String basePath = ResourceUtils.getURL("classpath:").getPath();
        String realPath=basePath+fileLocation;
        File newFile = new File(realPath);
        // 如果文件夹不存在、则新建
        if (!newFile.exists())
            newFile.mkdirs();
        String fileName=null;
        //对即将存入的图片进行格式化命名
        if(role==1)
            fileName = "file."+taskOrReportId+".(tid)."+file.getOriginalFilename();
        else
            fileName = "pic."+taskOrReportId+".(rid)."+file.getOriginalFilename();
        //将时间中的冒号改为'.'，因为文件名中不允许有':'
        fileName=fileName.replace(':', '.');

        //查找是否已经上传过该图片：发包方对同一个任务不能上传两个相同的文件 众包工人对一个报告不能上传两张相同的图片
        File baseDir = new File(realPath);
        File[] files = baseDir.listFiles();
        File tempFile=null;
        for (int i = 0; i < files.length; i++) {
            tempFile=files[i];
            if(tempFile.getName().equals(fileName))
                throw MyException.of(ExceptionType.SERVER_ERROR, "您已经上传过该文件！");
        }

        String originalName = file.getOriginalFilename();
        String newName;
        String type = "unknown";
        String size = String.format("%.2f", (file.getSize() * 1.0 / 1024 / 1024)) + " MB";
        if(originalName!=null && originalName.lastIndexOf(".")!=-1){
            // 取扩展名
            String ext = originalName.substring(originalName.lastIndexOf("."));
            type = originalName.substring(originalName.lastIndexOf(".")+1);
            // 构造新文件名
            newName=fileName;
        }else
            newName = UUID.randomUUID().toString();
        // 根据目标地址构造文件输出流
        String storePath=realPath+"/";
        //FileOutputStream fileOutputStream1 = new FileOutputStream(directoryPath + newName);
        FileOutputStream fileOutputStream2 = new FileOutputStream(storePath + newName);
        // 将文件复制到映射的地址
        //FileCopyUtils.copy(file.getInputStream(),fileOutputStream1);//存储到本地，测试用，目录在D:\app
        FileCopyUtils.copy(file.getInputStream(),fileOutputStream2);

        return new FileInfoVO(newName, type, size);
    }

    /**
     * 加载文件为资源
     * @param directoryPath 目录路径（以 / 结尾）
     * @param fileName 文件名
     * @return 输入流资源
     */
    public static Resource loadFileAsResource(String directoryPath, String fileName) {
        try {
            Path filePath = Paths.get(directoryPath+fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists())
                return resource;
        } catch (MalformedURLException ex) {
            throw MyException.of(ExceptionType.SERVER_ERROR, "服务端错误，加载文件资源时出错！");
        }
        return null;
    }

    public static boolean delete(String directoryPath, String fileName){
        if(StringUtils.hasText(fileName)){
            File file = new File(directoryPath + fileName);
            if(file.exists()) {
                // 当且仅当文件被成功删除后返回true
                return file.delete();
            }
        }
        return false;
    }



    /**
     * 检查目录路径是否有效，若当前路径对应的目录不存在，则尝试创建目录
     * @param directoryPath 目录路径
     * @return 若目录不存在且创建失败则返回false，否则返回true
     */
    private static boolean checkDirectoryPath(String directoryPath) {
        File dir = new File(directoryPath);
        // 如果文件夹不存在则创建
        if(!dir.exists() && !dir.isDirectory()){
            log.debug("用于存放上传文件的文件夹不存在，正在尝试创建..");
            return dir.mkdirs();
        }
        return true;
    }
}
