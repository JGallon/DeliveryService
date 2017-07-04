package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.btn_in)
//    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AVOSCloud.initialize(this, "9Bey6hIH7AAIV17V8yzyFj9f-gzGzoHsz", "4HV9Drjm63B4zAeVXwu3pHwa");
//        AVOSCloud.setDebugLogEnabled(true);
        Button btn = (Button) findViewById(R.id.btn_in);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
