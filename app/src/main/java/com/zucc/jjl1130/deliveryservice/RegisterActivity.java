package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zucc.jjl1130.deliveryservice.R.id.user;

public class RegisterActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        getSupportActionBar().setHomeAsUpIndicator(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back)
                        .sizeDp(16).color(ContextCompat.getColor(this, R.color.md_white_1000))
        );
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final TextView errortxt = (TextView) findViewById(R.id.error);
        final MaterialEditText account = (MaterialEditText) findViewById(user);
        final MaterialEditText pwd = (MaterialEditText) findViewById(R.id.pwd);
        final MaterialEditText pwd2 = (MaterialEditText) findViewById(R.id.pwd2);
        final MaterialEditText email = (MaterialEditText) findViewById(R.id.email);
        final MaterialEditText phone = (MaterialEditText) findViewById(R.id.phone);
        StateButton btn = (StateButton) findViewById(R.id.register);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd.getText().toString().equals("") || pwd2.getText().toString().equals("") || account.getText().toString().equals("") || phone.getText().toString().equals("") || email.getText().toString().equals("")) {
                    errortxt.setText("the content can not be blank");
                    return;
                }
                if (!pwd.getText().toString().equals(pwd2.getText().toString())) {
                    errortxt.setText("Two passwords are different");
                    return;
                }
//                AVObject user = new AVObject("_User");
//                user.put("account", account.getText().toString());
//                user.put("password", pwd.getText().toString());
//                user.put("email", email.getText().toString());
//                user.put("phone", phone.getText().toString());
                AVUser user = new AVUser();
                user.setEmail(email.getText().toString());
                user.setPassword(pwd.getText().toString());
                user.setUsername(account.getText().toString());
                user.setMobilePhoneNumber(phone.getText().toString());
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            errortxt.setText("register success");
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            RegisterActivity.this.finish();
                        } else {
                            String error = e.toString();
                            System.out.println(error);
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
                            if (errorcode == 202) {
//                                new AlertDialog.Builder(RegisterActivity.this)
//                                        .setMessage("用户已经被注册")
//                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                            }
//                                        })
//                                        .show();
                                errortxt.setText("Username has already been taken");
                            } else if (errorcode == 127) {
                                errortxt.setText("The mobile phone number was invalid");
                            } else if (errorcode == 203) {
                                errortxt.setText("Email has already been taken");
                            } else if (errorcode == 214) {
                                errortxt.setText("Mobile phone number has already been taken");
                            } else if (errorcode == 125) {
                                errortxt.setText("The email address was invalid.");
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
