package com.limelisest.guide.placeholder;

import com.limelisest.guide.ui.MainActivity.buy_list.ItemFragment;
import com.limelisest.guide.ui.MainActivity.home.HomeFragment;

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

    // 列表，用于存放商品种类
    public static List<String> ItemClass = new ArrayList<String>();
    static {
        try {
            db = new MyDataBase();
            ITEMS = db.GetItemList(ItemFragment.item_class);
            UserITEMS=db.GetUserList();
            ItemClass=db.GetItemClass();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void reflash(){
        try {
            ITEMS = db.GetItemList(ItemFragment.item_class);
            UserITEMS=db.GetUserList();
            ItemClass=db.GetItemClass();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getShoppingCarItemList() {
        try {
            ShoppingCarITEMS =db.GetShoppingCarItemNumList(LoginContent.LoginUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static class PlaceholderItem {
        public final String id;
        public final String name;
        public final String price;
        public String num;
        public String num_stock;

        public PlaceholderItem(String id, String name, String price,String num,String num_stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.num = num;
            this.num_stock=num_stock;
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