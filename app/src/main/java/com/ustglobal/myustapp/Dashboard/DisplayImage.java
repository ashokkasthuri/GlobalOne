package com.ustglobal.myustapp.Dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Shubham Singhal.
 */

public class DisplayImage extends AppCompatActivity {

    private ImageView image;
    private ImageView imgback;
    private String imageURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageURL =  getIntent().getStringExtra("image");
        init();
        setDimension();

        Picasso.with(this).load(imageURL).into(image);
    }

    private void init() {
        image = (ImageView) findViewById(R.id.image);
        imgback = (ImageView) findViewById(R.id.imgback);
        PhotoViewAttacher attacher = new PhotoViewAttacher(image);
        attacher.update();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(DisplayImage.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

        imgback.getLayoutParams().height = (int) (height * 0.08);
        imgback.getLayoutParams().width = (int) (height * 0.08);
    }


}
