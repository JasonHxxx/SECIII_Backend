package team.software.collect.mapperservice.picture;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.mapperservice.picture
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-26-15:29
 */
import team.software.collect.po.picture.Picture;

import java.util.List;

public interface PictureMapper {
    int insert(Picture record);

    List<Picture> selectByRid(Integer rid);
}