package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView listview = null;
    private List<BeanOrder> orderlist = null;

    @Override
    public void onResume() {
        super.onResume();
        orderlist = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("Order");
        query.whereEqualTo("user", AVUser.getCurrentUser().getObjectId());
        query.orderByAscending("state");// 升序
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                Log.e("size", list.size() + "");
                for (int i = 0; i < list.size(); i++) {
                    AVObject tmp = list.get(i);
                    BeanOrder order = new BeanOrder();
                    order.setOrederID(tmp.getObjectId());
                    order.setCreatedate(tmp.getCreatedAt());
                    order.setUser(tmp.getString("user"));
                    order.setUsername(tmp.getString("username"));
                    order.setCourier(tmp.getString("courier"));
                    order.setCouriername(tmp.getString("couriername"));
                    order.setState(tmp.getInt("state"));
                    order.setDescription(tmp.getString("description"));
                    order.setDetail(tmp.getString("detail"));
                    order.setEndlat(tmp.getDouble("endlat"));
                    order.setEndlng(tmp.getDouble("endlng"));
                    order.setStartlat(tmp.getDouble("startlat"));
                    order.setStartlng(tmp.getDouble("startlng"));
                    order.setPay(tmp.getDouble("pay"));
                    order.setFlag(tmp.getInt("flag"));
                    order.setRate(tmp.getDouble("rate"));
                    order.setComment(tmp.getString("comment"));
                    orderlist.add(order);
                }
                OrderAdapter orderAdapter = new OrderAdapter(getContext(), orderlist);
                orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BeanOrder beanOrder) {
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", beanOrder);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                listview.setLayoutManager(
                        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                listview.setAdapter(orderAdapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        listview = (RecyclerView) view.findViewById(R.id.order_list);
        return view;
    }
}
