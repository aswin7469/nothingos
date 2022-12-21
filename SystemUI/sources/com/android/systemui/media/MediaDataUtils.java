package com.android.systemui.media;

public class MediaDataUtils {
    /* JADX WARNING: type inference failed for: r4v3, types: [java.lang.CharSequence] */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getAppLabel(android.content.Context r2, java.lang.String r3, java.lang.String r4) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            android.content.pm.PackageManager r2 = r2.getPackageManager()
            r0 = 0
            android.content.pm.ApplicationInfo r1 = r2.getApplicationInfo(r3, r0)     // Catch:{ NameNotFoundException -> 0x0011 }
        L_0x0011:
            if (r1 == 0) goto L_0x0017
            java.lang.CharSequence r4 = r2.getApplicationLabel(r1)
        L_0x0017:
            java.lang.String r4 = (java.lang.String) r4
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataUtils.getAppLabel(android.content.Context, java.lang.String, java.lang.String):java.lang.String");
    }
}
