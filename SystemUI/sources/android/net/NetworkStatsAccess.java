package android.net;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.UserHandle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class NetworkStatsAccess {

    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
        public static final int DEFAULT = 0;
        public static final int DEVICE = 3;
        public static final int DEVICESUMMARY = 2;
        public static final int USER = 1;
    }

    private NetworkStatsAccess() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int checkAccessLevel(android.content.Context r7, int r8, int r9, java.lang.String r10) {
        /*
            java.lang.Class<android.app.admin.DevicePolicyManager> r0 = android.app.admin.DevicePolicyManager.class
            java.lang.Object r0 = r7.getSystemService(r0)
            android.app.admin.DevicePolicyManager r0 = (android.app.admin.DevicePolicyManager) r0
            java.lang.String r1 = "phone"
            java.lang.Object r1 = r7.getSystemService(r1)
            android.telephony.TelephonyManager r1 = (android.telephony.TelephonyManager) r1
            long r2 = android.os.Binder.clearCallingIdentity()
            r4 = 0
            r5 = 1
            if (r1 == 0) goto L_0x0025
            int r1 = r1.checkCarrierPrivilegesForPackageAnyPhone(r10)     // Catch:{ all -> 0x0020 }
            if (r1 != r5) goto L_0x0025
            r1 = r5
            goto L_0x0026
        L_0x0020:
            r7 = move-exception
            android.os.Binder.restoreCallingIdentity(r2)
            throw r7
        L_0x0025:
            r1 = r4
        L_0x0026:
            android.os.Binder.restoreCallingIdentity(r2)
            if (r0 == 0) goto L_0x0033
            boolean r2 = r0.isDeviceOwnerApp(r10)
            if (r2 == 0) goto L_0x0033
            r2 = r5
            goto L_0x0034
        L_0x0033:
            r2 = r4
        L_0x0034:
            int r3 = android.os.UserHandle.getAppId(r9)
            java.lang.String r6 = "android.permission.NETWORK_STACK"
            int r8 = r7.checkPermission(r6, r8, r9)
            if (r8 != 0) goto L_0x0042
            r8 = r5
            goto L_0x0043
        L_0x0042:
            r8 = r4
        L_0x0043:
            if (r1 != 0) goto L_0x0074
            if (r2 != 0) goto L_0x0074
            r1 = 1000(0x3e8, float:1.401E-42)
            if (r3 == r1) goto L_0x0074
            if (r8 == 0) goto L_0x004e
            goto L_0x0074
        L_0x004e:
            boolean r8 = hasAppOpsPermission(r7, r9, r10)
            if (r8 != 0) goto L_0x0072
            java.lang.String r8 = "android.permission.READ_NETWORK_USAGE_HISTORY"
            int r7 = r7.checkCallingOrSelfPermission(r8)
            if (r7 != 0) goto L_0x005d
            goto L_0x0072
        L_0x005d:
            if (r0 == 0) goto L_0x006d
            boolean r7 = r0.isProfileOwnerApp(r10)
            if (r7 != 0) goto L_0x006b
            boolean r7 = r0.isDeviceOwnerApp(r10)
            if (r7 == 0) goto L_0x006d
        L_0x006b:
            r7 = r5
            goto L_0x006e
        L_0x006d:
            r7 = r4
        L_0x006e:
            if (r7 == 0) goto L_0x0071
            return r5
        L_0x0071:
            return r4
        L_0x0072:
            r7 = 2
            return r7
        L_0x0074:
            r7 = 3
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.NetworkStatsAccess.checkAccessLevel(android.content.Context, int, int, java.lang.String):int");
    }

    public static boolean isAccessibleToUser(int i, int i2, int i3) {
        int identifier = UserHandle.getUserHandleForUid(i).getIdentifier();
        int identifier2 = UserHandle.getUserHandleForUid(i2).getIdentifier();
        if (i3 == 1) {
            return i == 1000 || i == -4 || i == -5 || identifier == identifier2;
        }
        if (i3 == 2) {
            return i == 1000 || i == -4 || i == -5 || i == -1 || identifier == identifier2;
        }
        if (i3 != 3) {
            return i == i2;
        }
        return true;
    }

    private static boolean hasAppOpsPermission(Context context, int i, String str) {
        if (str == null) {
            return false;
        }
        int noteOp = ((AppOpsManager) context.getSystemService("appops")).noteOp("android:get_usage_stats", i, str, (String) null, (String) null);
        if (noteOp == 3) {
            if (context.checkCallingPermission("android.permission.PACKAGE_USAGE_STATS") == 0) {
                return true;
            }
            return false;
        } else if (noteOp == 0) {
            return true;
        } else {
            return false;
        }
    }
}
