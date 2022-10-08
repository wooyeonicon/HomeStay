package yh.recommend.service.impl;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yh.recommend.dao.HouseDao;
import yh.recommend.dao.UserDao;
import yh.recommend.entity.House;
import yh.recommend.service.HouseService;

import java.util.List;
@Service
public class HouseServiceImpl implements HouseService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HouseDao houseDao;

    //添加房源
    @Override
    public int insertHouse(String picture, double price, int number, String information, String housename, String address, String label, int huId){
        return houseDao.insertHouse(picture, price, number, information, housename, address, label, huId);
    }

    //下架房源
    @Override
    public int deleteHouse(int houseId){
        return houseDao.deleteHouse(houseId);
    }
    //修改房源价格
    @Override
    public int updatePrice(String housename,double price){
        return houseDao.updatePrice(housename,price);
    }
    //修改图片
    @Override
    public int updatePicture(String housename,String picture){
        return houseDao.updatePicture(housename,picture);
    }
    //商家查看自己所有房源
    @Override
    public List<House> checkSelfHouse(int huId){
        return houseDao.checkSelfHouse(huId);
    }
    //预定成功后，numbered会增加
    @Override
    public int updateNumbered(String housename,int numbered){
        return houseDao.updateNumbered(housename,numbered);
    }
    //退订成功，numbered会减少
    @Override
    public int reduceNumbered(String housename,int numbered){
        return houseDao.reduceNumbered(housename,numbered);
    }
    //关于房子名字模糊查找
    @Override
    public List<House> checkDimName(String housename){
        return houseDao.checkDimName(housename);
    }
    //名字的具体查询
    @Override
    public House checkName(String housename){
        return houseDao.checkName(housename);
    }
    //通过房源id，查询房源信息
    @Override
    public House checkId(int houseId){
        return houseDao.checkId(houseId);
    }

    @Override
    public List<House> checkAllScore() {
        return houseDao.checkAllScore();
    }

    //推荐,根据风格进行模糊查询

    @Override
    public List<House> checkAllScores(String label) {
        return houseDao.checkAllScores(label);
    }

    @Override
    public House checkHotelAndUser(int houseId) {
        return houseDao.checkHotelAndUser(houseId);
    }

    @Override
    public int updateHouseScore(int houseId, double score) {
        return houseDao.updateHouseScore(houseId,score);
    }

    @Override
    public List<House> recommendHotel(String address, String label) {
        return houseDao.recommendHotel(address,label);
    }

    @Override
    public List<House> recommendHotelPrice(String address, String label) {
        return houseDao.recommendHotelPrice(address,label);
    }

    @Override
    public List<House> recommendHotelScore(String address, String label) {
        return houseDao.recommendHotelScore(address,label);
    }

    @Override
    public List<House> recommendHotelScorePrice(String address, String label) {
        return houseDao.recommendHotelScorePrice(address,label);
    }

    @Override
    public List<House> randomHotel() {
        return houseDao.randomHotel();
    }
}
