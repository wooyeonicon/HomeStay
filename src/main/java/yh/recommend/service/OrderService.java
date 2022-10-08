package yh.recommend.service;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderService {
    //预定房源
    int insertOrder(String nowtime, String checktime, String leavetime,String oname, int onumber, double priced, int ouId, int ohId);
    //商家改变订单的状态（确认入住）
    int updateOrder(int state, int orderId, int ouId, int ohId);
    //查看所有订单
    List<Order> checkAllOrder(int ouId);
    //查看所有有效订单（state = 0）
    List<Order> checkEffective(int state, int ouId);

    //通过订单号，查找该订单
    Order checkOrder(int orderId);
    //系统管理员查看需要惩罚用户
    List<Order> checkCancel(int state);
    //商家查询所有有效订单
    List<Order> checkEffectives(int ohId);
    //财务管理

    List<Order> checkByState(int ohId);

    int CountsByState(int ohId);

    int CountByState(int ohId);

    List<Order> checkAllByHouseId(int ohId);

    List<Order> checkAllByHouseIdAndState(int ohId);

    double SumOrderByUserId(int ouId);

    double SumOrderByHouseId(int ohId);
}
