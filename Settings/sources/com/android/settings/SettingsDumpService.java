package com.android.settings;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.NetworkTemplate;
import android.net.Uri;
import android.os.IBinder;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.settings.applications.ProcStatsData;
import com.android.settings.datausage.lib.DataUsageLib;
import com.android.settingslib.applications.RecentAppOpsAccess;
import com.android.settingslib.net.DataUsageController;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsDumpService extends Service {
    static final Intent BROWSER_INTENT = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
    static final String KEY_ANOMALY_DETECTION = "anomaly_detection";
    static final String KEY_DATAUSAGE = "datausage";
    static final String KEY_DEFAULT_BROWSER_APP = "default_browser_app";
    static final String KEY_MEMORY = "memory";
    static final String KEY_SERVICE = "service";
    static final String KEY_STORAGE = "storage";

    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(KEY_SERVICE, "Settings State");
            jSONObject.put(KEY_STORAGE, dumpStorage());
            jSONObject.put(KEY_DATAUSAGE, dumpDataUsage());
            jSONObject.put(KEY_MEMORY, dumpMemory());
            jSONObject.put(KEY_DEFAULT_BROWSER_APP, dumpDefaultBrowser());
            jSONObject.put(KEY_ANOMALY_DETECTION, dumpAnomalyDetection());
        } catch (Exception e) {
            e.printStackTrace();
        }
        printWriter.println(jSONObject);
    }

    private JSONObject dumpMemory() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        ProcStatsData procStatsData = new ProcStatsData(this, false);
        procStatsData.refreshStats(true);
        ProcStatsData.MemInfo memInfo = procStatsData.getMemInfo();
        jSONObject.put("used", String.valueOf(memInfo.realUsedRam));
        jSONObject.put("free", String.valueOf(memInfo.realFreeRam));
        jSONObject.put("total", String.valueOf(memInfo.realTotalRam));
        jSONObject.put("state", procStatsData.getMemState());
        return jSONObject;
    }

    private JSONObject dumpDataUsage() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        DataUsageController dataUsageController = new DataUsageController(this);
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(SubscriptionManager.class);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TelephonyManager.class);
        PackageManager packageManager = getPackageManager();
        if (telephonyManager.isDataCapable()) {
            JSONArray jSONArray = new JSONArray();
            for (SubscriptionInfo subscriptionInfo : subscriptionManager.getAvailableSubscriptionInfoList()) {
                JSONObject dumpDataUsage = dumpDataUsage(DataUsageLib.getMobileTemplateForSubId(telephonyManager, subscriptionInfo.getSubscriptionId()), dataUsageController);
                dumpDataUsage.put("subId", subscriptionInfo.getSubscriptionId());
                jSONArray.put(dumpDataUsage);
            }
            jSONObject.put("cell", jSONArray);
        }
        if (packageManager.hasSystemFeature("android.hardware.wifi")) {
            jSONObject.put("wifi", dumpDataUsage(new NetworkTemplate.Builder(4).build(), dataUsageController));
        }
        if (packageManager.hasSystemFeature("android.hardware.ethernet")) {
            jSONObject.put("ethernet", dumpDataUsage(new NetworkTemplate.Builder(5).build(), dataUsageController));
        }
        return jSONObject;
    }

    private JSONObject dumpDataUsage(NetworkTemplate networkTemplate, DataUsageController dataUsageController) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        DataUsageController.DataUsageInfo dataUsageInfo = dataUsageController.getDataUsageInfo(networkTemplate);
        jSONObject.put("carrier", dataUsageInfo.carrier);
        jSONObject.put("start", dataUsageInfo.startDate);
        jSONObject.put("usage", dataUsageInfo.usageLevel);
        jSONObject.put("warning", dataUsageInfo.warningLevel);
        jSONObject.put("limit", dataUsageInfo.limitLevel);
        return jSONObject;
    }

    private JSONObject dumpStorage() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (VolumeInfo volumeInfo : ((StorageManager) getSystemService(StorageManager.class)).getVolumes()) {
            JSONObject jSONObject2 = new JSONObject();
            if (volumeInfo.isMountedReadable()) {
                File path = volumeInfo.getPath();
                jSONObject2.put("used", String.valueOf(path.getTotalSpace() - path.getFreeSpace()));
                jSONObject2.put("total", String.valueOf(path.getTotalSpace()));
            }
            jSONObject2.put("path", volumeInfo.getInternalPath());
            jSONObject2.put("state", volumeInfo.getState());
            jSONObject2.put("stateDesc", volumeInfo.getStateDescription());
            jSONObject2.put("description", volumeInfo.getDescription());
            jSONObject.put(volumeInfo.getId(), jSONObject2);
        }
        return jSONObject;
    }

    /* access modifiers changed from: package-private */
    public String dumpDefaultBrowser() {
        ResolveInfo resolveActivity = getPackageManager().resolveActivity(BROWSER_INTENT, 65536);
        if (resolveActivity == null || resolveActivity.activityInfo.packageName.equals(RecentAppOpsAccess.ANDROID_SYSTEM_PACKAGE_NAME)) {
            return null;
        }
        return resolveActivity.activityInfo.packageName;
    }

    /* access modifiers changed from: package-private */
    public JSONObject dumpAnomalyDetection() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("anomaly_config_version", String.valueOf(getSharedPreferences("anomaly_pref", 0).getInt("anomaly_config_version", 0)));
        return jSONObject;
    }
}
