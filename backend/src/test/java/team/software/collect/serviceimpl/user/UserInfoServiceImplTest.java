package team.software.collect.serviceimpl.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import team.software.collect.enums.UserRole;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.mapperservice.user.UserInfoMapper;
import team.software.collect.po.user.UserInfo;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.user.UserInfoVO;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceImplTest {
    @Mock
    UserInfoMapper userInfoMapper;
    @Mock
    PortraitMapper portraitMapper;
    @InjectMocks
    UserInfoServiceImpl userInfoServiceImpl;

    @Before
    public void setUp() {
    }

    @Test
    public void testUserRegister() {
        //在PO层增加新的构造函数
        UserInfo userInfo=new UserInfo(3, "user3", "3", "3", UserRole.WORKER);
        //需要的VO通过PO构造，参数直接用set
        UserInfoVO userInfoVO=new UserInfoVO(userInfo);
        userInfoVO.setPassword("3");

        when(userInfoMapper.insert(any())).thenReturn(1);
        when(userInfoMapper.selectByPhone(anyString())).thenReturn(null);

        when(portraitMapper.insert(any())).thenReturn(1);

        ResultVO<UserInfoVO> result = userInfoServiceImpl.userRegister(userInfoVO);
        UserInfoVO reUserInfoVO = result.getData();
        reUserInfoVO.setCreateTime(null);
        result.setData(reUserInfoVO);

        //构造想要得到的结果
        userInfoVO.setPassword(null);
        userInfoVO.setCreateTime(null);
        assertEquals(new ResultVO<UserInfoVO>(Constant.REQUEST_SUCCESS, "账号注册成功！", userInfoVO), result);
    }

    @Test
    public void testUserFailRegister() {
        UserInfo userInfo=new UserInfo(3, "user3", "3", "3", UserRole.WORKER);
        UserInfoVO userInfoVO=new UserInfoVO(userInfo);

        when(userInfoMapper.insert(any())).thenReturn(1);
        when(userInfoMapper.selectByPhone(anyString())).thenReturn(null);
        ResultVO<UserInfoVO> result = userInfoServiceImpl.userRegister(userInfoVO);
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "用户名或密码不得为空！"), result);

        when(userInfoMapper.selectByPhone(anyString())).thenReturn(new UserInfo());
        result = userInfoServiceImpl.userRegister(userInfoVO);
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "这个手机号已经注册过账号。"), result);

    }

    @Test
    public void testUserLogin() {
        UserInfo userInfo=new UserInfo(7, "5", "5", "5", UserRole.TASKPOSTER);
        when(userInfoMapper.selectByPhone(anyString())).thenReturn(userInfo);

        ResultVO<UserInfoVO> result = userInfoServiceImpl.userLogin("5", "5");
        UserInfoVO reUserInfoVO = result.getData();
        reUserInfoVO.setCreateTime(null);
        result.setData(reUserInfoVO);

        UserInfoVO userInfoVO=new UserInfoVO(userInfo);
        userInfoVO.setPassword("3");
        userInfoVO.setPassword(null);
        userInfoVO.setCreateTime(null);
        assertEquals(new ResultVO<UserInfoVO>(Constant.REQUEST_SUCCESS, "账号登陆成功！", userInfoVO), result);
    }

    @Test
    public void testUserFailLogin() {
        UserInfo userInfo=new UserInfo(3, "user3", "3", "3", UserRole.WORKER);
        when(userInfoMapper.selectByPhone(anyString())).thenReturn(null);

        ResultVO<UserInfoVO> result = userInfoServiceImpl.userLogin("3", "3");
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "这个手机号尚未注册过账号。"), result);

        when(userInfoMapper.selectByPhone(anyString())).thenReturn(userInfo);
        result = userInfoServiceImpl.userLogin("3", "4");
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "密码错误！"), result);
    }

    @Test
    public void testGetUserInfo() {
        when(userInfoMapper.selectByPrimaryKey(anyInt())).thenReturn(new UserInfo(new UserInfoVO()));

        UserInfoVO result = userInfoServiceImpl.getUserInfo(Integer.valueOf(0));
        assertEquals(new UserInfoVO(new UserInfo()), result);

        when(userInfoMapper.selectByPrimaryKey(anyInt())).thenReturn(null);
        result = userInfoServiceImpl.getUserInfo(Integer.valueOf(0));
        assertEquals(new UserInfoVO(new UserInfo()), result);
    }

    @Test
    public void testGetAll() {
        when(userInfoMapper.selectAll()).thenReturn(Arrays.<UserInfo>asList(new UserInfo(new UserInfoVO())));

        List<UserInfo> result = userInfoServiceImpl.getAll();
        assertEquals(Arrays.<UserInfo>asList(new UserInfo(new UserInfoVO())), result);
    }
}
