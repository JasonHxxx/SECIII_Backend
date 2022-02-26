package team.software.collect.po.report;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import team.software.collect.vo.report.ReportInfoVO;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.po.report
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-23-22:15
 */

@Data
public class ReportInfo implements Serializable {
    private Integer rid;

    private Integer uid;

    private Integer tid;

    private String intro;

    private String recoverTips;

    private String device;

    List<String> pictures;

    public ReportInfo() {

    }

    public ReportInfo(ReportInfoVO reportInfoVO) {
        rid = reportInfoVO.getRid();
        uid = reportInfoVO.getUid();
        tid = reportInfoVO.getTid();
        intro = reportInfoVO.getIntro();
        recoverTips = reportInfoVO.getRecoverTips();
        device = reportInfoVO.getDevice();
        pictures = reportInfoVO.getPictures();

    }
    private static final long serialVersionUID = 1L;
}