package com.limelisest.guide.ui.ManagerActivity;

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

import com.limelisest.guide.R;
import com.limelisest.guide.databinding.ActivityManagerBinding;

public class ManagerActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityManagerBinding binding;
    public static String User=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_manager);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_manager);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11451 && resultCode == RESULT_OK){
            
        }
        if (requestCode == 1 && resultCode == RESULT_OK){
            if (data != null){
                User=data.getStringExtra("user");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}