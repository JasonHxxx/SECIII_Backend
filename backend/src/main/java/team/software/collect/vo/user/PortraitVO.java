package team.software.collect.vo.user;

import lombok.Data;
import team.software.collect.po.user.Portrait;

import java.math.BigDecimal;

@Data
public class PortraitVO {
    /**
     * 用户画像主键
     */
    private Integer pid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 能力值
     */
    private BigDecimal ability;

    /**
     * 任务偏好
     */
    private String preference;

    /**
     * 活跃度
     */
    private BigDecimal activity;

    /**
     * 测试设备
     */
    private String device;

    private Integer commentsNum;

    public PortraitVO(){
    }

    public PortraitVO(Portrait portrait){
        pid=portrait.getPid();
        uid=portrait.getUid();
        ability=portrait.getAbility();
        preference=portrait.getPreference();
        activity=portrait.getActivity();
        device=portrait.getDevice();
        commentsNum=portrait.getCommentsNum();
    }
}
