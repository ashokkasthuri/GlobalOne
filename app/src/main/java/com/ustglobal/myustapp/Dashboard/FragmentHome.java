package com.ustglobal.myustapp.Dashboard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.Common.URL;
import com.ustglobal.myustapp.Common.UST;
import com.ustglobal.myustapp.R;
import com.ustglobal.myustapp.Temp.ImageSliderFragmentNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Shubham Singhal.
 */

public class FragmentHome extends Fragment {

    private View rootView;
    //  private ViewPager mpager;
    private ImageSliderFragmentNew adapter;
    private HomeAdapter adapter1;
    private RecyclerView eventList;
    private AlertDialog alertDNew;
    private ProgressBar progress_bar;
    private String type, subType;
    public static HashMap<String, JSONArray> json_arr = new HashMap<>();
    //  private ArrayList<JSONArray> json_arr = new ArrayList<>();
    private ArrayList<EventsOBJ> events_arr = new ArrayList<>();
    private ArrayList<EventsOBJ> events_arr_flash = new ArrayList<>();
    //    private ArrayList<EventsOBJ> events_arr2 = new ArrayList<>();
//    private ArrayList<EventsOBJ> events_arr3 = new ArrayList<>();
//    private ArrayList<EventsOBJ> events_arr4 = new ArrayList<>();
//    private ArrayList<EventsOBJ> events_arr5 = new ArrayList<>();
//    private ArrayList<EventsOBJ> events_arr6 = new ArrayList<>();
    private Handler handler = new Handler();
    private SwipeRefreshLayout swipeLayout;
    private ProgressDialog progress;

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_events, container, false);
        init();
        setDimension();
      //  showAlertDialog("Loading..\nPlease Wait.");
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
     //   progress.setProgress(150);
        progress.setCancelable(false);
        progress.show();

