package com.sugengandreas.distrotest1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sander on 2/24/2016.
 */
public class FragmentAbout extends android.support.v4.app.Fragment {

    public static final String TAG = FragmentAbout.class.getSimpleName();

    public static FragmentAbout newInstance() {
        return new FragmentAbout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        return v;
    }
}
