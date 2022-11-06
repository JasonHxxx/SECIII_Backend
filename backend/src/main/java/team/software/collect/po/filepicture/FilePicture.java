package team.software.collect.po.filepicture;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import team.software.collect.vo.filepicture.FilePictureVO;

/**
 * file_picture
 * @author
 */
@Data
public class FilePicture implements Serializable {
    /**
     * 文件（图片）id
     */
    private Integer fid;

    /**
     * 任务id
     */
    private Integer tid;

    /**
     * 报告id
     */
    private Integer rid;

    /**
     * 文件标题
     */
    private String title;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Double fileSize;

    /**
     * 文件上传时间
     */
    private Date uploadTime;

    public FilePicture(){
    }

    public FilePicture(FilePictureVO filePictureVO){
        fid=filePictureVO.getFid();
        tid=filePictureVO.getTid();
        rid=filePictureVO.getRid();
        title=filePictureVO.getTitle();
        fileName=filePictureVO.getFileName();
        fileType=filePictureVO.getFileType();
        fileSize=filePictureVO.getFileSize();
        uploadTime=filePictureVO.getUploadTime();
    }

    public FilePicture(Integer tid,Integer rid,String title,String fileName,String fileType){
        this.tid=tid;
        this.rid=rid;
        this.title=title;
        this.fileName=fileName;
        this.fileType=fileType;
    }

    private static final long serialVersionUID = 1L;
}
