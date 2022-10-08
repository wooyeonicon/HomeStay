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
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;


@Controller
@CrossOrigin
public class UserController {
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


    //注册
    /**
     *用户名作为唯一标识
     * username
     * password
     * phone
     * category
     */
    @RequestMapping(value = {"/user/registerUser"},method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestBody Map<String, Object> map, HttpServletRequest request){
        String username1 = map.get("username").toString();
        if(userService.checkUser(username1)!=null){
            return new Result("注册失败",0);
        }else{
            int i = userService.insertAdministrator(
                    username1,
                    map.get("compassword").toString(),
                    map.get("phone").toString(),
                    Integer.parseInt(map.get("radio").toString())
            );
            if(i==0){
                return new Result("注册失败",0);
            }else{
                return new Result("注册成功",1);
            }
        }
    }
    //用户登录（1）
    /**
     * username
     * password
     * category
     */
    @RequestMapping(value = {"/login"},method = RequestMethod.POST)
    @ResponseBody
    public Result postLogin(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {


        User user = userService.loginAdministrator(
                map.get("username").toString(),
                map.get("password").toString(),
                Integer.parseInt(map.get("category").toString())
        );
        System.out.println("==============================="+user);
        if(user!=null){
            //创建token令牌
            String token = UUID.randomUUID() + "";
            System.out.println("==========================="+token);
            //存放到redis数据库中，存放时长
            Duration duration = Duration.ofMinutes(30L);
            String json = com.alibaba.fastjson.JSON.toJSONString(user);//序列化

            redisTemplate.opsForValue().set(token, json, duration.getSeconds());

            System.out.println(token);
            return new Result(user,"登陆成功",1,token);
        }else{
            return new Result("登录失败",0);
        }
    }

    //（管理员）查询所有普通用户信息category=0
    //查询所有商家信息category=1
    /**
     *头中:username
     * category
     *
     */
    @RequestMapping(value = {"/system/checkAllUserinfos"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkAllUserInformation(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        if(user2.getCategory()==2){
            List<User> userss = userService.checkUsers(0);
            return new Result(userss,"查询成功",1);
        }else{
            return new Result("查看失败",0);
        }
    }
    //管理员查看用户的详细信息
    @RequestMapping(value = {"/system/checkUserinfos"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkUserInformation(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==2){
            List<Order> o = orderService.checkEffective(1,Integer.parseInt(map.get("userId").toString()));//用户订单数
            double d = orderService.SumOrderByUserId(Integer.parseInt(map.get("userId").toString()));
            Result r =  new Result(d,"查询成功",o.size());
            List<Result> l = new ArrayList<>();
            l.add(r);
            return new Result(l,"查询成功",1);
        }
        return new Result("查询失败",0);
    }
    //管理员查询用户的详细信息
    @RequestMapping(value = {"/system/checkAllBusinessinfos"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkBusinessInformation(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==2){
            List<User> userss = userService.checkUsers(1);//所有商家
            List<Result> lists = new ArrayList<>();
            for(User u : userss){
                List<House> list = houseService.checkSelfHouse(u.getUserId());
                int j = list.size();
                double sum = 0;
                for(House h : list){
                    double d = orderService.SumOrderByHouseId(h.getHouseId());
                    sum += d;
                }
                Result r = new Result(u,"success",j,sum,"成功");
                lists.add(r);
            }
            return new Result(lists,"查询成功",1);
        }
        return new Result("查询失败",0);
    }
    //用户预订成功（1）
    /**
     * 头中username
     * housename
     * checktime
     * leavetime
     * oname真实姓名
     * onumber数量  : 房源数默认为0
     *
     *注意：不能重复预订同一个民宿
     * 注意注意：不能在一个时间段内预定同一个民宿，其余时间是可以的。
     * 默认state=0
     */
    @RequestMapping(value = {"/HouseOrder"},method = RequestMethod.POST)
    @ResponseBody
    public Result reservation(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);

        House house = houseService.checkId(Integer.parseInt(map.get("houseId").toString()));
        String time = map.get("duration").toString();
        String s1[] = time.split(",");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date().getTime()); //当前时间
        String dateTime1 = dateFormat.format(new Date(Long.valueOf(s1[0]))); //开始时间
        String dateTime2 = dateFormat.format(new Date(Long.valueOf(s1[1])));  //结束时间

        //计算用户的积分
        double price1 = 0;
        if(user2.getIntegral()<=20){   //积分小于等于20（原价）
            price1 = house.getPrice();
        }else if(user2.getIntegral()>20 && user2.getIntegral()<=100){   //积分在20到100之间，9折
            price1 = house.getPrice()*0.9;
        }else{
            price1 = house.getPrice()*0.8;      //积分在100以上，8折
        }

        List<Order> orderList = orderService.checkByState(house.getHouseId());
        if(orderList.size() == 0){   //刚开始启动
            System.out.println("=========进来======");
            if(user2.getCategory()==0) {
                CalculationDays days = new CalculationDays();
                long day = days.CalculationDay(dateTime1, dateTime2);
                System.out.println("====总价格====" + price1 * day);
                int s = orderService.insertOrder(date,
                        dateTime1,
                        dateTime2,
                        map.get("name").toString(),
                        1,
                        price1 * day,   //总价格
                        user2.getUserId(),
                        house.getHouseId());
                if(s>0){
                    return new Result("预订成功,在中间",1);
                }
                else{
                    return new Result("预订失败，没在中间",0);
                }
            }else {
                return new Result("预订失败，没有权限",0);
            }
        }else if(orderList.size() == 1){
            System.out.println("可以进来吗============================");
            Date d1 = null;
            Date d2 = null;
            Date d3 = null;
            Date d4 = null;
            try {
                d1 = dateFormat.parse(orderList.get(0).getLeavetime());//上一个的结束时间
                d2 = dateFormat.parse(orderList.get(0).getChecktime()); //上一个的开始时间
                d3 = dateFormat.parse(dateTime1); //开始时间
                d4 = dateFormat.parse(dateTime2);//结束时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(d1.getTime() < d3.getTime() && d2.getTime() < d3.getTime()){  //比结束时间更大
                if(user2.getCategory()==0) {
                    CalculationDays days = new CalculationDays();
                    long day = days.CalculationDay(dateTime1, dateTime2);
                    System.out.println("====总价格====" + price1 * day);
                    int s = orderService.insertOrder(date,
                            dateTime1,
                            dateTime2,
                            map.get("name").toString(),
                            1,
                            price1 * day,   //总价格
                            user2.getUserId(),
                            house.getHouseId());
                    if(s>0){
                        return new Result("预订成功,在中间",1);
                    }
                    else{
                        return new Result("预订失败，没在中间",0);
                    }
                }else {
                    return new Result("预订失败，没有权限",0);
                }
            }else if(d4.getTime()<d2.getTime() && d3.getTime() < d2.getTime()){   //比开始时间更小
                if(user2.getCategory()==0) {
                    CalculationDays days = new CalculationDays();
                    long day = days.CalculationDay(dateTime1, dateTime2);
                    System.out.println("====总价格====" + price1 * day);
                    int s = orderService.insertOrder(date,
                            dateTime1,
                            dateTime2,
                            map.get("name").toString(),
                            1,
                            price1 * day,   //总价格
                            user2.getUserId(),
                            house.getHouseId());
                    if(s>0){
                        return new Result("预订成功,在中间",1);
                    }
                    else{
                        return new Result("预订失败，没在中间",0);
                    }
                }else {
                    return new Result("预订失败，没有权限",0);
                }
            }else{
                return new Result("预定失败,因为在这个时间内",0);
            }
        }else{ //预订了两个
            System.out.println("当当当===============================");
            Collections.sort(orderList, new Comparator<Order>() {  //从小到大排序
                @Override
                public int compare(Order o1, Order o2) {
                    SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
                    Date sdate = null;
                    Date eDate = null;
                    try {
                        sdate = df.parse(o1.getChecktime());
                        eDate = df.parse(o2.getChecktime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return (int) (sdate.getTime() - eDate.getTime());
                }
            });
            System.out.println("我排序了哦===================================");
            //如果时间在中间的话
            for(int i=0;i<=orderList.size()-2;i++){
                System.out.println("我进循环了========================");
                Date dates = null;
                Date dates1 = null;
                Date dates2 = null;
                Date datess = null;
                Date da1 = null;
                Date da2 = null;

                try {
                    dates = dateFormat.parse(orderList.get(i+1).getChecktime());//下一个的开始时间
                    dates2 = dateFormat.parse(orderList.get(i+1).getLeavetime()); //下一个结束时间
                    dates1 = dateFormat.parse(orderList.get(i).getLeavetime());//上一个的结束时间
                    datess = dateFormat.parse(orderList.get(i).getChecktime()); //上一个的开始时间
                    da1 = dateFormat.parse(dateTime1);
                    da2 = dateFormat.parse(dateTime2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(dates1.getTime() < da1.getTime() && da2.getTime() < dates.getTime()){
                    if(user2.getCategory()==0) {
                        CalculationDays days = new CalculationDays();
                        long day = days.CalculationDay(dateTime1, dateTime2);
                        System.out.println("====总价格====" + price1 * day);
                        int s = orderService.insertOrder(date,
                                dateTime1,
                                dateTime2,
                                map.get("name").toString(),
                                1,
                                price1 * day,   //总价格
                                user2.getUserId(),
                                house.getHouseId());
                        if(s>0){
                            return new Result("预订成功,在中间",1);
                        }
                        else{
                            return new Result("预订失败，没在中间",0);
                        }
                    }else {
                        return new Result("预订失败，没有权限",0);
                    }
                }else if(da2.getTime()<datess.getTime()){
                    if(user2.getCategory()==0) {
                        CalculationDays days = new CalculationDays();
                        long day = days.CalculationDay(dateTime1, dateTime2);
                        System.out.println("====总价格====" + price1 * day);
                        int s = orderService.insertOrder(date,
                                dateTime1,
                                dateTime2,
                                map.get("name").toString(),
                                1,
                                price1 * day,   //总价格
                                user2.getUserId(),
                                house.getHouseId());
                        if (s > 0) {
                            return new Result("预订成功，在前面", 1);
                        } else {
                            return new Result("预订失败，在前面", 0);
                        }
                    }
                }else if(dates2.getTime() < da1.getTime()){
                    if(user2.getCategory()==0) {
                        CalculationDays days = new CalculationDays();
                        long day = days.CalculationDay(dateTime1, dateTime2);
                        System.out.println("====总价格====" + price1 * day);
                        int s = orderService.insertOrder(date,
                                dateTime1,
                                dateTime2,
                                map.get("name").toString(),
                                1,
                                price1 * day,   //总价格
                                user2.getUserId(),
                                house.getHouseId());
                        if (s > 0) {
                            return new Result("预订成功，在后面", 1);
                        } else {
                            return new Result("预订失败，在后面", 0);
                        }
                    }
                }
            }
        }
        return new Result("预订失败，在最外层", 0);
    }


    //退订(1)
    /**
     * 头中：username
     * orderId
     * state=1
     *退订只能在入住时间之前可以退订，在入住时间开始之后，不可以退订
     *
     * Integer.parseInt(map.get("state").toString())
     */
    @RequestMapping(value = {"/user/UnsubscribeStates"},method = RequestMethod.POST)
    @ResponseBody
    public Result unsubscribe(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory()==0){
            int orderId1 = Integer.parseInt(map.get("orderId").toString());
            Order order = orderService.checkOrder(orderId1);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //计算时间
            String nowDate = dateFormat.format(new Date().getTime()); //当前时间
            String startDate = order.getChecktime();  //获取入住时间
            Date nDate = null;
            Date sDate = null;
            try {
                nDate = dateFormat.parse(nowDate);
                sDate = dateFormat.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(nDate.getTime()<=sDate.getTime()){    //当前时间早于入住时间
                int i = orderService.updateOrder(2,
                        orderId1,
                        order.getOuId(),
                        order.getOhId()
                );
                return new Result("退订成功",1);
            }else{
                return new Result("退订失败",0);
            }
        }else{
            return new Result("退订失败",0);
        }
    }
    //确认入住（理论上：确认时间<入住时间)(1)
    /**
     *username
     * orderId
     * state=2
     * 在确认入住后，适当的改变用户的爱好，这样才能更好的实现推荐
     */
    @RequestMapping(value = {"/business/confirmByBusiness"},method = RequestMethod.POST)
    @ResponseBody
    public Result confirm(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);//商家

        //根据订单，确认入住。
        Order order = orderService.checkOrder(Integer.parseInt(map.get("orderId").toString()));
        House house = houseService.checkId(order.getOhId());
        //User user3 = userService.checkUserId(order.getOuId());  //本人
        if(user2.getCategory()==1 && house.getNumber()-house.getNumbered()>= 1){
            User user = userService.checkUserId(order.getOuId());
            List<Order> o = orderService.checkEffective(1,user.getUserId());

            //推荐第三步：
            if(o.size() == 0){  //如果订单数为空
                //推荐第二步
                int k = userService.updateStyle(user.getUsername(),house.getLabel());
            }else if(o.size()>2){  //如果订单数大于2的时候
                //推荐第三步
                List<String> ls = new ArrayList<>();
                List<Order> o11 = orderService.checkEffective(1,user.getUserId());//先用预订的测试一下
                for(Order o1 : o11){
                    House house2 = houseService.checkId(o1.getOhId());
                    String[] s = house2.getLabel().split(",");   //将每个订单的label进行切割
                    for(int n=0;n<s.length;n++){
                        ls.add(s[n]);
                    }
                }
                Set<String> uniqueSet = new HashSet<>(ls);
                Map<String,Integer> maap= new HashMap<>();
                for (String temp : uniqueSet) {
                    int num = Collections.frequency(ls, temp);
                    maap.put(temp, num);
                }
                List<Map.Entry<String,Integer>> list = new ArrayList<>(maap.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String,Integer>>() {
                    @Override
                    public int compare(Map.Entry<String,Integer> cmap,Map.Entry<String,Integer> cmap2){
                        return cmap2.getValue()-cmap.getValue();
                    }
                });
                List<String> l = new ArrayList<>();
                int i=0;
                for(Map.Entry entry : list){
                    l.add((String) entry.getKey());
                    i += 1;
                    if(i==5){
                        break;
                    }
                }
                String join = String.join(",", l);
                int k = userService.updateStyle(user.getUsername(),join);
            }


            int i = orderService.updateOrder(1,
                    Integer.parseInt(map.get("orderId").toString()),
                    order.getOuId(),
                    order.getOhId()
            );
            int j = houseService.updateNumbered(house.getName(),
                    house.getNumbered()+1);//已用房数+onumber

            int s = userService.increaseIntegral(user.getUsername());//积分自动+5
            if(i==0){
                return new Result("确认入住失败",0);
            }else{
                return new Result("确认入住成功",1);
            }
        }else{
            return new Result("确认入住失败",0);
        }
    }

    //确认离开(理论上：离开时间>入住时间，没写)
    /**
     * username
     */
    @RequestMapping(value = {"/shahjsdhjsds"},method = RequestMethod.POST)
    @ResponseBody
    public Result confirmLeave(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if (user2.getCategory() == 1) {
            Order order = orderService.checkOrder(Integer.parseInt(map.get("orderId").toString()));
            House house = houseService.checkId(order.getOhId());

            int j = houseService.reduceNumbered(house.getName(),house.getNumbered()-order.getOnumber());
            if(j>0){
                return new Result("确认离开成功",1);
            }else{
                return new Result("确认离开失败",0);
            }
        }else{
            return new Result("确认离开失败",0);
        }
    }
    //查询个人信息（1）
    /**
     *username
     *
     */
    @RequestMapping(value = {"/user/info"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkMyself(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        //String username1 = request.getHeader("username");
        String username1 = map.get("username").toString();
        User user = userService.checkUser(username1);
        if(user!=null){
            return new Result(user,"查询成功",1);
        }else{
            return new Result(null,"查询失败",0);
        }
    }

    //修改个人信息（1）
    /**
     *username
     *
     */
    @RequestMapping(value = {"/user/edit"},method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserInfo(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        int userId1 = Integer.parseInt(map.get("userId").toString());
        String username = map.get("username").toString();
        String phone = map.get("phone").toString();
        User user = userService.checkUserId(userId1);
        if(user!=null){
            int i = userService.updateUserInfo(userId1,username,phone);
            if(i==1){
                return new Result("修改成功",1);
            }else{
                return new Result("修改成功",0);
            }
        }else{
            return new Result("查询失败",0);
        }
    }

    //管理员查看商家投诉订单
    /**
     * username
     * state
     */
    @RequestMapping(value = {"/dhsjgdhsdghs"},method = RequestMethod.POST)
    @ResponseBody
    public Result checkState(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String username1 = request.getHeader("username");
        User user = userService.checkUser(username1);
        if(user.getCategory()==2){
            List<Order> orders = orderService.checkCancel(Integer.parseInt(map.get("state").toString()));
            if(orders!=null){
               return new Result(orders,"查看成功",1);
            }else{
                return new Result(null,"查看失败",0);
            }
        }else{
            return new Result("查看失败",0);
        }
    }


    //管理员惩罚（消除积分）
    /**
     * username
     * orderId
     */
    @RequestMapping(value = {"/dsghgdhsgdhs"},method = RequestMethod.POST)
    @ResponseBody
    public Result cancel(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String username1 = request.getHeader("username");
        User user = userService.checkUser(username1);
        if(user.getCategory()==2){
            Order order = orderService.checkOrder(Integer.parseInt(map.get("orderId").toString()));
            User user1 = userService.checkUserId(order.getOuId());
            int i = userService.cancelIntegral(user1.getUsername());
            if(i>0){
                return new Result("惩罚成功",1);
            }else{
                return new Result("惩罚失败",0);
            }
        }else{
            return new Result("惩罚失败",0);
        }
    }

    /**
     * 第一步推荐,通过第二步后，又到第三步（1,0）自动
     */
    @RequestMapping(value = {"/recommand"},method = RequestMethod.POST)
    @ResponseBody
    public Result recommend01(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        //String username1 = request.getHeader("username");
//        String username1 = map.get("username").toString();
//        User user = userService.checkUser(username1);
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user = JSON.parseObject(user1, User.class);
        if(user.getCategory()==0){
            if(user.getStyle()==null){//新用户，根据评分越高和价格越低来推荐。
                List<House> houses = houseService.checkAllScore();
                if(houses!=null){
                    return new Result(houses,"查询成功",1);
                }else{
                    return new Result("查询失败",0);
                }
            }else{                        //老用户，根据用户的风格来推荐，同时在预订完成以后，会自动将用户的风格进行更新。
                String[] s = user.getStyle().split(",");//对各种风格进行分割。
                List<House> list = new ArrayList<House>();
                for(int n=0;n<s.length;n++){
                    List<House> tempList = null;
                    tempList = houseService.checkAllScores(s[n]);
                    //tempList = houseDao.checkAllScores(s[n]);
                    for(int j = 0;j < tempList.size(); ++j ) {
                        list.add(tempList.get(j));
                    }
                }
                int flag = 1;
                Map<Integer,Integer> map1 = new HashMap<>();
                for(House h : list){
                    int currId = h.getHouseId();
                    if(map1.containsKey(currId)){
                        Integer i = map1.get(currId);
                        i++;
                        map1.put(currId,i);
                    }else{
                        map1.put(currId,flag);
                    }
                }
                List<Map.Entry<Integer,Integer>> list1=new ArrayList<Map.Entry<Integer,Integer>>(map1.entrySet());
                Collections.sort(list1, new Comparator<Map.Entry<Integer, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                List<House> list3 = new ArrayList<>();
                int p=0;
                for (Map.Entry<Integer, Integer> mapping : list1){
                    //System.out.println(mapping.getKey()+": "+mapping.getValue());
                    House houses = houseService.checkId(mapping.getKey());
                    list3.add(p++,houses);
                    //System.out.println("====================="+houses);
                }
                return new Result(list3,"查询成功",1);
            }
        }else{
            return new Result("查询失败",0);
        }
    }
    /**
     * 选择性推荐
     * /postHotelRecommends
     */
    @RequestMapping(value = {"/postHotelRecommends"},method = RequestMethod.POST)
    @ResponseBody
    public Result recommendHotels(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //取缓存
        String user1 = (String) redisTemplate.opsForValue().get(token);
        //转化格式
        User user2 = JSON.parseObject(user1, User.class);
        if(user2.getCategory() == 0){
            String address = map.get("address").toString();
            String region = map.get("region").toString();//1.好评优先  2.价格优先
            String price = map.get("price").toString();  //1.低价优先  2.高价优先
            String label = map.get("label").toString();
            List<House> house = new ArrayList<>();
            if(map.get("region").toString().equals("1")){  //好评优先
                if(map.get("price").toString().equals("1")){  //低价优先
                    house = houseService.recommendHotel(address,label);//好评优先，低价优先
                }else{                                       //高价优先
                    house = houseService.recommendHotelPrice(address,label);
                }
            }else{                                     //价格优先
                if(map.get("price").toString().equals("1")){  //低价
                    house = houseService.recommendHotelScore(address,label);
                }else{                                       //高价
                    house = houseService.recommendHotelScorePrice(address,label);
                }
            }
            return new Result(house,"查询成功",1);
        }
        return new Result("查询失败",0);
    }

}
