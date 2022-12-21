package android.net.wifi.util;

import android.content.ApexEnvironment;
import android.content.pm.ApplicationInfo;
import android.os.UserHandle;
import java.p026io.File;

public class Environment {
    private static final String TAG = "Environment";
    private static final String WIFI_APEX_NAME = "com.android.wifi";
    private static final String WIFI_APEX_PATH = new File("/apex", WIFI_APEX_NAME).getAbsolutePath();

    public static File getWifiSharedDirectory() {
        return ApexEnvironment.getApexEnvironment(WIFI_APEX_NAME).getDeviceProtectedDataDir();
    }

    public static File getWifiUserDirectory(int i) {
        return ApexEnvironment.getApexEnvironment(WIFI_APEX_NAME).getCredentialProtectedDataDirForUser(UserHandle.of(i));
    }

    public static boolean isAppInWifiApex(ApplicationInfo applicationInfo) {
        return applicationInfo.sourceDir.startsWith(WIFI_APEX_PATH);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isVndkApiLevelNewerThan(int r4) {
        /*
            java.util.Optional r0 = com.android.wifi.p018x.android.sysprop.VndkProperties.vendor_vndk_version()
            java.lang.String r1 = ""
            java.lang.Object r0 = r0.orElse(r1)
            java.lang.String r0 = (java.lang.String) r0
            boolean r1 = r0.isEmpty()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x002b
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x0019 }
            goto L_0x002c
        L_0x0019:
            java.lang.String r0 = "REL"
            java.lang.String r1 = android.os.Build.VERSION.CODENAME
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x002b
            java.lang.String r4 = "Environment"
            java.lang.String r0 = "developer build, bypass the vndk version check"
            android.util.Log.d(r4, r0)
            return r2
        L_0x002b:
            r0 = r3
        L_0x002c:
            if (r0 <= r4) goto L_0x002f
            goto L_0x0030
        L_0x002f:
            r2 = r3
        L_0x0030:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.util.Environment.isVndkApiLevelNewerThan(int):boolean");
    }
}
