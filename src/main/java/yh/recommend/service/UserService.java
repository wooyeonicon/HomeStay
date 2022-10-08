package yh.recommend.service;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.User;

import java.util.List;

public interface UserService {
    //添加系统管理员（用户名，密码，电话，权限）
    int insertAdministrator(String username,String password,String phone,int category);
    //管理员登录（通过用户名，密码）
    User loginAdministrator(String username,String password,int category);

    //检查是否为普通用户（用户名）
    User checkUser(String username);
    //预订成功，普通用户积分+5
    int increaseIntegral(String username);
    //退订成功，普通用户积分-5
    int reduceIntegral(String username);
    //惩罚机制，取消用户积分
    int cancelIntegral(String username);

    //查询所有普通用户(或者商家)的积分信息（信息）
    List<User> checkUsers(int category);

    /////////////////////////////////////
    //注册普通用户
    int insertUser(String username, String password, String phone, int category, String style);
    //使用用户id查看
    User checkUserId(int userId);

    //推荐第二步
    int updateStyle(String username,String style);

    //修改个人信息
    int updateUserInfo(int userId,String username,String phone);
}
