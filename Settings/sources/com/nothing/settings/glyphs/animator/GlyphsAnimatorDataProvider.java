package com.nothing.settings.glyphs.animator;

import android.content.Context;
import android.text.TextUtils;
import androidx.collection.LruCache;
import java.util.List;

public class GlyphsAnimatorDataProvider {
    public static LruCache<String, List<GlyphsAnimatorDataBean>> sCaches = new LruCache<>(50);

    public List<GlyphsAnimatorDataBean> getGlyphsAnimatorData(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List<GlyphsAnimatorDataBean> list = sCaches.get(str);
        return list == null ? readGlyphsAnimatorDataFromAsset(context, str) : list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x007f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0080 A[SYNTHETIC, Splitter:B:30:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x008a A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x008b A[SYNTHETIC, Splitter:B:37:0x008b] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0096 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0097 A[SYNTHETIC, Splitter:B:44:0x0097] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:33:0x0085=Splitter:B:33:0x0085, B:26:0x007a=Splitter:B:26:0x007a} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.nothing.settings.glyphs.animator.GlyphsAnimatorDataBean> readGlyphsAnimatorDataFromAsset(android.content.Context r9, java.lang.String r10) {
        /*
            r8 = this;
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            r0 = 0
            android.content.Context r9 = r9.getApplicationContext()     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
            android.content.res.AssetManager r9 = r9.getAssets()     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
            java.io.InputStream r9 = r9.open(r10)     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
            r1.<init>(r9)     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
            java.io.BufferedReader r9 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
            r9.<init>(r1)     // Catch:{ FileNotFoundException -> 0x0084, IOException -> 0x0079 }
        L_0x001c:
            java.lang.String r0 = r9.readLine()     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            if (r0 == 0) goto L_0x0065
            com.nothing.settings.glyphs.animator.GlyphsAnimatorDataBean r1 = new com.nothing.settings.glyphs.animator.GlyphsAnimatorDataBean     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            r1.<init>()     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            java.util.StringTokenizer r2 = new java.util.StringTokenizer     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            java.lang.String r3 = ","
            r2.<init>(r0, r3)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            r0 = 0
            r3 = r0
        L_0x0030:
            boolean r4 = r2.hasMoreTokens()     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            if (r4 == 0) goto L_0x0061
            java.lang.String r4 = r2.nextToken()     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            double r4 = java.lang.Double.parseDouble(r4)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 != 0) goto L_0x0048
            r1.setLedValue(r3, r0)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            goto L_0x005e
        L_0x0048:
            r6 = 4630826316843712512(0x4044000000000000, double:40.0)
            double r4 = r4 * r6
            r6 = 4661225614328463360(0x40b0000000000000, double:4096.0)
            double r4 = r4 / r6
            r6 = 4633641066610819072(0x404e000000000000, double:60.0)
            double r4 = r4 + r6
            r6 = 4643176031446892544(0x406fe00000000000, double:255.0)
            double r4 = r4 * r6
            r6 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r4 = r4 / r6
            int r4 = (int) r4     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            r1.setLedValue(r3, r4)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
        L_0x005e:
            int r3 = r3 + 1
            goto L_0x0030
        L_0x0061:
            r8.add(r1)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            goto L_0x001c
        L_0x0065:
            androidx.collection.LruCache<java.lang.String, java.util.List<com.nothing.settings.glyphs.animator.GlyphsAnimatorDataBean>> r0 = sCaches     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            r0.put(r10, r8)     // Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0071, all -> 0x006e }
            r9.close()     // Catch:{ IOException -> 0x008f }
            goto L_0x0093
        L_0x006e:
            r10 = move-exception
            r0 = r9
            goto L_0x0094
        L_0x0071:
            r10 = move-exception
            r0 = r9
            goto L_0x007a
        L_0x0074:
            r10 = move-exception
            r0 = r9
            goto L_0x0085
        L_0x0077:
            r10 = move-exception
            goto L_0x0094
        L_0x0079:
            r10 = move-exception
        L_0x007a:
            r10.printStackTrace()     // Catch:{ all -> 0x0077 }
            if (r0 != 0) goto L_0x0080
            return r8
        L_0x0080:
            r0.close()     // Catch:{ IOException -> 0x008f }
            goto L_0x0093
        L_0x0084:
            r10 = move-exception
        L_0x0085:
            r10.printStackTrace()     // Catch:{ all -> 0x0077 }
            if (r0 != 0) goto L_0x008b
            return r8
        L_0x008b:
            r0.close()     // Catch:{ IOException -> 0x008f }
            goto L_0x0093
        L_0x008f:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0093:
            return r8
        L_0x0094:
            if (r0 != 0) goto L_0x0097
            return r8
        L_0x0097:
            r0.close()     // Catch:{ IOException -> 0x009b }
            goto L_0x009f
        L_0x009b:
            r8 = move-exception
            r8.printStackTrace()
        L_0x009f:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.glyphs.animator.GlyphsAnimatorDataProvider.readGlyphsAnimatorDataFromAsset(android.content.Context, java.lang.String):java.util.List");
    }
}
