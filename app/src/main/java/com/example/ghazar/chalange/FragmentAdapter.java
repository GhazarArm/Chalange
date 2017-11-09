package com.example.ghazar.chalange;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ghazar on 11/8/17.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    private Tag1 tag1;
    private Tag2 tag2;

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                tag1 = new Tag1();
                MainActivity.m_mainActivity.setTab1(tag1);
                return tag1;
            case 1:
                tag2 = new Tag2();
                MainActivity.m_mainActivity.setTab2(tag2);
                return tag2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "Profile";
            case 1:return "Search";
            default:return null;
        }
    }
}
