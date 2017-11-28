package com.example.ghazar.chalange.Tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ghazar.chalange.Activitys.MainActivity;

/**
 * Created by ghazar on 11/8/17.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    private Tag1 tag1;
    private Tag2 tag2;
    private MyProfile m_myProfile;

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                m_myProfile = new MyProfile();
                MainActivity.m_mainActivity.setTabMyProfile(m_myProfile);
                return m_myProfile;
            case 1:
                tag1 = new Tag1();
                MainActivity.m_mainActivity.setTab1(tag1);
                return tag1;
            case 2:
                tag2 = new Tag2();
                MainActivity.m_mainActivity.setTab2(tag2);
                return tag2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "";
            case 1:return "";
            case 2:return "";
            default:return null;
        }
    }
}
