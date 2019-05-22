package com.ustglobal.myustapp.NavigationDrawer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.R;

/**
 * Created by Shubham Singhal.
 */
public class WebView extends AppCompatActivity {

    Toolbar toolbar;
    private android.webkit.WebView view;
    private String url;
    private String title;
  //  AlertDialog alertNew;
    private ProgressDialog progressD;
    String obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        obj = getIntent().getStringExtra("obj");

        if(obj.equalsIgnoreCase("AboutUs"))
            url = "http://www.ust-global.com/";
        else if (obj.equalsIgnoreCase("Tech_trends"))
            url = "http://ec2-13-228-226-32.ap-southeast-1.compute.amazonaws.com:8080/might/#/";

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        progressD=new ProgressDialog(WebView.this);
        progressD.setMessage("Loading Data");
        progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressD.setCancelable(false);
       // progressD.setProgress(150);
        progressD.show();


        view = (android.webkit.WebView) findViewById(R.id.webView);
        view.setWebChromeClient(new WebChromeClient());
     //   shoewAlertDialogNew("Loading..\nPlease Wait.");
        view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(android.webkit.WebView view, int progress) {
                if (progress == 20) {
                   // alertNew.cancel();
                    progressD.cancel();
                }
            }
        });
        view.setWebViewClient(new WebViewClient());
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.getSettings().setSupportZoom(true);
        view.loadUrl(url);
    }


//    /**
//     * Alert Dialog
//     *
//     * @param message
//     */
//    private void shoewAlertDialogNew(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(WebView.this);
//        builder.setMessage(message);
//        builder.setCancelable(false);
//        alertNew = builder.create();
//        alertNew.show();
//    }
}
