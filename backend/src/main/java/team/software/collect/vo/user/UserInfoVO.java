package team.software.collect.vo.user;

import team.software.collect.enums.UserRole;
import team.software.collect.po.user.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class UserInfoVO {
    private Integer uid;

    private String uname;

    private String phone;

    private String password;

    private UserRole userRole;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public UserInfoVO() {
    }

    public UserInfoVO(@NonNull UserInfo userInfo) {//与User类不同的是，这里不需要赋值password，因为后端PO不需要向前端PO传递password
        uid = userInfo.getUid();
        uname = userInfo.getUname();
        phone = userInfo.getPhone();
        userRole = userInfo.getUserRole();
        createTime = userInfo.getCreateTime();
    }
}
