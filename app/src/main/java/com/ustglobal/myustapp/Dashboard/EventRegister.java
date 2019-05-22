package com.ustglobal.myustapp.Dashboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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


public class EventRegister extends AppCompatActivity {

    Toolbar toolbar;
    private android.webkit.WebView view;
    private String url;
    private String title;
    AlertDialog alertNew;
    private ProgressDialog progressD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        url = getIntent().getStringExtra("url");
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        progressD = new ProgressDialog(EventRegister.this);
        progressD.setMessage("Loading Data");
        progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //  progressD.setProgress(150);
        progressD.setCancelable(false);
        progressD.show();

        if (Constants.tech)
            getSupportActionBar().setTitle("Read More");
        else if (Constants.news)
            getSupportActionBar().setTitle("Read More");
        else
            getSupportActionBar().setTitle("Register");


        view = (android.webkit.WebView) findViewById(R.id.webView);
        view.setWebChromeClient(new WebChromeClient());
        //  shoewAlertDialogNew("Loading..\nPlease Wait.");
        view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(android.webkit.WebView view, int progress) {
                if (progress == 100) {
                    progressD.cancel();
                    //alertNew.cancel();
                }
            }
        });
        view.setWebViewClient(new WebViewClient());
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.getSettings().setSupportZoom(true);
        if (Constants.news)
            view.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        else
            view.loadUrl(url);
    }


//    /**
//     * Alert Dialog
//     *
//     * @param message
//     */
//    private void shoewAlertDialogNew(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(EventRegister.this);
//        builder.setMessage(message);
//        builder.setCancelable(false);
//        alertNew = builder.create();
//        alertNew.show();
//    }
}
