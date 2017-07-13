package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
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
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

public class GetDetailActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private AMap aMap = null;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_detail);
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
        TextView salary = (TextView) findViewById(R.id.salary);
        TextView note = (TextView) findViewById(R.id.note);
        TextView client = (TextView) findViewById(R.id.user);
        TextView date = (TextView) findViewById(R.id.date);
        StateButton btn = (StateButton) findViewById(R.id.getOrder);
        final TextView errortxt = (TextView) findViewById(R.id.error);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVQuery<AVObject> query = new AVQuery<>("Order");
                query.whereEqualTo("objectId", beanOrder.getOrederID());
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        AVObject tmp = list.get(0);
                        if (tmp.getInt("state") == 0) {
                            AVObject upload = AVObject.createWithoutData("Order", beanOrder.getOrederID());
                            upload.put("couriername", AVUser.getCurrentUser().getUsername());
                            upload.put("courier", AVUser.getCurrentUser().getObjectId());
                            upload.put("state", 1);
                            upload.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    errortxt.setText("pick up successfully!");
                                }
                            });
                        } else {
                            errortxt.setText("This order has been picked up!");
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
//        DecimalFormat df = new DecimalFormat("#.00");
        salary.setText(String.format("%.2f", beanOrder.getPay()));
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
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
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
        start_markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_point));
        LatLng start_latLng = new LatLng(beanOrder.getStartlat(), beanOrder.getStartlng());
        start_markerOptions.position(start_latLng);
        aMap.addMarker(start_markerOptions);
        MarkerOptions end_markerOptions = new MarkerOptions();
        end_markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point));
        LatLng end_latLng = new LatLng(beanOrder.getEndlat(), beanOrder.getEndlng());
        end_markerOptions.position(end_latLng);
        aMap.addMarker(end_markerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(end_latLng));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
