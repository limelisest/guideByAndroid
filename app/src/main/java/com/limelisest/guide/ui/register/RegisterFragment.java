package com.limelisest.guide.ui.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.limelisest.guide.R;
import com.limelisest.guide.databinding.FragmentRegisterBinding;
import com.limelisest.guide.placeholder.MyDataBase;
import com.limelisest.guide.ui.home.HomeFragment;

import java.sql.SQLException;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User= String.valueOf(binding.editTextRegisterUser.getText());
                String Password1=String.valueOf(binding.editTextRegisterPassword1.getText());
                String Password2=String.valueOf(binding.editTextRegisterPassword2.getText());
                if(!Password1.equals("") && !Password2.equals("") && !User.equals("")){
                    if(Password1.equals(Password2)){
                        try {
                            MyDataBase db=new MyDataBase();
                            int status=db.RegisterUser(User,Password1);
                            switch (status){
                                case 1:
                                    Toast.makeText(root.getContext(), "此用户名已存在", Toast.LENGTH_SHORT).show();break;
                                case -1:
                                    Toast.makeText(root.getContext(), "未知错误", Toast.LENGTH_SHORT).show();break;
                                case 0:
                                    Toast.makeText(root.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.action_registerFragment_to_navigation_home2);
                                    break;

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {Toast.makeText(root.getContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(root.getContext(), "请输入正确的账号或者密码", Toast.LENGTH_SHORT).show();}
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.action_registerFragment_to_navigation_home2);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}