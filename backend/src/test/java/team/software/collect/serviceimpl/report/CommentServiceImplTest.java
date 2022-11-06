package team.software.collect.serviceimpl.report;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.software.collect.mapperservice.report.CommentMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.report.Comment;
import team.software.collect.po.report.Report;
import team.software.collect.po.user.Portrait;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.CommentVO;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

public class CommentServiceImplTest {
    @Mock
    CommentMapper commentMapper;
    @Mock
    ReportMapper reportMapper;
    @Mock
    PortraitMapper portraitMapper;
    @InjectMocks
    CommentServiceImpl commentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPostComment(){
        Report report=new Report(0,0,"intro","recover","1,0,0,0,0,0");
        report.setRid(2);
        report.setScore(new BigDecimal(1));
        report.setCommentNum(1);
        when(commentMapper.insert(any())).thenReturn(1);
        when(reportMapper.selectByPrimaryKey(anyInt())).thenReturn(report);
        when(reportMapper.updateByPrimaryKey(any())).thenReturn(1);
        when(portraitMapper.selectByUid(anyInt())).thenReturn(new Portrait(2,new BigDecimal(1),"1,1,0",new BigDecimal(1),"1,0,0,0,0,0",2));
        when(portraitMapper.updateByPrimaryKey(any())).thenReturn(1);

        ResultVO<CommentVO> result = commentServiceImpl.postComment(new CommentVO(new Comment(0,2,2,new BigDecimal(1), "very good")));

        CommentVO commentVO=new CommentVO();
        commentVO.setRid(Integer.valueOf(2));
        commentVO.setUid(Integer.valueOf(2));
        commentVO.setComments("very good");
        commentVO.setCid(0);
        commentVO.setScore(new BigDecimal(1));
        Assertions.assertEquals(new ResultVO<CommentVO>(Integer.valueOf(1), "评论成功！", commentVO), result);
    }

    @Test
    void testGetCommentsByRid(){
        when(commentMapper.selectByRid(anyInt())).thenReturn(Arrays.<Comment>asList(new Comment(Integer.valueOf(0), Integer.valueOf(0), "very good")));

        PageInfo<CommentVO> result = commentServiceImpl.getCommentsByRid(1, 1,0);
        PageInfo<CommentVO> pageInfo=new PageInfo<CommentVO>();
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(1);
        pageInfo.setSize(1);
        pageInfo.setStartRow(1);
        pageInfo.setEndRow(1);
        pageInfo.setTotal(1);
        pageInfo.setPages(1);
        Page<CommentVO> li=new Page<>();
        li.setCount(true);
        li.setPageNum(1);
        li.setPageSize(1);
        li.setStartRow(0);
        li.setEndRow(1);
        li.setTotal(1);
        li.setPages(1);
        li.setReasonable(null);
        li.setPageSizeZero(null);
        pageInfo.setList(li);
        pageInfo.setIsFirstPage(true);
        pageInfo.setIsLastPage(true);
        pageInfo.setNavigatePages(8);
        pageInfo.setNavigateFirstPage(1);
        pageInfo.setNavigateLastPage(1);
        int[] list=new int[]{1};
        pageInfo.setNavigatepageNums(list);
//        PageHelper.startPage(1, 1);
//        List<Comment> commentList=commentMapper.selectByRid(0);
//        PageInfo<Comment> po = new PageInfo<>(commentList);
//        PageInfo<CommentVO> pageComments = PageInfoUtil.convert(po, CommentVO.class);
        Assertions.assertEquals(pageInfo.toString(), result.toString());
    }
}
