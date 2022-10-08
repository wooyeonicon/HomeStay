package yh.recommend.dao;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.House;

import java.util.List;

public interface HouseDao {
    //添加房源(图片,价格，总数量，具体信息，房源名字，地址，标签，外键)
    int insertHouse(@Param("picture") String picture,
                    @Param("price") double price,
                    @Param("number") int number,
                    @Param("information") String information,
                    @Param("housename") String housename,
                    @Param("address") String address,
                    @Param("label") String label,
                    @Param("huId") int huId);
    //下架房源
    int deleteHouse(@Param("houseId") int housename);

    //修改价格
    int updatePrice(@Param("housename") String housename,@Param("price") double price);
    //修改图片
    int updatePicture(@Param("housename") String housename,@Param("picture") String picture);
    //商家查看自己所有房源
    List<House> checkSelfHouse(@Param("huId") int huId);
    //预定成功后，numbered会增加
    int updateNumbered(@Param("housename") String housename,@Param("numbered") int numbered);
    //退订（重新恢复）成功，numbered会减少（2个功能）
    int reduceNumbered(@Param("housename") String housename,@Param("numbered") int numbered);
    //关于房子名字模糊查找
    List<House> checkDimName(@Param("housename") String housename);
    //名字的具体查询
    House checkName(@Param("housename") String housename);
    //通过房源id，查询房源信息
    House checkId(@Param("houseId") int houseId);

    //关于第一步推荐
    List<House> checkAllScore();

    //推荐,根据风格进行模糊查询
    List<House> checkAllScores(@Param("label") String label);

    House checkHotelAndUser(@Param("houseId") int houseId);

    int updateHouseScore(@Param("houseId") int houseId,@Param("score") double score);

    List<House> recommendHotel(@Param("address") String address,@Param("label") String label);//评分优先，价格升序
    List<House> recommendHotelPrice(@Param("address") String address,@Param("label") String label);

    List<House> recommendHotelScore(@Param("address") String address,@Param("label") String label);
    List<House> recommendHotelScorePrice(@Param("address") String address,@Param("label") String label);

    List<House> randomHotel();

}
