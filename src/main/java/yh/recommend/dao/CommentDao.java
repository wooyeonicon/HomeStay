package yh.recommend.dao;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.Comment;

import java.util.List;

public interface CommentDao {
    //添加评论
    int insertComment(@Param("comments") String comments,@Param("score") int score,@Param("coId") int coId,@Param("chId") int chId);
    //进行回复
    int updateComment(@Param("commentId") int commentId,@Param("reply") String reply);
    //查看所有评论
    List<Comment> checkAll(@Param("chId") int chId);


}
