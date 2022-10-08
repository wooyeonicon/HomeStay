package yh.recommend.service;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.House;

import java.util.List;

public interface HouseService {
    //添加房源
    int insertHouse(String picture, double price, int number, String information, String housename, String address, String label, int huId);
    //下架房源
    int deleteHouse(int houseId);
    //修改价格
    int updatePrice(String housename,double price);
    //修改图片
    int updatePicture(String housename,String picture);
    //商家查看自己所有房源
    List<House> checkSelfHouse(int huId);
    //预定成功后，numbered会增加
    int updateNumbered(String housename,int numbered);
    //退订成功，numbered会减少
    int reduceNumbered(String housename,int numbered);
    //关于房子名字模糊查找
    List<House> checkDimName(String housename);
    //名字的具体查询
    House checkName(String housename);
    //通过房源id，查询房源信息
    House checkId(int houseId);

    //第一步推荐
    List<House> checkAllScore();

    //推荐,根据风格进行模糊查询
    List<House> checkAllScores(String label);

    House checkHotelAndUser(int houseId);

    int updateHouseScore(int houseId,double score);

    List<House> recommendHotel(String address,String label);
    List<House> recommendHotelPrice(String address,String label);

    List<House> recommendHotelScore(String address,String label);
    List<House> recommendHotelScorePrice(String address,String label);

    List<House> randomHotel();

}
