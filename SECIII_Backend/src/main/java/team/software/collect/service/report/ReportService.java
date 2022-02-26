package team.software.collect.service.report;

import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportInfoVO;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.service.report
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-15:57
 */
public interface ReportService {
    // 众包工人上传测试报告
    ResultVO<ReportInfoVO> postReport(ReportInfoVO reportInfoVO);

    // 获取测试报告信息
    ResultVO<ReportInfoVO> getReport(Integer rid);
}
