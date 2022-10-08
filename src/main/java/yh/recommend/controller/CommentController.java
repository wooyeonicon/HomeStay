package yh.recommend.controller;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yh.recommend.entity.*;
import yh.recommend.service.*;
import yh.recommend.utils.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HouseAndOrderAndCommentService houseAndOrderAndCommentService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;// 转到方法中的话，return “redirect:方法Mapping”

    /**
     * //添加评论
     *     int insertComment(String comments,int score,int coId,int chId);
     *     //进行回复
     *     int updateComment(int commentId,String reply);
     *     //查看所有评论
     *     List<Comment> checkAll(int chId);
     */


    //添加评论(用户添加评论)(1)：用户评价完成以后，将会计算出入住这个房子所有的人，所得出的平均的评分。修改house中的score
    /**
     * username
     * comments
     * score
     * coId
     *评论只能在确认订单以后，才可以由普通用户进行评论。
     * int insertComment(String comments,int score,int coId,int chId);
     */
    @RequestMapping(value = {"/user/commitComments"},method = RequestMethod.POST)
    @ResponseBody
    public Result insertComments(@RequestBody Map<String, Object> map, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==0){
            Order order = orderService.checkOrder(Integer.parseInt(map.get("orderId").toString()));  //订单Id
            if(order != null && order.getState()==1){
                int i = commentService.insertComment(map.get("comments").toString(), //评论
                        Integer.parseInt(map.get("score").toString()), //评分
                        order.getOrderId(), //订单Id
                        order.getOhId() //房源Id
                );
                if(i>0){
                    List<Comment> comments = commentService.checkAll(order.getOhId());
                    int counts = comments.size();
                    double sum = 0;
                    for(Comment c : comments){
                        sum += c.getScore();
                    }
                    double s = 0;
                    s = sum/counts;
                    int j = houseService.updateHouseScore(order.getOhId(),s);
                    return new Result("评论成功",1);
                }else{
                    return new Result("评论失败",0);
                }
            }else{
                return new Result("没有确认",0);
            }
        }else{
            return new Result("权限不足",0);
        }
    }
    //查看所有该房子的评论(1)
    @RequestMapping(value = {"/business/checkAllCommentsByHouseId"},method = RequestMethod.POST)
    @ResponseBody
    public Result CheckAllByHouseIdComments(@RequestBody Map<String, Object> map, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory() == 1){
            List<HouseAndOrderAndComment> houseAndOrderAndComments = houseAndOrderAndCommentService.checkAllCommentsByHouseId(Integer.parseInt(map.get("houseId").toString()));
            if(houseAndOrderAndComments != null){
                return new Result(houseAndOrderAndComments,"查看成功",1);
            }else{
                return new Result("查看失败",0);
            }
        }else{
            return new Result("查看失败",0);
        }
    }

    //进行回复(1)

    /**
     *username
     * commentId
     * reply
     *
     *  int updateComment(int commentId,String reply);
     */
    @RequestMapping(value = {"/business/replyComments"},method = RequestMethod.POST)
    @ResponseBody
    public Result replyComments(@RequestBody Map<String, Object> map, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==1){
            int i = commentService.updateComment(Integer.parseInt(map.get("commentId").toString()),
                        map.get("reply").toString()
                    );
            if(i>0){
                return new Result("回复成功",1);
            }else{
                return new Result("回复失败",0);
            }
        }else{
            return new Result("回复失败",0);
        }
    }
    //查看所有评论

    /**
     * username
     *
     * chId：房源id
     *
     *  List<Comment> checkAll(int chId);
     */
    @RequestMapping(value = {"/commentss"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkComments(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        List<Comment> comments = commentService.checkAll(Integer.parseInt(map.get("chId").toString()));
        if(comments!=null){
            return new Result(comments,"查看成功",1);
        }else{
            return new Result(null,"查看失败",0);
        }
    }

    //查询该房子的一条评论。
    @RequestMapping(value = {"/postHotelComment"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkOneComment(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        House house = houseService.checkId(Integer.parseInt(map.get("houseId").toString()));
        User user = userService.checkUserId(house.getHuId());
        //user.getUsername()商家名
        if(user2.getCategory()==0){
            List<Comment> list = commentService.checkAll(Integer.parseInt(map.get("houseId").toString()));
            if(list.size() == 0){
                return new Result("没有该房源相关评论",0);
            }else{
                Order order = orderService.checkOrder(list.get(0).getCoId());
                User user3 = userService.checkUserId(order.getOuId());
                //user3.getUsername()用户名
                Result r = new Result(list.get(0),user.getUsername(),1,user3.getUsername(),order.getLeavetime());
                List<Result> results = new ArrayList<>();
                results.add(r);
                return new Result(results,"查询成功",1);
            }
        }else{
            return new Result("查询失败",0);
        }
    }


    //查询该房子的所有评论。

    //查询该房子的一条评论。
    @RequestMapping(value = {"/postHotelEveryComments"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkEveryComments(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        House house = houseService.checkId(Integer.parseInt(map.get("houseId").toString()));
        User user = userService.checkUserId(house.getHuId());

        List<Result> results = new ArrayList<>();
        //user.getUsername()商家名
        if(user2.getCategory()==0){
            List<Comment> list = commentService.checkAll(Integer.parseInt(map.get("houseId").toString()));
            for(Comment c : list){
                Order order = orderService.checkOrder(c.getCoId());
                User user3 = userService.checkUserId(order.getOuId());
                //user3.getUsername()用户名
                Result r = new Result(c,user.getUsername(),1,user3.getUsername(),order.getLeavetime());
                results.add(r);
            }
            return new Result(results,"查询成功",1);
        }
        return new Result("查询失败",0);
    }
}
