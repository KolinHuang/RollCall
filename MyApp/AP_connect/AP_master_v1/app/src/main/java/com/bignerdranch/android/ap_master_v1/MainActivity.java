package com.bignerdranch.android.ap_master_v1;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private String WIFI_HOTSPOT_SSID = "myAP";
    private String WIFI_SHARE_KEY = "12345678";
    private Button mOpenAPButton;
    private Button mCloseAPButton;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiTools wt = new WifiTools(wifiManager);
        mOpenAPButton = findViewById(R.id.bt_openap);
        mOpenAPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean enable = wt.createWifiHotspot(WIFI_HOTSPOT_SSID,WIFI_SHARE_KEY);
                if (enable) {
                    mTextView.setText("热点已开启 SSID:" + WIFI_HOTSPOT_SSID + " password:" + WIFI_SHARE_KEY);
                } else {
                    mTextView.setText("创建热点失败");
                }
            }
        });


        mCloseAPButton = findViewById(R.id.bt_closeap);
        mCloseAPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isClosed = wt.closeWifiHotspot();
                if(isClosed){
                    mTextView.setText("热点已关闭");
                }else {
                    mTextView.setText("关闭失败");
                }
            }
        });

        mTextView = findViewById(R.id.tv_show_connectionInfo);
    }


}
