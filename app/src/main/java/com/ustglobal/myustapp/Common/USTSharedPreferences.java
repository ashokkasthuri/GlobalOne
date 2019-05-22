package com.ustglobal.myustapp.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shubham Singhal.
 */

public class USTSharedPreferences {

    public interface IE {
        /**
         * Preference file name
         **/
        public static final String MyPREFERENCES = "MyPrefs";

        public static final String user_loggedIn = "loggedIn";

        public static final String email = "email";

        public static final String uid = "uid";

        public static final String firebase_token = "firebase_token";
    }

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public USTSharedPreferences(Context _context) {

        sharedpreferences = _context.getSharedPreferences(IE.MyPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public void setPrefrenceStringdata(String key, String value) {

        editor.putString(key, value);
        editor.commit();

    }

    public String getPrefrenceStringdata(String key, String default_value) {
        return sharedpreferences.getString(key, default_value);
    }

    public void setPrefrenceIntdata(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getPrefrenceIntdata(String key, int default_value) {
        return sharedpreferences.getInt(key, default_value);
    }

    public void setPrefrenceBooleandata(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getPrefrenceBooleandata(String key, boolean default_value) {
        return sharedpreferences.getBoolean(key, default_value);
    }
}
