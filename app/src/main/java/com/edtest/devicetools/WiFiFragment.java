package com.edtest.devicetools;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.custom.CustomDeviceManager;
import com.samsung.android.knox.custom.SystemManager;
import com.samsung.android.knox.restriction.RestrictionPolicy;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv6.IPv6Address;
import inet.ipaddr.mac.MACAddress;

public class WiFiFragment extends Fragment {
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "WIFI_FRAGMENT: ";

    private String title;
    private int page;

    private MyRecyclerViewAdapter adapter;
    private RoamListViewAdapter roamAdapter;
    private WifiManager wifiManager;

    TextView ssidTextView;
    TextView ipTextView;
    TextView macTextView;
    TextView randMacTextView;
    TextView apMacTextView;
    TextView signalTextView;
    TextView bandTextView;
    TextView roamTextView;

    ScheduledFuture randomTask;
    ScheduledThreadPoolExecutor exec;
    Runnable periodicTask;
    int scanInterval = 2; //seconds

    ArrayList<String> roamApMacs;
    ArrayList<String> roamSignals;
    ArrayList<String> roamBands;
    ArrayList<String> roamOldNew;

    ArrayList<String> signalStrengths;
    ArrayList<Integer> signalColors;
    ArrayList<String> signalCount;

    Context c;

    //TODO - MISSING ITEMS
    // random mac is not showing - only hardware mac


    public static WiFiFragment newInstance(int page, String title) {
        WiFiFragment wiFiFragment = new WiFiFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        wiFiFragment.setArguments(args);
        return wiFiFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        c = getContext();

        int maxSize = 10;

        signalStrengths = new ArrayList<>();
        signalColors = new ArrayList<>();
        signalCount = new ArrayList<>();

        for (int i=1; i < maxSize+1 ; i++)
        {
            signalCount.add(Integer.toString(i));
            signalColors.add(1);
            signalStrengths.add("00");
        }

        roamApMacs = new ArrayList<>();
        roamSignals = new ArrayList<>();
        roamBands = new ArrayList<>();
        roamOldNew = new ArrayList<>();

        roamApMacs.add("AP MAC");
        roamBands.add("BAND");
        roamSignals.add("SIGNAL");
        roamOldNew.add("TO/FR");

        //start WiFi
        startWiFi();

        //heartbeat
        final int[] heartbeat = {0};

        //roam counting
        final int[] roamCount = {0};
        final String[] previousApMac = {""};
        final String[] previousSignal = {""};
        final String[] previousBand = {""};

        periodicTask = new Runnable(){
            @Override
            public void run() {
                try {
                    Log.w("TASK:",Integer.toString(heartbeat[0]));
                    heartbeat[0] = heartbeat[0] + 2;
                    //TODO - handle when switching APs - somehow show this transition in the chart?
                    // make a log at the bottom that shows the last data before the roam and the new AP roamed to

                    //TODO - enable logging of current AP information captured below?

                    //TODO - is this information only changing when the wifi receiver gets new info?
                    // - it may be useless to scan as frequently in this task?? verify with walk about
                    int ip = 0;
                    String myIP = "0.0.0.0";
                    String apMAC = "AA:AA:AA:AA:AA:AA";
                    String ssid = "SEARCHING";
                    int freq = 0;
                    int linkSpeed = 0;
                    int rssi = 0;
                    int band = 0;
                    if (wifiManager.isWifiEnabled()) {
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        ip = wifiInfo.getIpAddress();
                        myIP = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
                        apMAC = wifiInfo.getBSSID();
                        String tmpssid = wifiInfo.getSSID();
                        ssid = tmpssid.replace("\"", "");
                        freq = wifiInfo.getFrequency();
                        linkSpeed = wifiInfo.getLinkSpeed();
                        rssi = wifiInfo.getRssi();
                        band = GetBand.freqToBand(freq);
                    }

                    String randMacAddr = "00:00:00:00:00:00";
                    String knoxMacAddr = "00:00:00:00:00:00";
                    //get WIFI RANDOM MAC address
                    randMacAddr = getMacAddr();
                    if (Build.BRAND.equals("samsung")) {
                        knoxMacAddr = getKnoxMacAddr();
                    }

                    signalStrengths.add(0,Integer.toString(rssi));

                    int sc = 1;
                    if (rssi > 80) {
                        sc = 3;
                    } else if ( rssi > 70) {
                        sc = 2;
                    }
                    signalColors.add(0, sc);

                    //only want 5 in here
                    //Log.w("TASK:", "SIGNAL_STRENGTH_SIZE:" + signalStrengths.size());
                    if (signalStrengths.size() > maxSize) {
                        signalStrengths.remove(maxSize);
                    }
                    //Log.w("TASK:", "SIGNAL_COLORS_SIZE:" + signalColors.size());
                    if (signalColors.size() > maxSize) {
                        signalColors.remove(maxSize);
                    }

                    String finalSsid = ssid;
                    String finalMyIP = myIP;
                    String finalMAC = randMacAddr;
                    String finalKnoxMacAddr = knoxMacAddr;
                    String finalApMAC = apMAC;
                    String finalRssi = Integer.toString(rssi);
                    String bandString = "";
                    if (freq > 5000) {
                        bandString = "(5Ghz)";
                    } else {
                        bandString = "(2.4Ghz)";
                    }
                    String finalBandString = Integer.toString(band) + " " + bandString;

                    Log.w(TAG, TAG2 + "PREVIOUS_MAC:" + previousApMac[0]);
                    Log.w(TAG, TAG2 + "CURRENT_MAC:" + apMAC);
                    if (previousApMac[0].equals("")) {
                        //firt time thru
                        previousApMac[0] = apMAC;
                    }
                    if (!previousApMac[0].equals(apMAC)) {
                        Log.w(TAG, TAG2 + "ROAMED");
                        //new ap - roamed
                        //add previous first to list view
                        roamApMacs.add(previousApMac[0]);
                        roamSignals.add(previousSignal[0]);
                        roamBands.add(previousBand[0]);
                        //now add new
                        roamApMacs.add(finalApMAC);
                        roamSignals.add(finalRssi);
                        roamBands.add(finalBandString);
                        //update count tracking
                        roamCount[0]++;
                        roamOldNew.add(roamCount[0] + "-FROM:");
                        roamOldNew.add(roamCount[0] + "-TO  :");
                    }
                    previousApMac[0] = apMAC;
                    previousBand[0] = finalBandString;
                    previousSignal[0] = finalRssi;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            roamAdapter.notifyDataSetChanged();
                            ssidTextView.setText(finalSsid);
                            ipTextView.setText(finalMyIP);
                            macTextView.setText(finalKnoxMacAddr);
                            randMacTextView.setText(finalMAC);
                            apMacTextView.setText(finalApMAC);
                            signalTextView.setText(finalRssi);
                            bandTextView.setText(finalBandString);
                            roamTextView.setText(Integer.toString(roamCount[0]));
                        }
                    });

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        exec = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_wifi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setup display elements
        ssidTextView = getView().findViewById(R.id.ssidTextView);
        ipTextView = getView().findViewById(R.id.ipTextView);
        macTextView = getView().findViewById(R.id.macTextView);
        randMacTextView = getView().findViewById(R.id.randMacTextView);
        apMacTextView = getView().findViewById(R.id.apMacTextView);
        signalTextView = getView().findViewById(R.id.signalTextView);
        bandTextView = getView().findViewById(R.id.bandTextView);
        roamTextView = getView().findViewById(R.id.roamTextView);

        // set up the RecyclerView for bar graph
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView1);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        adapter = new MyRecyclerViewAdapter(c, signalColors, signalStrengths, signalCount);
        recyclerView.setAdapter(adapter);

