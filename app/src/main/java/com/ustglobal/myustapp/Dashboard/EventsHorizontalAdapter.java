package com.ustglobal.myustapp.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */
public class EventsHorizontalAdapter extends RecyclerView.Adapter<EventsHorizontalAdapter.ViewHolder> {

    private static LayoutInflater inflater = null;
    private Activity act;
    private ViewHolder holder;
    private JSONArray arr;
    private DisplayImageOptions options;
    private ArrayList<EventsOBJ> events;
    // private static final String[] IMAGES = {"http://mms.businesswire.com/media/20140721005710/en/424771/5/Biden_-_Sajan.jpg","http://www.prathidhwani.org/wp-content/uploads/2014/04/UST2.jpg","http://www.ust-global.com/sites/default/files/sajan-with-minerva-tantaco-new-york-citys-chief-technology-officer.png","http://cms.technopark.org/zcmspg/zupload/media/web_media/5363/1468230940252_ust.jpg"};

    public EventsHorizontalAdapter(Activity act, ArrayList<EventsOBJ> events) {
        this.act = act;
        this.events = events;
        inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_row_service, null);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(act).load(events.get(position).getImageURL()).into(holder.cover);
        holder.text.setText(events.get(position).getTitle());

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(act, EventDetail.class);
                in.putExtra("obj", events.get(position));
                act.startActivity(in);
            }
        });

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
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView cover, img_settings;
        RelativeLayout row;

        public ViewHolder(View v) {
            super(v);
            cover = (ImageView) v.findViewById(R.id.cover);
            img_settings = (ImageView) v.findViewById(R.id.img_settings);
            //   transparent_bg = (RelativeLayout) v.findViewById(R.id.transparent_bg);
            text = (TextView) v.findViewById(R.id.text);
            row = (RelativeLayout) v.findViewById(R.id.row);

        }
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(act);
        int height = size.heightPixels;
        int width = size.widthPixels;

        //  holder.transparent_bg.getLayoutParams().height = (int) (height * 0.045);

        holder.cover.getLayoutParams().height = (int) (height * 0.15);
        holder.cover.getLayoutParams().width = (int) (height * 0.165);

        holder.text.getLayoutParams().width = (int) (height * 0.15);

        // holder.row.getLayoutParams().width = (int) (width * 0.35);
        holder.img_settings.getLayoutParams().height = (int) (height * 0.03);
        holder.img_settings.getLayoutParams().width = (int) (height * 0.03);
        holder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.017));
    }


}
