package com.ustglobal.myustapp.Dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;
import com.ustglobal.myustapp.Temp.ImageSliderFragmentNew;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Shubham Singhal.
 */

public class FragmentEvents extends Fragment {

    private View rootView;
  //  private ViewPager mpager;
    private ImageSliderFragmentNew adapter;
    private EventsAdapter adapter1;
    private RecyclerView eventList;
    private AlertDialog alertDNew;
    private String type;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_events, container, false);
        init();
        setDimension();
        showAlertDialog("Loading..\nPlease Wait.");
        new Thread(new ThreadGetEvents()).start();
        //   parseData();
        return rootView;
    }

    private void init() {
       // mpager = (ViewPager) rootView.findViewById(R.id.pager);
        eventList = (RecyclerView) rootView.findViewById(R.id.data);
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
//        try {
//            Constants.events.clear();
//            Constants.blog_list.clear();;
//            Constants.blog_list_flash.clear();
//            String url = URL.getEvents;
//            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.e("Response", response + "");
//                    if (response != null) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            JSONArray arr_blogs = obj.getJSONArray("Blogs");
//                            for (int j = 0; j < arr_blogs.length(); j++) {
//                                JSONObject obj_blogs = arr_blogs.getJSONObject(j);
//                                BlogsOBJ blog = new BlogsOBJ();
//                                if(obj_blogs.getString("Category").equalsIgnoreCase("Flash")){
//                                    blog.setSuperCategory(obj_blogs.getString("SuperCategory"));
//                                    blog.setSuperCatg(obj_blogs.getJSONArray("SuperCatg"));
//                                    Constants.blog_list.add(blog);
//                                }
//
//
//                            }
//
////
////                            JSONArray arr_flash = obj.getJSONArray("Flash");
////                            JSONArray arr_sports = obj.getJSONArray("Sports");
////                            JSONArray arr_technology = obj.getJSONArray("Technology");
////                            JSONArray arr_news = obj.getJSONArray("News Letter");
////                            JSONArray arr_ust_quiz = obj.getJSONArray("UST Quiz");
////                            JSONArray arr_ust_news = obj.getJSONArray("UST News");
////
////                            json_arr.add(arr_flash);
////                            json_arr.add(arr_sports);
////                            json_arr.add(arr_technology);
////                            json_arr.add(arr_news);
////                            json_arr.add(arr_ust_quiz);
////                            json_arr.add(arr_ust_news);
//
////                            for (int i = 0; i < Constants.blog_list.size(); i++) {
////                                BlogsOBJ obj_new = Constants.blog_list.get(i);
////                                json_arr.put(obj_new.getCategory(), obj_new.getCatgData());
////                            }
//
//                                events_arr_flash.clear();
//                                BlogsOBJ obj_new_flash = Constants.blog_list_flash.get(0);
//                                type = obj_new_flash.getCategory();
//                                JSONArray arr_flash = obj_new_flash.getCatgData();
//                                for (int i = 0; i < arr_flash.length(); i++) {
//                                    JSONObject obj_new1 = arr_flash.getJSONObject(i);
//                                    EventsOBJ events_obj = new EventsOBJ();
//                                    events_obj.setPriority(obj_new1.getString("Priority"));
//                                    events_obj.setTitle(obj_new1.getString("Title"));
//                                    events_obj.setSubtitile(obj_new1.getString("Subtitile"));
//                                    events_obj.setDate(obj_new1.getString("Date"));
//                                    events_obj.setDescription(obj_new1.getString("Description"));
//                                    events_obj.setImageURL(obj_new1.getString("ImageURL"));
//                                    events_obj.setRegistrationLink(obj_new1.getString("RegistrationLink"));
//                                    events_arr_flash.add(events_obj);
//                                }
//
//
//                            for (int l = 0; l < Constants.blog_list.size(); l++) {
//                                events_arr.clear();
//                                BlogsOBJ obj_new = Constants.blog_list.get(l);
//                                type = obj_new.getCategory();
//                                JSONArray arr = obj_new.getCatgData();
//                                for (int k = 0; k < arr.length(); k++) {
//                                    JSONObject obj_new1 = arr.getJSONObject(k);
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
//
//
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    alertDNew.cancel();
//                                  //  ArrayList<EventsOBJ> flash_arr = Constants.events.get("Flash");
////                                    adapter = new ImageSliderFragmentNew(getChildFragmentManager(), events_arr_flash);
////                                    mpager.setAdapter(adapter);
////                                    adapter.notifyDataSetChanged();
//
//                                    adapter1 = new EventsAdapter(getActivity(), Constants.events);
//                                    eventList.setAdapter(adapter1);
//                                    eventList.setHasFixedSize(true);
//                                    eventList.setNestedScrollingEnabled(false);
//                                    LinearLayoutManager glManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
//                                    eventList.setLayoutManager(glManager);
//
//                                }
//                            });
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    alertDNew.cancel();
//                                }
//                            });
//                        }
//
//
//                    } else {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                alertDNew.cancel();
//                            }
//                        });
//                    }
//                }
//            },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(final VolleyError volleyError) {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String error = volleyError.getMessage();
//                                    Log.e("Error", error + "'");
//                                }
//                            });
//                        }
//                    });
//
//            request.setRetryPolicy(new DefaultRetryPolicy(
//                    20 * 1000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            UST.getInstance().addToRequestQueue(request);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    alertDNew.cancel();
//                }
//            });
//        }
    }

//    public void parseData() {
//        try {
//            JSONArray arr = new JSONArray(loadJSONFromAsset());
//            JSONObject obj = null;
//            for (int i = 0; i < arr.length(); i++) {
//                obj = arr.getJSONObject(i);
//                EventsOBJ events_obj = new EventsOBJ();
//                events_obj.setPriority(obj.getString("Priority"));
//                events_obj.setTitle(obj.getString("Title"));
//                events_obj.setSubtitile(obj.getString("Subtitile"));
//                events_obj.setDate(obj.getString("Date"));
//                events_obj.setDescription(obj.getString("Description"));
//                events_obj.setImageURL(obj.getString("ImageURL"));
//                events_obj.setRegistrationLink(obj.getString("RegistrationLink"));
//
//                if (obj.getString("Priority").equalsIgnoreCase("1"))
//                    events_arr1.add(events_obj);
//                if (obj.getString("Priority").equalsIgnoreCase("2"))
//                    events_arr2.add(events_obj);
//                if (obj.getString("Priority").equalsIgnoreCase("3"))
//                    events_arr3.add(events_obj);
//                if (obj.getString("Priority").equalsIgnoreCase("4"))
//                    events_arr4.add(events_obj);
//                if (obj.getString("Priority").equalsIgnoreCase("5"))
//                    events_arr5.add(events_obj);
//            }
//
//            Constants.events.put(1, events_arr1);
//            Constants.events.put(2, events_arr2);
//            Constants.events.put(3, events_arr3);
//            Constants.events.put(4, events_arr4);
//            Constants.events.put(5, events_arr5);
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    alertDNew.cancel();
//                    adapter = new ImageSliderFragmentNew(getChildFragmentManager(), Constants.events.get(1));
//                    mpager.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                    adapter1 = new EventsAdapter(getActivity(), Constants.events);
//                    eventList.setAdapter(adapter1);
//                    eventList.setHasFixedSize(true);
//                    eventList.setNestedScrollingEnabled(false);
//                    LinearLayoutManager glManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
//                    eventList.setLayoutManager(glManager);
//
//                }
//            });
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);
        alertDNew = builder.create();
        alertDNew.show();
    }
}
