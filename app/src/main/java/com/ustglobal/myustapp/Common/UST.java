package com.ustglobal.myustapp.Common;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
/**
 * Created by Shubham Singhal.
 */

public class UST extends Application {
    private static UST instance = null;

    private RequestQueue mRequestQueue;
    final private String TAG = this.getClass().getName();

    /**
     * get instance of this class
     *
     * @return
     */
    static public UST getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
    }




    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            String userAgent = "volley/0";
            try {
                String packageName = getApplicationContext().getPackageName();
                PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
                userAgent = packageName + "/" + info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
            }

//            HttpStack httpStack = new OwnHttpClientStack(AndroidHttpClient.newInstance(userAgent));
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), httpStack);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
