package yh.recommend.service.impl;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yh.recommend.dao.UserDao;
import yh.recommend.entity.Order;
import yh.recommend.entity.User;
import yh.recommend.service.UserService;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    //注册系统管理员
    @Override
    public int insertAdministrator(String username,String password,String phone,int category){
        return(userDao.insertAdministrator(username,password,phone,category));
    }
    //管理员登录
    @Override
    public User loginAdministrator(String username, String password, int category){
        User user = userDao.loginAdministrator(username,password,category);
        if(user!=null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
    //检查是否为普通用户（用户名）
    @Override
    public User checkUser(String username){
//        User user = userDao.checkUser(username);
//        if(user.getCategory()==0){
//            return user;
//        }
//        return null;
        return userDao.checkUser(username);
    }
    //预订成功，普通用户积分+5
    @Override
    public int increaseIntegral(String username){
        return (userDao.increaseIntegral(username));
    }
    //退订成功，普通用户积分-5
    @Override
    public int reduceIntegral(String username){
        return (userDao.reduceIntegral(username));
    }
    //惩罚机制，取消用户积分
    @Override
    public int cancelIntegral(String username){
        return (userDao.cancelIntegral(username));
    }

    //查询所有普通用户(或者商家)的积分信息（信息）
    @Override
    public List<User> checkUsers(int category){
        return userDao.checkUsers(category);
    }

    ////////////////////////////////
    //注册普通用户
    @Override
    public int insertUser(String username, String password, String phone, int category, String style){
        return (userDao.insertUser(username,password,phone,category,style));
    }
    //使用用户id查看
    @Override
    public User checkUserId(int userId){
        return userDao.checkUserId(userId);
    }

    //推荐第二步


    @Override
    public int updateStyle(String username, String style) {
        return userDao.updateStyle(username,style);
    }

    @Override
    public int updateUserInfo(int userId, String username, String phone) {
        return userDao.updateUserInfo(userId,username,phone);
    }
}
