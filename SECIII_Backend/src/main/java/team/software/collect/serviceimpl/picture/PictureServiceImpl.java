package team.software.collect.serviceimpl.picture;

import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.picture.PictureMapper;

import javax.annotation.Resource;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.serviceimpl
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-16:24
 */

@Service
public class PictureServiceImpl {
    @Resource
    private PictureMapper pictureMapper;
}
