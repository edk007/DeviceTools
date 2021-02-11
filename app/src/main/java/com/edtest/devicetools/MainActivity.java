package com.edtest.devicetools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.restriction.RestrictionPolicy;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter viewPagerAdapter;
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "MAIN_ACTIVITY: ";

    //KNOX
    private static final int DEVICE_ADMIN_ADD_RESULT_ENABLE = 1;
    private ComponentName mDeviceAdmin;
    private DevicePolicyManager mDPM;

    //TODO - handle landscape and increase max for singal count?  or show roaming log next to chart?
    // if these are fragments i can move them side by side?

    //TODO - add in the package dumping capabilities from the package tool app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SAMSUNG KNOX
        if (Build.BRAND.equals("samsung")) {
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDeviceAdmin = new ComponentName(MainActivity.this, AdminReceiver.class);
        }

        //LOG TEST
        Log.e(TAG, "LOG_TEST_E");
        Log.d(TAG, "LOG_TEST_D");
        Log.i(TAG, "LOG_TEST_I");
        Log.v(TAG, "LOG_TEST_V");
        Log.w(TAG, "LOG_TEST_w");
        Log.wtf(TAG, "LOG_TEST_WTF");


        TabLayout tablayout = findViewById(R.id.tab_tablayout);
        ViewPager tabViewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabViewPager.setAdapter(viewPagerAdapter);

        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        boolean knox = true;
        boolean da = true;
        if (Build.BRAND.equals("samsung")) {
            knox = checkKnox();
            da = mDPM.isAdminActive(mDeviceAdmin);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || !da || !knox) {
            //if we don't have permission - need to get it granted
            Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean checkKnox() {
        EnterpriseDeviceManager enterpriseDeviceManager = EnterpriseDeviceManager.getInstance(this);
        RestrictionPolicy restrictionPolicy = enterpriseDeviceManager.getRestrictionPolicy();
        boolean isCameraEnabled = restrictionPolicy.isCameraEnabled(false);
        try {
            // this is a fake test - if it throws an exception we do not have DA or we do not have an active license
            boolean result = restrictionPolicy.setCameraState(!isCameraEnabled);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    } //checkKnox

}