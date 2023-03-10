package com.limelisest.guide.placeholder;

import java.sql.SQLException;

public class LoginContent {
    public static String LoginUser=null;
    public static String LoginPassword=null;
    MyDataBase db;
    public LoginContent(){
        try {
            db = new MyDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int LoginUser(String USER,String PASSWORD) throws SQLException {
        int status=db.LoginUser(USER,PASSWORD);
        if (status == 0){
            LoginUser=USER;
            LoginPassword=PASSWORD;
        }
        return status;

    }
}
