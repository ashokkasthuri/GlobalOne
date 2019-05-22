package com.ustglobal.myustapp.Dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ustglobal.myustapp.R;


/**
 * Created by Shubham Singhal.
 */

public class FragmentSummary extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        init();
        setDimension();

        return rootView;
    }

    private void init(){
    }

    private void setDimension(){

    }
}
