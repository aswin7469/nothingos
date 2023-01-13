package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.ArraySet;
import android.util.Log;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Set;

class WifiTrackerInjector {
    private static final String TAG = "WifiTrackerInjector";
    private static final String WIFI_RES_PACKAGE = "com.android.wifi.resources";
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
        if (mWifiRes == null) {
            try {
                Context createPackageContext = mContext.createPackageContext(WIFI_RES_PACKAGE, 3);
                mWifiResContext = createPackageContext;
                mWifiRes = createPackageContext.getResources();
                mGbkSsidSupported = mWifiRes.getBoolean(getWifiResId("bool", "config_vendor_wifi_gbk_ssid_supported"));
            } catch (Exception e) {
                Log.e(TAG, "exception in createPackageContext: " + e);
                throw new RuntimeException((Throwable) e);
            }
        }
    }

    private int getWifiResId(String str, String str2) {
        Resources resources = mWifiRes;
        if (resources != null) {
            return resources.getIdentifier(str2, str, WIFI_RES_PACKAGE);
        }
        Log.e(TAG, "no WIFI resources, fail to get " + str + BaseIconCache.EMPTY_CLASS_NAME + str2);
        return -1;
    }

    WifiTrackerInjector(Context context) {
        this.mIsDemoMode = NonSdkApiWrapper.isDemoMode(context);
        mContext = context;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        String[] split = context.getString(C3351R.string.wifitrackerlib_no_attribution_annotation_packages).split(NavigationBarInflaterView.BUTTON_SEPARATOR);
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
