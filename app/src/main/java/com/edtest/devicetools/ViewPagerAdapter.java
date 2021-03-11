package com.edtest.devicetools;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WiFiFragment.newInstance(0, "WIFI");
            case 1:
                return CellFragment.newInstance(1, "CELL");
            case 2:
                return BatteryFragment.newInstance(2, "BATTERY");
            case 3:
                return InfoFragment.newInstance(3, "INFO");
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
            case 0:
                return "WIFI";
            case 1:
                return "CELL";
            case 2:
                return "BATTERY";
            case 3:
                return "INFO";
            default:
                return null;
        }
    }
}
