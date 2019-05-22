package com.ustglobal.myustapp.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.R;

import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */

public class SubCategoryList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView eventList;
    private ArrayList<SubCategoryOBJ> sub_obj;
    private SubCategoryListAdapter adapter;
    private String obj;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        obj = getIntent().getStringExtra("obj");

        if(obj.equalsIgnoreCase("Technology"))
            Constants.tech = true;
        else
            Constants.tech = false;

        if(obj.equalsIgnoreCase("News @ Malaysia"))
            Constants.news = true;
        else
            Constants.news = false;

        sub_obj = Constants.sub_events.get(obj);
        init();
        setDimension();
        if (sub_obj != null) {
            adapter = new SubCategoryListAdapter(this, sub_obj);
            eventList.setAdapter(adapter);
            eventList.setHasFixedSize(true);
            eventList.setNestedScrollingEnabled(false);
            LinearLayoutManager glManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
            eventList.setLayoutManager(glManager);
        }
    }

    private void init() {
        eventList = (RecyclerView) findViewById(R.id.data);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(obj);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(SubCategoryList.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in  = new Intent(SubCategoryList.this,Dashboard.class);
        startActivity(in);
        finish();
    }
}
