package team.software.collect.po.user;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import team.software.collect.vo.user.PortraitVO;

/**
 * portrait
 * @author
 */
@Data
public class Portrait implements Serializable {
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

    public Portrait(){
    }

    public Portrait(PortraitVO portraitVO){
        pid=portraitVO.getPid();
        uid=portraitVO.getUid();
        ability=portraitVO.getAbility();
        preference=portraitVO.getPreference();
        activity=portraitVO.getActivity();
        device=portraitVO.getDevice();
        commentsNum= portraitVO.getCommentsNum();
    }

    public Portrait(BigDecimal ability, String preference, BigDecimal activity, String device) {
        this.ability = ability;
        this.preference = preference;
        this.activity = activity;
        this.device = device;
    }

    public Portrait(Integer uid,BigDecimal ability,String preference,BigDecimal activity,String device){
        this.uid=uid;
        this.preference=preference;
        this.device=device;
        this.ability=ability;
        this.activity=activity;
    }

    public Portrait(Integer uid,BigDecimal ability,String preference,BigDecimal activity,String device,Integer commentsNum){
        this.uid=uid;
        this.preference=preference;
        this.device=device;
        this.ability=ability;
        this.activity=activity;
        this.commentsNum=commentsNum;
    }

    private static final long serialVersionUID = 1L;
}
