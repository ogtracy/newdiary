package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by arun prasad on 28-09-2015.
 */
//java file for thoughts
public class FourthFragment extends Fragment {

    public static FourthFragment newInstance() {
        FourthFragment fragment = new FourthFragment();
        return fragment;
    }

    public FourthFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);
        return rootView;//calling thoughts page
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}