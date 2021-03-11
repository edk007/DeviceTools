package com.edtest.devicetools;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
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

import com.samsung.android.knox.custom.CustomDeviceManager;
import com.samsung.android.knox.custom.SystemManager;

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

public class CellFragment extends Fragment {
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "CELL_FRAGMENT: ";

    private String title;
    private int page;

    TelephonyManager telephonyManager;

    TextView cellNetworkLabelTextView;
    RecyclerView cellLogRecyclerView, cellNetworkInfoView, cellSignalStrengthView;
    private InfoRecyclerViewAdapter cellLogAdapter;
    private CellSignalViewAdapter cellSignalViewAdapter;
    private CellInfoViewAdapter cellInfoViewAdapter;

    Context c;

    ScheduledFuture cellInfoTask;
    ScheduledThreadPoolExecutor exec;
    Runnable cellInfoRunnable;
    int scanInterval = 2; //seconds

    //TODO manage max size based on width of display and portrait/landscape
    int maxSize = 10;

    ArrayList<String> signalStrengths;
    ArrayList<Integer> signalColors;
    ArrayList<String> signalCount;
    ArrayList<String> cellInfoValues;

    //signal range variables
    int bar1 = -110;
    int bar2 = -100;
    int bar3 = -90;
    int bar4 = -80;

