package com.limelisest.guide.ui.ManagerActivity.manageritem;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.util.Constant;
import com.limelisest.guide.R;
import com.limelisest.guide.databinding.ActivityNfcactivityBinding;

public class NFCActivity extends AppCompatActivity {
    private ActivityNfcactivityBinding binding;
    NfcAdapter mNfcAdapter;
    PendingIntent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityNfcactivityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcactivity);
        initNfc();
    }

    private void initNfc() {
        // 获取默认的NFC控制器
        // 初始化NfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(NFCActivity.this, "设备不支持NFC!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(NFCActivity.this, "请在系统设置中先启用NFC功能!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // 初始化PendingIntent
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 当前app正在前端界面运行，这个时候有intent发送过来，那么系统就会调用onNewIntent回调方法，将intent传送过来
        // 我们只需要在这里检验这个intent是否是NFC相关的intent，如果是，就调用处理方法
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }
    }
    //启动
    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, pIntent, null, null);
    }

    //解析
    private void processIntent(Intent intent) {
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String CardId =ByteArrayToHexString(tagFromIntent.getId());
        Toast.makeText(NFCActivity.this, CardId, Toast.LENGTH_LONG).show();
        Intent intent1=new Intent();

        intent1.putExtra("RFID",CardId);

        setResult(RESULT_OK, intent1);
        finish();
    }

    //转为16进制字符串
    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";


        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}