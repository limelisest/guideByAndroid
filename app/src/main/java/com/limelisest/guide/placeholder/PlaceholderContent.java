package com.limelisest.guide.placeholder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderContent {

    // 数据库
    public static MyDataBase db;

    // 列表，用于存放 PlaceholderItem
    public static List<PlaceholderItem> ITEMS;

    // 列表，用于存放 购物车(PlaceholderItem)
    public static List<PlaceholderItem> ShoppingCarITEMS = new ArrayList<PlaceholderItem>();

    // 列表，用于存放 用户列表
    public static List<PlaceholderUser> UserITEMS = new ArrayList<PlaceholderUser>();

    static {
        try {
            db = new MyDataBase();
            ITEMS = db.GetItemList();
            UserITEMS=db.GetUserList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void reflash(){
        try {
            db = new MyDataBase();
            ITEMS = db.GetItemList();
            UserITEMS=db.GetUserList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static class PlaceholderItem {
        public final String id;
        public final String name;
        public final String price;
        public String num;

        public PlaceholderItem(String id, String name, String price,String num) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.num = num;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class PlaceholderUser {
        public final String id;
        public final String user_name;

        public PlaceholderUser(String id, String user_name) {
            this.id = id;
            this.user_name = user_name;
        }

        @Override
        public String toString() {
            return user_name;
        }
    }
}