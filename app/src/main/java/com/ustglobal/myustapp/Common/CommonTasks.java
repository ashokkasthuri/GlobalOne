package com.ustglobal.myustapp.Common;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Shubham Singhal.
 */

public class CommonTasks {


    public static DisplayMetrics getScreenSize(Activity activity) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        (activity).getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics;
    }

//    public static void setFont(Context ctx, TextView view) {
//        Typeface roboto = Typeface.createFromAsset(ctx.getAssets(),
//                "fonts/Roboto-Regular.ttf");
//        view.setTypeface(roboto);
//    }
//
//
//    public static boolean isNetworkAvailable(Activity act) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }
//
//    public static String md5(String s) {
//
//        byte[] hash;
//
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Huh, MD5 should be supported?", e);
//
//
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
//        }
//
//        StringBuilder hex = new StringBuilder(hash.length * 2);
//
//        for (byte b : hash) {
//            int i = (b & 0xFF);
//            if (i < 0x10) hex.append('0');
//            hex.append(Integer.toHexString(i));
//        }
//
//        return hex.toString();
//
//    }

}
