package com.android.settings.wifi;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiConfiguration;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.Utils;
import com.android.wifitrackerlib.WifiEntry;
import java.nio.charset.StandardCharsets;
/* loaded from: classes.dex */
public class WifiUtils extends com.android.settingslib.wifi.WifiUtils {
    public static boolean isSSIDTooLong(String str) {
        return !TextUtils.isEmpty(str) && str.getBytes(StandardCharsets.UTF_8).length > 32;
    }

    public static boolean isSSIDTooShort(String str) {
        return TextUtils.isEmpty(str) || str.length() < 1;
    }

    public static boolean isHotspotPasswordValid(String str, int i) {
        try {
            new SoftApConfiguration.Builder().setPassphrase(str, i);
            return true;
        } catch (IllegalArgumentException unused) {
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x003a, code lost:
        if (r2.getPackageUidAsUser(r4.getPackageName(), r1.getDeviceOwnerUserId()) == r7.creatorUid) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x005c, code lost:
        if (r2.getPackageUidAsUser(r1.getPackageName(), r3) == r7.creatorUid) goto L16;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x005f -> B:17:0x0060). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean isNetworkLockedDown(Context context, WifiConfiguration wifiConfiguration) {
        boolean z;
        if (wifiConfiguration == null) {
            return false;
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        PackageManager packageManager = context.getPackageManager();
        UserManager userManager = (UserManager) context.getSystemService("user");
        if (packageManager.hasSystemFeature("android.software.device_admin") && devicePolicyManager == null) {
            return true;
        }
        if (devicePolicyManager != null) {
            ComponentName deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser();
            try {
                if (deviceOwnerComponentOnAnyUser == null) {
                    if (devicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
                        int managedProfileId = Utils.getManagedProfileId(userManager, UserHandle.myUserId());
                        ComponentName profileOwnerAsUser = devicePolicyManager.getProfileOwnerAsUser(managedProfileId);
                        if (profileOwnerAsUser != null) {
                        }
                    }
                }
                z = true;
            } catch (PackageManager.NameNotFoundException unused) {
            }
            return !z && Settings.Global.getInt(context.getContentResolver(), "wifi_device_owner_configs_lockdown", 0) != 0;
        }
        z = false;
        if (!z) {
            return false;
        }
    }

    public static WifiConfiguration getWifiConfig(WifiEntry wifiEntry, ScanResult scanResult) {
        int security;
        if (wifiEntry == null && scanResult == null) {
            throw new IllegalArgumentException("At least one of WifiEntry and ScanResult input is required.");
        }
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        if (wifiEntry == null) {
            wifiConfiguration.SSID = "\"" + scanResult.SSID + "\"";
            security = getWifiEntrySecurity(scanResult);
        } else {
            if (wifiEntry.getWifiConfiguration() == null) {
                wifiConfiguration.SSID = "\"" + wifiEntry.getSsid() + "\"";
            } else {
                wifiConfiguration.networkId = wifiEntry.getWifiConfiguration().networkId;
                wifiConfiguration.hiddenSSID = wifiEntry.getWifiConfiguration().hiddenSSID;
            }
            security = wifiEntry.getSecurity();
        }
        switch (security) {
            case 0:
                wifiConfiguration.setSecurityParams(0);
                break;
            case 1:
                wifiConfiguration.setSecurityParams(1);
                break;
            case 2:
                wifiConfiguration.setSecurityParams(2);
                break;
            case 3:
                wifiConfiguration.setSecurityParams(3);
                break;
            case 4:
                wifiConfiguration.setSecurityParams(6);
                break;
            case 5:
                wifiConfiguration.setSecurityParams(4);
                break;
            case 6:
                wifiConfiguration.setSecurityParams(5);
                break;
        }
        return wifiConfiguration;
    }

    public static int getWifiEntrySecurity(ScanResult scanResult) {
        if (scanResult.capabilities.contains("WEP")) {
            return 1;
        }
        if (scanResult.capabilities.contains("SAE")) {
            return 5;
        }
        if (scanResult.capabilities.contains("PSK")) {
            return 2;
        }
        if (scanResult.capabilities.contains("EAP_SUITE_B_192")) {
            return 6;
        }
        if (scanResult.capabilities.contains("EAP")) {
            return 3;
        }
        return scanResult.capabilities.contains("OWE") ? 4 : 0;
    }
}
