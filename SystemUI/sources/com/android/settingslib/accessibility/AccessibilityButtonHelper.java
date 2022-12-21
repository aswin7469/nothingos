package com.android.settingslib.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

public class AccessibilityButtonHelper {
    public static boolean isRequestedByMagnification(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_display_magnification_navbar_enabled", 0) == 1;
    }

    public static boolean isRequestedByAccessibilityService(Context context) {
        List<AccessibilityServiceInfo> enabledAccessibilityServiceList = ((AccessibilityManager) context.getSystemService(AccessibilityManager.class)).getEnabledAccessibilityServiceList(-1);
        if (enabledAccessibilityServiceList != null) {
            int size = enabledAccessibilityServiceList.size();
            for (int i = 0; i < size; i++) {
                if ((enabledAccessibilityServiceList.get(i).flags & 256) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRequested(Context context) {
        return isRequestedByMagnification(context) || isRequestedByAccessibilityService(context);
    }
}
