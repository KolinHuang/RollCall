package com.bignerdranch.android.connecttomysql;


import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="MainActivity";
    private Button Send;
    private Button Receive;
    private TextView textView;
    private String response;
    private EditText inputId_P;
    private EditText inputLastName;
    private EditText inputFirstName;
    private EditText inputAddress;
    private EditText inputCity;

    private String idString;
    private String lnString;
    private String fnString;
    private String adStirng;
    private String ctString;

    String s;
    int retCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        Receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive();
                textView.setText(response);

            }
        });
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void initViews(){
        Send =(Button) findViewById(R.id.Send);
        Receive= (Button) findViewById(R.id.Receive);
        textView=(TextView) findViewById(R.id.textView);

    }



    /*从MySQL里获取数据*/
    private void receive() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        response=executeHttpGet();
                    }
                }

        ).start();
    }
    private String executeHttpGet() {

        HttpURLConnection con=null;
        InputStream in=null;
        String path="http://192.168.1.125/android_connect_failed/get_all_persons.php";
        try {
            con= (HttpURLConnection) new URL(path).openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoInput(true);
            con.setRequestMethod("GET");
            if(con.getResponseCode()==200){

                in=con.getInputStream();
                return parseInfo(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String parseInfo(InputStream in) throws IOException {
        BufferedReader  br=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb=new StringBuilder();
        String line=null;
        while ((line=br.readLine())!=null){
            sb.append(line+"\n");
        }
        Log.i(TAG, "parseInfo: sb:"+sb.toString());
        return sb.toString();
    }

    /*发送数据给MySQL数据库*/

    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加个人信息");
        View view= View.inflate(MainActivity.this,R.layout.dialog_custom,null);
        builder.setView(view);

        builder.setPositiveButton("确定", new OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {


                idString=inputId_P.getText().toString();
                lnString=inputLastName.getText().toString();
                fnString=inputFirstName.getText().toString();
                adStirng=inputAddress.getText().toString();
                ctString=inputCity.getText().toString();
                start("http://192.168.1.125/android_connect_failed/create_person.php");
            }
        });

        builder.setNegativeButton("取消",new OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog ad=builder.create();
        ad.show();

        inputId_P= (EditText)ad.findViewById(R.id.et_Id_P);
        inputLastName= (EditText)ad.findViewById(R.id.et_LastName);
        inputFirstName= (EditText)ad.findViewById(R.id.et_FirstName);
        inputAddress= (EditText)ad.findViewById(R.id.et_Address);
        inputCity= (EditText)ad.findViewById(R.id.et_City);


    }



    private void start(String s){
        //初始化okhttp客户端
        OkHttpClient client = new OkHttpClient.Builder().build();
        //创建post表单，获取edittext的各种信息
        RequestBody post = new FormBody.Builder()
                .add("Id_P",idString)
                .add("LastName",lnString)
                .add("FirstName",fnString)
                .add("Address",adStirng)
                .add("City",ctString)
                .build();
        //开始请求，填入url，提交表单
        final Request request = new Request.Builder()
                .url(s)
                .post(post)
                .build();

        //客户端回调
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败的情况（一般是网络链接问题，服务器错误等）
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //UI线程运行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //临时变量（这是okhttp的一个锅，一次请求的response.body().string()只能用一次，否则就会报错）
                            MainActivity.this.s = response.body().string();

                            //解析出后端返回的数据来
                            JSONObject jsonObject = new JSONObject(String.valueOf(MainActivity.this.s));
                            retCode = jsonObject.getInt("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //客户端自己判断是否成功。
                        if (retCode == 1) {
                            Toast.makeText(MainActivity.this,"成功!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,"错误!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
