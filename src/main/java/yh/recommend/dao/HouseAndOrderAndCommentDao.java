package yh.recommend.dao;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.HouseAndOrderAndComment;

import java.util.List;

public interface HouseAndOrderAndCommentDao {
    List<HouseAndOrderAndComment> checkAllCommentsByHouseId(@Param("houseId") int houseId);
}
