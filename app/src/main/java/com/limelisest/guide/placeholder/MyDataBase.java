package com.limelisest.guide.placeholder;

import android.os.Bundle;
import android.util.Log;

import com.mysql.jdbc.PreparedStatement;

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

    public List<PlaceholderContent.PlaceholderItem> GetItemList() throws SQLException {
        // 列表，用于存放 Map
        List<PlaceholderContent.PlaceholderItem> ITEMS = new ArrayList<PlaceholderContent.PlaceholderItem>();

        // Map 类型的变量，用于存放数据
        Map<String, PlaceholderContent.PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderContent.PlaceholderItem>();

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
            String num = String.valueOf(GetItemNum(id));
            PlaceholderContent.PlaceholderItem item =new PlaceholderContent.PlaceholderItem(id,name,price,"1",num);
            ITEMS.add(item);
            ITEM_MAP.put(item.id, item);
        }
        statement.close();
        return ITEMS;
    }
    public String GetItemNum(String id)throws SQLException{
        //获取用于向数据库发送sql语句的statement
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from stock where id='"+id+"'";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            return String.valueOf(rs.getInt("num"));
        }
        statement.close();
        return "0";
    }
    public List<PlaceholderContent.PlaceholderUser> GetUserList() throws SQLException {
        // 列表，用于存放 Map
        List<PlaceholderContent.PlaceholderUser> ITEMS = new ArrayList<PlaceholderContent.PlaceholderUser>();

        //获取用于向数据库发送sql语句的statement
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from user";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            String id = String.valueOf(rs.getInt("id"));
            String user_name = String.valueOf(rs.getString("user_name"));
            PlaceholderContent.PlaceholderUser item =new PlaceholderContent.PlaceholderUser(id,user_name);
            ITEMS.add(item);
        }
        statement.close();
        return ITEMS;
    }

    public Bundle GetItemInfo(String item_id) throws SQLException{
        Bundle ITEMS = new Bundle();
        assert connection != null;
        Statement statement = connection.createStatement();
        String sql = "select * from item where id='"+item_id+"'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            String name =String.valueOf(rs.getString("name"));
            String info=String.valueOf(rs.getString("info"));
            String price=String.valueOf(rs.getDouble("price"));
            String area_x=String.valueOf(rs.getInt("area_x"));
            String area_y=String.valueOf(rs.getInt("area_y"));
            String QRCODE=String.valueOf(rs.getString("QRCODE"));
            String EAN13=String.valueOf(rs.getString("EAN13"));
            String RFID=String.valueOf(rs.getString("RFID"));

            ITEMS.putString("name",name);
            ITEMS.putString("info",info);
            ITEMS.putString("price",price);
            ITEMS.putString("area_x",area_x);
            ITEMS.putString("area_y",area_y);
            ITEMS.putString("QRCODE",QRCODE);
            ITEMS.putString("EAN13",EAN13);
            ITEMS.putString("RFID",RFID);
        }
        statement.close();
        String num=GetItemNum(item_id);
        ITEMS.putString("num",num);
        return ITEMS;
    }

    public int UpdateItemInfo(Bundle data) throws SQLException{
        String id=data.getString("id");
        String name =data.getString("name");
        String info=data.getString("info");
        String price=data.getString("price");
        String num= data.getString("num");
        String area_x=data.getString("area_x");
        String area_y=data.getString("area_y");
        String QRCODE=data.getString("QRCODE");
        String EAN13=data.getString("EAN13");
        String RFID=data.getString("RFID");
        String sql="update item set " +
                "name='"+name+"'," +
                "info='"+info+"'," +
                "price='"+price+"'," +
                "area_x='"+area_x+"'," +
                "area_y='"+area_y+"'," +
                "QRCODE='"+QRCODE+"'," +
                "EAN13='"+EAN13+"'," +
                "RFID='"+RFID+"' where id='"+id+"'";
        Statement statement = connection.createStatement();
        int rs = statement.executeUpdate(sql);
        int rs2 = 0;
        int rs_row=0;
        ResultSet rs3=statement.executeQuery("SELECT * FROM `stock` WHERE `id` = +"+id+" ORDER BY `id` ASC");
        if (rs3.next()){
            rs_row++;
        }
        if (rs_row <= 0 ){
            statement.executeUpdate("insert into stock set id='"+id+"',num='"+num+"'");
        }
        if (rs_row >0){
            rs2=statement.executeUpdate("update stock set num='"+num+"' where id='"+id+"'");
        }


        statement.close();
        if (rs != 0 && rs2 !=0){
            return 0;
        }
        return -1;
    }

    public int LoginUser(String USER,String PASSWORD) throws SQLException {
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from user where user_name='" +USER+ "' and password='"+PASSWORD+"'";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            statement.close();
            return 0;
        }
        statement.close();
        return -1;
    }

    public int RegisterUser(String USER,String PASSWORD) throws SQLException {
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from user where user_name='" +USER+ "'";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            return 1;//检测到已有用户名
        }
        //插入新用户
//        String sql2= String.format("INSERT INTO `guide`.`user` (`id`, `user_name`, `password`, `info`) VALUES (NULL, '%s', '%s', NULL);", USER,PASSWORD);
        PreparedStatement ps = (PreparedStatement) connection.prepareStatement("INSERT INTO `guide`.`user` (`id`, `user_name`, `password`, `info`) VALUES (NULL, ?, ?, NULL);");
        ps.setString(1,USER);
        ps.setString(2,PASSWORD);
        int status= ps.executeUpdate();
//        ResultSet rs2 = statement.executeQuery(sql2);
        if (status !=0 ){
            statement.close();
            return 0;
        }
        statement.close();
        return -1;
    }

    public int LoginAdmin(String USER,String PASSWORD) throws SQLException {
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = "select * from operator where user_name='" +USER+ "' and password='"+PASSWORD+"'";
        //向数据库发送sql，并获取代表结果集的resultSet
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            statement.close();
            return 0;
        }
        statement.close();
        return -1;
    }
    public Bundle GetUserInfo(String id) throws SQLException{
        Bundle USER = new Bundle();
        assert connection != null;
        Statement statement = connection.createStatement();
        String sql = "select * from user where id='"+id+"'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()){
            String user_name =String.valueOf(rs.getString("user_name"));
            String password=String.valueOf(rs.getString("password"));
            String info=String.valueOf(rs.getString("info"));

            USER.putString("user_name",user_name);
            USER.putString("password",password);
            USER.putString("info",info);

        }
        statement.close();
        return USER;
    }
}
