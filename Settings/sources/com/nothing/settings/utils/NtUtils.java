package com.nothing.settings.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.nothing.experience.AppTracking;
import java.util.Arrays;
import java.util.stream.Collectors;

public class NtUtils {
    public static void trackRingtoneChanged(Context context, int i, String str, int i2) {
        Log.d("Settings.NtUtils", "trackRingtoneChanged: type = " + i + ", title = " + str + ", soundOnly = " + i2);
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        if (1 == i) {
            bundle.putString("ringtone_def", str);
            bundle2.putInt("ringtone_so", i2);
        } else if (2 == i) {
            bundle.putString("notification_def", str);
            bundle2.putInt("notification_so", i2);
        } else {
            return;
        }
        Log.d("Settings.NtUtils", "trackRingtoneChanged: [APP_TRACKING] " + bundle + ", " + bundle2);
        AppTracking.getInstance(context).logProductEvent("LED_Event", bundle);
        AppTracking.getInstance(context).logProductEvent("LED_Event", bundle2);
    }

    public static void trackIntGlyph(Context context, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(str, i);
        AppTracking.getInstance(context).logProductEvent("LED_Event", bundle);
        Log.d("Settings.NtUtils", "trackIntGlyph: [APP_TRACKING] key:" + str + ", value:" + i);
    }

    public static void trackIntArrayGlyph(Context context, String str, int[] iArr) {
        Bundle bundle = new Bundle();
        bundle.putIntArray(str, iArr);
        AppTracking.getInstance(context).logProductEvent("LED_Event", bundle);
        Log.d("Settings.NtUtils", "trackIntArrayGlyph: [APP_TRACKING] key:" + str + ", value:" + Arrays.stream(iArr).boxed().collect(Collectors.toList()));
    }
}
