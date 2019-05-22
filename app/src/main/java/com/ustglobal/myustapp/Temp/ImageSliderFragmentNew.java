package com.ustglobal.myustapp.Temp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.ustglobal.myustapp.Dashboard.EventsOBJ;

import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */
public class ImageSliderFragmentNew extends FragmentPagerAdapter {

    private ArrayList<EventsOBJ> events;

    public ImageSliderFragmentNew(FragmentManager fm, ArrayList<EventsOBJ> events) {
        super(fm);
        this.events = events;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment =  new ImageSliderPagerNew(events);

        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
