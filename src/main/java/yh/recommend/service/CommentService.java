package yh.recommend.service;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.Comment;

import java.util.List;

public interface CommentService {
    //添加评论
    int insertComment(String comments,int score,int coId,int chId);
    //进行回复
    int updateComment(int commentId,String reply);
    //查看所有评论
    List<Comment> checkAll(int chId);
}
