package com.ustglobal.myustapp.Dashboard;

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
import com.ustglobal.myustapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */

public class EventsList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView eventList;
    private String str;
    private JSONArray arr;
    private ArrayList<EventsOBJ> events_obj = new ArrayList<>();
    private EventsListAdapter adapter;
    private String type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        str = getIntent().getStringExtra("obj");
        try {
            arr = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // events_obj = (ArrayList<EventsOBJ>) getIntent().getSerializableExtra("obj");
        type = getIntent().getStringExtra("type");
        init();
        setDimension();

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject obj = arr.getJSONObject(i);
                EventsOBJ events = new EventsOBJ();
                events.setPriority(obj.getString("Priority"));
                events.setTitle(obj.getString("Title"));
                events.setSubtitile(obj.getString("Subtitile"));
                events.setDate(obj.getString("Date"));
                events.setDescription(obj.getString("Description"));
                events.setImageURL(obj.getString("ImageURL"));
                events.setRegistrationLink(obj.getString("RegistrationLink"));
                events_obj.add(events);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new EventsListAdapter(this, events_obj);
        eventList.setAdapter(adapter);
        eventList.setHasFixedSize(true);
        eventList.setNestedScrollingEnabled(false);
        LinearLayoutManager glManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        eventList.setLayoutManager(glManager);
    }

    private void init() {
        eventList = (RecyclerView) findViewById(R.id.data);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(EventsList.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

    }

}