    public static CellFragment newInstance(int page, String title) {
        CellFragment cellFragment = new CellFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        cellFragment.setArguments(args);
        return cellFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        c = getContext();

        signalStrengths = new ArrayList<>();
        signalColors = new ArrayList<>();
        signalCount = new ArrayList<>();

        cellInfoValues = new ArrayList<>();
        cellInfoValues.add("Scanning...");

        for (int i=1; i < maxSize+1 ; i++)
        {
            signalCount.add(Integer.toString(i));
            signalColors.add(1);
            signalStrengths.add("-100");
        }

        telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new CustomPhoneStateListener(c),PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED);
        telephonyManager.listen(new CustomPhoneStateListener(c),PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        cellInfoRunnable = () -> {
            try {
                //generic variables for cell information here
                int cellSignalStrength;
                String carrierBandString;
                String cellInfoRow1, cellInfoRow2, cellInfoRow3, cellInfoRow4, cellInfoRow5, cellInfoRow6;

                //TEMP TESTING VALUES
                cellSignalStrength = -120;
                carrierBandString = "LTE 1900";
                cellInfoRow1 = "ROW 1 HERE";
                cellInfoRow2 = "ROW 2 HERE";
                cellInfoRow3 = "ROW 3 HERE";
                cellInfoRow4 = "ROW 4 HERE";
                cellInfoRow5 = "ROW 5 HERE";
                cellInfoValues.clear();
                cellInfoValues.add(cellInfoRow1);
                cellInfoValues.add(cellInfoRow2);
                cellInfoValues.add(cellInfoRow3);
                cellInfoValues.add(cellInfoRow4);
                cellInfoValues.add(cellInfoRow5);

                //TODO ADD 5G
                //TODO ADD 3G
                try {
                    List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
                    Log.w(TAG, TAG2 + "CELL_INFO_SIZE:" + cellInfos.size());
                    if (isNRConnected(telephonyManager)) {
                        Log.w(TAG, TAG2 + "5G_CONNECTED");
                    }
                    for (int ci = 0; ci < cellInfos.size(); ci++) {
                        Log.w(TAG, TAG2 + "CELL_INFO_" + "LIST_" + ci + " " + cellInfos.get(ci).toString());
                        try {
                            if (cellInfos.get(ci) instanceof CellInfoWcdma) {
                                //WCDMA - 3G
                                Log.w(TAG, TAG2 + "WCDMA");
                            }
                            if (cellInfos.get(ci) instanceof CellInfoLte) {
                                Log.w(TAG, TAG2 + "LTE");

                                CellInfoLte ciL = (CellInfoLte) cellInfos.get(ci);
                                CellIdentityLte cidL = (CellIdentityLte) ciL.getCellIdentity();
                                CellSignalStrengthLte cssL = ciL.getCellSignalStrength();

                                if (ciL.isRegistered()) {
                                    //registered LTE connection
                                    int RSRP, RSRQ, ASU, TA, BAND, PCI, TAC, DLEARFCN, ULEARFCN;
                                    double dlFreq, ulFreq, TAmi, TAme;
                                    String providerString, frequncyString, GCI, MNC, MCC;

                                    RSRP = cssL.getRsrp();
                                    RSRQ = cssL.getRsrq();
                                    ASU = cssL.getAsuLevel();
                                    TAC = cidL.getTac();
                                    TA = cssL.getTimingAdvance();
                                    double TA_calc = TA * 78.07d;
                                    //METERS
                                    double TAmeters = (double) ((int) Math.round(TA_calc));
                                    TAme = TAmeters;
                                    //MILES
                                    double round2 = (double) Math.round(TA_calc * 6.213712E-4d * 100.0d);
                                    TAmi = round2 / 100.0d;

                                    MCC = cidL.getMccString();
                                    MNC = cidL.getMncString();
                                    GCI = String.format("%08X", cidL.getCi());
                                    PCI = cidL.getPci();
                                    DLEARFCN = cidL.getEarfcn();
                                    ULEARFCN = LTECarrierFrequency.getULEARFCN(DLEARFCN);
                                    BAND = LTECarrierFrequency.getBand(DLEARFCN);
                                    dlFreq = LTECarrierFrequency.getDLFREQ(DLEARFCN);
                                    ulFreq = LTECarrierFrequency.getULFREQ(DLEARFCN);
                                    providerString = String.valueOf(cidL.getOperatorAlphaLong());
                                    frequncyString = LTECarrierFrequency.getBandName(BAND);

                                    //build values for UI
                                    cellSignalStrength = RSRP;
                                    carrierBandString = providerString + " LTE Band:" + frequncyString + " #" + BAND + " (" + MCC + MNC + ")";
                                    cellInfoRow1 = "RSRP: " + RSRP + " dBm    " + ASU + " asu";
                                    cellInfoRow2 = "RSRQ: " + RSRQ + " dB   TA: " + TA + "  " + TAmi + " mi";
                                    cellInfoRow3 = "GCI: " + GCI + "    PCI: " + PCI + "    TAC: " + TAC;
                                    cellInfoRow4 = "UL EARFCN: " + ULEARFCN + "   " + ulFreq + " MHz";
                                    cellInfoRow5 = "DL EARFCN: " + DLEARFCN + "   " + dlFreq + " MHz";
                                    cellInfoValues.clear();
                                    cellInfoValues.add(cellInfoRow1);
                                    cellInfoValues.add(cellInfoRow2);
                                    cellInfoValues.add(cellInfoRow3);
                                    cellInfoValues.add(cellInfoRow4);
                                    cellInfoValues.add(cellInfoRow5);

                                } else {
                                    //this might be the 5G connection in the list??
                                    //Log.w(TAG, TAG2 + "CELL_INFO_LTE" + ciL.toString());
                                    //Log.w(TAG, TAG2 + "CELL_INFO_LTE" + cidL.toString());
                                    //Log.w(TAG, TAG2 + "CELL_INFO_LTE" + cssL.toString());

                                }
                            }//LTE
                            if (cellInfos.get(ci) instanceof CellInfoNr) {
                                Log.w(TAG, TAG2 + "5G");
                                int RSRP, RSRQ;
                                String providerString, frequncyString;

                                CellInfoNr ciL = (CellInfoNr) cellInfos.get(ci);
                                CellIdentityNr cidL = (CellIdentityNr) ciL.getCellIdentity();
                                CellSignalStrengthNr cssL = (CellSignalStrengthNr) ciL.getCellSignalStrength();
                                Log.w("CELL_INFO_5G", ciL.toString());
                                Log.w("CELL_INFO_5G", cidL.toString());
                                Log.w("CELL_INFO_5G", cssL.toString());
                                Log.w("CELL_INFO_5G_MCC", cidL.getMccString());
                                Log.w("CELL_INFO_5G_MNC", cidL.getMncString());
                                Log.w("CELL_INFO_5G_PCI", String.valueOf(cidL.getPci()));
                                Log.w("CELL_INFO_5G_TAC", String.valueOf(cidL.getTac()));
                                Log.w("CELL_INFO_5G_NCI", String.valueOf(cidL.getNci()));
                                Log.w("CELL_INFO_5G_NRARFCN", String.valueOf(cidL.getNrarfcn()));
                                Log.w("CELL_INFO_5G_OPERATOR", String.valueOf(cidL.getOperatorAlphaLong()));
                                Log.w("CELL_INFO_5G_OPERATOR", String.valueOf(cidL.getOperatorAlphaShort()));
                                Log.w("CELL_INFO_5G_ASU", String.valueOf(cssL.getAsuLevel()));
                                Log.w("CELL_INFO_5G_CSI_RSRP", String.valueOf(cssL.getCsiRsrp()));
                                Log.w("CELL_INFO_5G_CSI_RSRQ", String.valueOf(cssL.getCsiRsrq()));
                                Log.w("CELL_INFO_5G_CSI_SINR", String.valueOf(cssL.getCsiSinr()));
                                Log.w("CELL_INFO_5G_DBM", String.valueOf(cssL.getDbm()));
                                Log.w("CELL_INFO_5G_LEVEL", String.valueOf(cssL.getLevel()));
                                Log.w("CELL_INFO_5G_SS_RSRP", String.valueOf(cssL.getSsRsrp()));
                                Log.w("CELL_INFO_5G_SS_RSRQ", String.valueOf(cssL.getSsRsrq()));
                                Log.w("CELL_INFO_5G_SS_SINR", String.valueOf(cssL.getSsSinr()));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }//for loop on network information
                } catch (SecurityException se) {
                    se.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }//try for network inforamtion

                signalStrengths.add(0,Integer.toString(cellSignalStrength));

                int sc = 1;
                if (cellSignalStrength < bar1) { sc=1; }  //under bar1 threshold = 1 bar
                else if (isBetween(cellSignalStrength,bar1,bar2)) { sc=2; }  //between 1 and 2 is 2 bars
                else if (isBetween(cellSignalStrength, bar2, bar3)) { sc=3; }  //between 2 and 3 is 3 bars
                else if (isBetween(cellSignalStrength,bar3,bar4)) { sc=4; }  //between 3 and 4 is 4 bars
                else if (cellSignalStrength >=0) {sc = 1; }  //not a good signal - set to red
                else { sc=5; } //must be above 4 bars so set to 5 bars

                signalColors.add(0, sc);

                //only want MAX in here
                //Log.w(TAG,TAG2 + "SIGNAL_STRENGTH_SIZE:" + signalStrengths.size());
                if (signalStrengths.size() > maxSize) {
                    signalStrengths.remove(maxSize);
                }
                //Log.w(TAG,TAG2 + "SIGNAL_COLORS_SIZE:" + signalColors.size());
                if (signalColors.size() > maxSize) {
                    signalColors.remove(maxSize);
                }

                //TODO setup previous cell info variables to catch movement between one carrier and another to log into the roam dialog

                String finalCarrierBandString = carrierBandString;
                getActivity().runOnUiThread(() -> {
                    cellSignalViewAdapter.notifyDataSetChanged();
                    cellLogAdapter.notifyDataSetChanged();
                    cellInfoViewAdapter.notifyDataSetChanged();
                    //TODO update other views with cell data
                    cellNetworkLabelTextView.setText(finalCarrierBandString);
                });

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };

        exec = new ScheduledThreadPoolExecutor(1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_cell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setup display elements
        cellNetworkLabelTextView = getView().findViewById(R.id.cellNetworkLabelTextView);

        //setup recycler views
        cellLogRecyclerView = getView().findViewById(R.id.cellLogRecyclerView);
        cellNetworkInfoView = getView().findViewById(R.id.cellNetworkInfoView);
        cellSignalStrengthView = getView().findViewById(R.id.cellSignalStrengthView);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cellLogRecyclerView.setLayoutManager(verticalLayoutManager);

        LinearLayoutManager verticalLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cellNetworkInfoView.setLayoutManager(verticalLayoutManager2);

        cellSignalStrengthView = getView().findViewById(R.id.cellSignalStrengthView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cellSignalStrengthView.setLayoutManager(horizontalLayoutManager);

        cellSignalViewAdapter = new CellSignalViewAdapter(c, signalColors, signalStrengths, signalCount);
        cellSignalStrengthView.setAdapter(cellSignalViewAdapter);

        cellInfoViewAdapter = new CellInfoViewAdapter(c, cellInfoValues);
        cellNetworkInfoView.setAdapter(cellInfoViewAdapter);

        //TEMP DATA FOR LOGGER
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        titles.add("Cell:");
        values.add("cell");
        cellLogAdapter = new InfoRecyclerViewAdapter(getContext(), titles, values);
        cellLogRecyclerView.setAdapter(cellLogAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        cellInfoTask.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        cellInfoTask = exec.scheduleAtFixedRate(cellInfoRunnable,0,scanInterval, TimeUnit.SECONDS);
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public boolean isNRConnected(TelephonyManager telephonyManager) {
        try {
            Object obj = Class.forName(telephonyManager.getClass().getName())
                    .getDeclaredMethod("getServiceState", new Class[0]).invoke(telephonyManager, new Object[0]);
            // try extracting from string
            String serviceState = obj.toString();
            boolean is5gActive = serviceState.contains("nrState=CONNECTED") ||
                    serviceState.contains("nsaState=5") ||
                    (serviceState.contains("EnDc=true") &&
                            serviceState.contains("5G Allocated=true"));
            if (is5gActive) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}


