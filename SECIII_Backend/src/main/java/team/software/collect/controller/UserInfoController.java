package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import team.software.collect.service.user.UserInfoService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.user.UserFormVO;
import team.software.collect.vo.user.UserInfoVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value="用户controller", tags = "1.用户接口")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value="注册")
    @PostMapping("/register")
    public ResultVO<UserInfoVO> register(@RequestBody UserInfoVO userInfoVO){
        return userInfoService.userRegister(userInfoVO);
    }

    @ApiOperation(value="登陆")
    @PostMapping("/login")
    public ResultVO<UserInfoVO> login(@RequestBody UserFormVO userForm){
        return userInfoService.userLogin(userForm.getPhone(), userForm.getPassword());
    }

    @ApiOperation(value="用户已经正确登陆之后请求查看用户信息")
    @GetMapping("/{uid}")
    public UserInfoVO getUserInfo(@PathVariable Integer uid){
        return userInfoService.getUserInfo(uid);
    }

}
