package yh.recommend.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yh.recommend.entity.House;
import yh.recommend.entity.Order;
import yh.recommend.entity.OrderAndUserAndHouse;
import yh.recommend.entity.User;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static yh.recommend.utils.CalculationDays.getDaysByYearMonth;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class UserDaoTest {
    @Resource
    private UserDao userDao;
    @Resource
    private HouseDao houseDao;
    @Resource
    private OrderAndUserAndHouseDao orderAndUserAndHouseDao;
    @Resource
    private OrderDao orderDao;

    @Test
    public void test(){
        String username = "菜菜管理员";
        String password = "caicai123";
        String phone = "15353676785";
        int category = 2;
        System.out.println("-----------------");
        int i = userDao.insertAdministrator(username,password,phone,category);
        System.out.println("____________"+i);
    }
    @Test
    public void test1(){
        String username = "菜菜管理员";
        String password = "caicai123";
        int category = 2;
        User user = userDao.loginAdministrator(username,password,category);
        System.out.println("*****"+user);
    }
    @Test
    public void test2(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list);
        for(int i : list){
            System.out.println(i);
        }
    }
    @Test
    public void test3(){
        //算法
        String ss = "超赞房东,可开发票,自助入住,近地铁站,可以做饭";
        String[] s = ss.split(",");//对各种风格进行分割。
        List<House> list = new ArrayList<House>();;
        for(int n=0;n<s.length;n++){
            List<House> tempList = null;
            tempList = houseDao.checkAllScores(s[n]);
            for(int j = 0;j < tempList.size(); ++j ) {
                list.add(tempList.get(j));
            }
        }
        int flag = 1;
        Map<Integer,Integer> map = new HashMap<>();
        for(House h : list){
            int currId = h.getHouseId();
            if(map.containsKey(currId)){
                Integer i = map.get(currId);
                i++;
                map.put(currId,i);
            }else{
                map.put(currId,flag);
            }
        }
        List<Map.Entry<Integer,Integer>> list1=new ArrayList<>(map.entrySet());
        Collections.sort(list1, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (Map.Entry<Integer, Integer> mapping : list1){
            System.out.println(mapping.getKey()+": "+mapping.getValue());
            House houses = houseDao.checkId(mapping.getKey());
            System.out.println("====================="+houses);
        }
    }


    @Test
    public void test4(){
        ArrayList<String> list = new ArrayList<>();
        list.add("qqqq");
        System.out.println("============="+list.get(0));

    }
    @Test
    public void test5(){
        HashMap map = new HashMap();
        map.put("A","12,123");
        System.out.println(map.get("A"));
        String time = map.get("A").toString();
        String[] s = time.split(",");
        //String time = map.get("A").toString();
        //String[] strs = time.split(",");
        int start = Integer.parseInt(s[0]);
        int end = Integer.parseInt(s[1]);
        System.out.print("=================="+(start+end)+"=======================");

    }
    @Test
    public void test6(){
        Date date = new Date();
        System.out.println("=========1===="+date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String format = dateFormat.format(new Date(Long.valueOf()));
        //System.out.println("================="+format);
    }
    @Test
    public void test7(){
        int userId = 5;
        int p = 2;
        int page = (p-1)*4;
        List<OrderAndUserAndHouse> lists = orderAndUserAndHouseDao.selectByOrderId(userId,page);
        System.out.println("====================="+lists);
//        for(OrderAndUserAndHouse o : lists){
//            System.out.println("========================"+o);
//        }
    }
    @Test
    public void test8(){
        String time1 = "2022-04-10";
        String time2 = "2022-05-20";
            SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
            Date sdate = null;
            Date eDate = null;
            try {
                sdate = df.parse(time1);
                eDate = df.parse(time2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(sdate.getTime());
            System.out.println(eDate.getTime());

            if(sdate.getTime()<=eDate.getTime()){
                System.out.println("=====yes?=====");
            }
//            long betweendays = ( long) ((eDate.getTime() - sdate.getTime())
//                    / ( 1000 * 60 * 60 * 24) + 0.5); // 天数间隔
//        System.out.println(betweendays+"=======天数=========");

    }
    @Test
    public void test9(){
        List<Order> o = orderDao.checkByState(1);
        System.out.println("============================");
        if(o.size() == 0){
            System.out.println("=========为空=======");
        }
        for(Order o1 : o){
            System.out.println(o1+"===============================");
        }
    }
    @Test
    public void test10(){
        List<String> ls = new ArrayList<>();
        List<Order> o = orderDao.checkEffective(2,5);//先用预订的测试一下
        for(Order o1 : o){
            House house2 = houseDao.checkId(o1.getOhId());
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
        System.out.println(join);
    }
    @Test
    public void test11(){
        String s = "2022";
        int huId = 4;
        int i=0;
        List<OrderAndUserAndHouse> list = new ArrayList<>();
        List<OrderAndUserAndHouse> list1 = orderAndUserAndHouseDao.checkAllByHuId(huId);
        for(OrderAndUserAndHouse ouh : list1){
            String[] s1 = ouh.getChecktime().split("-");
            System.out.println(s1[0]+"===============s1[0]============");
            if(s1[0].equals(s)){
                System.out.println("=============等不等？==========");
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
        System.out.println(list2+"====================================");


    }
    @Test
    public void test12(){
//        String s = "04";
//        int i = 4;
//        if(Integer.parseInt(s) == i){
//            System.out.println("=========True=======");
//        }
        int maxDaysByDate = getDaysByYearMonth(2022, 2);
        System.out.println("================"+maxDaysByDate);
    }
    @Test
    public void test13(){
        String s = "[抢手房源, 可以做饭, 行李寄存, 超赞房东, 可开发票, 灵活取消]";
        s = s.substring(1,s.length() - 1);
        String str2 = s.replaceAll( " ",  "");
        System.out.println("==========================s==="+str2);
    }

    @Test
    public void test14(){
        List<Double> list = new ArrayList<>();
        double d = orderDao.SumOrderByUserId(4);
        if(d == 0.0){
            list.add(0.0);
            System.out.println("======================="+d);
            System.out.println("============"+list);
        }
        list.add(0.0);
        System.out.println("=======1====="+list);

    }
    @Test
    public void test15(){
       String address = "上海";
       String label = "可开发票";
       List<House> house = houseDao.recommendHotel(address,label);
        System.out.println("========================="+house);
    }
    @Test
    public void test16(){
        String username = "xiaoxiaoxiao";
        if(userDao.checkUser(username) != null){
            System.out.println("===========not null===========");
        }else{
            System.out.println(userDao.checkUser(username)+"=========================");
        }
    }

    @Test
    public void tset17(){
        for(int i=0;i<5;i++){
            System.out.println((int)(Math.random()*50+1)+"================================");
        }
    }

}
