package team.software.collect.vo.report;

import lombok.Data;
import team.software.collect.po.report.Report;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReportVO {
    private Integer rid;

    private Integer uid;

    private Integer tid;

    private Integer parentId;

    private String intro;

    private String recovertips;

    private String device;

    private BigDecimal score;

    private Date creatTime;

    private Integer commentNum;

    public ReportVO(){
    }

    public ReportVO(Report report){
        rid=report.getRid();
        uid=report.getUid();
        tid=report.getTid();
        parentId=report.getParentId();
        intro=report.getIntro();
        recovertips=report.getRecovertips();
        device=report.getDevice();
        score=report.getScore();
        creatTime=report.getCreatTime();
        commentNum=report.getCommentNum();
    }
}
