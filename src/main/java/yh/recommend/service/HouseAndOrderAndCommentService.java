package yh.recommend.service;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.HouseAndOrderAndComment;

import java.util.List;

public interface HouseAndOrderAndCommentService {
    List<HouseAndOrderAndComment> checkAllCommentsByHouseId(int houseId);

}
