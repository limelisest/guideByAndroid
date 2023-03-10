package com.limelisest.guide.placeholder;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyDataBase {
    // 列表，用于存放 Map
    public static final List<PlaceholderContent.PlaceholderItem> ITEMS = new ArrayList<PlaceholderContent.PlaceholderItem>();

    // Map 类型的变量，用于存放数据
    public static final Map<String, PlaceholderContent.PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderContent.PlaceholderItem>();

    private static final String URL = "jdbc:mysql://120.79.71.233:3306/guide";
    private static final String USER = "guide";
    private static final String PASSWORD = "lime";
    private final Connection connection = getConn();

    public MyDataBase() throws SQLException {
    }

    private Connection getConn() throws SQLException {
        Connection connection = null;
        try{
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //获取与数据库的连接
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Log.e("sql","connect to SQL :"+URL+"\n");

        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    List<PlaceholderContent.PlaceholderItem> GetItemList() throws SQLException {
        //获取用于向数据库发送sql语句的statement
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from item";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            String id = String.valueOf(rs.getInt("id"));
            String name = String.valueOf(rs.getString("name"));
            String price = String.valueOf(rs.getString("price"));
            PlaceholderContent.PlaceholderItem item =new PlaceholderContent.PlaceholderItem(id,name,price,"1");
            ITEMS.add(item);
            ITEM_MAP.put(item.id, item);
        }
        return ITEMS;
    }

    public int LoginUser(String USER,String PASSWORD) throws SQLException {
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from user where user_name='" +USER+ "' and password='"+PASSWORD+"'";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            System.out.println(rs);
            return 0;
        }
        return -1;
    }

    public int RegisterUser(String USER,String PASSWORD1,String PASSWORD2) throws SQLException {
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from user where user_name='" +USER+ "'";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            return 1;//检测到已有用户名
        }
        if (!Objects.equals(PASSWORD1, PASSWORD2)){
            return 2;//密码不相同
        }
        //插入新用户
        String sql2= String.format("INSERT INTO `guide`.`user` (`id`, `user_name`, `password`, `info`) VALUES (NULL, '%s', '%s', NULL);", USER,PASSWORD1);
        ResultSet rs2 = statement.executeQuery(sql2);
        if (rs2.next()){
            return 0;
        }
        return -1;
    }
}
