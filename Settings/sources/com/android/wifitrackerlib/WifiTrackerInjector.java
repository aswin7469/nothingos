package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.ArraySet;
import android.util.Log;
import java.util.Set;

class WifiTrackerInjector {
    private static Context mContext;
    private static boolean mGbkSsidSupported;
    private static Resources mWifiRes;
    private static Context mWifiResContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final boolean mIsDemoMode;
    private final Set<String> mNoAttributionAnnotationPackages = new ArraySet();
    private final UserManager mUserManager;

    public static boolean isGbkSsidSupported() {
        return mGbkSsidSupported;
    }

    private void initializeWifiRes() {
        String[] stringArray;
        if (mWifiRes == null) {
            try {
                Context createPackageContext = mContext.createPackageContext("com.android.wifi.resources", 3);
                mWifiResContext = createPackageContext;
                mWifiRes = createPackageContext.getResources();
                mGbkSsidSupported = mWifiRes.getBoolean(getWifiResId("bool", "config_vendor_wifi_gbk_ssid_supported"));
                int wifiResId = getWifiResId("array", "config_wifiCharsetsForSsidTranslation");
                if (wifiResId != 0 && (stringArray = mWifiRes.getStringArray(wifiResId)) != null && stringArray.length != 0) {
                    mGbkSsidSupported = false;
                }
            } catch (Exception e) {
                Log.e("WifiTrackerInjector", "exception in createPackageContext: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    private int getWifiResId(String str, String str2) {
        Resources resources = mWifiRes;
        if (resources != null) {
            return resources.getIdentifier(str2, str, "com.android.wifi.resources");
        }
        Log.e("WifiTrackerInjector", "no WIFI resources, fail to get " + str + "." + str2);
        return -1;
    }

    WifiTrackerInjector(Context context) {
        this.mIsDemoMode = NonSdkApiWrapper.isDemoMode(context);
        mContext = context;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        String[] split = context.getString(R$string.wifitrackerlib_no_attribution_annotation_packages).split(",");
        for (String add : split) {
            this.mNoAttributionAnnotationPackages.add(add);
        }
        initializeWifiRes();
    }

    /* access modifiers changed from: package-private */
    public boolean isDemoMode() {
        return this.mIsDemoMode;
    }

    public UserManager getUserManager() {
        return this.mUserManager;
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return this.mDevicePolicyManager;
    }

    /* access modifiers changed from: package-private */
    public Set<String> getNoAttributionAnnotationPackages() {
        return this.mNoAttributionAnnotationPackages;
    }
}
