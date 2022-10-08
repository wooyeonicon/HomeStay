package yh.recommend.dao;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderDao {
    /**
     * private int orderId;
     *     private String nowtime;//预定时间
     *     private String checktime;//入住时间
     *     private String leavetime;//离开时间
     *     private int state;//状态
     *     private String oname;//个人姓名
     *     private int onumber;//房间数（***房间数不能大于未使用的房间数）
     *     private double priced;//折后价格
     *     private int ouId;//订单用户外键
     *     private int ohId;//订单房源外键
     * @return
     */
    //预定房源,产生订单,默认0为预订
    int insertOrder(@Param("nowtime") String nowtime,
                    @Param("checktime") String checktime,
                    @Param("leavetime") String leavetime,
                    @Param("oname") String oname,
                    @Param("onumber") int onumber,
                    @Param("priced") double priced,
                    @Param("ouId") int ouId,
                    @Param("ohId") int ohId);


    /**
     * 商家改变订单的状态（确认入住）
     * 0：预订成功
     * 1：确认入住
     * 2：退订成功
     * @param state
     * @return
     */
    int updateOrder(@Param("state") int state,
                    @Param("orderId") int orderId,
                    @Param("ouId") int ouId,
                    @Param("ohId") int ohId);
    //查看所有订单
    List<Order> checkAllOrder(@Param("ouId") int ouId);
    //查看所有有效订单（state = 0）
    List<Order> checkEffective(@Param("state") int state,
                               @Param("ouId") int ouId);
    //通过订单号，查找该订单
    Order checkOrder(@Param("orderId") int orderId);
    //系统管理员查看需要惩罚用户
    List<Order> checkCancel(@Param("state") int state);
    //商家查询所有有效订单
    List<Order> checkEffectives(@Param("ohId") int ohId);

    List<Order> checkByState(@Param("ohId") int ohId);

    int CountsByState(@Param("ohId") int ohId);

    int CountByState(@Param("ohId") int ohId);

    List<Order> checkAllByHouseId(@Param("ohId") int ohId);

    List<Order> checkAllByHouseIdAndState(@Param("ohId") int ohId);

    double SumOrderByUserId(@Param("ouId") int ouId);

    double SumOrderByHouseId(@Param("ohId") int ohId);

}
