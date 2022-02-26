package team.software.collect.serviceimpl.report;

import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.picture.PictureMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.po.picture.Picture;
import team.software.collect.po.report.ReportInfo;
import team.software.collect.service.report.ReportService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportInfoVO;

import javax.annotation.Resource;
import java.util.*;

/**
 * @progect: SECIII_Backend
 * @package: team.software.collect.serviceimpl.report
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-02-24-15:58
 */

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Resource
    private PictureMapper pictureMapper;


    @Override
    public ResultVO<ReportInfoVO> postReport(ReportInfoVO reportInfoVO) {
        for (ReportInfo reportInfo : reportMapper.selectByUid(reportInfoVO.getUid())) {
            if (reportInfo.getTid().equals(reportInfoVO.getTid()))
                return new ResultVO<>(Constant.REQUEST_FAIL, "已上传该任务的报告！");
        }

        ReportInfo reportInfo = new ReportInfo(reportInfoVO);
        Integer rid = reportMapper.insert(reportInfo);

        if (rid >= 0) {
            for (String base64 : reportInfoVO.getPictures()) {
                Picture picture = new Picture(rid, base64);
                if (pictureMapper.insert(picture) < 0) {
                    return new ResultVO<>(Constant.REQUEST_FAIL, "图片上传失败！");
                }
            }
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "报告上传失败，请重新再试！");
    }

    @Override
    public ResultVO<ReportInfoVO> getReport(Integer rid) {
        ReportInfo reportInfo = reportMapper.selectByPrimaryKey(rid);
        List<Picture> pictures = pictureMapper.selectByRid(rid);
        if (pictures.isEmpty() || reportInfo == null)
            return new ResultVO<>(Constant.REQUEST_FAIL, "获取报告失败");

        List<String> base64s = new ArrayList<>();
        for (Picture picture : pictures) {
            base64s.add(picture.getBase64());
        }
        reportInfo.setPictures(base64s);
        return new ResultVO<ReportInfoVO>(Constant.REQUEST_SUCCESS, "获取报告成功", new ReportInfoVO(reportInfo));

    }
}
