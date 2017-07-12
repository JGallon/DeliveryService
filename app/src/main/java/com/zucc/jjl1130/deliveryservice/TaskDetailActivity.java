package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.DecimalFormat;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

public class TaskDetailActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private AMap aMap = null;
    private int state = -1;
    private int flag = -1;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        getSupportActionBar().setHomeAsUpIndicator(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back)
                        .sizeDp(16).color(ContextCompat.getColor(this, R.color.md_white_1000))
        );
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = this.getIntent();
        final BeanOrder beanOrder = (BeanOrder) intent.getSerializableExtra("order");
        TextView original = (TextView) findViewById(R.id.original);
        final TextView terminal = (TextView) findViewById(R.id.terminal);
        final TextView salary = (TextView) findViewById(R.id.salary);
        TextView note = (TextView) findViewById(R.id.note);
        TextView client = (TextView) findViewById(R.id.client);
        TextView date = (TextView) findViewById(R.id.date);
        final StateButton btn = (StateButton) findViewById(R.id.button);
        final TextView errortxt = (TextView) findViewById(R.id.error);
        final LinearLayout ratingbarlayout = (LinearLayout) findViewById(R.id.ratingbarlayout);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar_order);
        TextView ratinginfo = (TextView) findViewById(R.id.order_rating_info);
        final LinearLayout commentlayout = (LinearLayout) findViewById(R.id.commentlayout);
        TextView comment = (TextView) findViewById(R.id.comment);
        final TextView statetxt = (TextView) findViewById(R.id.state);
        ImageView chat = (ImageView) findViewById(R.id.chat);
        state = beanOrder.getState();
        flag = beanOrder.getFlag();
        if (state == 1) {
            statetxt.setText("Transporting");
            btn.setText("Confirm arrived");
//            btn.setVisibility(View.GONE);
        } else if (state == 2) {
//            btn.setText("Confirm");
            btn.setVisibility(View.GONE);
            statetxt.setText("arrived");
        } else if (state == 3) {
            btn.setVisibility(View.GONE);
            statetxt.setText("finished");
            commentlayout.setVisibility(View.VISIBLE);
            ratingbarlayout.setVisibility(View.VISIBLE);
            if (flag == 1) {
//                btn.setVisibility(View.GONE);
                ratingBar.setRating((float) beanOrder.getRate());
                ratinginfo.setText(beanOrder.getRate() + "");
                comment.setText(beanOrder.getComment());
            } else {
//                btn.setText("Comment");
                ratingBar.setRating((float) 0.0);
                ratinginfo.setText("No ratings");
                comment.setText("Empty");
            }
        } else {
            // 不会出现state为0状态
            chat.setVisibility(View.GONE);
            statetxt.setText("Waiting");
            btn.setVisibility(View.GONE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVObject upload = AVObject.createWithoutData("Order", beanOrder.getOrederID());
                upload.put("state", 2);
                upload.saveInBackground();
                state = 2;
                statetxt.setText("arrived");
                btn.setVisibility(View.GONE);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LCChatKit.getInstance().open(AVUser.getCurrentUser().getUsername(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (null == e) {
//                            finish();
                            Intent intent = new Intent(TaskDetailActivity.this, LCIMConversationActivity.class);
                            intent.putExtra(LCIMConstants.PEER_ID, beanOrder.getUsername());
                            startActivity(intent);
                        } else {
                            Toast.makeText(TaskDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        double lng = beanOrder.getEndlng();
        double lat = beanOrder.getEndlat();
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                terminal.setText(regeocodeAddress.getCity() + regeocodeAddress.getDistrict() + regeocodeAddress.getTownship() + regeocodeAddress.getNeighborhood());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
        original.setText(beanOrder.getDescription());
        DecimalFormat df = new DecimalFormat("#.00");
        salary.setText(df.format(beanOrder.getPay()));
        note.setText(beanOrder.getDetail());
        client.setText(beanOrder.getUsername());
        date.setText(beanOrder.getCreatedate() + "");

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//                Log.e("locarion", location.getAltitude() + " " + location.getLongitude() + " " + location.getLatitude());
//                mylat = location.getLatitude();
//                mylng = location.getLongitude();
            }
        });
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        MarkerOptions start_markerOptions = new MarkerOptions();
//        start_markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_point));
        start_markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_point));
        LatLng start_latLng = new LatLng(beanOrder.getStartlat(), beanOrder.getStartlng());
        start_markerOptions.position(start_latLng);
        aMap.addMarker(start_markerOptions);
        MarkerOptions end_markerOptions = new MarkerOptions();
        end_markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_end_point));
        LatLng end_latLng = new LatLng(beanOrder.getEndlat(), beanOrder.getEndlng());
        end_markerOptions.position(end_latLng);
        aMap.addMarker(end_markerOptions);
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(end_latLng));
    }
}
