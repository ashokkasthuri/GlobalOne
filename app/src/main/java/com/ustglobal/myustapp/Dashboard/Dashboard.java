package com.ustglobal.myustapp.Dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ustglobal.myustapp.Common.USTSharedPreferences;
import com.ustglobal.myustapp.Login.Login;
import com.ustglobal.myustapp.NavigationDrawer.FragmentDrawer;
import com.ustglobal.myustapp.NavigationDrawer.WebView;
import com.ustglobal.myustapp.R;

/**
 * Created by Shubham Singhal.
 */

public class Dashboard extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Fragment fragment;
    private USTSharedPreferences sp = null;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sp = new USTSharedPreferences(getApplicationContext());
        init();
        setDimension();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
      //  getSupportActionBar().setTitle("Home");
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(Dashboard.this);
        checkLocationPermission();

    }

    private void setDimension() {

    }


    @Override
    public void onDrawerItemSelected(View view, int position) {

        if (position == 0) {
            getSupportActionBar().setTitle("Home");
            Fragment fr = new FragmentHome();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        if (position == 1) {
            getSupportActionBar().setTitle("Profile");
            fragment = new FragmentProfile();
            FragmentManager fm = getSupportFragmentManager();
           // fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (position == 2) {
         //   getSupportActionBar().setTitle("Events");
//            Fragment fr = new FragmentEvents();
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment, fr);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
            Intent in = new Intent(Dashboard.this, SubCategoryList.class);
            in.putExtra("obj", "Events");
            startActivity(in);
        }

        if (position == 3) {
            Intent in = new Intent(Dashboard.this, SubCategoryList.class);
            in.putExtra("obj", "Technology");
            startActivity(in);
        }
        if (position == 4) {
            Intent in = new Intent(Dashboard.this, WebView.class);
            in.putExtra("obj", "Tech_trends");
            startActivity(in);
        }

        if (position == 5) {
            Intent in = new Intent(Dashboard.this, SubCategoryList.class);
            in.putExtra("obj", "News @ Malaysia");
            startActivity(in);
        }

        if (position == 6) {
            Intent in = new Intent(Dashboard.this, SubCategoryList.class);
            in.putExtra("obj", "Announcements");
            startActivity(in);
        }


        if (position == 7) {
//            getSupportActionBar().setTitle("Book An Uber");
//            Fragment fr = new FragmentBookUber(Dashboard.this);
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment, fr);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
            Intent in = new Intent(Dashboard.this,BookUber.class);
            startActivity(in);

        }


        if (position == 8) {
//            getSupportActionBar().setTitle("#USTGlobal");
//            Fragment fr = new HashTags();
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment, fr);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

            Intent in = new Intent(Dashboard.this,HashTags.class);
            startActivity(in);

        }

        if (position == 9) {
            Intent in = new Intent(Dashboard.this, WebView.class);
            in.putExtra("obj", "AboutUs");
            startActivity(in);
        }

        if (position == 10) {
            sp.setPrefrenceBooleandata(USTSharedPreferences.IE.user_loggedIn, false);
            sp.setPrefrenceStringdata(USTSharedPreferences.IE.email, null);
            sp.setPrefrenceStringdata(USTSharedPreferences.IE.uid, null);
            Intent in = new Intent(Dashboard.this, Login.class);
            startActivity(in);
            finish();


        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 4000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if the fragment is an instance of the right fragment
        if (fragment instanceof FragmentProfile) {
            FragmentProfile my = (FragmentProfile) fragment;
            // Pass intent or its data to the fragment's method
            my.resumeIntent(intent);
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSupportActionBar().setTitle("Home");
//        Fragment fr = new FragmentHome();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment, fr);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }
}
