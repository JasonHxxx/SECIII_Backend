package team.software.collect.po.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import team.software.collect.vo.report.ReportVO;

/**
 * report
 * @author
 */
@Data
public class Report implements Serializable {
    /**
     * 报告id
     */
    private Integer rid;

    /**
     * 众包工人id
     */
    private Integer uid;

    /**
     * 任务id
     */
    private Integer tid;

    private Integer parentId;

    /**
     * 缺陷情况说明
     */
    private String intro;

    /**
     * 缺陷复现步骤
     */
    private String recovertips;

    /**
     * 测试设备信息
     */
    private String device;

    /**
     * v2.0综合评分信息
     */
    private BigDecimal score;

    private Date creatTime;

    private Integer commentNum;

    public Report(){
    }

    public Report(ReportVO reportVO){
        rid=reportVO.getRid();
        uid=reportVO.getUid();
        tid=reportVO.getTid();
        parentId=reportVO.getParentId();
        intro=reportVO.getIntro();
        recovertips=reportVO.getRecovertips();
        device=reportVO.getDevice();
        score=reportVO.getScore();
        creatTime=reportVO.getCreatTime();
        commentNum=reportVO.getCommentNum();
    }

    public Report(Integer uid, Integer tid, String intro, String recovertips,String device){
        this.uid=uid;
        this.tid=tid;
        this.intro=intro;
        this.recovertips=recovertips;
        this.device=device;
    }

    public Report(Integer uid, Integer tid, Integer parentId,String intro, String recovertips, String device){
        this.uid=uid;
        this.tid=tid;
        this.parentId=parentId;
        this.intro=intro;
        this.recovertips=recovertips;
        this.device=device;
    }

    private static final long serialVersionUID = 1L;
}
