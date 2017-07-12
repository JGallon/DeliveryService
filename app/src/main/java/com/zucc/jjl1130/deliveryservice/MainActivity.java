package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    //    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 100;
    public static MainActivity instance = null;
    @BindView(R.id.btn_in)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        AVOSCloud.initialize(this, "9Bey6hIH7AAIV17V8yzyFj9f-gzGzoHsz", "4HV9Drjm63B4zAeVXwu3pHwa");
        AVOSCloud.setDebugLogEnabled(true);
//        initGPS();
        Button btn = (Button) findViewById(R.id.btn_in);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 判断GPS是否开启,并弹出打开GPS的界面
     */
//    private void initGPS() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            //申请WRITE_EXTERNAL_STORAGE权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
//        }
//    }
}
