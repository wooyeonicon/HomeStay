package yh.recommend.dao;

import org.apache.ibatis.annotations.Param;
import yh.recommend.entity.User;

import java.util.List;

public interface UserDao {
    //注册管理员（用户名，密码，电话，权限=2）
    int insertAdministrator(@Param("username") String username,
                            @Param("password") String password,
                            @Param("phone") String phone,
                            @Param("category") int category);
    //管理员登录（通过用户名，密码,权限）
    User loginAdministrator(@Param("username") String username,
                            @Param("password") String password,
                            @Param("category") int category);

    //检查是否为存在该用户（用户名）
    User checkUser(@Param("username") String username);

    //预订成功，普通用户积分+5
    int increaseIntegral(@Param("username") String username);
    //退订成功，普通用户积分-5
    int reduceIntegral(@Param("username") String username);
    //惩罚机制，取消用户积分
    int cancelIntegral(@Param("username") String username);

    //查询所有普通用户(或者商家)的积分信息（信息）
    List<User> checkUsers(@Param("category") int category);

    /**
     * 普通用户
     *
     */
    //注册普通用户
    int insertUser(@Param("username") String username,
                   @Param("password") String password,
                   @Param("phone") String phone,
                   @Param("category") int category,
                   @Param("style") String style);
    //登录相同

    //商家注册相同
    //商家登录相同
    //使用用户id查看
    User checkUserId(@Param("userId") int userId);

    //推荐第二步
    int updateStyle(@Param("username") String username,@Param("style") String style);

    //修改个人信息
    int updateUserInfo(@Param("userId") int userId,@Param("username") String username,@Param("phone") String phone);
}
