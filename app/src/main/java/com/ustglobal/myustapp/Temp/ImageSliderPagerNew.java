package com.ustglobal.myustapp.Temp;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ustglobal.myustapp.Dashboard.EventsOBJ;
import com.ustglobal.myustapp.R;
import com.viewpagerindicator.CirclePageIndicator;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shubham Singhal.
 */
public class ImageSliderPagerNew extends Fragment {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
 //   private static final String[] IMAGES = {"https://media.glassdoor.com/o/20/0b/d2/2a/ust-global-kochi.jpg", "http://cdn02.androidauthority.net/wp-content/uploads/2016/01/MotoLogo0116-840x579.jpg", "http://www.intel.com/etc/designs/intel/us/en/images/printlogo.png"};
 //   private ArrayList<String> ImagesArray = new ArrayList<>();
    private ArrayList<EventsOBJ> events;
    View rootView;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.image_sliding_pager, container, false);
        init();
        return rootView;
    }

    public ImageSliderPagerNew(){

    }

    public ImageSliderPagerNew (ArrayList<EventsOBJ> events){
        this.events = events;
    }

    private void init() {
//        ImagesArray.clear();
//        for (int i = 0; i < IMAGES.length; i++)
//            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingPageApadterNew(getActivity(), events));


        CirclePageIndicator indicator = (CirclePageIndicator)
                rootView.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = events.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 8000, 8000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                Toast.makeText(getActivity(),"SELECTED",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }
}
