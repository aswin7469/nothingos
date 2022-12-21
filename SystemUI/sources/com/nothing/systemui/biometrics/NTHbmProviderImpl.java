package com.nothing.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.nothing.systemui.util.NTLogUtil;

public class NTHbmProviderImpl implements UdfpsHbmProvider {
    private static final String HBM_PATH = "/sys/class/drm/sde-conn-1-DSI-1/hbm_mode";
    private static final String OLD_HBM_PATH = "/sys/class/backlight/panel0-backlight/hbm_mode";
    private static final String TAG = "NtHBMProviderImpl";
    private boolean mHbmEnabled;

    public void enableHbm(boolean z, Runnable runnable) {
        if (!this.mHbmEnabled) {
            if (runnable != null) {
                runnable.run();
            }
            this.mHbmEnabled = true;
            NTLogUtil.m1682i(TAG, "------HBM ENABLED-1---------");
        }
    }

    public void disableHbm(Runnable runnable) {
        if (this.mHbmEnabled) {
            if (runnable != null) {
                runnable.run();
            }
            this.mHbmEnabled = false;
            NTLogUtil.m1682i(TAG, "------HBM DISABLED---------");
        }
    }

    public void disableHbm() {
        NTLogUtil.m1682i(TAG, "------HBM DISABLED-------disableHbm--");
    }

    public void enableHbm() {
        if (!this.mHbmEnabled) {
            this.mHbmEnabled = true;
            NTLogUtil.m1682i(TAG, "------HBM ENABLED----------");
        }
    }

    public boolean isHbmEnabled() {
        return this.mHbmEnabled;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0058 A[SYNTHETIC, Splitter:B:22:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x007b A[SYNTHETIC, Splitter:B:29:0x007b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeHbmNode(boolean r8) {
        /*
            r7 = this;
            java.lang.String r0 = "NtHBMProviderImpl"
            java.lang.String r1 = "start write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:"
            java.lang.String r2 = "Unable to write /sys/class/drm/sde-conn-1-DSI-1/hbm_mode"
            if (r8 == 0) goto L_0x000b
            java.lang.String r3 = "1"
            goto L_0x000d
        L_0x000b:
            java.lang.String r3 = "0"
        L_0x000d:
            r4 = 0
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x003b }
            java.lang.String r6 = "/sys/class/drm/sde-conn-1-DSI-1/hbm_mode"
            r5.<init>((java.lang.String) r6)     // Catch:{ IOException -> 0x003b }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r4.<init>((java.lang.String) r1)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            java.lang.StringBuilder r1 = r4.append((java.lang.String) r3)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            android.util.Slog.d(r0, r1)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            java.lang.String r1 = "US-ASCII"
            byte[] r1 = r3.getBytes((java.lang.String) r1)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r5.write((byte[]) r1)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r5.close()     // Catch:{ IOException -> 0x0031 }
        L_0x0031:
            r7 = 1
            goto L_0x005c
        L_0x0033:
            r7 = move-exception
            r4 = r5
            goto L_0x0079
        L_0x0036:
            r1 = move-exception
            r4 = r5
            goto L_0x003c
        L_0x0039:
            r7 = move-exception
            goto L_0x0079
        L_0x003b:
            r1 = move-exception
        L_0x003c:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0039 }
            r5.<init>((java.lang.String) r2)     // Catch:{ all -> 0x0039 }
            java.lang.String r2 = r1.getMessage()     // Catch:{ all -> 0x0039 }
            java.lang.StringBuilder r2 = r5.append((java.lang.String) r2)     // Catch:{ all -> 0x0039 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0039 }
            android.util.Slog.e(r0, r2)     // Catch:{ all -> 0x0039 }
            r1.printStackTrace()     // Catch:{ all -> 0x0039 }
            r7.writeOldHbmNode(r8)     // Catch:{ all -> 0x0039 }
            if (r4 == 0) goto L_0x005b
            r4.close()     // Catch:{ IOException -> 0x005b }
        L_0x005b:
            r7 = 0
        L_0x005c:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r1 = "end write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:"
            r8.<init>((java.lang.String) r1)
            java.lang.StringBuilder r8 = r8.append((java.lang.String) r3)
            java.lang.String r1 = ",  result: "
            java.lang.StringBuilder r8 = r8.append((java.lang.String) r1)
            java.lang.StringBuilder r7 = r8.append((boolean) r7)
            java.lang.String r7 = r7.toString()
            android.util.Slog.d(r0, r7)
            return
        L_0x0079:
            if (r4 == 0) goto L_0x007e
            r4.close()     // Catch:{ IOException -> 0x007e }
        L_0x007e:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.biometrics.NTHbmProviderImpl.writeHbmNode(boolean):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0055 A[SYNTHETIC, Splitter:B:22:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0078 A[SYNTHETIC, Splitter:B:29:0x0078] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeOldHbmNode(boolean r6) {
        /*
            r5 = this;
            java.lang.String r5 = "NtHBMProviderImpl"
            java.lang.String r0 = "start write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:"
            java.lang.String r1 = "Unable to write /sys/class/backlight/panel0-backlight/hbm_mode"
            if (r6 == 0) goto L_0x000b
            java.lang.String r6 = "1"
            goto L_0x000d
        L_0x000b:
            java.lang.String r6 = "0"
        L_0x000d:
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x003b }
            java.lang.String r4 = "/sys/class/backlight/panel0-backlight/hbm_mode"
            r3.<init>((java.lang.String) r4)     // Catch:{ IOException -> 0x003b }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r2.<init>((java.lang.String) r0)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            java.lang.StringBuilder r0 = r2.append((java.lang.String) r6)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            android.util.Slog.d(r5, r0)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            java.lang.String r0 = "US-ASCII"
            byte[] r0 = r6.getBytes((java.lang.String) r0)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r3.write((byte[]) r0)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r3.close()     // Catch:{ IOException -> 0x0031 }
        L_0x0031:
            r0 = 1
            goto L_0x0059
        L_0x0033:
            r5 = move-exception
            r2 = r3
            goto L_0x0076
        L_0x0036:
            r0 = move-exception
            r2 = r3
            goto L_0x003c
        L_0x0039:
            r5 = move-exception
            goto L_0x0076
        L_0x003b:
            r0 = move-exception
        L_0x003c:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0039 }
            r3.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0039 }
            java.lang.String r1 = r0.getMessage()     // Catch:{ all -> 0x0039 }
            java.lang.StringBuilder r1 = r3.append((java.lang.String) r1)     // Catch:{ all -> 0x0039 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0039 }
            android.util.Slog.e(r5, r1)     // Catch:{ all -> 0x0039 }
            r0.printStackTrace()     // Catch:{ all -> 0x0039 }
            if (r2 == 0) goto L_0x0058
            r2.close()     // Catch:{ IOException -> 0x0058 }
        L_0x0058:
            r0 = 0
        L_0x0059:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "end write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:"
            r1.<init>((java.lang.String) r2)
            java.lang.StringBuilder r6 = r1.append((java.lang.String) r6)
            java.lang.String r1 = ",  result: "
            java.lang.StringBuilder r6 = r6.append((java.lang.String) r1)
            java.lang.StringBuilder r6 = r6.append((boolean) r0)
            java.lang.String r6 = r6.toString()
            android.util.Slog.d(r5, r6)
            return
        L_0x0076:
            if (r2 == 0) goto L_0x007b
            r2.close()     // Catch:{ IOException -> 0x007b }
        L_0x007b:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.biometrics.NTHbmProviderImpl.writeOldHbmNode(boolean):void");
    }
}
