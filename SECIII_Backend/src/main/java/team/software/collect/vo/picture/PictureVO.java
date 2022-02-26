package team.software.collect.vo.picture;
import lombok.Data;
import lombok.NonNull;
import team.software.collect.po.picture.Picture;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.vo.picture
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-16:29
 */

@Data
public class PictureVO {
    private Integer pid;

    private Integer rid;

    private String base64;

    public PictureVO () {

    }

    public PictureVO(@NonNull Picture picture) {
        pid = picture.getPid();
        rid = picture.getRid();
        base64 = picture.getBase64();
    }
}
