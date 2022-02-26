package team.software.collect.vo.report;

import lombok.Data;
import lombok.NonNull;
import team.software.collect.po.report.ReportInfo;

import java.util.List;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.vo.report
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-23-23:41
 */

@Data
public class ReportInfoVO {

    private Integer rid;

    private Integer uid;

    private Integer tid;

    private String intro;

    private String recoverTips;

    private String device;

    private List<String> pictures;

    public ReportInfoVO () {

    }

    public ReportInfoVO (@NonNull ReportInfo reportInfo) {
        rid = reportInfo.getRid();
        uid = reportInfo.getUid();
        tid = reportInfo.getTid();
        intro = reportInfo.getIntro();
        recoverTips = reportInfo.getRecoverTips();
        device = reportInfo.getDevice();
        pictures = reportInfo.getPictures();
    }

}
