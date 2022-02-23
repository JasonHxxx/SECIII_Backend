package team.software.collect.vo.task;

import team.software.collect.po.task.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class TaskVO {
    private Integer tid;

    private Integer uid;

    private String name;

    private String intro;

    private Integer maxWorkers;

    private Integer workerCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String type;

    public TaskVO(){
    }

    public TaskVO(@NonNull Task task){
        tid = task.getTid();
        uid = task.getUid();
        name = task.getName();
        intro = task.getIntro();
        maxWorkers = task.getMaxWorkers();
        workerCnt = task.getWorkerCnt();
        beginTime = task.getBeginTime();
        endTime = task.getEndTime();
        type = task.getType();
    }

}
