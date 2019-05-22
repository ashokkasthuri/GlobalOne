package com.ustglobal.myustapp.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolderNew> {
    private static LayoutInflater inflater = null;
    private Activity act;
    private ViewHolderNew holder;
    private ArrayList<Tweet> list;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public HashTagAdapter(Activity act, ArrayList<Tweet> list, RecyclerView recycler) {
        this.act = act;
        this.list = list;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TwitterConfig config = new TwitterConfig.Builder(act)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("PZzW8bn1iuZtEihYNNXRsivTx", "9xDwUO05v6OFrGjxhoh4utOiYeRscWQswwBmRcOaW3O0OS5dfU"))
                .debug(true)
                .build();
        Twitter.initialize(config);


        if (recycler.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recycler.getLayoutManager();
            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    @Override
    public ViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hashtags, null);
        holder = new ViewHolderNew(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderNew holder, int position) {
        setDimension();
        Tweet obj = list.get(position);

        final TweetView tweetView = new TweetView(act, obj, R.style.tw__TweetLightWithActionsStyle);
        holder.layout.addView(tweetView);

        View v = new View(act);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        param.setMargins(0,0,0,12);
        v.setLayoutParams(param);
        v.setBackgroundColor(Color.parseColor("#D0D2D3"));
        holder.layout.addView(v);


//        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
////                LinearLayout lp = new LinearLayout(act);
////                final LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////                lp.setLayoutParams(layout);
////                lp.setOrientation(LinearLayout.VERTICAL);
//
//                final TweetView tweetView = new TweetView(act, result.data, R.style.tw__TweetLightWithActionsStyle);
//                holder.layout.addView(tweetView);
//
//                View v = new View(act);
//                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
//                param.setMargins(0,0,0,12);
//                v.setLayoutParams(param);
//                v.setBackgroundColor(Color.parseColor("#D0D2D3"));
//                holder.layout.addView(v);
//
//               // holder.layout.addView(lp);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                // Toast.makeText(...).show();
//            }
//        });

    }

//    @Override
//    public int getItemViewType(int position) {
//        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolderNew extends RecyclerView.ViewHolder {
        LinearLayout layout;

        public ViewHolderNew(View v) {
            super(v);
            //  layout =  v.findViewById(R.id.layout);
            layout = (LinearLayout) v.findViewById(R.id.layout);


        }
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(act);
        int height = size.heightPixels;
        int width = size.widthPixels;

    }


    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

}
