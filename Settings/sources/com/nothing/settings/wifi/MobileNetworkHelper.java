package com.nothing.settings.wifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.List;

public class MobileNetworkHelper {
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final PackageManager mPackageManager;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyManager mTelephonyManager;
    private final UserManager mUserManager;
    private final WifiManager mWifiManager;

    public boolean isSimCardPresent(int i) {
        return (i == 0 || 1 == i) ? false : true;
    }

    public MobileNetworkHelper(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mPackageManager = applicationContext.getPackageManager();
        this.mUserManager = (UserManager) applicationContext.getSystemService(UserManager.class);
        this.mTelephonyManager = (TelephonyManager) applicationContext.getSystemService(TelephonyManager.class);
        this.mWifiManager = (WifiManager) applicationContext.getSystemService(WifiManager.class);
        this.mConnectivityManager = (ConnectivityManager) applicationContext.getSystemService(ConnectivityManager.class);
        this.mSubscriptionManager = SubscriptionManager.from(applicationContext);
    }

    public void setWiFiEnabled(boolean z) {
        if (!this.mPackageManager.hasSystemFeature("android.hardware.wifi")) {
            Log.w("MobileNetworkHelper", "setWiFiEnabled failed. no feature android.hardware.wifi.");
        } else if (this.mWifiManager.isWifiEnabled() != z) {
            this.mWifiManager.setWifiEnabled(z);
        }
    }

    public void setDeviceProvisioningMobileDataEnabled(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "device_provisioning_mobile_data", z ? 1 : 0);
    }

    @SuppressLint({"MissingPermission"})
    public String getRequiredNetwork() {
        if (this.mUserManager.isPrimaryUser()) {
            return SystemProperties.get("ro.setupwizard.require_network", "");
        }
        return SystemProperties.get("ro.setupwizard.user_req_network", "");
    }

    public boolean isWiFiRequired() {
        return "wifi".equals(getRequiredNetwork());
    }

    @SuppressLint({"MissingPermission"})
    public boolean isSimMissing() {
        List<SubscriptionInfo> allSubscriptionInfoList = this.mSubscriptionManager.getAllSubscriptionInfoList();
        if (allSubscriptionInfoList != null) {
            for (SubscriptionInfo subscriptionInfo : allSubscriptionInfoList) {
                int simState = this.mTelephonyManager.getSimState(subscriptionInfo.getSimSlotIndex());
                Log.d("MobileNetworkHelper", "isSimMissing: subInfo.getSimSlotIndex() = " + subscriptionInfo.getSimSlotIndex() + ", simState = " + simState);
                if (simState != 1 && simState != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMobileNetworkSupport() {
        return this.mPackageManager.hasSystemFeature("android.hardware.telephony");
    }

    public boolean isNetworkRoaming() {
        return this.mTelephonyManager.isNetworkRoaming();
    }

    @SuppressLint({"MissingPermission"})
    public boolean isWiFiConnectedAndUsable() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && !this.mConnectivityManager.isActiveNetworkMetered() && networkCapabilities != null && networkCapabilities.hasCapability(16);
    }
}
