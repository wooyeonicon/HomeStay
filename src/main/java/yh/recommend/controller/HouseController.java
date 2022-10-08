package yh.recommend.controller;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yh.recommend.entity.House;
import yh.recommend.entity.Order;
import yh.recommend.entity.OrderAndUserAndHouse;
import yh.recommend.entity.User;
import yh.recommend.service.HouseService;
import yh.recommend.service.OrderAndUserAndHouseService;
import yh.recommend.service.OrderService;
import yh.recommend.service.UserService;
import yh.recommend.utils.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static yh.recommend.utils.CalculationDays.getDaysByYearMonth;

@Controller
@CrossOrigin
public class HouseController {
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

    //商家上架房源(1)
    /**
     * username
     *     int insertHouse(@Param("picture") String picture,
     *                     @Param("price") double price,
     *                     @Param("number") int number,
     *                     @Param("information") String information,
     *                     @Param("housename") String housename,
     *                     @Param("address") String address,
     *                     @Param("label") String label,
     *House checkName(String housename);
     */
    @RequestMapping(value = {"/business/oncarriages"},method = RequestMethod.POST)
    @ResponseBody
    public Result shelves(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);


        if(user2.getCategory()==1){
            //如果房源存在，就不可以添加
            House house = houseService.checkName(map.get("housename").toString());

            String s = map.get("label").toString();
            s = s.substring(1,s.length() - 1);
            String str = s.replaceAll( " ",  "");

            if(house!=null){
                return new Result("上架失败",1);
            }else{
                int i = houseService.insertHouse(map.get("imgUrl").toString(),
                        Double.parseDouble(map.get("price").toString()),
                        Integer.parseInt(map.get("number").toString()),
                        map.get("information").toString(),
                        map.get("housename").toString(),
                        map.get("address").toString(),
                        str,
                        user2.getUserId()
                );
                if(i>0){
                    return new Result("上架成功",1);
                }else{
                    return new Result("上架失败",0);
                }
            }
        }else{
            return new Result("上架失败",0);
        }
    }
        /**
         * username
         * housename
         * 下架房源(1)
         *int deleteHouse(String housename);
        */

    @RequestMapping(value = {"/business/undercarriages"},method = RequestMethod.POST)
    @ResponseBody
    public Result upShelves(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date().getTime()); //当前时间String
        Date d1 = null;
        Date d2 = null;

        if(user2.getCategory()==1){
            House house =  houseService.checkId(Integer.parseInt(map.get("houseId").toString()));
            List<Order> orders = orderService.checkByState(house.getHouseId());
            int i=0,flag=0;
            if(orders == null){ // 没有订单的情况
                i = houseService.deleteHouse(house.getHouseId()); //直接下架
                return new Result("下架成功",1);
            }else{          //有订单的情况  0,1,2
                for(Order o : orders){  //2. 订单中没有预订的
                    if(o.getState() == 0){
                        flag=1;
                        break;//如果有订单是预订的，就没办法进行下架。
                    }else if(o.getState() == 1){  //如果订单确认后。
                        try {
                            d1 = dateFormat.parse(o.getLeavetime());//结束时间
                            d2 = dateFormat.parse(date); //当前时间
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(d2.getTime()<d1.getTime()){
                            flag=1;
                            break;
                        }
                    }
                }
                if(flag==0){
                    i = houseService.deleteHouse(house.getHouseId()); //直接下架
                    return new Result("下架成功",1);
                }else{
                    return new Result("下架失败",0);
                }
            }
        }else{
            return new Result("下架失败",0);
        }
    }
    //对房源进行模糊查询
    /**
     * housename
     *     List<House> checkDimName(String housename);
     */
    @RequestMapping(value = {"/housess"},method = RequestMethod.POST)
    @ResponseBody
    public Result fuzzyCheck(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        List<House> houses = houseService.checkDimName(map.get("housename").toString());
        if(houses!=null){
            return new Result(houses,"查询成功",1);
        }else{
            return new Result(null,"查询失败",0);
        }
    }

    //商家查看自己所有房源(这个完成后，才可以下架)
    /**
     * username
     *
     *     List<House> checkSelfHouse(int huId);
     */
    @RequestMapping(value = {"/housesss"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkAll(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String username1 = request.getHeader("username");
        User user = userService.checkUser(username1);
        if(user.getCategory()==1){
            List<House> houses = houseService.checkSelfHouse(user.getUserId());
            if(houses!=null){
                return new Result(houses,"查看成功",1);
            }else{
                return new Result(null,"查看失败",0);
            }
        }else{
            return new Result(null,"查看失败",0);
        }
    }
    //修改价格(1)
    /**
     * username
     * housename
     * price
     *
     *  int updatePrice(String housename,double price);
     */
    @RequestMapping(value = {"/business/updateHouseinfo"},method = RequestMethod.POST)
    @ResponseBody
    public Result updatePrices(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);


        String username1 = request.getHeader("username");
        if(user2.getCategory()==1){
            House house = houseService.checkId(Integer.parseInt(map.get("houseId").toString()));
            if(house.getHuId()==user2.getUserId()){
                int i = houseService.updatePrice(house.getHousename(),
                        Double.parseDouble(map.get("price").toString()));
                if(i>0){
                    return new Result("修改成功",1);
                }else{
                    return new Result("修改失败",0);
                }
            }else{
                return new Result("修改失败",0);
            }
        }else{
            return new Result("修改失败",0);
        }
    }
    //修改图片(不写)

    /**
     * username
     * housename
     * picture
     *
     *    int updatePicture(String housename,String picture);
     */
    @RequestMapping(value = {"/housesssssss"},method = RequestMethod.POST)
    @ResponseBody
    public Result updatePictures(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String username1 = request.getHeader("username");
        User user = userService.checkUser(username1);
        if(user.getCategory()==1){
            House house = houseService.checkName(map.get("housename").toString());
            if(house.getHuId()==user.getUserId()){
                int i = houseService.updatePicture(map.get("housename").toString(),map.get("picture").toString());
                if(i>0){
                    return new Result("修改成功",1);
                }else{
                    return new Result("修改失败",0);
                }
            }else{
                return new Result("修改失败",0);
            }
        }else{
            return new Result("修改失败",0);
        }
    }
    //查看房源
    @RequestMapping(value = {"/selectHouse"},method = RequestMethod.POST)
    @ResponseBody
    public Result selectHouse(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        int houseId1 = Integer.parseInt(map.get("houseId").toString());
//        House house = houseService.checkId(houseId1);
//        User user = userService.checkUserId(house.getHuId());
        House house = houseService.checkHotelAndUser(houseId1);
        return new Result(house,"查询成功",1);

    }

    //查看房源
    @RequestMapping(value = {"/checkHouseInfo"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkHouse(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        int houseId1 = Integer.parseInt(map.get("houseId").toString());
       House house = houseService.checkId(houseId1);
        return new Result(house,"查询成功",1);

    }





    //查询直接的一年的总财政(1)
    @RequestMapping(value = {"/business/checkAllFinancess"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkAllFinances(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==1){
            String s = map.get("year").toString();//一年
            List<OrderAndUserAndHouse> list = new ArrayList<>();
            List<OrderAndUserAndHouse> list1 = orderAndUserAndHouseService.checkAllByHuId(user2.getUserId());//查询该用户的所有订单
            for(OrderAndUserAndHouse ouh : list1){
                String[] s1 = ouh.getChecktime().split("-");
                if(s1[0].equals(s)){
                    list.add(ouh);
                }
            }
            List<Double> list2 = new ArrayList<>();
            for(int j=1;j<=12;j++){
                double counts = 0;
                for(OrderAndUserAndHouse o : list){
                    String[] s1 = o.getChecktime().split("-");
                    if(Integer.parseInt(s1[1]) == j){
                        counts += o.getPriced();
                    }
                }
                list2.add(j-1,counts);
            }
            return new Result(list2,"查询成功",1);
        }
        return new Result("查询失败",0);
    }


    //查询一年中的某个月的财政情况
    @RequestMapping(value = {"/business/checkMonthFinances"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkMonthFinances(@RequestBody Map<String, Object> map, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==1){
            String s = map.get("time").toString();//2022-02
            List<OrderAndUserAndHouse> list = new ArrayList<>();
            List<OrderAndUserAndHouse> list1 = orderAndUserAndHouseService.checkAllByHuId(user2.getUserId());//查询该用户的所有订单
            for(OrderAndUserAndHouse ouh : list1){
                String[] s1 = ouh.getChecktime().split("-");
                String s2 = s1[0]+"-"+s1[1];
                if(s2.equals(s)){
                    list.add(ouh);
                }
            }
            List<Double> list2 = new ArrayList<>();
            if(list.size()>0){
                String[] s1 = list.get(0).getChecktime().split("-");
                int days = getDaysByYearMonth(Integer.parseInt(s1[0]),Integer.parseInt(s1[1]));
                for(int j=1;j<=days;j++){
                    double counts = 0;
                    for(OrderAndUserAndHouse oo : list){
                        String[] s3 = oo.getChecktime().split("-");
                        if(Integer.parseInt(s3[2]) == j){
                            counts += oo.getPriced();
                        }
                    }
                    list2.add(j-1,counts);
                }
                List<String> ss2 = new ArrayList<>();
                for(int i=0;i<list2.size();i++){
                    String s3 = (i+1)+"";
                    ss2.add(s3);
                }
                Result r =  new Result(list2,"查询成功",1,ss2,"success");
                return new Result(r,"查询成功",1);
            }else{
                return new Result("查询失败",0);
            }
        }else{
            return new Result("查询失败",0);
        }
    }

    //随机生成五个（样式，随机，图片样式，数据）
    @RequestMapping(value = {"/randomHotel"},method = RequestMethod.POST)
    @ResponseBody
    public Result randomHotels(@RequestBody Map<String, Object> map, HttpServletRequest request){
        List<House> houses = houseService.randomHotel();
        return new Result(houses,"查询成功",1);
    }
}
