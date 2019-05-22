package com.ustglobal.myustapp.NavigationDrawer;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Shubham Singhal.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerModel> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Activity act;
    MyViewHolder holder;

    public NavigationDrawerAdapter(Activity _act, List<NavDrawerModel> data) {
        this.act = _act;
        inflater = LayoutInflater.from(_act);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerModel current = data.get(position);

        holder.title.setText(current.getTitle());
        holder.rowIcon.setBackgroundResource(current.getIcon());
        setDimension();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView rowIcon;
        RelativeLayout row;
        View view;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            rowIcon = (ImageView) itemView.findViewById(R.id.rowIcon);
            row = (RelativeLayout) itemView.findViewById(R.id.row);
            view = itemView.findViewById(R.id.view);

        }
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(act);

        int height = size.heightPixels;
        int width = size.widthPixels;

        int img_height = (int) (height * 0.04);
        int img_width = (int) (width * 0.04);

        holder.rowIcon.getLayoutParams().height = img_height;
        holder.rowIcon.getLayoutParams().width = img_height;

     //   holder.row.getLayoutParams().height = (int) (height * 0.06);

        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.027));
        holder.view.getLayoutParams().height = (int) (height * 0.001);
        // CommonTasks.setFont(act, holder.title);
    }

}
