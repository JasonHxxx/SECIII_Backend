package team.software.collect.vo.filepicture;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import team.software.collect.po.filepicture.FilePicture;

import java.util.Date;

@Data
public class FilePictureVO {
    private Integer fid;

    private Integer tid;

    private Integer rid;

    private String title;

    private String fileName;

    private String fileType;

    private Double fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;

    public FilePictureVO(){
    }

    public FilePictureVO(FilePicture filePicture){
        fid=filePicture.getFid();
        tid=filePicture.getTid();
        rid=filePicture.getRid();
        title=filePicture.getTitle();
        fileName=filePicture.getFileName();
        fileType=filePicture.getFileType();
        fileSize=filePicture.getFileSize();
        uploadTime=filePicture.getUploadTime();
    }
}
