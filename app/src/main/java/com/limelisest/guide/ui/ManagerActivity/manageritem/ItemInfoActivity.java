package com.limelisest.guide.ui.ManagerActivity.manageritem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.util.BitmapUtil;
import com.google.zxing.util.Constant;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.limelisest.guide.R;
import com.limelisest.guide.databinding.ActivityItemInfoBinding;
import com.limelisest.guide.placeholder.LoginContent;
import com.limelisest.guide.placeholder.MyDataBase;
import com.limelisest.guide.placeholder.PlaceholderContent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class ItemInfoActivity extends AppCompatActivity {

    private ActivityItemInfoBinding binding;
    public static String flag = null;
    public static String item_id = null;
    public int CHOOSE_PHOTO = 10000;
    Bitmap picture=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> list=new ArrayList<>(PlaceholderContent.ItemClass);
        // 接收id
        item_id=getIntent().getStringExtra("item_id");
        flag=getIntent().getStringExtra("flag");
        binding = ActivityItemInfoBinding.inflate(getLayoutInflater());
        if (Objects.equals(flag, "new")){
            binding.textView.setText("你正在创建一个新的物品");
        }
        else if (Objects.equals(flag, "update")){
            binding.textView.setText("你编辑的物品ID是："+item_id);
        }
        try {
            Bundle bundle=PlaceholderContent.db.GetItemInfo(item_id);
            binding.editItemName.setText(bundle.getString("name"));
            binding.editItemInfo.setText(bundle.getString("info"));
            binding.editItemPrice.setText(bundle.getString("price"));
            binding.editItemNum.setText(bundle.getString("num"));
            binding.editItemAreaX.setText(bundle.getString("area_x"));
            binding.editItemAreaY.setText(bundle.getString("area_y"));
            binding.editItemQRCode.setText(bundle.getString("QRCODE"));
            binding.editItemENA13.setText(bundle.getString("EAN13"));
            binding.editItemRFID.setText(bundle.getString("RFID"));
            String item_class=bundle.getString("class");

            list.remove(0);
            ArrayAdapter<String> spinnerDataAdapter = new ArrayAdapter<String>(ItemInfoActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, list);
            spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerItemClass.setAdapter(spinnerDataAdapter);
            int i=0;
            for(String line: list){
                if (Objects.equals(line, item_class)){
                    break;
                }
                i++;
            }
            binding.spinnerItemClass.setSelection(i);

            binding.imageView.setImageBitmap(PlaceholderContent.db.GetItemPicture(item_id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 提交修改
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
                    bundle.putString("num", String.valueOf(binding.editItemNum.getText()));
                    bundle.putString("area_x", String.valueOf(binding.editItemAreaX.getText()));
                    bundle.putString("area_y", String.valueOf(binding.editItemAreaY.getText()));
                    bundle.putString("QRCODE", String.valueOf(binding.editItemQRCode.getText()));
                    bundle.putString("EAN13", String.valueOf(binding.editItemENA13.getText()));
                    bundle.putString("RFID", String.valueOf(binding.editItemRFID.getText()));
                    bundle.putString("class",String.valueOf(list.get(binding.spinnerItemClass.getSelectedItemPosition())));

                    if (Objects.equals(flag, "update")){
                        int status=PlaceholderContent.db.UpdateItemInfo(bundle);
                        int status2=0;
                        if (picture != null){
                            status2=PlaceholderContent.db.UpdateItemPicture(item_id,picture);
                        }
                        if ( status == 0 && status2 == 0){
                            Toast.makeText(view.getContext(), "物品(id="+item_id+")"+name+"信息修改成功", Toast.LENGTH_SHORT).show();
                            PlaceholderContent.reflash();
                            finish();
                    }
                    else if (Objects.equals(flag, "new")){

                    }
                    }else {
                        Toast.makeText(view.getContext(), "修改失败，未知错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        // 输入新种类
        binding.buttonItemClassNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(ItemInfoActivity.this);
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemInfoActivity.this);
                builder.setTitle("输入种类名").setIcon(android.R.drawable.ic_dialog_info)
                        .setView(inputServer).setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String _sign = inputServer.getText().toString();
                        if(_sign!=null && !_sign.isEmpty()) {
                            list.add(_sign);
                            ArrayAdapter<String> spinnerDataAdapter = new ArrayAdapter<String>(ItemInfoActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, list);
                            spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerItemClass.setAdapter(spinnerDataAdapter);
                            binding.spinnerItemClass.setSelection(list.size()-1);
                        }
                        else{
                            Toast.makeText(ItemInfoActivity.this,"输入的商品种类为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });
        // 二维码扫码
        binding.buttonScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CaptureActivity.class);
                startActivityForResult(intent, Constant.REQ_QR_CODE);
            }
        });
        // 条形码扫码
        binding.buttonScanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CaptureActivity.class);
                startActivityForResult(intent, Constant.REQ_BAR_CODE);
            }
        });
        // 读卡
        binding.buttonScanRFID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NFCActivity.class);
                startActivityForResult(intent, Constant.REQ_NFC_CODE);
            }
        });
        // 添加图片
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    /**
     * 处理选择的图片
     * @param data
     */
    private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        final Uri uri = data.getData();
        picture = BitmapUtil.decodeUri(this, uri, 128, 128);
        binding.imageView.setImageBitmap(picture);
    }

    // 扫描结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "扫码成功", Toast.LENGTH_SHORT).show();
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            binding.editItemQRCode.setText(scanResult);
        }
        if (requestCode == Constant.REQ_BAR_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "扫码成功", Toast.LENGTH_SHORT).show();
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            binding.editItemENA13.setText(scanResult);
        }
        if (requestCode == Constant.REQ_NFC_CODE && resultCode == RESULT_OK) {
            String scanResult = data.getStringExtra("RFID");
            binding.editItemRFID.setText(scanResult);
        }
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK) {
            //读取照片
            handleAlbumPic(data);
        }
    }
}