package com.sugengandreas.distrotest1;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugengandreas.distrotest1.adapter.TabsJenisPagerAdapter;
import com.sugengandreas.distrotest1.tabs.SlidingTabLayout;


public class FragmentBarang extends android.support.v4.app.Fragment {
    private ViewPager viewPager;
    private SlidingTabLayout tabs;
    public static FragmentBarang newInstance() {
        return new FragmentBarang();
    }
    public static final String TAG = FragmentBarang.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_barang, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        TabsJenisPagerAdapter adapter = new TabsJenisPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs = (SlidingTabLayout) v.findViewById(R.id.slidingTabs);
        tabs.setDistributeEvenly(true);
        tabs.setSelectedIndicatorColors(getResources().getColor(
                R.color.color_accent));
        tabs.setBackgroundColor(getResources().getColor(R.color.tab_background));
        tabs.setViewPager(viewPager);
        return v;
    }

}
