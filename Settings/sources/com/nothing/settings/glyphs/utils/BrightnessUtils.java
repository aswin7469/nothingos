package com.nothing.settings.glyphs.utils;

import android.os.SystemProperties;
import android.text.TextUtils;

public class BrightnessUtils {
    private static int sCurrentBrightnessMax = -1;
    private static int sCurrentBrightnessMin = -1;

    public static int getLedBrightnessMax() {
        int i = sCurrentBrightnessMax;
        if (i != -1) {
            return i;
        }
        int i2 = TextUtils.equals(SystemProperties.get("ro.phone.shell.color"), "white") ? 1306 : 3840;
        sCurrentBrightnessMax = i2;
        return i2;
    }

    public static int getLedBrightnessMin() {
        int i = sCurrentBrightnessMin;
        if (i != -1) {
            return i;
        }
        int i2 = TextUtils.equals(SystemProperties.get("ro.phone.shell.color"), "white") ? 241 : 750;
        sCurrentBrightnessMin = i2;
        return i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f A[SYNTHETIC, Splitter:B:17:0x003f] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0045 A[SYNTHETIC, Splitter:B:21:0x0045] */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeLedBrightness(int r4) {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x001f }
            java.lang.String r2 = "/sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br"
            r1.<init>(r2)     // Catch:{ IOException -> 0x001f }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ IOException -> 0x001a, all -> 0x0017 }
            byte[] r4 = r4.getBytes()     // Catch:{ IOException -> 0x001a, all -> 0x0017 }
            r1.write(r4)     // Catch:{ IOException -> 0x001a, all -> 0x0017 }
            r1.close()     // Catch:{ IOException -> 0x0042 }
            goto L_0x0042
        L_0x0017:
            r4 = move-exception
            r0 = r1
            goto L_0x0043
        L_0x001a:
            r4 = move-exception
            r0 = r1
            goto L_0x0020
        L_0x001d:
            r4 = move-exception
            goto L_0x0043
        L_0x001f:
            r4 = move-exception
        L_0x0020:
            java.lang.String r1 = "LedBrightnessUtils"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x001d }
            r2.<init>()     // Catch:{ all -> 0x001d }
            java.lang.String r3 = "Unable to write /sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br"
            r2.append(r3)     // Catch:{ all -> 0x001d }
            java.lang.String r3 = r4.getMessage()     // Catch:{ all -> 0x001d }
            r2.append(r3)     // Catch:{ all -> 0x001d }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x001d }
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x001d }
            r4.printStackTrace()     // Catch:{ all -> 0x001d }
            if (r0 == 0) goto L_0x0042
            r0.close()     // Catch:{ IOException -> 0x0042 }
        L_0x0042:
            return
        L_0x0043:
            if (r0 == 0) goto L_0x0048
            r0.close()     // Catch:{ IOException -> 0x0048 }
        L_0x0048:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.glyphs.utils.BrightnessUtils.writeLedBrightness(int):void");
    }
}
