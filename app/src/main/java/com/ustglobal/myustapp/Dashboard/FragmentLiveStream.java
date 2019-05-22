//package com.internal.ustglobal.Dashboard;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Chronometer;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.internal.ustglobal.R;
//import com.streamaxia.android.StreamaxiaPublisher;
//import com.streamaxia.android.handlers.EncoderHandler;
//import com.streamaxia.android.handlers.RecordHandler;
//import com.streamaxia.android.handlers.RtmpHandler;
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
//public class FragmentLiveStream extends Fragment implements RtmpHandler.RtmpListener, RecordHandler.RecordListener,
//        EncoderHandler.EncodeListener{
//    private View rootView;
//    private TextView stateTextView, startStopTextView;
//    private CameraView mCameraView;
//    private Chronometer mChronometer;
//
//    private static final int REQUEST_PERMISSIONS = 1;
//    private static String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
//
//    // Set default values for the streamer
//    public final static String streamaxiaStreamName = "shubham";
//    public final static int bitrate = 500;
//    public final static int width = 720;
//    public final static int height = 1280;
//
//    private StreamaxiaPublisher mPublisher;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_live_stream, container, false);
//        init();
//        setDimension();
//        verifyStoragePermissions(getActivity());
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//     //   hideStatusBar();
//
//        mPublisher = new StreamaxiaPublisher(mCameraView, getActivity());
//
//        mPublisher.setEncoderHandler(new EncoderHandler(this));
//        mPublisher.setRtmpHandler(new RtmpHandler(this));
//        mPublisher.setRecordEventHandler(new RecordHandler(this));
//
//        setStreamerDefaultValues();
//        return rootView;
//    }
//
//    private void init() {
//        mChronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
//
//        stateTextView = (TextView) rootView.findViewById(R.id.state_text);
//        startStopTextView = (TextView) rootView.findViewById(R.id.start_stop);
//
//        mCameraView = (CameraView) rootView.findViewById(R.id.preview);
//
//        startStopTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startStopStream();
//            }
//        });
//    }
//
//    private void setDimension() {
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        try{
//        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            mCameraView.start();
//            stopStreaming();
//            stopChronometer();
//            startStopTextView.setText("START");
//        }
//        }catch (Exception e){
//            e.printStackTrace();
//            Toast.makeText(getActivity(),"Error occurred while loading camera",Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mCameraView.stop();
//        mPublisher.pauseRecord();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPublisher.stopPublish();
//        mPublisher.stopRecord();
//    }
//
//    public void startStopStream() {
//        if (startStopTextView.getText().toString().toLowerCase().equals("start")) {
//            startStopTextView.setText("STOP");
//            mChronometer.setBase(SystemClock.elapsedRealtime());
//            mChronometer.start();
//            mPublisher.startPublish("rtmp://rtmp.streamaxia.com/streamaxia/" + streamaxiaStreamName);
//        } else {
//            startStopTextView.setText("START");
//            stopChronometer();
//            mPublisher.stopPublish();
//        }
//    }
//
//    private void stopStreaming() {
//        mPublisher.stopPublish();
//    }
//
//    private void setStatusMessage(final String msg) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                stateTextView.setText("[" + msg + "]");
//            }
//        });
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mPublisher.setScreenOrientation(newConfig.orientation);
//    }
//
//    public static void verifyStoragePermissions(Activity act) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(act, PERMISSIONS, REQUEST_PERMISSIONS);
//        }
//    }
//
////    private void hideStatusBar() {
////        View decorView = getActivity().getWindow().getDecorView();
////        // Hide the status bar.
////        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
////        decorView.setSystemUiVisibility(uiOptions);
////    }
//
//    private void setStreamerDefaultValues() {
//        mPublisher.setVideoBitRate(bitrate);
//        mPublisher.setVideoOutputResolution(width, height, getResources().getConfiguration().orientation);
//    }
//
//    private void stopChronometer() {
//        mChronometer.setBase(SystemClock.elapsedRealtime());
//        mChronometer.stop();
//    }
//
//    private void handleException(Exception e) {
//        try {
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            mPublisher.stopPublish();
//        } catch (Exception e1) {
//            // Ignore
//        }
//    }
//
//    @Override
//    public void onNetworkWeak() {
//
//    }
//
//    @Override
//    public void onNetworkResume() {
//
//    }
//
//    @Override
//    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
//
//    }
//
//    @Override
//    public void onRecordPause() {
//
//    }
//
//    @Override
//    public void onRecordResume() {
//
//    }
//
//    @Override
//    public void onRecordStarted(String s) {
//
//    }
//
//    @Override
//    public void onRecordFinished(String s) {
//
//    }
//
//    @Override
//    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRecordIOException(IOException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpConnecting(String s) {
//        setStatusMessage(s);
//    }
//
//    @Override
//    public void onRtmpConnected(String s) {
//        setStatusMessage(s);
//        startStopTextView.setText("STOP");
//    }
//
//    @Override
//    public void onRtmpVideoStreaming() {
//
//    }
//
//    @Override
//    public void onRtmpAudioStreaming() {
//
//    }
//
//    @Override
//    public void onRtmpStopped() {
//        setStatusMessage("STOPPED");
//    }
//
//    @Override
//    public void onRtmpDisconnected() {
//        setStatusMessage("Disconnected");
//    }
//
//    @Override
//    public void onRtmpVideoFpsChanged(double v) {
//
//    }
//
//    @Override
//    public void onRtmpVideoBitrateChanged(double v) {
//
//    }
//
//    @Override
//    public void onRtmpAudioBitrateChanged(double v) {
//
//    }
//
//    @Override
//    public void onRtmpSocketException(SocketException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpIOException(IOException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpIllegalStateException(IllegalStateException e) {
//        handleException(e);
//    }
//
//    @Override
//    public void onRtmpAuthenticationg(String s) {
//
//    }
//}