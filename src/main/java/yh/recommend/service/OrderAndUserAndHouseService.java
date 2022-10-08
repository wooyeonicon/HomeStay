package yh.recommend.service;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.OrderAndUserAndHouse;

import java.util.List;

public interface OrderAndUserAndHouseService {
    List<OrderAndUserAndHouse> selectByOrderId(int userId,int page);
    int selectAllCount(int userId);

    List<OrderAndUserAndHouse> selectByOrderIdAndState(int userId,int page,int state);
    int selectAllByStateCount(int userId,int state);
    List<OrderAndUserAndHouse> checkAllByHouseId(int houseId);

    List<OrderAndUserAndHouse> checkAllByHuId(int huId);
    List<OrderAndUserAndHouse> checkAllOrdersByState();
}
