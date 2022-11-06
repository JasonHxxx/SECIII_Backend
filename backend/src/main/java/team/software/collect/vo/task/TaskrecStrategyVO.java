package team.software.collect.vo.task;

import lombok.Data;
import team.software.collect.po.task.TaskrecStrategy;

import java.math.BigDecimal;

@Data
public class TaskrecStrategyVO {
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

    public TaskrecStrategyVO(){
    }

    public TaskrecStrategyVO(TaskrecStrategy taskrecStrategy){
        sid=taskrecStrategy.getSid();
        abilitypercent=taskrecStrategy.getAbilitypercent();
        preferpercent=taskrecStrategy.getPreferpercent();
        activitypercent=taskrecStrategy.getActivitypercent();
        devicepercent=taskrecStrategy.getDevicepercent();
        uid=taskrecStrategy.getUid();
        title=taskrecStrategy.getTitle();
    }
}
