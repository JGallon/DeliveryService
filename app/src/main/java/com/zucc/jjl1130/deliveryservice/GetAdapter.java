package com.zucc.jjl1130.deliveryservice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by JGallon on 2017/7/9.
 */

public class GetAdapter extends RecyclerView.Adapter<GetAdapter.ViewHolder> {

    private Context mcontext;
    private List<BeanOrder> mlist = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public GetAdapter(Context context, List<BeanOrder> list) {
        mcontext = context;
        mlist = list;
        Log.e("list", mlist.size() + "");
    }

    public GetAdapter(Context context) {
        mcontext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDate(List<BeanOrder> list) {
        mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(mcontext).inflate(R.layout.item_get, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BeanOrder beanOrder = mlist.get(position);
        holder.original.setText(beanOrder.getDescription());
        double lng = beanOrder.getEndlng();
        double lat = beanOrder.getEndlat();
        GeocodeSearch geocodeSearch = new GeocodeSearch(mcontext);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                holder.terminal.setText(regeocodeAddress.getCity() + regeocodeAddress.getDistrict() + regeocodeAddress.getTownship() + regeocodeAddress.getNeighborhood());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
        DecimalFormat df = new DecimalFormat("#.00");
        holder.salary.setText(df.format(beanOrder.getPay()));
        holder.note.setText(beanOrder.getDetail());
//        holder.btn_get.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnItemClickListener != null)
//                    mOnItemClickListener.onItemClick(beanOrder);
//            }
//        });
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(beanOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public interface OnItemClickListener {
        void onItemClick(BeanOrder beanOrder);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.item_get_container)
//        LinearLayout mContainer;
//
//        @BindView(R.id.original)
//        TextView original;
//
//        @BindView(R.id.terminal)
//        TextView terminal;
//
//        @BindView(R.id.salary)
//        TextView salary;
//
//        @BindView(R.id.note)
//        TextView note;
//
//        @BindView(R.id.get)
//        StateButton btn_get;
        LinearLayout mContainer = (LinearLayout) itemView.findViewById(R.id.item_get_container);
        TextView original = (TextView) itemView.findViewById(R.id.original);
        TextView terminal = (TextView) itemView.findViewById(R.id.terminal);
        TextView salary = (TextView) itemView.findViewById(R.id.salary);
        TextView note = (TextView) itemView.findViewById(R.id.note);
//        StateButton btn_get = (StateButton) itemView.findViewById(R.id.get);

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
