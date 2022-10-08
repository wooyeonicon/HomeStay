package yh.recommend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yh.recommend.dao.OrderDao;
import yh.recommend.entity.Order;
import yh.recommend.service.OrderService;

import java.util.Date;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderDao orderDao;

    //预定房源
    @Override
    public int insertOrder(String nowtime, String checktime, String leavetime, String oname, int onumber, double priced, int ouId, int ohId){
        return orderDao.insertOrder(nowtime,checktime,leavetime,oname,onumber,priced,ouId,ohId);
    }
    //商家改变订单的状态（确认入住）
    @Override
    public int updateOrder(int state, int orderId, int ouId, int ohId){
        return orderDao.updateOrder(state,orderId,ouId,ohId);
    }
    //查看所有订单
    @Override
    public List<Order> checkAllOrder(int ouId){
        return orderDao.checkAllOrder(ouId);
    }
    //查看所有有效订单（state = 0）
    @Override
    public List<Order> checkEffective(int state, int ouId){
        return orderDao.checkEffective(state,ouId);
    }

    //通过订单号，查找该订单
    @Override
    public Order checkOrder(int orderId){
        return orderDao.checkOrder(orderId);
    }
    //系统管理员查看需要惩罚用户
    @Override
    public List<Order> checkCancel(int state){
        return orderDao.checkCancel(state);
    }
    //商家查询所有有效订单
    @Override
    public List<Order> checkEffectives(int ohId){
        return orderDao.checkEffectives(ohId);
    }

    @Override
    public List<Order> checkByState(int ohId) {
        return orderDao.checkByState(ohId);
    }

    @Override
    public int CountsByState(int ohId) {
        return orderDao.CountsByState(ohId);
    }

    @Override
    public int CountByState(int ohId) {
        return orderDao.CountByState(ohId);
    }

    @Override
    public List<Order> checkAllByHouseId(int ohId) {
        return orderDao.checkAllByHouseId(ohId);
    }

    @Override
    public List<Order> checkAllByHouseIdAndState(int ohId) {
        return orderDao.checkAllByHouseIdAndState(ohId);
    }

    @Override
    public double SumOrderByUserId(int ouId) {
        return orderDao.SumOrderByUserId(ouId);
    }

    @Override
    public double SumOrderByHouseId(int ohId) {
        return orderDao.SumOrderByHouseId(ohId);
    }
}
