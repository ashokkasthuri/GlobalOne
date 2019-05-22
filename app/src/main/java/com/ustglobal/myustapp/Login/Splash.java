package com.ustglobal.myustapp.Login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.USTSharedPreferences;
import com.ustglobal.myustapp.Dashboard.Dashboard;
import com.ustglobal.myustapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Shubham Singhal.
 */

public class Splash extends AppCompatActivity {

    private ImageView logo;
    private USTSharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String android_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sp = new USTSharedPreferences(getApplicationContext());
        logo = (ImageView) findViewById(R.id.logo);

        DisplayMetrics size = CommonTasks.getScreenSize(Splash.this);
        int height = size.heightPixels;
        int width = size.widthPixels;
        logo.getLayoutParams().height = (int) (height * 0.2);
        logo.getLayoutParams().width = (int) (width * 0.85);
        showHashKey(getApplicationContext());

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                init(Splash.this);
            }
        }).start();
    }

    private void init(Context ctx) {
        // TODO Auto-generated method stub
        try {
            Thread.sleep(2000);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    // TODO Auto-generated method stub

                    boolean loggedIn = sp.getPrefrenceBooleandata(USTSharedPreferences.IE.user_loggedIn, false);
                    if (loggedIn) {
                        startActivity(new Intent(Splash.this, Dashboard.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splash.this, Login.class));
                        finish();
                    }

                }
            });

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.internal.ustglobal", PackageManager.GET_SIGNATURES); //Your package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

}
