package team.software.collect.po.report;

import java.io.Serializable;
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

    public Report(){
    }

    public Report(ReportVO reportVO){
        rid=reportVO.getRid();
        uid=reportVO.getUid();
        tid=reportVO.getTid();
        intro=reportVO.getIntro();
        recovertips=reportVO.getRecovertips();
        device=reportVO.getDevice();
    }

    private static final long serialVersionUID = 1L;
}
