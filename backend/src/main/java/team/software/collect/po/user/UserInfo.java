package team.software.collect.po.user;

import java.io.Serializable;
import java.util.Date;

import team.software.collect.enums.UserRole;
import team.software.collect.vo.user.UserInfoVO;
import lombok.Data;

/**
 * user_info
 * @author
 */
@Data
public class UserInfo implements Serializable {
    /**
     * 用户id
     */
    private Integer uid;//要和xml文件里的变量名一样

    /**
     * 名称
     */
    private String uname;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色
     */
    private UserRole userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    public UserInfo() {
    }

    public UserInfo(UserInfoVO userInfoVO){
        uid = userInfoVO.getUid();
        uname = userInfoVO.getUname();
        phone = userInfoVO.getPhone();
        password = userInfoVO.getPassword();
        userRole = userInfoVO.getUserRole();
        createTime = userInfoVO.getCreateTime();
    }

    private static final long serialVersionUID = 1L;

    public UserInfo(int uid, String uname, String phone, String password, UserRole userRole) {
        this.uid=uid;
        this.uname=uname;
        this.phone=phone;
        this.password=password;
        this.userRole=userRole;
        this.createTime=new Date();
    }
}
