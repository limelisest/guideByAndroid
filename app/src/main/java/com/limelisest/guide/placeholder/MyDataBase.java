package com.limelisest.guide.placeholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.util.BitmapUtil;
import com.mysql.jdbc.BlobFromLocator;
import com.mysql.jdbc.PreparedStatement;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Blob;
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

    public List<PlaceholderContent.PlaceholderItem> GetItemList(String _class) throws SQLException {

        // 列表，用于存放 Map
        List<PlaceholderContent.PlaceholderItem> ITEMS = new ArrayList<PlaceholderContent.PlaceholderItem>();

        // Map 类型的变量，用于存放数据
        Map<String, PlaceholderContent.PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderContent.PlaceholderItem>();

        //获取用于向数据库发送sql语句的statement
        assert connection != null;
        Statement statement = connection.createStatement();
        //sql语句
        String sql = null;
        if(Objects.equals(_class, "所有商品")){
            sql = "select * from item";
        }else {
            sql = "select * from item where class='"+_class+"'";
        }
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
            String _class=String.valueOf(rs.getString("class"));

            ITEMS.putString("name",name);
            ITEMS.putString("info",info);
            ITEMS.putString("price",price);
            ITEMS.putString("area_x",area_x);
            ITEMS.putString("area_y",area_y);
            ITEMS.putString("QRCODE",QRCODE);
            ITEMS.putString("EAN13",EAN13);
            ITEMS.putString("RFID",RFID);
            ITEMS.putString("class",_class);

        }
        statement.close();
        String num=GetItemNum(item_id);
        ITEMS.putString("num",num);
        return ITEMS;
    }
    public List<String> GetItemClass() throws SQLException {
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery("SELECT class FROM item GROUP BY class");
        List<String> itemClass=new ArrayList<>();
        itemClass.add("所有商品");
        while (rs.next()){
            itemClass.add(rs.getString("class"));
        }
        return itemClass;
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
        String _class=data.getString("class");
        String sql="update item set " +
                "name='"+name+"'," +
                "info='"+info+"'," +
                "price='"+price+"'," +
                "area_x='"+area_x+"'," +
                "area_y='"+area_y+"'," +
                "QRCODE='"+QRCODE+"'," +
                "EAN13='"+EAN13+"'," +
                "class='"+_class+"'," +
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
    public int UpdateItemPicture(String id, Bitmap bitmap) throws SQLException {
        //转换图片为Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //查询存在
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery("select count(*) from picture where id='"+id+"'");

        int rs_num=0;
        while (rs.next()){
            rs_num=rs.getInt("count(*)");
        }
        if (rs_num == 0 ){
            //不存在 ，创建图片
            PreparedStatement insert=(PreparedStatement)connection.prepareStatement(
                    "insert into picture values(?,?)",Statement.RETURN_GENERATED_KEYS);
            insert.setInt(1, Integer.parseInt(id));
            insert.setString(2, imageString);
            int status=insert.executeUpdate();
            if(status != 0){
                return 0;
            }
        }else {
            // 存在，修改图片
            //不存在 ，创建图片
            PreparedStatement insert=(PreparedStatement)connection.prepareStatement(
                    "update picture set picture=? where id=?",Statement.RETURN_GENERATED_KEYS);
            insert.setString(1, imageString);
            insert.setInt(2, Integer.parseInt(id));
            int status=insert.executeUpdate();
            if(status != 0){
                return 0;
            }
        }
        statement.close();
        return -1;
    }
    public Bitmap GetItemPicture(String id) throws SQLException{
        //查询存在
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery("select * from picture where id='"+id+"'");
        Bitmap bitmap=null;
        while (rs.next()){
            String imageString=rs.getString("picture");
            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return bitmap;
    }
    public List<PlaceholderContent.PlaceholderItem> GetShoppingCarItemNumList(String user_name) throws SQLException {
        List<PlaceholderContent.PlaceholderItem> ITEMS=new ArrayList<>();
        assert connection != null;
        Statement statement = connection.createStatement();
        ResultSet rs=statement.executeQuery("select * from shopping_car where user_name='"+user_name+"'");
        while (rs.next()){
            String item_id=String.valueOf(rs.getInt("item_id"));
            String num=String.valueOf(rs.getInt("num"));
            Bundle bundle=GetItemInfo(item_id);
            String num_stock=GetItemNum(item_id);
            String name=bundle.getString("name");
            String price=bundle.getString("price");
            PlaceholderContent.PlaceholderItem item=new PlaceholderContent.PlaceholderItem(item_id,name,price,num,num_stock);
            ITEMS.add(item);
        }
        statement.close();
        return ITEMS;
    }

    public int SetShoppingCarItem(String item_id,String num) throws SQLException {
        String user_name=LoginContent.LoginUser;
        // 查询 ShoppingCar 数据库内有无符合条件的项目
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery("select * from shopping_car where user_name='"+user_name+"' and item_id='"+item_id+"'");
        int rs2=0;
        int rs3=0;
        int rs4=0;
        int i=0;
        while (rs.next()){
            i++;
        }
        if(i==0){
            rs3 = statement.executeUpdate("insert into shopping_car set user_name='"+user_name+"',item_id='"+item_id+"',num='"+num+"'");
        }else{
            if (Integer.parseInt(num)>0){
                rs2 =statement.executeUpdate("update shopping_car set num='"+num+"' where user_name='"+user_name+"' and item_id='"+item_id+"'");
            }else {
                rs4=statement.executeUpdate("delete from shopping_car where user_name='"+user_name+"' and item_id='"+item_id+"'");
            }

        }
        if (rs2!=0 | rs3!=0 | rs4!=0){
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
        PreparedStatement ps = (PreparedStatement) connection.prepareStatement("INSERT INTO `guide`.`user` (`id`, `user_name`, `password`, `info`) VALUES (NULL, ?, ?, NULL);");
        ps.setString(1,USER);
        ps.setString(2,PASSWORD);
        int status= ps.executeUpdate();
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

    public int UpdateUserInfo(Bundle data) throws SQLException{
        String id=data.getString("id");
        String user_name=data.getString("user_name");
        String password=data.getString("password");
        String info=data.getString("info");
        assert connection != null;
        Statement statement = connection.createStatement();

        int status=statement.executeUpdate("update user set user_name='"+user_name+"',password='"+password+"',info='"+info+"' where id='"+id+"'");
        if (status != 0){
            statement.close();
            return 0;
        }
        statement.close();
        return -1;
    }
}
