package com.zucc.jjl1130.deliveryservice;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment {

    private static Double price = 0.005;
    private MapView mMapView = null;
    private ListPopupWindow listPopupWindow = null;
    private MaterialEditText edt = null;
    private AMap aMap = null;
    //    private String[] list = {"item1", "item2", "item3", "item4"};
    private ArrayList<String> list = null;
    private GeocodeSearch geocodeSearch = null;
    private PoiSearch poiSearch = null;
    private Double latitude = -1.0;
    private Double longitude = -1.0;
    //    private Double relatitude = -1.0;
//    private Double relongitude = -1.0;
    private Double mylat = -1.0;
    private Double mylng = -1.0;
    private StateButton btn = null;
    private MaterialEditText edt_de = null;
    private TextView errortxt = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        list = new ArrayList<>();
        list.add("item");
        geocodeSearch = new GeocodeSearch(getContext());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                edt.setText(regeocodeAddress.getCity() + regeocodeAddress.getDistrict() + regeocodeAddress.getTownship() + regeocodeAddress.getNeighborhood());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                List<GeocodeAddress> res = geocodeResult.getGeocodeAddressList();
                Log.e("size", res.size() + "");
//                list.clear();
//                for (int j = 0; j < Math.min(14, res.size()); j++) {
//                    GeocodeAddress g = res.get(j);
//                    String s = g.getProvince() + g.getCity() + g.getDistrict() + g.getBuilding();
//                    Log.e("list", s);
//                    list.add(s);
//                }
                GeocodeAddress g = res.get(0);
                LatLonPoint latLonPoint = g.getLatLonPoint();
                longitude = latLonPoint.getLongitude();
                latitude = latLonPoint.getLatitude();
                LatLng latLng = new LatLng(latitude, longitude);
                aMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_click_position));
                markerOptions.position(latLng);
                aMap.addMarker(markerOptions);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                Log.e("poi", longitude + ":" + latitude);
            }
        });
        errortxt = (TextView) view.findViewById(R.id.error);
        edt_de = (MaterialEditText) view.findViewById(R.id.detail);
        btn = (StateButton) view.findViewById(R.id.addOrder);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt.getText().toString().equals("") || edt_de.getText().toString().equals("")) {
                    errortxt.setText("the content can not be blank");
                    return;
                }
                AVObject object = new AVObject("Order");
                object.put("user", AVUser.getCurrentUser().getObjectId());
                object.put("username", AVUser.getCurrentUser().getUsername());
                object.put("courier", "null");
                object.put("couriername", "null");
                object.put("state", 0);
                object.put("description", edt.getText().toString());
                object.put("detail", edt_de.getText().toString());
                object.put("endlng", mylng);
                object.put("endlat", mylat);
                object.put("startlng", longitude);
                object.put("startlat", latitude);
                object.put("rate", 0.0);
                object.put("flag", 0);
                object.put("comment", "null");
                LatLng latLng1 = new LatLng(mylat, mylng);
                LatLng latLng2 = new LatLng(latitude, longitude);
                Double pay = AMapUtils.calculateLineDistance(latLng1, latLng2) * price;
                object.put("pay", pay);
//                Date date = new Date();
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.d("saved", "success!");
                            Toast.makeText(getContext(), "add successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                aMap.clear();
                edt_de.setText("");
                edt.setText("");
                //0 未接 1 已接 2 快递员确认送达 3 完成
                // 跳转

            }
        });
        edt = (MaterialEditText) view.findViewById(R.id.point);
        edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getX() >= (edt.getWidth() - edt
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (edt.getText().toString().equals("")) {
                            list.clear();
                            list.add("No result");
                            showListPopulWindow();
                            edt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_up), null);
                        } else {
                            list.clear();
                            PoiSearch.Query query = new PoiSearch.Query(edt.getText().toString(), "", "");
                            query.setPageSize(14);// 设置每页最多返回多少条poiitem
                            query.setPageNum(0);//设置查询页码
                            PoiSearch poiSearch = new PoiSearch(getContext(), query);
                            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                                @Override
                                public void onPoiSearched(PoiResult poiResult, int i) {
                                    int pagenum = poiResult.getPageCount();
                                    ArrayList<PoiItem> tmp = poiResult.getPois();
                                    for (int j = 0; j < tmp.size(); j++) {
                                        PoiItem item = tmp.get(j);
                                        String s = item.getProvinceName() + item.toString();
                                        list.add(s);
                                    }
                                    if (tmp.size() == 0)
                                        list.add("No result");
                                    showListPopulWindow();
                                    edt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_up), null);
                                }

                                @Override
                                public void onPoiItemSearched(PoiItem poiItem, int i) {

                                }
                            });
                            poiSearch.searchPOIAsyn();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        mMapView = (MapView) view.findViewById(R.id.map);
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
                mylat = location.getLatitude();
                mylng = location.getLongitude();
            }
        });
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                aMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_point));
                markerOptions.position(latLng);
                aMap.addMarker(markerOptions);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                latitude = latLng.latitude;
                longitude = latLng.longitude;
//                edt.setText("lat:" + latitude + " lon:" + longitude);
                LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void showListPopulWindow() {
        listPopupWindow = new ListPopupWindow(getContext());

        listPopupWindow.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(edt);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edt.setText(list.get(i));
                GeocodeQuery query = new GeocodeQuery(edt.getText().toString(), "");
                geocodeSearch.getFromLocationNameAsyn(query);
                edt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_down), null);
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                edt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_down), null);
            }
        });
        listPopupWindow.show();
    }
}
