package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        getSupportActionBar().setHomeAsUpIndicator(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back)
                        .sizeDp(16).color(ContextCompat.getColor(this, R.color.md_white_1000))
        );
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        final TextView errortxt = (TextView) findViewById(R.id.error);
        final EditText user = (EditText) findViewById(R.id.user);
        final EditText pwd = (EditText) findViewById(R.id.pwd);
        StateButton signin = (StateButton) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().equals("") || pwd.getText().toString().equals("")) {
                    errortxt.setText("the content can not be blank");
                    return;
                }
                AVUser.logInInBackground(user.getText().toString(), pwd.getText().toString(), new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            errortxt.setText("login success");
                            String selfId = avUser.getObjectId();
//                            SessionManager session = SessionManager.getInstance(selfId);
                            startActivity(new Intent(LoginActivity.this, DisplayActivity.class));
                            LoginActivity.this.finish();
                            MainActivity.instance.finish();
                        } else {
                            String error = e.toString();
                            System.out.println("mmp " + error);
                            Pattern p = Pattern.compile("\\{.*?\\}");
                            Matcher m = p.matcher(error);
                            String s = "";
                            if (m.find()) {
                                s = m.group(0);
                            }
                            int errorcode = 0;
                            try {
                                JSONTokener jsonTokener = new JSONTokener(s);
                                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                                errorcode = jsonObject.getInt("code");
                            } catch (JSONException ex) {
                                System.out.println("json translate failed");
                            }
                            if (errorcode == 211) {
                                errortxt.setText("Could not find user");
                            } else if (errorcode == 210) {
                                errortxt.setText("The username and password mismatch");
                            } else {
                                errortxt.setText("Unknown error");
                            }
                        }
                    }
                });
            }
        });
    }
}
