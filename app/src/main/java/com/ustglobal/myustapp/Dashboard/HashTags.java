package com.ustglobal.myustapp.Dashboard;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.Common.EndlessRecyclerViewScrollListener;
import com.ustglobal.myustapp.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;


/**
 * Created by Shubham Singhal.
 */

public class HashTags extends AppCompatActivity {

    // private View rootView;
    private Toolbar toolbar;
    private AlertDialog alertDNew;
    private Handler handler = new Handler();
    private static String access_token;
  //  private RecyclerView listHash;
    private HashTagAdapter adapter;
    private ProgressDialog progress;
    LinearLayout layout;
    private SwipeRefreshLayout swipeLayout;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hashtags);
        init();
        setDimension();
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("L1cas1BNRywJhxDRwU0RKUH9Y", "02DJOr2PMeVUwUhKg7lryPQM20fj8w21L3isOLpY5sLZiQ3yVh"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        //   showAlertDialog("Loading..\nPlease Wait.");
        progress = new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // progress.setProgress(150);
        progress.setCancelable(false);
        progress.show();
        new Thread(new ThreadBearerToken()).start();
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("#USTGlobal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        listHash = (RecyclerView) findViewById(R.id.hashtags);
//        listHash.setHasFixedSize(true);
//        LinearLayoutManager glManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
//        listHash.setLayoutManager(glManager);

        layout = (LinearLayout)findViewById(R.id.layout);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                new Thread(new ThreadBearerToken()).start();
             //   new Thread(new ThreadGetHashTags()).start();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(this);
        int height = size.heightPixels;
        int width = size.widthPixels;

    }

    private static String getB64Auth(String key, String secret) {
        String source = key + ":" + secret;
        String base64 = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return base64;
    }

    class ThreadBearerToken implements Runnable {
        @Override
        public void run() {
            getBearerToken();
        }
    }

    private void getBearerToken() {
        try {

            String url = "https://api.twitter.com/oauth2/token";
            String parameters = "grant_type=" + "client_credentials";
            String response = getPostDataFromServer(parameters, url);
            if (response != null) {
                try {
                    JSONObject obj = new JSONObject(response);
                    access_token = obj.getString("access_token");
                    if (access_token != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                layout.removeAllViews();
                                new Thread(new ThreadGetHashTags()).start();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //  alertDNew.cancel();
                                progress.cancel();
                                Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //alertDNew.cancel();
                            progress.cancel();
                            Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //  alertDNew.cancel();
                        progress.cancel();
                        Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // alertDNew.cancel();
                    progress.cancel();
                    Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class ThreadGetHashTags implements Runnable {

        @Override
        public void run() {
            getHashtags();
        }
    }


    private void getHashtags() {
        try {
         //   Constants.tweet_list.clear();
           // "https://api.twitter.com/1.1/search/tweets.json?q=%40ustglobal%2BOR%2B%23ustglobal-filter:retweets&count=1000"
            String url = "https://api.twitter.com/1.1/search/tweets.json?q=%40ustglobal%2BOR%2B%23ustglobal-filter:retweets";
            String response = getDataFromServer(url);
            if (response != null) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arr = obj.getJSONArray("statuses");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject new_obj = arr.getJSONObject(i);
                        final long tweetId = Long.valueOf(new_obj.getString("id"));
                        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
                            @Override
                            public void success(final Result<Tweet> result) {
                           //     Constants.tweet_list.add(result.data);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeLayout.setRefreshing(false);

                                        final TweetView tweetView = new TweetView(HashTags.this, result.data, R.style.tw__TweetLightWithActionsStyle);
                                        layout.addView(tweetView);

                                        View v = new View(HashTags.this);
                                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                                        param.setMargins(0,0,0,12);
                                        v.setLayoutParams(param);
                                        v.setBackgroundColor(Color.parseColor("#D0D2D3"));
                                        layout.addView(v);
                                    }
                                });
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                // Toast.makeText(...).show();
                                String str = exception.toString();
                                String str1 = str;
                            }
                        });


//
//                        HashTagOBJ hash_obj = new HashTagOBJ();
//                        hash_obj.setCreated_at(new_obj.getString("created_at"));
//                        hash_obj.setId(new_obj.getString("id"));
//                        hash_obj.setText(new_obj.getString("text"));
//                        Constants.hash_list.add(hash_obj);
                    }
//
//                    for (int j = 0; j < Constants.hash_list.size(); j++) {
//                        HashTagOBJ hash_obj = Constants.hash_list.get(j);
//                        final long tweetId = Long.valueOf(hash_obj.getId());
//                        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
//                            @Override
//                            public void success(Result<Tweet> result) {
//                                Constants.tweet_list.add(result.data);
//                            }
//
//                            @Override
//                            public void failure(TwitterException exception) {
//                                // Toast.makeText(...).show();
//                                String str = exception.toString();
//                                String str1 = str;
//                            }
//                        });
//                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //   alertDNew.cancel();

                            progress.cancel();
                            swipeLayout.setRefreshing(false);
//                            adapter = new HashTagAdapter(HashTags.this, Constants.tweet_list, listHash);
//                            listHash.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                            adapter.setOnLoadMoreListener(new HashTagAdapter.OnLoadMoreListener() {
//                                @Override
//                                public void onLoadMore() {
//                                    //add progress item
//                                    Constants.hash_list.add(null);
//                                    adapter.notifyItemInserted( Constants.hash_list.size() - 1);
//
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            //remove progress item
//                                            Constants.hash_list.remove( Constants.hash_list.size() - 1);
//                                            adapter.notifyItemRemoved( Constants.hash_list.size());
//                                            //add items one by one
//                                            for (int i = 0; i < Constants.hash_list.size(); i++) {
//                                                Constants.hash_list.add(new HashTagOBJ("ID "  + ( Constants.hash_list.size() + 1)));
//                                                //Constants.hash_list.add("Item " + ( Constants.hash_list.size() + 1));
//                                                adapter.notifyItemInserted( Constants.hash_list.size());
//                                            }
//                                            adapter.setLoaded();
//                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                                        }
//                                    }, 2000);
//                                    System.out.println("load");
//                                }
//                            });

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //  alertDNew.cancel();
                            progress.cancel();
                            swipeLayout.setRefreshing(false);
                            Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //alertDNew.cancel();
                        progress.cancel();
                        swipeLayout.setRefreshing(false);
                        Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //   alertDNew.cancel();
                    progress.cancel();
                    swipeLayout.setRefreshing(false);
                    Toast.makeText(HashTags.this, "Error Occurred,Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public static String getDataFromServer(String u) throws Exception {

        java.net.URL url = null;
        String result = null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            url = new java.net.URL(u);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add reuqest header
            con.setRequestMethod("GET");
            con.setRequestProperty("Host", "api.twitter.com");
            con.setRequestProperty("User-Agent", "My Twitter App v1.0.23");
            con.setRequestProperty("Authorization", "Bearer " + access_token );
            //  con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            result = response.toString();
            System.out.println(response.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String getPostDataFromServer(String parametrs, String str_url) {
        java.net.URL url = null;
        String result = null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            url = new java.net.URL(str_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Host", "api.twitter.com");
            con.setRequestProperty("User-Agent", "My Twitter App v1.0.23");
            con.setRequestProperty("Authorization", getB64Auth("L1cas1BNRywJhxDRwU0RKUH9Y", "02DJOr2PMeVUwUhKg7lryPQM20fj8w21L3isOLpY5sLZiQ3yVh"));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

//            con.setRequestProperty("Content-Length", "29");
//            con.setRequestProperty("Accept-Encoding", "gzip");

            // String urlParameters = "Username="+AppConstants.AuthTokenUsername + "&password="+AppConstants.AuthTokenPassword;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parametrs);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + parametrs);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            result = response.toString();
            //print result
            System.out.println(response.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        alertDNew = builder.create();
        alertDNew.show();
    }

}
