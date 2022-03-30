package com.zyp.dao.user;

import com.zyp.pojo.Role;
import com.zyp.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到登陆的用户
    public User getLoginUser(Connection connection,String userCode) throws Exception;
    //修改当前用户密码
    public int updatePwd(Connection connection,int id,String password)throws Exception;
    //根据用户名和角色查询用户总数
    public  int getUserCount(Connection connection,String username,int userRole) throws SQLException;
   //通过条件查询-userList
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;
   //获取角色列表

}
