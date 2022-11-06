package team.software.collect.vo.report;

import lombok.Data;
import team.software.collect.po.report.Comment;

import java.math.BigDecimal;

@Data
public class CommentVO {
    /**
     * 评论id
     */
    private Integer cid;

    /**
     * 对应报告id
     */
    private Integer rid;

    /**
     * 评论人id
     */
    private Integer uid;

    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 文本评论
     */
    private String comments;

    public CommentVO(){
    }

    public CommentVO(Comment comment){
        cid=comment.getCid();
        rid=comment.getRid();
        uid=comment.getUid();
        score=comment.getScore();
        comments=comment.getComments();
    }
}
