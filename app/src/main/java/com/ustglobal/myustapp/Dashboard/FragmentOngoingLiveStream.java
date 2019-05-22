//package com.internal.ustglobal.Dashboard;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.view.LayoutInflater;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Chronometer;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.exoplayer.AspectRatioFrameLayout;
//import com.internal.ustglobal.R;
//import com.streamaxia.android.StreamaxiaPublisher;
//import com.streamaxia.android.handlers.EncoderHandler;
//import com.streamaxia.android.handlers.RecordHandler;
//import com.streamaxia.android.handlers.RtmpHandler;
//import com.streamaxia.player.StreamaxiaPlayer;
//import com.streamaxia.player.listener.StreamaxiaPlayerState;
//
//import java.io.IOException;
//import java.net.SocketException;
//
//import libs.google.android.cameraview.CameraView;
//
///**
// * Created by Shubham Singhal.
// */
//
//public class FragmentOngoingLiveStream extends Fragment implements StreamaxiaPlayerState {
//    private View rootView;
//    private AspectRatioFrameLayout aspectRatioFrameLayout;
//    private SurfaceView surfaceView;
//    private ProgressBar progressBar;
//    private TextView txt;
//    private Uri uri;
//    private Handler handler = new Handler();
//    private ImageView playBtn;
//
//    private StreamaxiaPlayer mStreamaxiaPlayer = new StreamaxiaPlayer();
//
//    private int STREAM_TYPE = 0;
//
//    Runnable hide = new Runnable() {
//        @Override
//        public void run() {
//            playBtn.setVisibility(View.GONE);
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_ongoing_live_stream, container, false);
//        init();
//        setDimension();
//        uri = Uri.parse("rtmp://rtmp.streamaxia.com/streamaxia/shubham");
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        playBtn.setTag("play");
//        initRTMPExoPlayer();
//        return rootView;
//    }
//
//    private void init() {
//        txt = (TextView) rootView.findViewById(R.id.txt);
//        playBtn = (ImageView)rootView.findViewById(R.id.play);
//        aspectRatioFrameLayout = (AspectRatioFrameLayout) rootView.findViewById(R.id.video_frame);
//
//        surfaceView = (SurfaceView) rootView.findViewById(R.id.surface_view);
//
//        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
//
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playBtn.postDelayed(hide, 1000);
//                if (playBtn.getTag().equals("play")) {
//                    mStreamaxiaPlayer.play(uri, StreamaxiaPlayer.TYPE_RTMP);
//                    surfaceView.setBackgroundColor(Color.TRANSPARENT);
//                    progressBar.setVisibility(View.GONE);
//                    playBtn.setTag("stop");
//                  //  playBtn.setImageResource(R.drawable.pause);
//                    setAspectRatioFrameLayoutOnClick();
//                } else {
//                    mStreamaxiaPlayer.stop();
//                    progressBar.setVisibility(View.VISIBLE);
//                    playBtn.setTag("play");
//                    playBtn.setImageResource(R.drawable.play);
//                }
//            }
//        });
//    }
//
//    private void setDimension() {
//
//    }
//
//    private void setAspectRatioFrameLayoutOnClick() {
//        aspectRatioFrameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playBtn.setVisibility(View.VISIBLE);
//                playBtn.postDelayed(hide, 1000);
//            }
//        });
//    }
//
//    private void initRTMPExoPlayer() {
//        try {
//            mStreamaxiaPlayer.initStreamaxiaPlayer(surfaceView, aspectRatioFrameLayout, txt, this, getActivity(), uri);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mStreamaxiaPlayer.stop();
//    }
//
//    @Override
//    public void stateENDED() {
//        progressBar.setVisibility(View.GONE);
//        playBtn.setImageResource(R.drawable.play);
//    }
//
//    @Override
//    public void stateBUFFERING() {
//        progressBar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void stateIDLE() {
//        progressBar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void statePREPARING() {
//        progressBar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void stateREADY() {
//        progressBar.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void stateUNKNOWN() {
//        progressBar.setVisibility(View.VISIBLE);
//    }
//}