package com.zyp.dao.user;

import com.mysql.jdbc.StringUtils;
import com.zyp.dao.BaseDao;
import com.zyp.pojo.Role;
import com.zyp.pojo.User;
import com.zyp.service.UserServiceImpl;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws Exception{
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        User user=null;
        if(connection!=null){
        String sql="select * from smbms_user where userCode=?";
        Object[] params={userCode};
        rs=BaseDao.execute(connection,preparedStatement,rs,sql,params);
        if(rs.next()){
            user=new User();
            user.setId(rs.getInt("id"));
            user.setUserCode(rs.getString("userCode"));
            user.setUserName(rs.getString("userName"));
            user.setUserPassword(rs.getString("userPassword"));
            user.setGender(rs.getInt("gender"));
            user.setBirthday(rs.getDate("birthday"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            user.setUserRole(rs.getInt("userRole"));
            user.setCreatedBy(rs.getInt("createdBy"));
            user.setCreationDate(rs.getTimestamp("creationDate"));
            user.setModifyBy(rs.getInt("modifyBy"));
            user.setModifyDate(rs.getTimestamp("modifyDate"));
        }
        BaseDao.closeResource(null,preparedStatement,rs);

    }
     return user;
    }
    public int updatePwd(Connection connection,int id,String password) throws Exception{

        PreparedStatement pstm = null;
        int execute =0;
        if(connection!=null) {
            String sql = "update smbms_user set  userPassword = ? where id = ?";
            Object[] params = {password,id};
            execute = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }

        System.out.println("UserDaoImpl1"+password);
        return execute;

    }

    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
      PreparedStatement pstm=null;
      ResultSet rs=null;
      int count=0;
      if(connection!=null){
          StringBuffer sql=new StringBuffer();
          sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole=r.id");

          //定义一个列表，用于存放我们的参数
          ArrayList<Object> list = new ArrayList<Object>();

          if (!StringUtils.isNullOrEmpty(username)) {
              sql.append(" and u.userName like ?");
              list.add("%" + username + "%");
          }
          if (userRole > 0) {
              sql.append(" and u.userRole = ?");
              list.add(userRole);
          }

          Object[] parms = list.toArray();
          System.out.println("UserDaoImpl->getUserCount"+sql.toString());
          try {
              rs= BaseDao.execute(connection, pstm, rs, sql.toString(), parms);
          } catch (Exception e) {
              e.printStackTrace();
          }
          if(rs.next()){
               count = rs.getInt("count");
          }
          BaseDao.closeResource(null,pstm,rs);


      }
      return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id ");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //在数据库中，分页使用 limit startIndex，pagesize；总数
            //当前页（当前页-1）*页面大小
            //0，  5      1    0       01234
            //5，  5      2    5       26789
            //10， 5      3    10
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            //System.out.println("sql---->" + sql.toString());
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return userList;
    }



//    @Test
////  public void test(){
////        UserServiceImpl userService = new UserServiceImpl();
////        int userCount = userService.getUserCount(null, 0);
////        System.out.println(userCount);
////    }


}
