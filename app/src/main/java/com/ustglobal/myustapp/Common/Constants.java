package com.ustglobal.myustapp.Common;

import com.ustglobal.myustapp.Dashboard.BlogsOBJ;
import com.ustglobal.myustapp.Dashboard.EventsOBJ;
import com.ustglobal.myustapp.Dashboard.HashTagOBJ;
import com.ustglobal.myustapp.Dashboard.SubCategoryOBJ;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubham Singhal.
 */

public class Constants {


    public static HashMap<String,ArrayList<EventsOBJ>> events = new HashMap<>();

    public static Map<String,ArrayList<SubCategoryOBJ>> sub_events = new HashMap<String,ArrayList<SubCategoryOBJ>>();

    public static ArrayList<HashTagOBJ> hash_list = new ArrayList<>();
    public static ArrayList<Tweet> tweet_list = new ArrayList<>();

    public static ArrayList<BlogsOBJ> blog_list = new ArrayList<>();

    public static ArrayList<SubCategoryOBJ> subCategory_list = new ArrayList<>();

    public static boolean tech;

    public static boolean news;

//    public static boolean notification = false;
//
//    public static String type;
}
