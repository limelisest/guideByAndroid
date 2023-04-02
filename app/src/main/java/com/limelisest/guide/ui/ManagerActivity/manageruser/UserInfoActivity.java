package com.limelisest.guide.ui.ManagerActivity.manageruser;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.limelisest.guide.R;
import com.limelisest.guide.databinding.ActivityUserInfoBinding;
import com.limelisest.guide.placeholder.PlaceholderContent;

import java.sql.SQLException;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    public static String flag = null;
    public static String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 接收id
        id=getIntent().getStringExtra("id");
        Toast.makeText(UserInfoActivity.this, "你点击了"+id, Toast.LENGTH_SHORT).show();
        try {
            Bundle bundle= PlaceholderContent.db.GetUserInfo(id);
            Toast.makeText(UserInfoActivity.this, "读取数据成功", Toast.LENGTH_SHORT).show();
            binding.editUserName.setText(bundle.getString("user_name"));
            binding.editUserPassword.setText(bundle.getString("password"));
            binding.editUserInfo.setText(bundle.getString("info"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}