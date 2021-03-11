package com.edtest.devicetools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyDisplayInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class CustomPhoneStateListener extends PhoneStateListener {
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "CUSTOM_PHONE_STATE_LISTENER: ";

    Context context;

    CustomPhoneStateListener(Context c) {
        this.context = c;
    }

    @Override
    public void onDisplayInfoChanged(@NonNull TelephonyDisplayInfo telephonyDisplayInfo) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        } else {
            super.onDisplayInfoChanged(telephonyDisplayInfo);
            if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA) {
                Log.w(TAG, TAG2 + "5G_NSA");
            }
            if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE) {
                Log.w(TAG, TAG2 + "5G_NSA_MMWAVE");
            }
        }

    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.w(TAG, TAG2 + "SIGNAL_STRENGTHS_CHANGED:" + signalStrength.toString());
    }
}