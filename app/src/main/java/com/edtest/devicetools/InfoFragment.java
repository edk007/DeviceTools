package com.edtest.devicetools;

import android.app.usage.StorageStatsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.custom.CustomDeviceManager;
import com.samsung.android.knox.custom.SystemManager;
import com.samsung.android.knox.deviceinfo.DeviceInventory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv6.IPv6Address;
import inet.ipaddr.mac.MACAddress;

public class InfoFragment extends Fragment {
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "INFO_FRAGMENT: ";

    private String title;
    private int page;

    RecyclerView recyclerView;
    private InfoRecyclerViewAdapter adapter;

    private WifiManager wifiManager;

    String brand;
    String model;
    String deviceName;
    String rilSerialnumber;
    String carrierid;
    String androidVersion;
    String androidSDK;
    String buildNumber;
    String rilswver;
    String securitypatchdate;
    String knoxSDKversion;
    String knoxSDKAPI;
    String wifiMac;
    String ssid;
    String ipaddress;
    String randomMac;
    String totalStorage;
    String availableStorage;
    String androidID;

    public static InfoFragment newInstance(int page, String title) {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        //start WiFi
        startWiFi();

        //Collect all information here:
        //Brand
        brand = Build.BRAND;
        //Model
        model = Build.MODEL;

        //Device Name
        ContentResolver cr = getActivity().getContentResolver();
        Cursor globalCursor = null;
        globalCursor = cr.query(Settings.Global.CONTENT_URI, null, null, null, null);
        if(globalCursor != null && globalCursor.moveToFirst()){
            deviceName = Settings.Global.getString(getActivity().getContentResolver(),Settings.Global.DEVICE_NAME);
        }
        if (globalCursor != null) {
            globalCursor.close();
        }


        //ril.Serialnumber
        //ro.boot.carrierid
        //ril.sw_ver
        //Build.VERSION.SECURITY_PATCH
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Log.w("CLASS:", c.toString());

            Method get = c.getMethod("get", String.class);

            rilSerialnumber = (String) get.invoke(c, "ril.serialnumber");
            carrierid = (String) get.invoke(c,"ro.boot.carrierid");
            rilswver = (String) get.invoke(c,"ril.sw_ver");
            securitypatchdate = Build.VERSION.SECURITY_PATCH;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Android Version
        androidVersion = Build.VERSION.RELEASE;
        //Android SDK
        androidSDK = String.valueOf(Build.VERSION.SDK_INT);
        //BUILD.DISPLAY
        buildNumber = Build.DISPLAY;
        //Knox SDK Version
        knoxSDKversion = getKnoxVersion();
        //Knox SDK API
        knoxSDKAPI = String.valueOf(EnterpriseDeviceManager.getAPILevel());
        //WiFi MAC
        wifiMac = getKnoxMacAddr();
        //SSID
        //IP Address
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = 0;
            ip = wifiInfo.getIpAddress();
            ipaddress = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
            String tmpssid = wifiInfo.getSSID();
            ssid = tmpssid.replace("\"", "");
        }

        //Random MAC
        randomMac = getMacAddr();

        //Android ID
        androidID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        //Storage
        StorageStatsManager storageStatsManager = (StorageStatsManager) getActivity().getSystemService(Context.STORAGE_STATS_SERVICE);
        StorageManager storageManager = (StorageManager) getActivity().getSystemService(Context.STORAGE_SERVICE);
        try {
            UUID uuid2 = StorageManager.UUID_DEFAULT;
            long fgb = storageStatsManager.getFreeBytes(uuid2) / 1024 / 1024 / 1024;
            availableStorage = String.valueOf(fgb);
            long gb = storageStatsManager.getTotalBytes(uuid2) / 1024 / 1024 / 1024;
            totalStorage = String.valueOf(gb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setup recycler view i'll show all data in here
        recyclerView = getView().findViewById(R.id.infoRecyclerView);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        //Brand
        titles.add("Brand:");
        values.add(brand);
        //Model
        titles.add("Model:");
        values.add(model);
        //Device Name
        titles.add("Device Name:");
        values.add(deviceName);
        //ril.Serialnumber
        titles.add("Serial Number:");
        values.add(rilSerialnumber);
        //Settings.Secure.ANDROID_ID
        titles.add("Android ID:");
        values.add(androidID);
        //ro.boot.carrierid
        titles.add("Carrier ID:");
        values.add(carrierid);
        //Android Version
        titles.add("Android Version:");
        values.add(androidVersion);
        //Android SDK
        titles.add("Android SDK:");
        values.add(androidSDK);
        //BUILD.DISPLAY
        titles.add("Build Number:");
        values.add(buildNumber);
        //ril.sw_ver
        titles.add("SW Version:");
        values.add(rilswver);
        //ro.vendor.build.security_patch
        titles.add("Security Patch Level:");
        values.add(securitypatchdate);
        //Knox SDK Version
        titles.add("Knox SDK:");
        values.add(knoxSDKversion);
        //Knox API Version
        titles.add("Knox API:");
        values.add(knoxSDKAPI);
        //WiFi MAC
        titles.add("WiFi Hardware MAC:");
        values.add(wifiMac);
        //SSID
        titles.add("SSID:");
        values.add(ssid);
        //IP Address
        titles.add("IP Address:");
        values.add(ipaddress);
        //Random MAC
        titles.add("SSID Random MAC:");
        values.add(randomMac);
        //Storage
        titles.add("Storage free/total:");
        values.add(availableStorage + "GB / " + totalStorage + "GB");

        adapter = new InfoRecyclerViewAdapter(getContext(), titles, values);
        recyclerView.setAdapter(adapter);
    } //onViewCreated

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

    private String getKnoxVersion() {
        String kv="Unknown";
        try {
            int ver = EnterpriseDeviceManager.getAPILevel();
            switch (ver) {
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.BASE:
                    return "KNOX BASE";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_1_0_1:
                    return "Knox 1.0.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_1_0_2:
                    return "Knox 1.0.2";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_1_1:
                    return "Knox 1.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_1_2:
                    return "Knox 1.2";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_0:
                    return "Knox 2.0";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_1:
                    return "Knox 2.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_2:
                    return "Knox 2.2";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_3:
                    return "Knox 2.3";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_4:
                    return "Knox 2.4";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_4_1:
                    return "Knox 2.4.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_5:
                    return "Knox 2.5";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_5_1:
                    return "Knox 2.5.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_6:
                    return "Knox 2.6";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_7:
                    return "Knox 2.7";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_7_1:
                    return "Knox 2.7.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_8:
                    return "Knox 2.8";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_9:
                    return "Knox 2.9";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_0:
                    return "Knox 3.0";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_1:
                    return "Knox 3.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_2:
                    return "Knox 3.2";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_2_1:
                    return "Knox 3.2.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_3:
                    return "Knox 3.3";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_4:
                    return "Knox 3.4";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_4_1:
                    return "Knox 3.4.1";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_5:
                    return "Knox 3.5";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_6:
                    return "Knox 3.6";
                case EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_3_7:
                    return "Knox 3.7";
                default:
                    return "Knox Unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kv;
    }

}
