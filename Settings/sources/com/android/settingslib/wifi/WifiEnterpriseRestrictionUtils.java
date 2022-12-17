package com.android.settingslib.wifi;

import android.content.Context;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;

public class WifiEnterpriseRestrictionUtils {
    private static boolean isAtLeastT() {
        return true;
    }

    public static boolean isWifiTetheringAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_wifi_tethering")) {
            return true;
        }
        Log.w("WifiEntResUtils", "Wi-Fi Tethering isn't available due to user restriction.");
        return false;
    }

    public static boolean isWifiDirectAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_wifi_direct")) {
            return true;
        }
        Log.w("WifiEntResUtils", "Wi-Fi Direct isn't available due to user restriction.");
        return false;
    }

    public static boolean isAddWifiConfigAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_add_wifi_config")) {
            return true;
        }
        Log.w("WifiEntResUtils", "Wi-Fi Add network isn't available due to user restriction.");
        return false;
    }

    public static boolean isChangeWifiStateAllowed(Context context) {
        if (!hasUserRestrictionFromT(context, "no_change_wifi_state")) {
            return true;
        }
        Log.w("WifiEntResUtils", "WI-FI state isn't allowed to change due to user restriction.");
        return false;
    }

    @VisibleForTesting
    static boolean hasUserRestrictionFromT(Context context, String str) {
        UserManager userManager;
        if (isAtLeastT() && (userManager = (UserManager) context.getSystemService(UserManager.class)) != null) {
            return userManager.hasUserRestriction(str);
        }
        return false;
    }
}
