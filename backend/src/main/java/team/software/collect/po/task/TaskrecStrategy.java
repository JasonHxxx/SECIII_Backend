package team.software.collect.po.task;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import team.software.collect.vo.task.TaskrecStrategyVO;

/**
 * taskrec_strategy
 * @author
 */
@Data
public class TaskrecStrategy implements Serializable {
    /**
     * 任务推荐策略id
     */
    private Integer sid;

    /**
     * 能力占比
     */
    private BigDecimal abilitypercent;

    /**
     * 用户偏好占比
     */
    private BigDecimal preferpercent;

    /**
     * 用户活跃度占比
     */
    private BigDecimal activitypercent;

    /**
     * 设备占比
     */
    private BigDecimal devicepercent;

    /**
     * 有没有被管理员使用
     */
    private Integer uid;

    private String title;

    public TaskrecStrategy(){
    }

    public TaskrecStrategy(TaskrecStrategyVO taskrecStrategyVO){
        sid=taskrecStrategyVO.getSid();
        abilitypercent=taskrecStrategyVO.getAbilitypercent();
        preferpercent=taskrecStrategyVO.getPreferpercent();
        activitypercent=taskrecStrategyVO.getActivitypercent();
        devicepercent=taskrecStrategyVO.getDevicepercent();
        uid=taskrecStrategyVO.getUid();
        title= taskrecStrategyVO.getTitle();
    }

    public TaskrecStrategy(BigDecimal abilitypercent, BigDecimal preferpercent, BigDecimal activitypercent, BigDecimal devicepercent) {
        this.abilitypercent = abilitypercent;
        this.preferpercent = preferpercent;
        this.activitypercent = activitypercent;
        this.devicepercent = devicepercent;
    }

    public TaskrecStrategy(Integer sid,BigDecimal abilitypercent, BigDecimal preferpercent, BigDecimal activitypercent, BigDecimal devicepercent,Integer uid,String title) {
        this.abilitypercent = abilitypercent;
        this.preferpercent = preferpercent;
        this.activitypercent = activitypercent;
        this.devicepercent = devicepercent;
        this.sid=sid;
        this.uid=uid;
        this.title=title;
    }

    private static final long serialVersionUID = 1L;
}
