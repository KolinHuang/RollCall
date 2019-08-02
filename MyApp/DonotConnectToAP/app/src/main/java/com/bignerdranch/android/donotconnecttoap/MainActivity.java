package com.bignerdranch.android.donotconnecttoap;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mCheckButton;
    private TextView mShowInfoTextView;
    private WifiManager mWifiManager;
    boolean found = false;
    int currentRSSI = 0;


    public static final int A = 50;//接收机和发射机间隔1m时的信号强度
    public static final double n = 4;//N = 10 * n ,其中n为环境衰减因子，3.25-4.5

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> sr = mWifiManager.getScanResults();
        for(int i = 0;i < sr.size();++i){
            System.out.println(sr.get(i).SSID);
            if(sr.get(i).SSID.equals("Kolin")) {
                currentRSSI = sr.get(i).level;
            }
        }


        mCheckButton = findViewById(R.id.bt_Check);
        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double dist = Math.pow(10,((Math.abs(currentRSSI)-A)/(10*n)));
                if(currentRSSI != 0 ){
                    Toast toast = Toast.makeText(MainActivity.this,
                            "签到成功",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(MainActivity.this,
                            "签到失败",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }
}
