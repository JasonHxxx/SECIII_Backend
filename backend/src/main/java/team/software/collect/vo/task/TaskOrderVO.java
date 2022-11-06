package team.software.collect.vo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import team.software.collect.po.task.TaskOrder;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TaskOrderVO {

    private Integer id;

    private BigDecimal cost;

    private Integer tid;

    private Integer uid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    public TaskOrderVO(){
    }

    public TaskOrderVO(TaskOrder taskOrder){
        id=taskOrder.getId();
        cost=taskOrder.getCost();
        tid=taskOrder.getTid();
        uid=taskOrder.getUid();
        createTime=taskOrder.getCreateTime();
        endTime=taskOrder.getEndTime();
    }

}
