package com.sugengandreas.distrotest1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sugengandreas.distrotest1.FragmentBarang1;
import com.sugengandreas.distrotest1.FragmentBarang2;
import com.sugengandreas.distrotest1.FragmentBarang3;

import java.util.HashMap;
import java.util.Map;

public class TabsJenisPagerAdapter extends FragmentPagerAdapter {

    private String[] mainTitles = { "Baju", "celana","Topi"};
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;


    public TabsJenisPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();

    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new FragmentBarang1();
            case 1:
                return new FragmentBarang2();
            case 2:
                return new FragmentBarang3();

        }

        return null;
    }

    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] titles = {};
        titles = mainTitles;
        return titles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);

        }
        return obj;
    }


}

