package com.zucc.jjl1130.deliveryservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVOSCloud;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "9Bey6hIH7AAIV17V8yzyFj9f-gzGzoHsz", "4HV9Drjm63B4zAeVXwu3pHwa");
        AVOSCloud.setDebugLogEnabled(true);

    }
}
