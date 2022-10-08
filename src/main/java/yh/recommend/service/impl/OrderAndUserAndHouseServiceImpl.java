package yh.recommend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yh.recommend.dao.HouseDao;
import yh.recommend.dao.OrderAndUserAndHouseDao;
import yh.recommend.entity.OrderAndUserAndHouse;
import yh.recommend.service.OrderAndUserAndHouseService;

import java.util.List;
@Service
public class OrderAndUserAndHouseServiceImpl implements OrderAndUserAndHouseService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderAndUserAndHouseDao orderAndUserAndHouseDao;

    @Override
    public List<OrderAndUserAndHouse> selectByOrderId(int userId,int page) {
        return orderAndUserAndHouseDao.selectByOrderId(userId,page);
    }

    @Override
    public int selectAllCount(int userId) {
        return orderAndUserAndHouseDao.selectAllCount(userId);
    }

    @Override
    public List<OrderAndUserAndHouse> selectByOrderIdAndState(int userId, int page, int state) {
        return orderAndUserAndHouseDao.selectByOrderIdAndState(userId,page,state);
    }

    @Override
    public int selectAllByStateCount(int userId, int state) {
        return orderAndUserAndHouseDao.selectAllByStateCount(userId,state);
    }

    @Override
    public List<OrderAndUserAndHouse> checkAllByHouseId(int houseId) {
        return orderAndUserAndHouseDao.checkAllByHouseId(houseId);
    }

    @Override
    public List<OrderAndUserAndHouse> checkAllByHuId(int huId) {
        return orderAndUserAndHouseDao.checkAllByHuId(huId);
    }

    @Override
    public List<OrderAndUserAndHouse> checkAllOrdersByState() {
        return orderAndUserAndHouseDao.checkAllOrdersByState();
    }
}
