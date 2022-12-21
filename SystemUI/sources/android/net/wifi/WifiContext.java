package android.net.wifi;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.wifi.util.Environment;
import android.util.Log;
import java.util.List;
import java.util.stream.Collectors;

public class WifiContext extends ContextWrapper {
    private static final String ACTION_RESOURCES_APK = "com.android.server.wifi.intent.action.SERVICE_WIFI_RESOURCES_APK";
    private static final String ACTION_WIFI_DIALOG_APK = "com.android.server.wifi.intent.action.WIFI_DIALOG_APK";
    private static final String SERVICE_WIFI_PACKAGE_NAME = "android";
    private static final String TAG = "WifiContext";
    private AssetManager mWifiAssetsFromApk;
    private String mWifiDialogApkPkgName;
    private String mWifiOverlayApkPkgName;
    private Resources mWifiResourcesFromApk;
    private Resources.Theme mWifiThemeFromApk;

    public String getServiceWifiPackageName() {
        return "android";
    }

    public WifiContext(Context context) {
        super(context);
    }

    public String getWifiOverlayApkPkgName() {
        String str = this.mWifiOverlayApkPkgName;
        if (str != null) {
            return str;
        }
        String apkPkgNameForAction = getApkPkgNameForAction(ACTION_RESOURCES_APK);
        this.mWifiOverlayApkPkgName = apkPkgNameForAction;
        if (apkPkgNameForAction == null) {
            Log.e(TAG, "Attempted to fetch resources before Wifi Resources APK is loaded!", new IllegalStateException());
            return null;
        }
        Log.i(TAG, "Found Wifi Resources APK at: " + this.mWifiOverlayApkPkgName);
        return this.mWifiOverlayApkPkgName;
    }

    public String getWifiDialogApkPkgName() {
        String str = this.mWifiDialogApkPkgName;
        if (str != null) {
            return str;
        }
        String apkPkgNameForAction = getApkPkgNameForAction(ACTION_WIFI_DIALOG_APK);
        this.mWifiDialogApkPkgName = apkPkgNameForAction;
        if (apkPkgNameForAction == null) {
            Log.e(TAG, "Attempted to fetch WifiDialog apk before it is loaded!", new IllegalStateException());
            return null;
        }
        Log.i(TAG, "Found Wifi Dialog APK at: " + this.mWifiDialogApkPkgName);
        return this.mWifiDialogApkPkgName;
    }

    private String getApkPkgNameForAction(String str) {
        List<ResolveInfo> queryIntentActivities = getPackageManager().queryIntentActivities(new Intent(str), 1048576);
        Log.i(TAG, "Got resolveInfos for " + str + ": " + queryIntentActivities);
        queryIntentActivities.removeIf(new WifiContext$$ExternalSyntheticLambda0());
        if (queryIntentActivities.isEmpty()) {
            return null;
        }
        if (queryIntentActivities.size() > 1) {
            Log.w(TAG, "Found > 1 APK that can resolve " + str + ": " + ((String) queryIntentActivities.stream().map(new WifiContext$$ExternalSyntheticLambda1()).collect(Collectors.joining(", "))));
        }
        return queryIntentActivities.get(0).activityInfo.applicationInfo.packageName;
    }

    static /* synthetic */ boolean lambda$getApkPkgNameForAction$0(ResolveInfo resolveInfo) {
        return !Environment.isAppInWifiApex(resolveInfo.activityInfo.applicationInfo);
    }

    private Context getResourcesApkContext() {
        try {
            String wifiOverlayApkPkgName = getWifiOverlayApkPkgName();
            if (wifiOverlayApkPkgName != null) {
                return createPackageContext(wifiOverlayApkPkgName, 0);
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(TAG, "Failed to load resources", e);
            return null;
        }
    }

    public AssetManager getAssets() {
        Context resourcesApkContext;
        if (this.mWifiAssetsFromApk == null && (resourcesApkContext = getResourcesApkContext()) != null) {
            this.mWifiAssetsFromApk = resourcesApkContext.getAssets();
        }
        return this.mWifiAssetsFromApk;
    }

    public Resources getResources() {
        Context resourcesApkContext;
        if (this.mWifiResourcesFromApk == null && (resourcesApkContext = getResourcesApkContext()) != null) {
            this.mWifiResourcesFromApk = resourcesApkContext.getResources();
        }
        return this.mWifiResourcesFromApk;
    }

    public Resources.Theme getTheme() {
        Context resourcesApkContext;
        if (this.mWifiThemeFromApk == null && (resourcesApkContext = getResourcesApkContext()) != null) {
            this.mWifiThemeFromApk = resourcesApkContext.getTheme();
        }
        return this.mWifiThemeFromApk;
    }

    public void resetResourceCache() {
        this.mWifiOverlayApkPkgName = null;
        this.mWifiAssetsFromApk = null;
        this.mWifiResourcesFromApk = null;
        this.mWifiThemeFromApk = null;
    }

    public WifiStringResourceWrapper getStringResourceWrapper(int i, int i2) {
        return new WifiStringResourceWrapper(this, i, i2);
    }
}
