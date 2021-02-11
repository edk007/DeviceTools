package com.edtest.devicetools;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return WiFiFragment.newInstance(0, "WIFI");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return BatteryFragment.newInstance(1, "BATTERY");
            case 2: // Fragment # 1 - This will show SecondFragment
                return InfoFragment.newInstance(2, "INFO");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return "WIFI";
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return "BATTERY";
            case 2: // Fragment # 1 - This will show SecondFragment
                return "INFO";
            default:
                return null;
        }
    }
}
