package team.software.collect.service.report;

import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.CommentVO;

public interface CommentService {
    ResultVO<CommentVO> postComment(CommentVO commentVO);
    PageInfo<CommentVO> getCommentsByRid(Integer currPage, Integer pageSize, Integer  rid);
}
