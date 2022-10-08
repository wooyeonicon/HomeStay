package yh.recommend.dao;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.OrderAndUserAndHouse;

import java.util.List;

public interface OrderAndUserAndHouseDao {
    List<OrderAndUserAndHouse> selectByOrderId(@Param("userId") int userId,@Param("page") int page);
    int selectAllCount(@Param("userId") int userId);

    List<OrderAndUserAndHouse> selectByOrderIdAndState(@Param("userId") int userId,@Param("page") int page,@Param("state") int state);
    int selectAllByStateCount(@Param("userId") int userId,@Param("state") int state);

    List<OrderAndUserAndHouse> checkAllByHouseId(@Param("houseId") int houseId);

    List<OrderAndUserAndHouse> checkAllByHuId(@Param("huId") int huId);

    List<OrderAndUserAndHouse> checkAllOrdersByState();
}
