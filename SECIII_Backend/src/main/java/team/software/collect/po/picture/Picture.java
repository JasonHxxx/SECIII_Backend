package team.software.collect.po.picture;


import lombok.Data;
import team.software.collect.vo.picture.PictureVO;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.po.picture
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-16:27
 */

@Data
public class Picture {
    private Integer pid;

    private Integer rid;

    private String base64;

    public Picture () {

    }

    public Picture (Integer rid, String base64) {
        this.pid = null;
        this.rid = rid;
        this.base64 = base64;
    }

    public Picture (PictureVO pictureVO) {
        pid = pictureVO.getPid();
        rid = pictureVO.getRid();
        base64 = pictureVO.getBase64();
    }

    private static final long serialVersionUID = 1L;
}