//        String str = "There are some updates in Technology, please check @ Technology";
//        String[] split = str.split("@");
//        String message = split[0].trim();
//        Constants.type = split[1].trim();

        new Thread(new ThreadGetEvents()).start();
        //   parseData();
        return rootView;
    }

    private void init() {
        // mpager = (ViewPager) rootView.findViewById(R.id.pager);
        progress_bar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        eventList = (RecyclerView) rootView.findViewById(R.id.data);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                executorService.submit(new ThreadGetEvents());
                executorService.submit(new ThreadGetVersion());
               // new Thread(new ThreadGetVersion()).start();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(getActivity());
        int height = size.heightPixels;
        int width = size.widthPixels;

        //  mpager.getLayoutParams().height = (int) (height * 0.34);
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("events.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    class ThreadGetEvents implements Runnable {

        @Override
        public void run() {
            getEvents();
        }
    }


    private void getEvents() {
        try {
            Constants.events.clear();
            Constants.blog_list.clear();
            Constants.sub_events.clear();
            //     Constants.subCategory_list.clear();
            String url = URL.getEvents;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response + "");
                    if (response != null) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr_blogs = obj.getJSONArray("Blogs");
                            for (int j = 0; j < arr_blogs.length(); j++) {
                                JSONObject obj_blogs = arr_blogs.getJSONObject(j);
                                BlogsOBJ blog = new BlogsOBJ();
                                blog.setSuperCategory(obj_blogs.getString("SuperCategory"));
                                blog.setSuperCatg(obj_blogs.getJSONArray("SuperCatg"));
                                Constants.blog_list.add(blog);

                            }

                            for (int i = 0; i < Constants.blog_list.size(); i++) {
                                ArrayList<SubCategoryOBJ> sub_arr = new ArrayList<>();
                                BlogsOBJ blog = Constants.blog_list.get(i);
                                subType = blog.getSuperCategory();
                                JSONArray arr_sub = blog.getSuperCatg();
                                for (int m = 0; m < arr_sub.length(); m++) {
                                    JSONObject obj_sub = arr_sub.getJSONObject(m);
                                    SubCategoryOBJ sub_obj = new SubCategoryOBJ();
                                    String str = obj_sub.getString("SubCategory");
                                    sub_obj.setSubCategory(obj_sub.getString("SubCategory"));
                                    sub_obj.setSubCatgData(obj_sub.getJSONArray("SubCatgData"));
                                    sub_arr.add(sub_obj);
                                }
                                if (!Constants.sub_events.containsKey(subType))
                                    Constants.sub_events.put(subType, sub_arr);
                            }

//                            for (int k = 0; k < Constants.sub_events.size(); k++) {
//                                events_arr.clear();
//                                SubCategoryOBJ obj_new = Constants.sub_events.get(k);
//                                type = obj_new.getSubCategory();
//                                JSONArray arr = obj_new.getSubCatgData();
//                                for (int l = 0; l < arr.length(); l++) {
//                                    JSONObject obj_new1 = arr.getJSONObject(l);
//                                    EventsOBJ events_obj = new EventsOBJ();
//                                    events_obj.setPriority(obj_new1.getString("Priority"));
//                                    events_obj.setTitle(obj_new1.getString("Title"));
//                                    events_obj.setSubtitile(obj_new1.getString("Subtitile"));
//                                    events_obj.setDate(obj_new1.getString("Date"));
//                                    events_obj.setDescription(obj_new1.getString("Description"));
//                                    events_obj.setImageURL(obj_new1.getString("ImageURL"));
//                                    events_obj.setRegistrationLink(obj_new1.getString("RegistrationLink"));
//                                    events_arr.add(events_obj);
//                                }
//                                Constants.events.put(type, events_arr);
//                                System.out.println(Constants.events.toString());
//                            }


                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeLayout.setRefreshing(false);
                                    progress.cancel();
                                 //   alertDNew.cancel();
                                    adapter1 = new HomeAdapter(getActivity(), Constants.blog_list, Constants.sub_events);
                                    eventList.setAdapter(adapter1);
                                    eventList.setHasFixedSize(true);
                                    eventList.setNestedScrollingEnabled(false);
                                    LinearLayoutManager glManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
                                    eventList.setLayoutManager(glManager);

//                                    if(Constants.notification){
//                                        Constants.notification = false;
//                                        Intent in = new Intent(getActivity(), SubCategoryList.class);
//                                        in.putExtra("obj", Constants.type);
//                                        startActivity(in);
//                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                   // alertDNew.cancel();
                                    progress.cancel();
                                    swipeLayout.setRefreshing(false);
                                }
                            });
                        }


                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                               // alertDNew.cancel();
                                progress.cancel();
                                swipeLayout.setRefreshing(false);
                            }
                        });
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError volleyError) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String error = volleyError.getMessage();
                                    Log.e("Error", error + "'");
                                  //  alertDNew.cancel();
                                    progress.cancel();
                                    swipeLayout.setRefreshing(false);
                                }
                            });
                        }
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    20 * 1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            UST.getInstance().addToRequestQueue(request);

        } catch (Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                  //  alertDNew.cancel();
                    progress.cancel();
                    swipeLayout.setRefreshing(false);
                }
            });
        }
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);
        alertDNew = builder.create();
        alertDNew.show();
    }


    class ThreadGetVersion implements Runnable {

        @Override
        public void run() {
            getVersion();
        }
    }

    private void getVersion() {
        try {
            String url = URL.getAppVersion;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response + "");
                    if (response != null) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("versionctrl");
                            JSONObject obj_new = arr.getJSONObject(0);
                            String number = obj_new.getString("number");
                            PackageManager manager = getActivity().getPackageManager();
                            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                            String app_version_name = String.valueOf(info.versionName);
                            if (!app_version_name.equalsIgnoreCase(number)) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialogNew("This version is not supported anymore. Please update to latest version.", 1);
                                    }
                                });
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // alertDNew.cancel();

                            }
                        });
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError volleyError) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    20 * 1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            UST.getInstance().addToRequestQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlertDialogNew(String message, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (i == 1) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.internal.ustglobal")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.internal.ustglobal")));
                    }
                }
                return;
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                getActivity().finish();
                System.exit(0);
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        new Thread(new ThreadGetVersion()).start();
    }
}
