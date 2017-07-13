package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class GetFragment extends Fragment {

    private RecyclerView listview = null;
    private GetAdapter getAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get, container, false);
        listview = (RecyclerView) view.findViewById(R.id.get_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
//        orderlist = new ArrayList<>();
        getAdapter = new GetAdapter(getContext());
        getAdapter.setOnItemClickListener(new GetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BeanOrder beanOrder) {
                Intent intent = new Intent(getActivity(), GetDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", beanOrder);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listview.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listview.setAdapter(getAdapter);
        return view;
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        AVQuery<AVObject> query = new AVQuery<>("Order");
        query.whereEqualTo("state", 0);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                Log.e("size", list.size() + "");
                List<BeanOrder> orderlist = new ArrayList<BeanOrder>();
                for (int i = 0; i < list.size(); i++) {
                    AVObject tmp = list.get(i);
                    BeanOrder order = new BeanOrder();
                    order.setOrederID(tmp.getObjectId());
                    order.setCreatedate(tmp.getCreatedAt());
                    order.setUser(tmp.getString("user"));
                    order.setUsername(tmp.getString("username"));
                    order.setState(tmp.getInt("state"));
                    order.setDescription(tmp.getString("description"));
                    order.setDetail(tmp.getString("detail"));
                    order.setEndlat(tmp.getDouble("endlat"));
                    order.setEndlng(tmp.getDouble("endlng"));
                    order.setStartlat(tmp.getDouble("startlat"));
                    order.setStartlng(tmp.getDouble("startlng"));
                    order.setPay(tmp.getDouble("pay"));
                    orderlist.add(order);
                }
                getAdapter.setDate(orderlist);
                getAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
