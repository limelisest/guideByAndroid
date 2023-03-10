package com.limelisest.guide.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.limelisest.guide.R;
import com.limelisest.guide.databinding.FragmentHomeBinding;
import com.limelisest.guide.placeholder.LoginContent;
import com.limelisest.guide.placeholder.MyDataBase;

import java.sql.SQLException;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (LoginContent.LoginUser != null){
            binding.editTextUser.setText(LoginContent.LoginUser);
            binding.editTextPassword.setText(LoginContent.LoginPassword);
            binding.editTextUser.setEnabled(false);
            binding.editTextPassword.setEnabled(false);
            binding.buttonLogin.setText("登出");
        }
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            binding.editTextUser.setEnabled(false);
                            binding.editTextPassword.setEnabled(false);
                            binding.buttonLogin.setText("登出");
                        }else {
                            Toast.makeText(root.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    LoginContent.LoginUser = null;
                    LoginContent.LoginPassword=null;
                    binding.editTextUser.setEnabled(true);
                    binding.editTextPassword.setEnabled(true);
                    binding.buttonLogin.setText("登录");
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