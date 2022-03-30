package com.zyp.service;

import com.zyp.dao.BaseDao;
import com.zyp.dao.user.UserDao;
import com.zyp.dao.user.UserDaoImpl;
import com.zyp.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{
    private UserDao userDao;
    public UserServiceImpl()
    {
        userDao=new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String password) {
        Connection connection=null;
        User user=null;

        try {
            connection= BaseDao.getConnection();
            user=userDao.getLoginUser(connection,userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);

        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) throws Exception {

        Connection connection = null;
        boolean flag = false;
        //修改密码
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection, id, pwd) > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);

        }
//        System.out.println("UserService2"+pwd);
         return  flag;

    }

    @Override
    public int getUserCount(String username, int userRole) {
        Connection connection=null;
        int count=0;
        try {
            connection=BaseDao.getConnection();
           count = userDao.getUserCount(connection, username, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userList;

    }

//    @Test
//    public void test(){
//        UserServiceImpl userService = new UserServiceImpl();
//        User admin = userService.login("test", "111");
//        System.out.println(admin.getUserPassword());
//
//    }
}
