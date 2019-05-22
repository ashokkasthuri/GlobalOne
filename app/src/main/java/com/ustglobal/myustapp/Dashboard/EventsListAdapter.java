package com.ustglobal.myustapp.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Shubham Singhal.
 */
public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolderNew> {
    private static LayoutInflater inflater = null;
    private Activity act;
    EventsHorizontalAdapter adapter;
    private ViewHolderNew holder;
    ArrayList<EventsOBJ> events;

    public EventsListAdapter(Activity act,ArrayList<EventsOBJ> events) {
        this.act = act;
        this.events = events;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events_list, null);
        holder = new ViewHolderNew(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderNew holder, final int position) {
        setDimension();

        Picasso.with(act).load(events.get(position).getImageURL()).placeholder(R.drawable.placeholder).into(holder.cover);
        holder.text.setText(events.get(position).getTitle());

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.news){
                    Intent in = new Intent(act, EventRegister.class);
                    in.putExtra("url", events.get(position).getRegistrationLink());
                    act.startActivity(in);
                }else {
                    Intent in = new Intent(act, EventDetail.class);
                    in.putExtra("obj", events.get(position));
                    act.startActivity(in);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolderNew extends RecyclerView.ViewHolder {
        CardView card_view;
        RelativeLayout row;
        TextView text;
        ImageView cover;

        public ViewHolderNew(View v) {
            super(v);
            card_view = (CardView) v.findViewById(R.id.card_view);
            cover = (ImageView) v.findViewById(R.id.cover);
            text = (TextView) v.findViewById(R.id.text);
            row = (RelativeLayout) v.findViewById(R.id.row);
        }
    }


    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(act);
        int height = size.heightPixels;
        int width = size.widthPixels;

        holder.cover.getLayoutParams().height = (int) (height * 0.2);

        holder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));

    }

}
