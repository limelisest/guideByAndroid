package com.limelisest.guide.ui.MainActivity.shopping_cart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.limelisest.guide.R;
import com.limelisest.guide.placeholder.LoginContent;
import com.limelisest.guide.placeholder.PlaceholderContent;

import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ShoppingCartItemFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart_list, container, false);
        // 添加item
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(new MyShoppingCartItemRecyclerViewAdapter(PlaceholderContent.ShoppingCarITEMS));

        view.findViewById(R.id.floatingActionButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String LoginUser= LoginContent.LoginUser;
                if (LoginUser != null){
                    Toast.makeText(view.getContext(),"登录的账号："+ LoginUser,Toast.LENGTH_SHORT).show();
                    startQrCode();
                }else {
                    Toast.makeText(view.getContext(),"请先登录账号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission
                    .CAMERA)) {
                Toast.makeText(Objects.requireNonNull(getContext()), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(), "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

}