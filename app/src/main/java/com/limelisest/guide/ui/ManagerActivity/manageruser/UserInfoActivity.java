package com.limelisest.guide.ui.ManagerActivity.manageruser;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
                String user_name= String.valueOf(binding.editUserName.getText());
                String password= String.valueOf(binding.editUserPassword.getText());
                String info= String.valueOf(binding.editUserInfo.getText());
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                bundle.putString("user_name",user_name);
                bundle.putString("password",password);
                bundle.putString("info",info);
                if (user_name.equals("") | password.equals("")){
                    Toast.makeText(UserInfoActivity.this,"用户名或者密码为空",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        int status = PlaceholderContent.db.UpdateUserInfo(bundle);
                        if (status==0){
                            Toast.makeText(UserInfoActivity.this,"用户信息更新成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if(status == -1){
                            Toast.makeText(UserInfoActivity.this,"更新失败，未知错误",Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}