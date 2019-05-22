package com.ustglobal.myustapp.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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


import com.cocosw.bottomsheet.BottomSheet;
import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Shubham Singhal.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolderNew> {
    private static LayoutInflater inflater = null;
    private Activity act;
    EventsHorizontalAdapter adapter;
    private ViewHolderNew holder;
    HashMap<String, ArrayList<EventsOBJ>> events;

    public EventsAdapter(Activity act, HashMap<String, ArrayList<EventsOBJ>> events) {
        this.act = act;
        this.events = events;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events, null);
        holder = new ViewHolderNew(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderNew holder, final int position) {
//
//        final BlogsOBJ obj = Constants.blog_list.get(position);
//        holder.txt_type.setText(obj.getCategory());
//
//        final ArrayList<EventsOBJ> events_obj = events.get(obj.getCategory());
//
//        Picasso.with(act).load(events_obj.get(0).getImageURL()).into(holder.cover);
//        holder.text.setText(events_obj.get(0).getTitle());
//
//        holder.txt_found.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(act,EventsList.class);
//                in.putExtra("obj",events_obj);
//                in.putExtra("type",obj.getCategory());
//                act.startActivity(in);
//
//            }
//        });

//        holder.row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in = new Intent(act, EventDetail.class);
//                in.putExtra("obj", events_obj.get(0));
//                act.startActivity(in);
//            }
//        });

        holder.img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(act).sheet(R.menu.options).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.action_edit:
                                break;
                            case R.id.action_delete:

                                break;
                            case R.id.action_dismiss:
                                break;
                        }
                    }
                }).show();
            }
        });

        setDimension();


//        if(position == 0)
//        holder.txt_type.setText("Sports");
//        if(position == 1)
//            holder.txt_type.setText("Technology");
//        if(position == 2)
//            holder.txt_type.setText("News Letter");
//        if(position == 3)
//            holder.txt_type.setText("UST Quiz");
//        if(position == 4)
//            holder.txt_type.setText("UST News");

//        LinearLayoutManager glManager = new LinearLayoutManager(act, OrientationHelper.HORIZONTAL, false);
//        holder.recyclerEvents.setLayoutManager(glManager);
//        adapter = new EventsHorizontalAdapter(act,events.get(obj.getCategory()));
//        holder.recyclerEvents.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolderNew extends RecyclerView.ViewHolder {
        //  RecyclerView recyclerEvents;
        CardView card_view;
        RelativeLayout layout_type, row;
        TextView txt_type, txt_found, text;
        ImageView cover, img_settings;

        public ViewHolderNew(View v) {
            super(v);
            card_view = (CardView) v.findViewById(R.id.card_view);
            //  recyclerEvents = (RecyclerView) v.findViewById(R.id.recyclerEvents);
            layout_type = (RelativeLayout) v.findViewById(R.id.layout_type);
            txt_type = (TextView) v.findViewById(R.id.txt_type);
            txt_found = (TextView) v.findViewById(R.id.txt_found);
            cover = (ImageView) v.findViewById(R.id.cover);
            img_settings = (ImageView) v.findViewById(R.id.img_settings);
            text = (TextView) v.findViewById(R.id.text);
            row = (RelativeLayout) v.findViewById(R.id.row);
        }
    }


    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(act);
        int height = size.heightPixels;
        int width = size.widthPixels;

       holder.cover.getLayoutParams().height = (int) (height * 0.2);
//        holder.cover.getLayoutParams().width = (int) (height * 0.165);

        //   holder.text.getLayoutParams().width = (int) (height * 0.15);

        // holder.row.getLayoutParams().width = (int) (width * 0.35);
        holder.img_settings.getLayoutParams().height = (int) (height * 0.05);
        holder.img_settings.getLayoutParams().width = (int) (height * 0.05);
        holder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));

        //   holder.card_view.getLayoutParams().height = (int) (height * 0.3);

        holder.txt_type.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.027));
        holder.txt_found.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.027));

    }

}
