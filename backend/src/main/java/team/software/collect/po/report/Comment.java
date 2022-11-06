package team.software.collect.po.report;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import team.software.collect.vo.report.CommentVO;

/**
 * comment
 * @author
 */
@Data
public class Comment implements Serializable {
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

    public Comment(){//PO层也要加无参构造函数
    }

    public Comment(CommentVO commentVO){
        cid= commentVO.getCid();;
        rid= commentVO.getRid();;
        uid= commentVO.getUid();;
        score=commentVO.getScore();
        comments=commentVO.getComments();
    }

    public Comment(Integer rid,Integer uid,String comments){
        this.rid=rid;
        this.uid=uid;
        this.comments=comments;
    }

    public Comment(Integer cid,Integer rid,Integer uid,BigDecimal score,String comments){
        this.rid=rid;
        this.uid=uid;
        this.comments=comments;
        this.cid=cid;
        this.score=score;
    }

    private static final long serialVersionUID = 1L;
}
