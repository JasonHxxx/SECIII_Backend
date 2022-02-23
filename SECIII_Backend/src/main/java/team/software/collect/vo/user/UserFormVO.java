package team.software.collect.vo.user;

import lombok.Data;

/*
用户登陆时传递信息用
 */
@Data
public class UserFormVO {
    private String phone;
    private String password;
}
