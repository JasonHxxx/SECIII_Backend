package team.software.collect.po.task;

import java.io.Serializable;
import java.util.Date;

import team.software.collect.vo.task.TaskVO;
import lombok.Data;

/**
 * task
 * @author
 */
@Data
public class Task implements Serializable {
    /**
     * 任务id
     */
    private Integer tid;

    /**
     * 发包方用户id
     */
    private Integer uid;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 介绍
     */
    private String intro;

    /**
     * 招募工人数
     */
    private Integer maxWorkers;

    /**
     * 已有工人数
     */
    private Integer workerCnt;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 测试类型
     */
    private String type;

    /*
    po层和vo层如果手动添加了带参数的构造函数，则都要加上无参构造函数，mybatis会用到，不加-接受相应请求时会报错
     */
    public Task(){
    }

    public Task(TaskVO taskVO){
        tid = taskVO.getTid();
        uid = taskVO.getUid();
        name = taskVO.getName();
        intro = taskVO.getIntro();
        maxWorkers = taskVO.getMaxWorkers();
        workerCnt = taskVO.getWorkerCnt();
        beginTime = taskVO.getBeginTime();
        endTime = taskVO.getEndTime();
        type = taskVO.getType();
    }
    private static final long serialVersionUID = 1L;
}
