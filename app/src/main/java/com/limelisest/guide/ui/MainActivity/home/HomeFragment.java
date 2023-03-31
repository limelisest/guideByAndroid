package com.limelisest.guide.ui.MainActivity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.limelisest.guide.ui.ManagerActivity.ManagerActivity;
import com.limelisest.guide.R;
import com.limelisest.guide.databinding.FragmentHomeBinding;
import com.limelisest.guide.placeholder.LoginContent;
import com.limelisest.guide.placeholder.MyDataBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 为Spinner添加项目
        List<String> list = new ArrayList<String>();
        list.add("用户");
        list.add("管理员");
        ArrayAdapter<String> spinnerDataAdapter = new ArrayAdapter<String>(root.getContext(),
                android.R.layout.simple_spinner_dropdown_item, list);
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLogin.setAdapter(spinnerDataAdapter);
        // 登录状态检测
        if (LoginContent.LoginUser != null){
            binding.editTextUser.setText(LoginContent.LoginUser);
            binding.editTextPassword.setText(LoginContent.LoginPassword);
            binding.editTextUser.setEnabled(false);
            binding.editTextPassword.setEnabled(false);
            binding.spinnerLogin.setEnabled(false);
            if (LoginContent.Admin){
                binding.spinnerLogin.setSelection(1);
            }else {
                binding.spinnerLogin.setSelection(0);
            }
            binding.buttonLogin.setText("登出");
        }
        binding.spinnerLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        LoginContent.Admin=false;
                        binding.editTextUser.setText("admin");
                        binding.editTextPassword.setText("admin");
                        break;
                    case 1:
                        LoginContent.Admin=true;
                        binding.editTextUser.setText("root");
                        binding.editTextPassword.setText("root");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginContent.Admin){
                    if (LoginContent.LoginUser == null){
                        String user= String.valueOf(binding.editTextUser.getText());
                        String password=String.valueOf(binding.editTextPassword.getText());
                        Toast t1= Toast.makeText(root.getContext(), "登录中。。。", Toast.LENGTH_SHORT);
                        t1.show();
                        try {
                            MyDataBase db=new MyDataBase();
                            int status=db.LoginUser(user,password);
                            t1.cancel();
                            if (status==0){
                                LoginContent.LoginUser=user;
                                LoginContent.LoginPassword=password;
                                Toast.makeText(root.getContext(), "登录成功,账号:"+LoginContent.LoginUser, Toast.LENGTH_SHORT).show();
                                SetLoginEnable(false);
                            }else {
                                Toast.makeText(root.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        LoginContent.LoginUser = null;
                        LoginContent.LoginPassword=null;
                        SetLoginEnable(true);
                    }
                }else {
                    if (LoginContent.LoginUser == null){

                        String user= String.valueOf(binding.editTextUser.getText());
                        String password=String.valueOf(binding.editTextPassword.getText());
                        Toast t1= Toast.makeText(root.getContext(), "登录中。。。", Toast.LENGTH_SHORT);
                        t1.show();
                        try {
                            MyDataBase db=new MyDataBase();
                            int status=db.LoginAdmin(user,password);
                            t1.cancel();
                            if (status==0){
                                LoginContent.LoginUser=user;
                                LoginContent.LoginPassword=password;
                                SetLoginEnable(false);
                                Toast.makeText(root.getContext(), "登录成功,管理账号:"+user, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), ManagerActivity.class);
                                startActivityForResult(intent,1);

                            }else {
                                Toast.makeText(root.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        LoginContent.LoginUser = null;
                        LoginContent.LoginPassword=null;
                        SetLoginEnable(true);
                    }
                }


            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_navigation_home_to_registerFragment);
            }
        });
        return root;
    }

    private void SetLoginEnable(boolean flag){
        if (flag){
            binding.editTextUser.setEnabled(true);
            binding.editTextPassword.setEnabled(true);
            binding.spinnerLogin.setEnabled(true);
            binding.buttonLogin.setText("登录");
        }else {
            binding.editTextUser.setEnabled(false);
            binding.editTextPassword.setEnabled(false);
            binding.spinnerLogin.setEnabled(false);
            binding.buttonLogin.setText("登出");
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}