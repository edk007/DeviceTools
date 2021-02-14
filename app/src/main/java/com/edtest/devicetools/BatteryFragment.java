package com.edtest.devicetools;

import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.BATTERY_SERVICE;

public class BatteryFragment extends Fragment {
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "BATTERY_FRAGMENT: ";

    private String title;
    private int page;

    RecyclerView recyclerView;
    private InfoRecyclerViewAdapter adapter;

    String batteryLevel;
    String timeToRecharge;
    boolean isCharging;

    public static BatteryFragment newInstance(int page, String title) {
        BatteryFragment batteryFragment = new BatteryFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        batteryFragment.setArguments(args);
        return batteryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        BatteryManager bm = (BatteryManager) getActivity().getSystemService(BATTERY_SERVICE);
        isCharging = bm.isCharging();
        batteryLevel = String.valueOf(bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
        timeToRecharge = bm.computeChargeTimeRemaining() / 1000 + " (seconds)";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_battery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setup recycler view i'll show all data in here
        recyclerView = getView().findViewById(R.id.batteryRecyclerView);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        //Charge Level
        titles.add("Charge Level:");
        values.add(batteryLevel + "%");
        //Time To Charge
        titles.add("Time To Charge:");
        values.add(timeToRecharge);
        //Is Charging?
        titles.add("Charging Status:");
        String chargingStatus = "Not Charging";
        if (isCharging) { chargingStatus = "Charging"; }
        values.add(chargingStatus);

        //Battery Serial
        titles.add("Battery Serial:");
        values.add("UNKNOWN");
        //Battery Manufacture Date
        titles.add("Manufacture Date:");
        values.add("UNKNOWN");
        //Battery Charge Cycles
        titles.add("Charge Cycle Count:");
        values.add("UNKNOWN");
        //Battery Capacity Remaining
        titles.add("Capacity Remaining:");
        values.add("UNKNOWN");

        adapter = new InfoRecyclerViewAdapter(getContext(), titles, values);
        recyclerView.setAdapter(adapter);

    }

}
