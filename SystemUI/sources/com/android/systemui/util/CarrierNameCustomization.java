package com.android.systemui.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import java.util.HashMap;
import javax.inject.Inject;

@SysUISingleton
public class CarrierNameCustomization {
    private final boolean DEBUG = Log.isLoggable("CarrierNameCustomization", 3);
    private final String TAG = "CarrierNameCustomization";
    private HashMap<String, String> mCarrierMap = new HashMap<>();
    private String mConnector;
    private boolean mRoamingCustomizationCarrierNameEnabled;
    private TelephonyManager mTelephonyManager;

    @Inject
    public CarrierNameCustomization(Context context) {
        this.mRoamingCustomizationCarrierNameEnabled = context.getResources().getBoolean(C1894R.bool.config_show_roaming_customization_carrier_name);
        this.mConnector = context.getResources().getString(C1894R.string.connector);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (this.mRoamingCustomizationCarrierNameEnabled) {
            loadCarrierMap(context);
        }
    }

    public boolean isRoamingCustomizationEnabled() {
        return this.mRoamingCustomizationCarrierNameEnabled;
    }

    public boolean isRoaming(int i) {
        String orDefault = this.mCarrierMap.getOrDefault(this.mTelephonyManager.getSimOperator(i), "");
        String orDefault2 = this.mCarrierMap.getOrDefault(this.mTelephonyManager.getNetworkOperator(i), "");
        if (this.DEBUG) {
            Log.d("CarrierNameCustomization", "isRoaming subId=" + i + " simOperator=" + this.mTelephonyManager.getSimOperator(i) + " networkOperator=" + this.mTelephonyManager.getNetworkOperator(i));
        }
        return !TextUtils.isEmpty(orDefault) && !TextUtils.isEmpty(orDefault2) && !orDefault.equals(orDefault2);
    }

    public String getRoamingCarrierName(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mCarrierMap.getOrDefault(this.mTelephonyManager.getSimOperator(i), "")).append(this.mConnector).append(this.mCarrierMap.getOrDefault(this.mTelephonyManager.getNetworkOperator(i), ""));
        return sb.toString();
    }

    private void loadCarrierMap(Context context) {
        for (String str : context.getResources().getStringArray(C1894R.array.customization_carrier_name_list)) {
            String[] split = str.trim().split(":");
            if (split.length != 2) {
                Log.e("CarrierNameCustomization", "invalid key value config " + str);
            } else {
                this.mCarrierMap.put(split[0], split[1]);
            }
        }
    }
}
