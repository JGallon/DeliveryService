package com.zucc.jjl1130.deliveryservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public class DisplayActivity extends AppCompatActivity {
    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 100;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
//    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    private double mylng = -1.0;
    private double mylat = -1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        initGPS();
        final WelcomeFragment welcomeFragment = new WelcomeFragment();
        setDefaultFragment(welcomeFragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        TextView userid = (TextView) findViewById(R.id.userid);
        AVUser currentUser = AVUser.getCurrentUser();
//        if (currentUser != null) {
//            // 跳转到首页
//            userid.setText(currentUser.getObjectId() + "");
//        } else {
//            //缓存用户对象为空时，可打开用户注册界面…
//        }
        setSupportActionBar(toolbar);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("DS");

//        mLocationListener = new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//
//            }
//        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        mylng = aMapLocation.getLongitude();
                        mylat = aMapLocation.getLatitude();
                        Log.e("position", mylng + ":" + mylat);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
        // Create a few sample profile
        //设置是否强制刷新WIFI，默认为true，强制刷新。
//        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
//        目前手机设备在长时间黑屏或锁屏时CPU会休眠，这导致定位SDK不能正常进行位置更新。若您有锁屏状态下获取位置的需求，您可以应用alarmManager实现1个可叫醒CPU的Timer，定时请求定位。

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AVObject upload = AVObject.createWithoutData("Position", AVUser.getCurrentUser().getString("positionID"));
                upload.put("lng", mylng);
                upload.put("lat", mylat);
                upload.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.d("upload", "success!");
                        }
                    }
                });
                handler.postDelayed(this, 4000);// 4s 上传一次
            }
        };
        handler.postDelayed(runnable, 4000);//每4s执行一次runnable.
//        handler.removeCallbacks(runnable);

        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName(currentUser.getUsername()).withEmail(currentUser.getEmail()).withIcon(R.drawable.ic_man);
//        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.md_red_400)
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("My Home").withIcon(R.drawable.ic_home).withSelectedIcon(R.drawable.ic_home_selected).withIdentifier(8),
                        new PrimaryDrawerItem().withName("My Orders").withIcon(R.drawable.ic_order).withSelectedIcon(R.drawable.ic_order_selected).withIdentifier(1),
                        new PrimaryDrawerItem().withName("My Tasks").withIcon(R.drawable.ic_delivery).withSelectedIcon(R.drawable.ic_delivery_selected).withIdentifier(2),//.withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED))
                        new PrimaryDrawerItem().withName("Add Order").withIcon(R.drawable.ic_add).withSelectedIcon(R.drawable.ic_add_selected).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Get Order").withIcon(R.drawable.ic_search).withSelectedIcon(R.drawable.ic_search_selected).withIdentifier(4),
//                        new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_advanced_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
//                        new PrimaryDrawerItem().withName("My Share").withIcon(R.drawable.ic_share).withSelectedIcon(R.drawable.ic_share_selected).withIdentifier(5),
                        new SectionDrawerItem().withName("Setting"),
                        new SecondaryDrawerItem().withName("Share APP").withIcon(R.drawable.ic_share).withIdentifier(5),
                        new SecondaryDrawerItem().withName("User Info").withIcon(R.drawable.ic_userinfo).withIdentifier(6),
                        new SecondaryDrawerItem().withName("Log Out").withIcon(R.drawable.ic_logout).withIdentifier(7)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
//                        if (drawerItem instanceof Nameable) {
//                            Toast.makeText(DisplayActivity.this, ((Nameable) drawerItem).getName().getText(DisplayActivity.this), Toast.LENGTH_SHORT).show();
                        if (drawerItem.getIdentifier() == 1) {
//                            Toast.makeText(DisplayActivity.this, drawerItem.getIdentifier() + "", Toast.LENGTH_SHORT).show();
                            OrderFragment orderFragment = new OrderFragment();
                            transaction.replace(R.id.frame_container, orderFragment);
                            transaction.commit();
                        } else if (drawerItem.getIdentifier() == 2) {
                            TaskFragment taskFragment = new TaskFragment();
                            transaction.replace(R.id.frame_container, taskFragment);
                            transaction.commit();
                        } else if (drawerItem.getIdentifier() == 3) {
                            AddFragment addFragment = new AddFragment();
                            transaction.replace(R.id.frame_container, addFragment);
                            transaction.commit();
                        } else if (drawerItem.getIdentifier() == 4) {
                            GetFragment getFragment = new GetFragment();
                            transaction.replace(R.id.frame_container, getFragment);
                            transaction.commit();
                        } else if (drawerItem.getIdentifier() == 5) {
                            String shareBody = "I am using this app, coming together with me!";
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DeliveryService");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "APP"));
                        } else if (drawerItem.getIdentifier() == 6) {
                            UserFragment userFragment = new UserFragment();
                            transaction.replace(R.id.frame_container, userFragment);
                            transaction.commit();
                        } else if (drawerItem.getIdentifier() == 7) {
                            AVUser.logOut();
                            startActivity(new Intent(DisplayActivity.this, LoginActivity.class));
                            handler.removeCallbacks(runnable);
//                            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                            DisplayActivity.this.finish();
                        } else if (drawerItem.getIdentifier() == 8) {
                            transaction.replace(R.id.frame_container, welcomeFragment);
                            transaction.commit();
                        }
//                        }
                        //we do not consume the event and want the Drawer to continue with the event chain
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();


        //get the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        //the CrossfadeDrawerLayout library can be found here: https://github.com/mikepenz/CrossfadeDrawerLayout
        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });


        /**
         * NOTE THIS IS A HIGHLY CUSTOM ANIMATION. USE CAREFULLY.
         * this animate the height of the profile to the height of the AccountHeader and
         * animates the height of the drawerItems to the normal drawerItems so the difference between Mini and normal Drawer is eliminated
         **/
        /*
        final double headerHeight = DrawerUIUtils.getOptimalDrawerWidth(this) * 9d / 16d;
        final double originalProfileHeight = UIUtils.convertDpToPixel(72, this);
        final double headerDifference = headerHeight - originalProfileHeight;
        final double originalItemHeight = UIUtils.convertDpToPixel(64, this);
        final double normalItemHeight = UIUtils.convertDpToPixel(48, this);
    final double itemDifference = originalItemHeight - normalItemHeight;
        crossfadeDrawerLayout.withCrossfadeListener(new CrossfadeDrawerLayout.CrossfadeListener() {
@Override
public void onCrossfade(View containerView, float currentSlidePercentage, int slideOffset) {
        for (int i = 0; i < miniResult.getAdapter().getItemCount(); i++) {
        IDrawerItem drawerItem = miniResult.getAdapter().getItem(i);
        if (drawerItem instanceof MiniProfileDrawerItem) {
        MiniProfileDrawerItem mpdi = (MiniProfileDrawerItem) drawerItem;
        mpdi.withCustomHeightPx((int) (originalProfileHeight + (headerDifference * currentSlidePercentage / 100)));
        } else if (drawerItem instanceof MiniDrawerItem) {
        MiniDrawerItem mdi = (MiniDrawerItem) drawerItem;
        mdi.withCustomHeightPx((int) (originalItemHeight - (itemDifference * currentSlidePercentage / 100)));
        }
        }
        miniResult.getAdapter().notifyDataSetChanged();
        }
        });
        */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setDefaultFragment(Fragment welcomeFragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_container, welcomeFragment);
        transaction.commit();
    }

    public void initGPS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
    }

}