        //setup roaming list
        RecyclerView roamListView = getView().findViewById(R.id.roamLogRecyclerView);
        roamListView.setLayoutManager(new LinearLayoutManager(getContext()));

        roamAdapter = new RoamListViewAdapter(c, roamApMacs, roamSignals, roamBands, roamOldNew);
        roamListView.setAdapter(roamAdapter);
        roamAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        randomTask.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        randomTask = exec.scheduleAtFixedRate(periodicTask,0,scanInterval, TimeUnit.SECONDS);
    }

    private void startWiFi() {
        //setup wifi manager
        wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getContext(), "TURN ON WIFI", Toast.LENGTH_LONG).show();
        }
    }

    //this will return a mac based on the IPv6 address of the WLAN adapter
    private String getMacAddr() {
        Log.w("STARTING NETWORK INTERFACES","");
        String macReturn = "BLANK";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

                //have WLAN now
                for (Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress ia = enumIpAddr.nextElement();
                    String iaToString = ia.toString();
                    String iaHostAddress = ia.getHostAddress();
                    byte[] getAddr = ia.getAddress();
                    Log.w("INET_ADDRESS_TOSTRING: ",iaToString);
                    //Log.w("INET_CANNONICAL_HOST:",ia.getCanonicalHostName());
                    Log.w("INET_HOST_ADDRESS:",iaHostAddress);
                    //Log.w("INET_HOST_NAME:", ia.getHostName());
                    Log.w("INET_ADDRESS:", String.valueOf(getAddr));

                    if (iaHostAddress.contains(":")) {
                        String subString = iaHostAddress.substring(0,iaHostAddress.indexOf("%"));
                        Log.w("INET_HOST_ADDRESS_SHORT",subString);
                        IPv6Address iPv6Address = new IPAddressString(subString).getAddress().toIPv6();
                        MACAddress macAddress = iPv6Address.toEUI(false);
                        macReturn = macAddress.toString();
                        Log.w("INET_HOST_TO_MAC:",macReturn);
                    }
                }

                return macReturn;
            }
        } catch (Exception ex) {
            //handle exception
            ex.printStackTrace();
        }
        return "ERROR";
    }//getMacAddr

    private String getKnoxMacAddr() {
        //MAC ADDRESS - REQUIRES DEVICE ADMIN AND LICENSE
        try {
            CustomDeviceManager cdm = CustomDeviceManager.getInstance();
            SystemManager kcsm = cdm.getSystemManager();
            return kcsm.getMacAddress();
        }
        catch (Exception e) {
            Log.w(TAG, TAG2 + "KNOX_EXCEPTION:" + e.toString());
            return null;
        }
    }//getKnoxMacAddr

}
