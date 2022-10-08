package yh.recommend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yh.recommend.dao.HouseAndOrderAndCommentDao;
import yh.recommend.dao.OrderAndUserAndHouseDao;
import yh.recommend.entity.HouseAndOrderAndComment;
import yh.recommend.service.HouseAndOrderAndCommentService;

import java.util.List;

@Service
public class HouseAndOrderAndCommentServiceImpl implements HouseAndOrderAndCommentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HouseAndOrderAndCommentDao houseAndOrderAndCommentDao;
    @Override
    public List<HouseAndOrderAndComment> checkAllCommentsByHouseId(int houseId) {
        return houseAndOrderAndCommentDao.checkAllCommentsByHouseId(houseId);
    }
}
