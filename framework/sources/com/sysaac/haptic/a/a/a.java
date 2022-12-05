package com.sysaac.haptic.a.a;

import android.util.Log;
import com.sysaac.haptic.b.a.e;
import com.sysaac.haptic.base.r;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Iterator;
/* loaded from: classes4.dex */
public class a {
    public static final String a = "NordicUtils";
    public static final byte b = 16;
    public static final byte c = 17;
    public static final int d = 1;
    public static final int e = 5;
    public static final int f = 15;
    public static final int g = 241;
    public static final int h = 4;
    public static final int i = 16;

    private static byte a(int i2) {
        if (i2 > 127) {
            i2 = 127;
        } else if (i2 < -128) {
            i2 = -128;
        }
        return (byte) i2;
    }

    private static int a(byte[] bArr, int i2, int i3) {
        int i4 = i2 + i3;
        int i5 = 0;
        while (i2 < i4) {
            i3--;
            i5 += (bArr[i2] & 255) << (i3 * 8);
            i2++;
        }
        return i5;
    }

    public static String a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        byte b2 = bArr[0];
        sb.append("\n He10Bytes length:" + bArr.length + ",event count:" + ((int) b2));
        int i3 = 1;
        while (i2 < b2) {
            byte b3 = bArr[i3];
            StringBuilder sb2 = new StringBuilder();
            sb2.append("\n ");
            i2++;
            sb2.append(i2);
            sb2.append(":\t Type:");
            sb2.append((int) b3);
            sb.append(sb2.toString());
            if (16 == b3) {
                sb.append("\t RelativeTime:" + a(bArr, i3 + 1, 2) + "\t Frequency:" + ((int) bArr[i3 + 3]) + "\t Intensity:" + ((int) bArr[i3 + 4]));
                i3 += 5;
            } else if (17 == b3) {
                sb.append("\t RelativeTime:" + a(bArr, i3 + 1, 2) + "\t Duration:" + a(bArr, i3 + 3, 2) + "\t Frequency0:" + ((int) bArr[i3 + 5]) + "\t Time1:" + a(bArr, i3 + 6, 2) + "\t Frequency1:" + ((int) bArr[i3 + 8]) + "\t Intensity1:" + ((int) bArr[i3 + 9]) + "\t Time2:" + a(bArr, i3 + 10, 2) + "\t Frequency2:" + ((int) bArr[i3 + 12]) + "\t Intensity2:" + ((int) bArr[i3 + 13]) + "\t Frequency3:" + ((int) bArr[i3 + 14]));
                i3 += 15;
            }
        }
        return sb.toString();
    }

    private static byte[] a(int i2, int i3) {
        byte[] bArr = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[(i3 - i4) - 1] = (byte) ((i2 >> (i4 * 8)) & 255);
        }
        return bArr;
    }

    private static byte[] a(com.sysaac.haptic.b.b.a aVar) {
        String str;
        if (aVar == null || aVar.b == null || aVar.b.size() < 1 || aVar.b.size() > 16) {
            return null;
        }
        byte[] bArr = new byte[241];
        bArr[0] = (byte) aVar.b.size();
        Iterator<e> it = aVar.b.iterator();
        int i2 = 1;
        while (it.hasNext()) {
            e next = it.next();
            if (next.a == null || next.a.e == null) {
                str = "null == patternItem.Event or null == patternItem.Event.Parameters";
            } else if ("transient".equals(next.a.a)) {
                bArr[i2] = 16;
                System.arraycopy(a(next.a.b, 2), 0, bArr, i2 + 1, 2);
                bArr[i2 + 3] = (byte) next.a.e.b;
                bArr[i2 + 4] = (byte) next.a.e.a;
                i2 += 5;
            } else if ("continuous".equals(next.a.a)) {
                bArr[i2] = 17;
                System.arraycopy(a(next.a.b, 2), 0, bArr, i2 + 1, 2);
                System.arraycopy(a(next.a.c, 2), 0, bArr, i2 + 3, 2);
                if (next.a.e.c == null || 4 != next.a.e.c.size()) {
                    str = "null == patternItem.Event.Parameters.Curve or POINT_COUNT != patternItem.Event.Parameters.Curve.size()";
                } else {
                    bArr[i2 + 5] = a(next.a.e.c.get(0).c + next.a.e.b);
                    System.arraycopy(a(next.a.e.c.get(1).a, 2), 0, bArr, i2 + 6, 2);
                    bArr[i2 + 8] = a(next.a.e.c.get(1).c + next.a.e.b);
                    bArr[i2 + 9] = (byte) (next.a.e.c.get(1).b * next.a.e.a);
                    System.arraycopy(a(next.a.e.c.get(2).a, 2), 0, bArr, i2 + 10, 2);
                    bArr[i2 + 12] = a(next.a.e.c.get(2).c + next.a.e.b);
                    bArr[i2 + 13] = (byte) (next.a.e.c.get(2).b * next.a.e.a);
                    bArr[i2 + 14] = a(next.a.e.c.get(3).c + next.a.e.b);
                    i2 += 15;
                }
            } else {
                str = "unknown event type.";
            }
            Log.w(a, str);
        }
        byte[] bArr2 = (byte[]) Array.newInstance(Byte.TYPE, i2);
        System.arraycopy(bArr, 0, bArr2, 0, i2);
        return bArr2;
    }

    public static byte[] a(File file) {
        return a(r.b(file));
    }

    public static byte[] a(String str) {
        if (1 != r.e(str)) {
            return null;
        }
        return a(r.f(str));
    }
}
