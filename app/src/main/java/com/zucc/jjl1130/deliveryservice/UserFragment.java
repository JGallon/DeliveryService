package com.zucc.jjl1130.deliveryservice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView user = (TextView) view.findViewById(R.id.user);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        TextView email = (TextView) view.findViewById(R.id.email);
        user.setText(AVUser.getCurrentUser().getUsername());
        phone.setText(AVUser.getCurrentUser().getMobilePhoneNumber());
        email.setText(AVUser.getCurrentUser().getEmail());
        return view;
    }
}
