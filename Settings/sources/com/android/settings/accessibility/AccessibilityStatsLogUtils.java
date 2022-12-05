package com.android.settings.accessibility;

import android.content.ComponentName;
import com.android.settings.core.instrumentation.SettingsStatsLog;
/* loaded from: classes.dex */
public final class AccessibilityStatsLogUtils {
    private static int convertToLoggingServiceEnabled(boolean z) {
        return z ? 1 : 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logAccessibilityServiceEnabled(ComponentName componentName, boolean z) {
        SettingsStatsLog.write(267, componentName.flattenToString(), convertToLoggingServiceEnabled(z));
    }
}
