package com.zucc.jjl1130.deliveryservice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

public class BlankFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        Button btn = (Button) view.findViewById(R.id.btn);
        final TextView txt = (TextView) view.findViewById(R.id.txt);
        final EditText edt = (EditText) view.findViewById(R.id.edt);


        PoiSearch.Query poiquery = new PoiSearch.Query("浙江大学", "", "0571");
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        poiquery.setPageSize(14);// 设置每页最多返回多少条poiitem
        poiquery.setPageNum(4);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(getContext(), poiquery);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                Log.e("item", poiItem.getDirection());
            }
        });
        poiSearch.searchPOIAsyn();


        final GeocodeSearch geocodeSearch = new GeocodeSearch(getContext());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                Log.e("formatAddress", "formatAddress:" + formatAddress);
                Log.e("formatAddress", "rCode:" + i);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    List<GeocodeAddress> res = geocodeResult.getGeocodeAddressList();
//                    Log.e("length", "res:" + res.size());
//                    txt.setText(res.size() + " ");
//                    txt.setText();
//                    GeocodeAddress g = res.get(0);
//                    LatLonPoint l = g.getLatLonPoint();
//                    String s = g.getProvince() + " " + g.getCity() + " " + g.getDistrict() + " " + l.getLatitude() + " " + l.getLongitude();
//                    txt.setText(s);
                }
//                else
//                    txt.setText("fai");

            }
        });
//        GeocodeQuery query = new GeocodeQuery("浙大", "0571");
//        geocodeSearch.getFromLocationNameAsyn(query);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String s = edt.getText().toString();
                LatLonPoint lp = new LatLonPoint(39.990117, 116.482231);
                RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
            }
        });
        return view;
    }
}
