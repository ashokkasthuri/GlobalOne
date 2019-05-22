package com.ustglobal.myustapp.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shubham Singhal.
 */

public class EventDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView image, img_register;
    private TextView txtTitle, txtDate, txtDescription, txt_register;
    private RelativeLayout layout_button,layoutImage;
    private EventsOBJ obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        obj = (EventsOBJ) getIntent().getSerializableExtra("obj");
        init();
        setDimension();
        setData();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Event Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        image = (ImageView) findViewById(R.id.image);
        img_register = (ImageView) findViewById(R.id.img_register);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDescription.setMovementMethod(new ScrollingMovementMethod());
        txt_register = (TextView) findViewById(R.id.txt_register);

        layout_button = (RelativeLayout) findViewById(R.id.layout_button);
        layoutImage = (RelativeLayout) findViewById(R.id.layoutImage);

        if (obj.getRegistrationLink().length() == 0)
            layout_button.setVisibility(View.INVISIBLE);

        if(Constants.tech)
            txt_register.setText("Read More");

        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(EventDetail.this, EventRegister.class);
                in.putExtra("url", obj.getRegistrationLink());
                startActivity(in);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(EventDetail.this,DisplayImage.class);
                in.putExtra("image",obj.getImageURL());
                startActivity(in);
            }
        });

    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(EventDetail.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

        layoutImage.getLayoutParams().height = (int) (height * 0.33);

        layout_button.getLayoutParams().width = (int) (width * 0.35);

        img_register.getLayoutParams().height = (int) (height * 0.035);
        img_register.getLayoutParams().width = (int) (height * 0.035);

        txt_register.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.024));
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.032));
        txtDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txtDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.028));

    }

    private void setData() {
        Picasso.with(this).load(obj.getImageURL()).placeholder(R.drawable.placeholder).into(image);

        String inputPattern = "EEE MMM dd yyyy HH:mm:ss";
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(obj.getDate());
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtTitle.setText(obj.getTitle());
        txtDate.setText(str);
        txtDescription.setText(obj.getDescription());
    }
}
