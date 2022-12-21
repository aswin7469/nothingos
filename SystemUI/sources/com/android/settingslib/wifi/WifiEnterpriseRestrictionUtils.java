package com.android.settingslib.wifi;

import android.content.Context;
import android.os.UserManager;
import android.util.Log;

public class WifiEnterpriseRestrictionUtils {
    private static final String TAG = "WifiEntResUtils";

    private static boolean isAtLeastT() {
        return true;
    }

    public static boolean isWifiTetheringAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_wifi_tethering")) {
            return true;
        }
        Log.w(TAG, "Wi-Fi Tethering isn't available due to user restriction.");
        return false;
    }

    public static boolean isWifiDirectAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_wifi_direct")) {
            return true;
        }
        Log.w(TAG, "Wi-Fi Direct isn't available due to user restriction.");
        return false;
    }

    public static boolean isAddWifiConfigAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_add_wifi_config")) {
            return true;
        }
        Log.w(TAG, "Wi-Fi Add network isn't available due to user restriction.");
        return false;
    }

    public static boolean isChangeWifiStateAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_change_wifi_state")) {
            return true;
        }
        Log.w(TAG, "WI-FI state isn't allowed to change due to user restriction.");
        return false;
    }

    static boolean hasUserRestrictionFromT(Context context, String str) {
        UserManager userManager;
        if (isAtLeastT() && (userManager = (UserManager) context.getSystemService(UserManager.class)) != null) {
            return userManager.hasUserRestriction(str);
        }
        return false;
    }
}
