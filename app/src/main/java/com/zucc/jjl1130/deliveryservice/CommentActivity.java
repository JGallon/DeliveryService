package com.zucc.jjl1130.deliveryservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class CommentActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final Intent intent = this.getIntent();
        final BeanOrder beanOrder = (BeanOrder) intent.getSerializableExtra("order");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        getSupportActionBar().setHomeAsUpIndicator(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back)
                        .sizeDp(16).color(ContextCompat.getColor(this, R.color.md_white_1000))
        );
        getSupportActionBar().setTitle("Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView rateinfo = (TextView) findViewById(R.id.rating_info);
        final EditText edt = (EditText) findViewById(R.id.comment);
        StateButton btn = (StateButton) findViewById(R.id.btn_comment);
        ratingBar.setRating(5);
        rateinfo.setText("5.0");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
                rateinfo.setText(v + "");
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmt = edt.getText().toString();
                double rate = ratingBar.getRating();
                AVObject upload = AVObject.createWithoutData("Order", beanOrder.getOrederID());
                upload.put("comment", cmt);
                upload.put("rate", rate);
                upload.put("flag", 1);
                upload.saveInBackground();
                Bundle bundle = new Bundle();
                beanOrder.setComment(cmt);
                beanOrder.setFlag(1);
                beanOrder.setRate(rate);
                bundle.putSerializable("order", beanOrder);
                intent.putExtras(bundle);
                CommentActivity.this.setResult(RESULT_OK, intent);
                CommentActivity.this.finish();
            }
        });
    }
}
