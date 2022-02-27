package team.software.collect.vo.report;

import lombok.Data;
import team.software.collect.po.report.Report;

@Data
public class ReportVO {
    private Integer rid;

    private Integer uid;

    private Integer tid;

    private String intro;

    private String recovertips;

    private String device;

    public ReportVO(){
    }

    public ReportVO(Report report){
        rid=report.getRid();
        uid=report.getUid();
        tid=report.getTid();
        intro=report.getIntro();
        recovertips=report.getRecovertips();
        device=report.getDevice();
    }
}
