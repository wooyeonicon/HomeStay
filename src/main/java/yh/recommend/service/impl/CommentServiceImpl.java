package yh.recommend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yh.recommend.dao.CommentDao;
import yh.recommend.dao.HouseDao;
import yh.recommend.entity.Comment;
import yh.recommend.service.CommentService;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CommentDao commentDao;

    //添加评论
    @Override
    public int insertComment(String comments,int score,int coId,int chId){
        return commentDao.insertComment(comments,score,coId,chId);
    }
    //进行回复
    @Override
    public int updateComment(int commentId,String reply){
        return commentDao.updateComment(commentId,reply);
    }
    //查看所有评论
    @Override
    public List<Comment> checkAll(int chId){
        return commentDao.checkAll(chId);
    }
}
