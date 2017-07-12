package com.zucc.jjl1130.deliveryservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView rateinfo = (TextView) findViewById(R.id.rating_info);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
                rateinfo.setText(v + "");
            }
        });
    }
}
