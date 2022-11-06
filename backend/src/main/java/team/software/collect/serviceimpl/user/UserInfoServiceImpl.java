package team.software.collect.serviceimpl.user;

import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.mapperservice.user.UserInfoMapper;
import team.software.collect.po.user.Portrait;
import team.software.collect.po.user.UserInfo;
import team.software.collect.service.user.UserInfoService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.user.UserInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    UserInfoMapper userInfoMapper;
    @Resource
    PortraitMapper portraitMapper;

    /*
    更改resultvo以及uservo的相关属性 balance属性
     */
    @Override
    public ResultVO<UserInfoVO> userRegister(UserInfoVO userInfoVO) {
        String phone = userInfoVO.getPhone();
        String uname = userInfoVO.getUname();
        String password = userInfoVO.getPassword();
        if(userInfoMapper.selectByPhone(phone) == null){
            if(StringUtils.hasText(uname) && StringUtils.hasText(password)){
                userInfoVO.setCreateTime(new Date());
                UserInfo userInfo = new UserInfo(userInfoVO);
                userInfoMapper.insert(userInfo);
                //重新创建UserInfoVO返回，其中不包含password信息
                System.out.println(new UserInfoVO(userInfo));
                //建立一个用户画像
                Portrait portrait=new Portrait();
                portrait.setUid(userInfo.getUid());
                //能力值设为满分的一半，解决冷启动问题
                portrait.setAbility(BigDecimal.valueOf(2.5));
                portrait.setCommentsNum(1);
                portrait.setPreference("0,0,0");
                portrait.setActivity(BigDecimal.valueOf(0));
                portrait.setDevice("0,0,0,0,0,0");
                portraitMapper.insert(portrait);
                return new ResultVO<>(Constant.REQUEST_SUCCESS, "账号注册成功！", new UserInfoVO(userInfo));
            }else{
                return new ResultVO<>(Constant.REQUEST_FAIL, "用户名或密码不得为空！");
            }
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "这个手机号已经注册过账号。");
    }

    @Override
    public ResultVO<UserInfoVO> userLogin(String phone, String password) {
        UserInfo userInfo = userInfoMapper.selectByPhone(phone);
        if(userInfo == null){
            return new ResultVO<>(Constant.REQUEST_FAIL, "这个手机号尚未注册过账号。");
        }else{
            if(!userInfo.getPassword().equals(password))
                return new ResultVO<>(Constant.REQUEST_FAIL, "密码错误！");
        }
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "账号登陆成功！", new UserInfoVO(userInfoMapper.selectByPhone(phone)));
    }

    @Override
    public UserInfoVO getUserInfo(Integer uid) {
        UserInfo userFromDB = userInfoMapper.selectByPrimaryKey(uid);
        //一般查询结果不会是null
        if(userFromDB == null){
            return new UserInfoVO();
        }else{
            return new UserInfoVO(userFromDB);
        }
    }


    @Override
    public List<UserInfo> getAll() {
        return userInfoMapper.selectAll();
    }
}
