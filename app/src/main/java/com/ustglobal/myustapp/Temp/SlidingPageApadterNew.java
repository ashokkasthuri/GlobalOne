package com.ustglobal.myustapp.Temp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Dashboard.EventsOBJ;
import com.ustglobal.myustapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */
public class SlidingPageApadterNew extends PagerAdapter {

    private LayoutInflater inflater;
    private Activity context;
    private ArrayList<EventsOBJ> events;
    private DisplayImageOptions options;


    public SlidingPageApadterNew(Activity context, ArrayList<EventsOBJ> events) {
        this.context = context;
        // this.Title = Title;
        this.events = events;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.image_sliding, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        final RelativeLayout mainLayout = (RelativeLayout) imageLayout
                .findViewById(R.id.mainLayout);
        DisplayMetrics size = CommonTasks.getScreenSize(context);
        int height = size.heightPixels;
        int width = size.widthPixels;

        Picasso.with(context).load(events.get(position).getImageURL()).into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
