package team.software.collect.service.user;

import team.software.collect.po.user.UserInfo;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.user.UserInfoVO;

import java.util.List;

public interface UserInfoService {
    // 用户注册
    ResultVO<UserInfoVO> userRegister(UserInfoVO userInfoVO);
    // 用户登录验证
    ResultVO<UserInfoVO> userLogin(String phone, String password);
    // 根据id查找用户
    UserInfoVO getUserInfo(Integer uid);

    List<UserInfo> getAll();
}
