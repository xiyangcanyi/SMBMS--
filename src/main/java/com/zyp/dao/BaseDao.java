package com.zyp.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    static{//静态代码块,在类加载的时候执行
        init();
    }

    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    public static void init(){
        Properties properties = new Properties();
        InputStream resourceAsStream = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver=properties.getProperty("driver");
        url=properties.getProperty("url");
        user=properties.getProperty("user");
        password=properties.getProperty("password");

    }
     //获取数据库连接
    public static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    //编写查询公共类
    public static ResultSet execute(Connection connection, PreparedStatement statement,ResultSet resultSet,String sql,Object[] params ) throws Exception{
        statement = connection.prepareStatement(sql);
        for(int i=0;i<params.length;i++){
            //setObject,占位符从1开始，但我们的数组时从0开始得
            statement.setObject(i+1,params[i]);
        }
        resultSet=statement.executeQuery();
        return resultSet;
    }
    public static int execute(Connection connection, PreparedStatement statement,String sql,Object[] params ) throws Exception{
        statement = connection.prepareStatement(sql);
        for(int i=0;i<params.length;i++){
            //setObject,占位符从1开始，但我们的数组时从0开始得
            statement.setObject(i+1,params[i]);
        }
        int updateRows=statement.executeUpdate();
        return updateRows;
    }
    //释放资源
    public static boolean closeResource(Connection connection,PreparedStatement statement,ResultSet resultSet){
            boolean flag=true;
            if(resultSet!=null){
                try {
                    resultSet.close();
                    resultSet=null;
                } catch (SQLException e) {
                    e.printStackTrace();
                    flag=false;
                }
            }
        if(statement!=null){
            try {
                statement.close();
                statement=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if(connection!=null){
            try {
                connection.close();
                connection=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        return flag;
    }


}
