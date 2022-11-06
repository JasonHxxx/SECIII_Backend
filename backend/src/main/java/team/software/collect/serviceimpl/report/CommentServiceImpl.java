package team.software.collect.serviceimpl.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import team.software.collect.mapperservice.report.CommentMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.report.Comment;
import team.software.collect.po.report.Report;
import team.software.collect.po.user.Portrait;
import team.software.collect.service.report.CommentService;
import team.software.collect.util.Constant;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.CommentVO;

import javax.annotation.Resource;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    CommentMapper commentMapper;
    @Resource
    PortraitMapper portraitMapper;
    @Resource
    ReportMapper reportMapper;

    @Override
    public ResultVO<CommentVO> postComment(CommentVO commentVO) {
        Comment comment=new Comment(commentVO);
        if(commentMapper.insert(comment)>0) {
            Report commentedReport=reportMapper.selectByPrimaryKey(commentVO.getRid());
            //更新被评论的报告的评分信息

            BigDecimal preScore=commentedReport.getScore();
            BigDecimal sum0=preScore.multiply(BigDecimal.valueOf(commentedReport.getCommentNum())).add(commentVO.getScore());
            Integer preCommentNum0=commentedReport.getCommentNum();
            Integer postCommentNum0=preCommentNum0+1;
            commentedReport.setCommentNum(postCommentNum0);
            BigDecimal postScore=sum0.divide(BigDecimal.valueOf(commentedReport.getCommentNum()));
            commentedReport.setScore(postScore);
            reportMapper.updateByPrimaryKey(commentedReport);

            Portrait portrait=portraitMapper.selectByUid(commentedReport.getUid());
            //在portrait里加一个字段，表示被多少人评论过
            //求均值
            BigDecimal preAbility=portrait.getAbility();
            BigDecimal sum=preAbility.multiply(BigDecimal.valueOf(portrait.getCommentsNum())).add(comment.getScore());
            Integer postCommentNum=portrait.getCommentsNum()+1;
            portrait.setCommentsNum(postCommentNum);
            BigDecimal postAbility=sum.divide(BigDecimal.valueOf(portrait.getCommentsNum()),2, RoundingMode.HALF_DOWN);//保留两位小数
            portrait.setAbility(postAbility);
            portraitMapper.updateByPrimaryKey(portrait);
            return new ResultVO<>(Constant.REQUEST_SUCCESS, "评论成功！", new CommentVO(comment));
        }
        return new ResultVO<>(Constant.REQUEST_FAIL,"数据库插入错误！");
    }

    @Override
    public PageInfo<CommentVO> getCommentsByRid(Integer currPage, Integer pageSize, Integer rid) {
        if(currPage==null || currPage<1) currPage=1;
        PageHelper.startPage(currPage, pageSize);
        List<Comment> commentList=commentMapper.selectByRid(rid);
        PageInfo<Comment> po = new PageInfo<>(commentList);
        PageInfo<CommentVO> pageComments = PageInfoUtil.convert(po, CommentVO.class);
        return pageComments;
    }
}
