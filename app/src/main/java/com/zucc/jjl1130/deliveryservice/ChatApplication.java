package com.zucc.jjl1130.deliveryservice;

import android.app.Application;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by JGallon on 2017/7/11.
 */

public class ChatApplication extends Application {
    private final String APP_ID = "9Bey6hIH7AAIV17V8yzyFj9f-gzGzoHsz";
    private final String APP_KEY = "4HV9Drjm63B4zAeVXwu3pHwa";

    @Override
    public void onCreate() {
        super.onCreate();
        // 关于 CustomUserProvider 可以参看后面的文档
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
    }
}
