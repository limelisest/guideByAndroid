package com.limelisest.guide.ui.ManagerActivity.manageritem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.zxing.util.Constant;
import com.limelisest.guide.R;
import com.limelisest.guide.databinding.ActivityItemInfoBinding;
import com.limelisest.guide.placeholder.LoginContent;
import com.limelisest.guide.placeholder.MyDataBase;
import com.limelisest.guide.placeholder.PlaceholderContent;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;

public class ItemInfoActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityItemInfoBinding binding;
    public static String item_id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 接收id
        item_id=getIntent().getStringExtra("item_id");
        binding = ActivityItemInfoBinding.inflate(getLayoutInflater());
        binding.textView.setText("你点击的物品ID是："+item_id);
        try {
            Bundle bundle=PlaceholderContent.db.GetItemInfo(item_id);
            binding.editItemName.setText(bundle.getString("name"));
            binding.editItemInfo.setText(bundle.getString("info"));
            binding.editItemPrice.setText(bundle.getString("price"));
            binding.editItemAreaX.setText(bundle.getString("area_x"));
            binding.editItemAreaY.setText(bundle.getString("area_y"));
            binding.editItemQRCode.setText(bundle.getString("QRCODE"));
            binding.editItemENA13.setText(bundle.getString("EAN13"));
            binding.editItemRFID.setText(bundle.getString("RFID"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        binding.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Bundle bundle=new Bundle();
                    bundle.putString("id",item_id);
                    String name=String.valueOf(binding.editItemName.getText());
                    bundle.putString("name", name);
                    bundle.putString("info", String.valueOf(binding.editItemInfo.getText()));
                    bundle.putString("price", String.valueOf(binding.editItemPrice.getText()));
                    bundle.putString("area_x", String.valueOf(binding.editItemAreaX.getText()));
                    bundle.putString("area_y", String.valueOf(binding.editItemAreaY.getText()));
                    bundle.putString("QRCODE", String.valueOf(binding.editItemQRCode.getText()));
                    bundle.putString("EAN13", String.valueOf(binding.editItemENA13.getText()));
                    bundle.putString("RFID", String.valueOf(binding.editItemRFID.getText()));
                    int status=PlaceholderContent.db.UpdateItemInfo(bundle);
                    System.out.println(status);
                    if ( status == 0){
                        Toast.makeText(view.getContext(), "物品(id="+item_id+")"+name+"信息修改成功", Toast.LENGTH_SHORT).show();
                        PlaceholderContent.reflash();
                        finish();
                    }else {
                        Toast.makeText(view.getContext(), "修改失败，未知错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

}