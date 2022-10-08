package yh.recommend.controller;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yh.recommend.entity.House;
import yh.recommend.entity.Order;
import yh.recommend.entity.OrderAndUserAndHouse;
import yh.recommend.entity.User;
import yh.recommend.service.HouseService;
import yh.recommend.service.OrderAndUserAndHouseService;
import yh.recommend.service.OrderService;
import yh.recommend.service.UserService;
import yh.recommend.utils.CalculationDays;
import yh.recommend.utils.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private OrderAndUserAndHouseService orderAndUserAndHouseService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;// 转到方法中的话，return “redirect:方法Mapping”

    //普通用户查看所有订单(1)
    /**
     * username
     *
     *     List<Order> checkAllOrder(int ouId);
     *
     */
    @RequestMapping(value = {"/user/checkOrders"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkAllOrders(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        Integer p = Integer.parseInt(map.get("page").toString());//传入第几页
        Integer page = (p-1)*4;
        if(user2.getCategory()==0){
            List<OrderAndUserAndHouse> o = orderAndUserAndHouseService.selectByOrderId(user2.getUserId(),page);
            int count = orderAndUserAndHouseService.selectAllCount(user2.getUserId());//总页数
            System.out.println("=====1====="+count);

            if(o!=null){
                Result result1 = new Result(o,count,1,"查看成功");
                System.out.print("======="+result1+"=============");
                return new Result(result1,"查看成功1",1);
            }else{
                return new Result(null,"查看失败",0);
            }
        }else{
            return new Result(null,"查看失败",0);
        }
    }
    //普通用户查看所有预订、确认、退订的订单（state = 0,1,2）(1)
    /**
     * username
     * state
     *
     * List<Order> checkEffective(int state, int ouId);
     */
    @RequestMapping(value = {"/user/checkValidOrders"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkEffectiveOrders(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        if(user2.getCategory()==0){
            Integer p = Integer.parseInt(map.get("page").toString());//传入第几页
            Integer page = (p-1)*4;  //计算传入的页
            List<OrderAndUserAndHouse> o = orderAndUserAndHouseService.selectByOrderIdAndState(user2.getUserId(),page,Integer.parseInt(map.get("state").toString()));
            int counts = orderAndUserAndHouseService.selectAllByStateCount(user2.getUserId(),Integer.parseInt(map.get("state").toString()));
            if(o!=null){
                Result result1 = new Result(o,counts,1,"查看成功");
                return new Result(result1,"查看成功1",1);
            }else{
                return new Result(null,"查看失败",0);
            }
        }else{
            return new Result(null,"查看失败",0);
        }
    }

    //商家查看自己的有效订单(确认入住的订单state<=1)(1)
    /**
     *username
     *state
     * 先根据Id查询出商家，再根据商家查询出他的所有房源，然后再根据所有的房源，查询出每一个房源所对应的订单。（已预订，已退订，已确认）
     *
     */
    @RequestMapping(value = {"/business/CheckAllByBusiness"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkEffectiveOrderss(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        if(user2.getCategory()==1){
            //根据用户的Id查看到所有商家的房子
            //根据房子再查到所有的订单总数，并且订单必须是确认入住的和已预订的。
            //根据房子再查到已预订的订单总数。

            List<House> houses = houseService.checkSelfHouse(user2.getUserId());
            List<Result> list = new ArrayList<>();
            for(House house : houses){
                int count = orderService.CountsByState(house.getHouseId());
                System.out.println("=========count=========="+count);
                int counts = orderService.CountByState(house.getHouseId());
                System.out.println("====================="+counts);
                Result r = new Result(house,count,counts,"查询成功");
                list.add(r);
            }
            if(list!=null){
                return new Result(list,"查看成功",1);
            }else{
                return new Result(null,"查看失败",0);
            }
        }else{
            return new Result(null,"查看失败",0);
        }
    }

    //商家查看到所有关于这个房子的所有预订订单，为确认入住做准备
    @RequestMapping(value = {"/business/CheckAllByBusinessOrders"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkAllOrderByHouseId(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==1){
            List<OrderAndUserAndHouse> lo = orderAndUserAndHouseService.checkAllByHouseId(Integer.parseInt(map.get("houseId").toString()));
            if(lo != null){
                return new Result(lo,"查询成功",1);
            }else{
                return new Result("查询失败",0);
            }
        }else{
            return new Result("查询失败",0);
        }
    }


    //商家计算自己财务（有效订单的钱数）

    /**
     *username
     * state
     */
    @RequestMapping(value = {"/ordersss"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkMoney(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String username1 = request.getHeader("username");
        User user = userService.checkUser(username1);
        Map<String,Double> mapp = null;
        if(user.getCategory()==1){
            List<House> houses = houseService.checkSelfHouse(user.getUserId());
            List<Order> orders = null;
            double money = 0;
            for(House house : houses){
                orders = orderService.checkEffectives(house.getHouseId());
                for(Order order : orders){
                    money += order.getOnumber()*order.getPriced();
                }
            }
            if(money!=0){
                mapp.put("money",money);
                return new Result(mapp,"查看成功",1);
            }else{
                return new Result(null,"查看失败",0);
            }
        }else{
            return new Result(null,"查看失败",0);
        }
    }
    //商家查看一段时间内的财务情况
    /**
     * username
     * state=2
     * start
     * end
     */
//    @RequestMapping(value = {"/orders"},method = RequestMethod.POST)
//    @ResponseBody
//    public Result checkPeriod(@RequestBody Map<String, Object> map, HttpServletRequest request) {
//        String username1 = request.getHeader("username");
//        User user = userService.checkUser(username1);
//        Map<String,Double> mapp = null;
//        if(user.getCategory()==1){
//            List<House> houses = houseService.checkSelfHouse(user.getUserId());
//            List<Order> orders = null;
//            double money = 0;
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date start1 = null;
//            Date end1 = null;
//            try {
//                start1 = dateFormat.parse(map.get("start").toString());
//                end1 = dateFormat.parse(map.get("end").toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            for(House house : houses){
//                orders = orderService.checkEffectives(Integer.parseInt(map.get("state").toString()),
//                        house.getHouseId());
//                for(Order order : orders){
//                    if(start1.before(order.getChecktime()) && end1.after(order.getChecktime())){ //start<入住时间<end
//                        money += order.getOnumber()*order.getPriced();
//                    }
//                }
//            }
//            if(money!=0){
//                mapp.put("money",money);
//                return new Result(mapp,"查看成功",1);
//            }else{
//                return new Result(null,"查看失败",0);
//            }
//        }else{
//            return new Result(null,"查看失败",0);
//        }
//    }
}
