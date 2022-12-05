package com.nothingos.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.nothing.experience.AppTracking;
/* loaded from: classes2.dex */
public class SystemUIEventUtils {
    private static AppTracking sAppTracking;

    private static void onProductEvent(Context context, String str, Bundle bundle) {
        if (sAppTracking == null) {
            sAppTracking = AppTracking.getInstance(context);
        }
        AppTracking appTracking = sAppTracking;
        if (appTracking != null) {
            appTracking.logProductEvent(str, bundle);
        }
    }

    public static void collectUnLockResults(Context context, String str, int i) {
        Log.i("SystemUIMobEventUtils", "onEvent name =OS_Activekey = " + str + ", value = " + i);
        Bundle bundle = new Bundle();
        bundle.putInt(str, i);
        onProductEvent(context, "OS_Active", bundle);
    }
}
