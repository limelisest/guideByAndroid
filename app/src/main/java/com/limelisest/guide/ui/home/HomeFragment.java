package com.limelisest.guide.ui.home;

import android.os.Bundle;
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
import com.limelisest.guide.placeholder.MyDataBase;

import java.sql.SQLException;

public class HomeFragment extends Fragment {
    public static String LoginUser=null;
    public static String LoginPassword=null;
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText mTextUser=root.findViewById(R.id.editTextUser);
        EditText mTextPassword=root.findViewById(R.id.editTextPassword);
        Button mButtonLogin=root.findViewById(R.id.buttonLogin);
        Button mButtonRegister=root.findViewById(R.id.buttonRegister);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user= String.valueOf(mTextUser.getText());
                String password=String.valueOf(mTextPassword.getText());
                Toast t1= Toast.makeText(root.getContext(), "登录中。。。", Toast.LENGTH_SHORT);
                t1.show();
                try {
                    MyDataBase db=new MyDataBase();
                    int status=db.LoginUser(user,password);
                    t1.cancel();
                    if (status==0){
                        LoginUser=user;
                        LoginPassword=password;
                        Toast.makeText(root.getContext(), "登录成功,账号:"+LoginUser, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(root.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_navigation_home_to_registerFragment);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}