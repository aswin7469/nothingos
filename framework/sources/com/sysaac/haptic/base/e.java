package com.sysaac.haptic.base;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class e extends d {
    public static AtomicInteger a = new AtomicInteger();
    private static final String b = "PatternHeImpl";
    private final Vibrator c;
    private final boolean d = false;
    private Class<?> e;
    private Context f;

    public e(Context context) {
        this.f = context;
        this.c = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            this.e = Class.forName("android.os.RichTapVibrationEffect");
        } catch (ClassNotFoundException e) {
            Log.i(b, "failed to reflect class: \"android.os.RichTapVibrationEffect\"!");
        }
        if (this.e == null) {
            try {
                this.e = Class.forName("android.os.VibrationEffect");
            } catch (ClassNotFoundException e2) {
                Log.i(b, "failed to reflect class: \"android.os.VibrationEffect\"!");
            }
        }
    }

    private int[] a(JSONArray jSONArray) {
        int[] iArr;
        String str;
        int i;
        if (jSONArray == null) {
            return null;
        }
        int[] iArr2 = new int[12];
        try {
            int length = jSONArray.length();
            int i2 = 5000;
            double d = 100.0d;
            String str2 = "Intensity";
            try {
                if (length == 4) {
                    int i3 = 0;
                    while (i3 < length) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i3);
                        int i4 = jSONObject.getInt("Time");
                        int[] iArr3 = iArr2;
                        int i5 = (int) (jSONObject.getDouble(str2) * 100.0d);
                        int i6 = jSONObject.getInt("Frequency");
                        if (c(i4, 0, i2) && c(i5, 0, 100) && c(i6, -100, 100)) {
                            int i7 = i3 * 3;
                            iArr3[i7] = i4;
                            iArr3[i7 + 1] = i5;
                            iArr3[i7 + 2] = i6;
                            i3++;
                            iArr2 = iArr3;
                            i2 = 5000;
                        }
                        Log.e(b, "point's time must be less than 5000, intensity must between 0~1, frequency must between -100 and 100");
                        return null;
                    }
                    return iArr2;
                } else if (length <= 4 || length > 16) {
                    return null;
                } else {
                    int i8 = 0;
                    while (i8 < length) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i8);
                        int i9 = jSONObject2.getInt("Time");
                        int i10 = (int) (jSONObject2.getDouble(str2) * d);
                        int i11 = jSONObject2.getInt("Frequency");
                        if (c(i9, 0, 5000) && c(i10, 0, 100) && c(i11, -100, 100)) {
                            if (i8 == 0) {
                                int i12 = i8 * 3;
                                iArr2[i12] = i9;
                                iArr2[i12 + 1] = i10;
                                iArr2[i12 + 2] = i11;
                                str = str2;
                            } else {
                                int i13 = length - 1;
                                if (i8 < i13) {
                                    str = str2;
                                    double d2 = length / 2.0d;
                                    int i14 = 1;
                                    if (i8 < Math.ceil(d2)) {
                                        i = (int) (Math.ceil(d2) - 1.0d);
                                    } else {
                                        i = (length / 2) - 1;
                                        i14 = 2;
                                    }
                                    int i15 = i14 * 3;
                                    iArr2[i15] = iArr2[i15] + (i9 / i);
                                    int i16 = i15 + 1;
                                    iArr2[i16] = iArr2[i16] + (i10 / i);
                                    int i17 = i15 + 2;
                                    iArr2[i17] = iArr2[i17] + (i11 / i);
                                } else {
                                    str = str2;
                                    if (i8 == i13) {
                                        iArr2[9] = i9;
                                        iArr2[10] = i10;
                                        iArr2[11] = i11;
                                    }
                                }
                            }
                            i8++;
                            str2 = str;
                            d = 100.0d;
                        }
                        Log.e(b, "point's time must be less than 5000, intensity must between 0~1, frequency must between -100 and 100");
                        return null;
                    }
                    return iArr2;
                }
            } catch (Exception e) {
                e = e;
                iArr = null;
                e.printStackTrace();
                return iArr;
            }
        } catch (Exception e2) {
            e = e2;
            iArr = null;
        }
    }

    private boolean b(int i, int i2, int i3) {
        StringBuilder sb;
        String format;
        if (i >= 22) {
            if (i == 22) {
                if (i3 != 1) {
                    sb = new StringBuilder();
                    sb.append("RichTap version is ");
                    format = String.format("%x", Integer.valueOf(i));
                    sb.append(format);
                    sb.append(" can not support he version: ");
                    sb.append(i3);
                }
                return true;
            }
            if (i == 23) {
                if (i3 != 1) {
                    sb = new StringBuilder();
                    sb.append("RichTap version is ");
                    format = String.format("%x", Integer.valueOf(i));
                    sb.append(format);
                    sb.append(" can not support he version: ");
                    sb.append(i3);
                }
            } else if (i == 24 && i3 != 1 && i3 != 2) {
                return false;
            }
            return true;
        }
        sb = new StringBuilder();
        sb.append("can not support he in richtap version:");
        sb.append(String.format("%x02", Integer.valueOf(i)));
        Log.e(b, sb.toString());
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00e9 A[Catch: Exception -> 0x0146, TryCatch #1 {Exception -> 0x0146, blocks: (B:8:0x0028, B:11:0x004d, B:14:0x0055, B:65:0x0078, B:49:0x0089, B:38:0x014c, B:41:0x0153, B:43:0x015f, B:44:0x0171, B:46:0x0175, B:16:0x008f, B:18:0x00ab, B:20:0x00b2, B:22:0x00d3, B:24:0x00e9, B:26:0x00ef, B:27:0x0111, B:29:0x011e, B:31:0x012e, B:50:0x010c, B:33:0x0136, B:57:0x00bf, B:59:0x00c6, B:66:0x0072, B:67:0x0043, B:71:0x013f), top: B:7:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0136 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int[] b(String str) {
        int[] iArr;
        boolean z;
        String str2;
        int i;
        int i2;
        String str3;
        String str4 = "RelativeTime";
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("Pattern");
            int min = Math.min(jSONArray.length(), 16);
            int[] iArr2 = new int[min * 17];
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (i3 < min) {
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i3).getJSONObject("Event");
                    String string = jSONObject.getString("Type");
                    if (!TextUtils.equals("continuous", string)) {
                        if (!TextUtils.equals("transient", string)) {
                            Log.e(b, "haven't get type value");
                            z = false;
                            break;
                        }
                        i = 4097;
                    } else {
                        i = 4096;
                    }
                    if (!jSONObject.has(str4)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("event:");
                        sb.append(i3);
                        sb.append(" don't have relativeTime parameters,set default:");
                        i2 = i3 * 400;
                        sb.append(i2);
                        Log.e(b, sb.toString());
                    } else {
                        i2 = jSONObject.getInt(str4);
                    }
                    if (i2 >= 0) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("Parameters");
                        int i6 = jSONObject2.getInt("Intensity");
                        int i7 = jSONObject2.getInt("Frequency");
                        String str5 = str4;
                        JSONArray jSONArray2 = jSONArray;
                        if (i == 4096) {
                            if (!c(i6, 0, 100) || !c(i7, 0, 100)) {
                                str3 = "intensity or frequency is out of [0,100] for continuous event!";
                            }
                            int i8 = i3 * 17;
                            iArr2[i8 + 0] = i;
                            iArr2[i8 + 1] = i2;
                            iArr2[i8 + 2] = i6;
                            iArr2[i8 + 3] = i7;
                            if (4096 == i) {
                                if (!jSONObject.has("Duration")) {
                                    Log.e(b, "event:" + i3 + " don't have duration parameters,set default:0");
                                    i5 = 0;
                                } else {
                                    i5 = jSONObject.getInt("Duration");
                                }
                                if (!c(i5, 0, 5000)) {
                                    str3 = "duration must be less than 5000";
                                } else {
                                    iArr2[i8 + 4] = i5;
                                    int[] a2 = a(jSONObject2.getJSONArray("Curve"));
                                    if (a2 != null) {
                                        System.arraycopy(a2, 0, iArr2, i8 + 5, 12);
                                    }
                                }
                            }
                            i3++;
                            i4 = i2;
                            str4 = str5;
                            jSONArray = jSONArray2;
                        } else {
                            if (i == 4097 && (!c(i6, 0, 100) || !c(i7, -50, 150))) {
                                str3 = "intensity out of [0, 100] or frequency out of [-50, 150] for transient event!";
                            }
                            int i82 = i3 * 17;
                            iArr2[i82 + 0] = i;
                            iArr2[i82 + 1] = i2;
                            iArr2[i82 + 2] = i6;
                            iArr2[i82 + 3] = i7;
                            if (4096 == i) {
                            }
                            i3++;
                            i4 = i2;
                            str4 = str5;
                            jSONArray = jSONArray2;
                        }
                        i4 = i2;
                        z = false;
                        break;
                    }
                    str3 = "relativeTime:" + i2;
                    Log.e(b, str3);
                    i4 = i2;
                    z = false;
                    break;
                } catch (Exception e) {
                    e = e;
                    iArr = iArr2;
                    e.printStackTrace();
                    return iArr;
                }
            }
            z = true;
            if (!z) {
                Log.e(b, "current he file data, isn't compliance!!!!!!!");
                return null;
            }
            if (4096 == iArr2[((min - 1) * 17) + 0]) {
                str2 = "last event type is continuous, totalDuration:" + (i4 + i5);
            } else {
                str2 = "last event type is transient, totalDuration:" + (i4 + 48);
            }
            Log.d(b, str2);
            return iArr2;
        } catch (Exception e2) {
            e = e2;
            iArr = null;
        }
    }

    private int[] b(JSONArray jSONArray) {
        if (jSONArray == null) {
            return null;
        }
        int[] iArr = new int[48];
        for (int i = 0; i < 48; i++) {
            iArr[i] = -1;
        }
        try {
            int length = jSONArray.length();
            if (length < 4) {
                return null;
            }
            int min = Math.min(length, 16);
            for (int i2 = 0; i2 < min; i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                int i3 = jSONObject.getInt("Time");
                int i4 = (int) (jSONObject.getDouble("Intensity") * 100.0d);
                int i5 = jSONObject.getInt("Frequency");
                if (c(i3, 0, 5000) && c(i4, 0, 100) && c(i5, -100, 100)) {
                    int i6 = i2 * 3;
                    iArr[i6 + 0] = i3;
                    iArr[i6 + 1] = i4;
                    iArr[i6 + 2] = i5;
                }
                Log.e(b, "point's time must be less than 5000, intensity must between 0~1, frequency must between -100 and 100");
                return null;
            }
            return iArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean c(int i, int i2, int i3) {
        return i >= i2 && i <= i3;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00ee A[Catch: Exception -> 0x0153, TryCatch #1 {Exception -> 0x0153, blocks: (B:8:0x0028, B:11:0x004d, B:14:0x0055, B:65:0x0078, B:49:0x0089, B:38:0x0159, B:41:0x0160, B:43:0x016c, B:44:0x017e, B:46:0x0182, B:16:0x008f, B:18:0x00ab, B:20:0x00b2, B:22:0x00d3, B:24:0x00ee, B:26:0x00f4, B:27:0x0116, B:29:0x0123, B:31:0x013b, B:50:0x0111, B:33:0x0143, B:57:0x00bf, B:59:0x00c6, B:66:0x0072, B:67:0x0043, B:71:0x014c), top: B:7:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0143 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int[] c(String str) {
        int[] iArr;
        boolean z;
        String str2;
        int i;
        int i2;
        String str3;
        String str4 = "RelativeTime";
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("Pattern");
            int min = Math.min(jSONArray.length(), 16);
            int[] iArr2 = new int[min * 55];
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (i3 < min) {
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i3).getJSONObject("Event");
                    String string = jSONObject.getString("Type");
                    if (!TextUtils.equals("continuous", string)) {
                        if (!TextUtils.equals("transient", string)) {
                            Log.e(b, "haven't get type value");
                            z = false;
                            break;
                        }
                        i = 4097;
                    } else {
                        i = 4096;
                    }
                    if (!jSONObject.has(str4)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("event:");
                        sb.append(i3);
                        sb.append(" don't have relativeTime parameters,set default:");
                        i2 = i3 * 400;
                        sb.append(i2);
                        Log.e(b, sb.toString());
                    } else {
                        i2 = jSONObject.getInt(str4);
                    }
                    if (i2 >= 0) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("Parameters");
                        int i6 = jSONObject2.getInt("Intensity");
                        int i7 = jSONObject2.getInt("Frequency");
                        String str5 = str4;
                        JSONArray jSONArray2 = jSONArray;
                        if (i == 4096) {
                            if (!c(i6, 0, 100) || !c(i7, 0, 100)) {
                                str3 = "intensity or frequency is out of [0,100] for continuous event!";
                            }
                            int i8 = i3 * 55;
                            iArr2[i8 + 0] = i;
                            iArr2[i8 + 1] = i2;
                            iArr2[i8 + 2] = i6;
                            iArr2[i8 + 3] = i7;
                            iArr2[i8 + 5] = 0;
                            if (4096 == i) {
                                if (!jSONObject.has("Duration")) {
                                    Log.e(b, "event:" + i3 + " don't have duration parameters,set default:0");
                                    i5 = 0;
                                } else {
                                    i5 = jSONObject.getInt("Duration");
                                }
                                if (!c(i5, 0, 5000)) {
                                    str3 = "duration must be less than 5000";
                                } else {
                                    iArr2[i8 + 4] = i5;
                                    JSONArray jSONArray3 = jSONObject2.getJSONArray("Curve");
                                    iArr2[i8 + 6] = jSONArray3.length();
                                    int[] b2 = b(jSONArray3);
                                    if (b2 != null) {
                                        System.arraycopy(b2, 0, iArr2, i8 + 7, 48);
                                    }
                                }
                            }
                            i3++;
                            i4 = i2;
                            str4 = str5;
                            jSONArray = jSONArray2;
                        } else {
                            if (i == 4097 && (!c(i6, 0, 100) || !c(i7, -50, 150))) {
                                str3 = "intensity out of [0, 100] or frequency out of [-50, 150] for transient event!";
                            }
                            int i82 = i3 * 55;
                            iArr2[i82 + 0] = i;
                            iArr2[i82 + 1] = i2;
                            iArr2[i82 + 2] = i6;
                            iArr2[i82 + 3] = i7;
                            iArr2[i82 + 5] = 0;
                            if (4096 == i) {
                            }
                            i3++;
                            i4 = i2;
                            str4 = str5;
                            jSONArray = jSONArray2;
                        }
                        i4 = i2;
                        z = false;
                        break;
                    }
                    str3 = "relativeTime :" + i2;
                    Log.e(b, str3);
                    i4 = i2;
                    z = false;
                    break;
                } catch (Exception e) {
                    e = e;
                    iArr = iArr2;
                    e.printStackTrace();
                    return iArr;
                }
            }
            z = true;
            if (!z) {
                Log.e(b, "current he file data, isn't compliance!!!!!!!");
                return null;
            }
            if (4096 == iArr2[((min - 1) * 55) + 0]) {
                str2 = "last event type is continuous, totalDuration:" + (i4 + i5);
            } else {
                str2 = "last event type is transient, totalDuration:" + (i4 + 80);
            }
            Log.d(b, str2);
            return iArr2;
        } catch (Exception e2) {
            e = e2;
            iArr = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:114:0x019e, code lost:
        r2 = "intensity out of [0, 100] or frequency out of [-50, 150] for transient event!";
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x02a4  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01bd A[Catch: Exception -> 0x030e, TryCatch #1 {Exception -> 0x030e, blocks: (B:35:0x00e1, B:119:0x00f0, B:38:0x0109, B:41:0x0111, B:117:0x0134, B:89:0x02f1, B:86:0x02f8, B:43:0x0153, B:45:0x016d, B:47:0x0174, B:49:0x01a1, B:51:0x01bd, B:99:0x01c3, B:53:0x01dc, B:97:0x01e9, B:55:0x01f9, B:56:0x021d, B:58:0x0223, B:62:0x024b, B:67:0x0256, B:70:0x025d, B:94:0x0279, B:76:0x0294, B:78:0x02ae, B:82:0x02ba, B:92:0x029d, B:104:0x017c, B:109:0x018d, B:111:0x0194, B:127:0x02ce), top: B:34:0x00e1 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02ba A[Catch: Exception -> 0x030e, LOOP:1: B:21:0x0085->B:82:0x02ba, LOOP_END, TryCatch #1 {Exception -> 0x030e, blocks: (B:35:0x00e1, B:119:0x00f0, B:38:0x0109, B:41:0x0111, B:117:0x0134, B:89:0x02f1, B:86:0x02f8, B:43:0x0153, B:45:0x016d, B:47:0x0174, B:49:0x01a1, B:51:0x01bd, B:99:0x01c3, B:53:0x01dc, B:97:0x01e9, B:55:0x01f9, B:56:0x021d, B:58:0x0223, B:62:0x024b, B:67:0x0256, B:70:0x025d, B:94:0x0279, B:76:0x0294, B:78:0x02ae, B:82:0x02ba, B:92:0x029d, B:104:0x017c, B:109:0x018d, B:111:0x0194, B:127:0x02ce), top: B:34:0x00e1 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x02b8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private i[] d(String str) {
        i[] iVarArr;
        JSONArray jSONArray;
        int length;
        i[] iVarArr2;
        int i;
        int i2;
        int i3;
        boolean z;
        i[] iVarArr3;
        String str2;
        String str3;
        String str4;
        int i4;
        String str5;
        int i5;
        int i6;
        String str6;
        int i7;
        String str7 = "Duration";
        String str8 = "Frequency";
        String str9 = "Intensity";
        String str10 = "RelativeTime";
        try {
            jSONArray = new JSONObject(str).getJSONArray("PatternList");
            length = jSONArray.length();
            byte[] bArr = new byte[length * 64];
            iVarArr2 = new i[length];
            i = 0;
            i2 = 0;
            i3 = 0;
            z = true;
        } catch (Exception e) {
            e = e;
            iVarArr = null;
        }
        while (i < length) {
            try {
                i iVar = new i(this);
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                int i8 = jSONObject.getInt("AbsoluteTime");
                iVar.a = i8;
                int i9 = i2 + i3;
                JSONArray jSONArray2 = jSONArray;
                if (i > 0 && i8 < i9) {
                    Log.e(b, "Bad pattern relative time in int:" + i + ",last patternDuration:" + i2 + ", absTimeLast:" + i3);
                    return null;
                }
                JSONArray jSONArray3 = jSONObject.getJSONArray("Pattern");
                int min = Math.min(16, jSONArray3.length());
                iVar.b = new g[min];
                int i10 = length;
                int i11 = 0;
                int i12 = -1;
                int i13 = 0;
                while (true) {
                    if (i13 >= min) {
                        str2 = str7;
                        str3 = str9;
                        str4 = str10;
                        iVarArr3 = iVarArr2;
                        i4 = i8;
                        str5 = str8;
                        i2 = i11;
                        break;
                    }
                    i4 = i8;
                    JSONArray jSONArray4 = jSONArray3;
                    JSONObject jSONObject2 = jSONArray3.getJSONObject(i13).getJSONObject("Event");
                    String string = jSONObject2.getString("Type");
                    int i14 = min;
                    boolean z2 = z;
                    if (!TextUtils.equals("continuous", string)) {
                        if (!TextUtils.equals("transient", string)) {
                            str2 = str7;
                            str3 = str9;
                            str4 = str10;
                            iVarArr3 = iVarArr2;
                            str5 = str8;
                            Log.e(b, "haven't get type value");
                            i2 = i11;
                            break;
                        }
                        iVar.b[i13] = new m(this);
                        i5 = 4097;
                    } else {
                        try {
                            iVar.b[i13] = new f(this);
                            i5 = 4096;
                        } catch (Exception e2) {
                            e = e2;
                            iVarArr = iVarArr2;
                        }
                    }
                    int i15 = jSONObject2.getInt("Index");
                    iVarArr3 = iVarArr2;
                    if (r.a(this.f)) {
                        if (1 == i15) {
                            i15 = 2;
                        } else if (2 == i15) {
                            i15 = 1;
                        }
                    }
                    try {
                        iVar.b[i13].f = (byte) i15;
                        if (!jSONObject2.has(str10)) {
                            Log.e(b, "event:" + i + " don't have relativeTime parameters,BAD he!");
                            return null;
                        }
                        int i16 = jSONObject2.getInt(str10);
                        if (i13 > 0 && i16 < i12) {
                            Log.w(b, "pattern ind:" + i + " event:" + i13 + " relative time seems not right!");
                        }
                        if (i16 < 0) {
                            Log.e(b, "relativeTime:" + i16);
                            str2 = str7;
                            str3 = str9;
                            str4 = str10;
                            i2 = i11;
                            z = false;
                            str5 = str8;
                            break;
                        }
                        JSONObject jSONObject3 = jSONObject2.getJSONObject("Parameters");
                        int i17 = jSONObject3.getInt(str9);
                        str4 = str10;
                        int i18 = jSONObject3.getInt(str8);
                        i6 = i11;
                        str6 = str8;
                        if (i5 == 4096) {
                            if (!c(i17, 0, 100) || !c(i18, 0, 100)) {
                                break;
                            }
                            iVar.b[i13].d = i5;
                            iVar.b[i13].g = i16;
                            iVar.b[i13].h = i17;
                            iVar.b[i13].i = i18;
                            if (4096 == i5) {
                                str2 = str7;
                                str3 = str9;
                                str5 = str6;
                                i7 = 48;
                            } else if (!jSONObject2.has(str7)) {
                                Log.e(b, "event:" + i + " don't have duration parameters");
                                return null;
                            } else {
                                i7 = jSONObject2.getInt(str7);
                                if (!c(i7, 0, 5000)) {
                                    Log.e(b, "duration must be less than 5000");
                                    str2 = str7;
                                    str3 = str9;
                                    z = false;
                                    i2 = i6;
                                    str5 = str6;
                                    break;
                                }
                                iVar.b[i13].j = i7;
                                JSONArray jSONArray5 = jSONObject3.getJSONArray("Curve");
                                ((f) iVar.b[i13]).a = (byte) jSONArray5.length();
                                int length2 = jSONArray5.length();
                                l[] lVarArr = new l[length2];
                                str2 = str7;
                                int i19 = 0;
                                int i20 = 0;
                                int i21 = -1;
                                while (i19 < jSONArray5.length()) {
                                    JSONObject jSONObject4 = jSONArray5.getJSONObject(i19);
                                    lVarArr[i19] = new l(this);
                                    i20 = jSONObject4.getInt("Time");
                                    String str11 = str9;
                                    JSONArray jSONArray6 = jSONArray5;
                                    int i22 = (int) (jSONObject4.getDouble(str9) * 100.0d);
                                    String str12 = str6;
                                    int i23 = jSONObject4.getInt(str12);
                                    if (i19 == 0 && i20 != 0) {
                                        Log.d(b, "time of first point is not 0,bad he!");
                                        return null;
                                    } else if (i19 > 0 && i20 < i21) {
                                        Log.d(b, "point times did not arrange in order,bad he!");
                                        return null;
                                    } else {
                                        lVarArr[i19].a = i20;
                                        lVarArr[i19].b = i22;
                                        lVarArr[i19].c = i23;
                                        i19++;
                                        str6 = str12;
                                        i21 = i20;
                                        str9 = str11;
                                        jSONArray5 = jSONArray6;
                                    }
                                }
                                str3 = str9;
                                str5 = str6;
                                if (i20 != i7) {
                                    Log.e(b, "event:" + i + " point last time do not match duration parameter");
                                    return null;
                                } else if (length2 > 0) {
                                    ((f) iVar.b[i13]).b = lVarArr;
                                } else {
                                    Log.d(b, "continuous event has nothing in point");
                                    z = false;
                                    int i24 = i7 + i16;
                                    i11 = i6 < i24 ? i24 : i6;
                                    if (!z) {
                                        i2 = i11;
                                        break;
                                    }
                                    i13++;
                                    str8 = str5;
                                    i12 = i16;
                                    i8 = i4;
                                    jSONArray3 = jSONArray4;
                                    min = i14;
                                    str7 = str2;
                                    str9 = str3;
                                    iVarArr2 = iVarArr3;
                                    str10 = str4;
                                }
                            }
                            z = z2;
                            int i242 = i7 + i16;
                            if (i6 < i242) {
                            }
                            if (!z) {
                            }
                        } else {
                            if (i5 == 4097 && (!c(i17, 0, 100) || !c(i18, -50, 150))) {
                                break;
                            }
                            iVar.b[i13].d = i5;
                            iVar.b[i13].g = i16;
                            iVar.b[i13].h = i17;
                            iVar.b[i13].i = i18;
                            if (4096 == i5) {
                            }
                            z = z2;
                            int i2422 = i7 + i16;
                            if (i6 < i2422) {
                            }
                            if (!z) {
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        iVarArr = iVarArr3;
                        e.printStackTrace();
                        return iVarArr;
                    }
                }
                String str13 = "intensity or frequency is out of [0,100] for continuous event!";
                Log.e(b, str13);
                str2 = str7;
                str3 = str9;
                i2 = i6;
                str5 = str6;
                z = false;
                if (!z) {
                    Log.e(b, "current he file data, isn't compliance!!!!!!!");
                    return null;
                }
                iVarArr3[i] = iVar;
                i++;
                str8 = str5;
                jSONArray = jSONArray2;
                length = i10;
                i3 = i4;
                str7 = str2;
                str9 = str3;
                iVarArr2 = iVarArr3;
                str10 = str4;
                e = e2;
                iVarArr = iVarArr2;
            } catch (Exception e4) {
                e = e4;
                iVarArr3 = iVarArr2;
            }
            e.printStackTrace();
            return iVarArr;
        }
        return iVarArr2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b3, code lost:
        android.util.Log.e(com.sysaac.haptic.base.e.b, "haven't get type value");
     */
    @Override // com.sysaac.haptic.base.d
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int a(String str) {
        int i;
        int a2;
        Log.d(b, "getNonRichTapPatternDuration");
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("Pattern");
            int min = Math.min(jSONArray.length(), 16);
            int i2 = min * 2;
            long[] jArr = new long[i2];
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (true) {
                if (i3 >= min) {
                    break;
                }
                JSONObject jSONObject = jSONArray.getJSONObject(i3).getJSONObject("Event");
                String string = jSONObject.getString("Type");
                if (!TextUtils.equals("continuous", string)) {
                    if (!TextUtils.equals("transient", string)) {
                        break;
                    }
                    int i6 = i3 * 2;
                    jArr[i6] = (jSONObject.getInt("RelativeTime") - i4) - i5;
                    if (jArr[i6] < 0) {
                        jArr[i6] = 50;
                    }
                    JSONObject jSONObject2 = jSONObject.getJSONObject("Parameters");
                    a2 = a(jSONObject2.getInt("Intensity"), jSONObject2.getInt("Frequency"));
                    jArr[i6 + 1] = a2;
                } else {
                    int i7 = i3 * 2;
                    jArr[i7] = (jSONObject.getInt("RelativeTime") - i4) - i5;
                    if (jArr[i7] < 0) {
                        jArr[i7] = 50;
                    }
                    a2 = jSONObject.getInt("Duration");
                    if (a2 > 50 && a2 < 100) {
                        a2 = 50;
                    } else if (a2 > 100) {
                        a2 -= 50;
                    }
                    jArr[i7 + 1] = a2;
                }
                i5 = a2;
                i4 = jSONObject.getInt("RelativeTime");
                i3++;
            }
            int i8 = 0;
            for (int i9 = 0; i9 < i2; i9++) {
                try {
                    i8 = (int) (i8 + jArr[i9]);
                } catch (Exception e) {
                    e = e;
                    i = i8;
                    e.printStackTrace();
                    return i;
                }
            }
            return i8;
        } catch (Exception e2) {
            e = e2;
            i = 0;
        }
    }

    @Override // com.sysaac.haptic.base.d
    public void a() {
        a(0, 0, 0);
    }

    @Override // com.sysaac.haptic.base.d
    public void a(int i) {
        a(-1, -1, i);
    }

    @Override // com.sysaac.haptic.base.d
    public void a(int i, int i2, int i3) {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                this.c.vibrate((VibrationEffect) this.e.getMethod("createPatternHeParameter", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
            } else {
                Log.e(b, "The system apk is low than 26,does not support richTap!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(b, "The system doesn't integrate richTap software");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [com.sysaac.haptic.base.e] */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.io.BufferedReader] */
    @Override // com.sysaac.haptic.base.d
    public void a(File file, int i, int i2, int i3, int i4) {
        if (!r.a(file.getPath(), r.c)) {
            return;
        }
        Log.d(b, "looper:" + i + " interval:" + i2 + " amplitude:" + i3 + " freq:" + i4);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = 0;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    while (true) {
                        try {
                            String readLine = bufferedReader3.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                        } catch (Exception e) {
                            e = e;
                            bufferedReader2 = bufferedReader3;
                            e.printStackTrace();
                            bufferedReader2.close();
                            bufferedReader = this;
                            bufferedReader.a(sb.toString(), i, i2, i3, i4);
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader3;
                            try {
                                bufferedReader.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            throw th;
                        }
                    }
                    bufferedReader3.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            } catch (Exception e4) {
                e = e4;
            }
            bufferedReader = this;
            bufferedReader.a(sb.toString(), i, i2, i3, i4);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.sysaac.haptic.base.d
    public void a(String str, int i, int i2, int i3, int i4) {
        int i5;
        int a2;
        String str2;
        if (this.c == null) {
            str2 = "Please call the init method";
        } else if (i >= 1) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                boolean c = r.c();
                if (!c) {
                    i5 = jSONObject.getJSONObject(r.X).getInt("Version");
                    if (!b(r.a(), r.b(), i5)) {
                        Log.e(b, "richtap version check failed, richTapMajorVersion:" + String.format("%x02", Integer.valueOf(a2)) + " heVersion:" + i5);
                        return;
                    }
                } else {
                    i5 = 0;
                }
                int[] b2 = r.a() < 24 ? b(str) : c(str);
                if (b2 == null) {
                    Log.e(b, "serialize he failed!! ,heVersion:" + i5);
                    return;
                }
                int length = b2.length;
                try {
                    if (Build.VERSION.SDK_INT < 26) {
                        Log.e(b, "The system is low than 26,does not support richTap!!");
                        return;
                    }
                    Method method = this.e.getMethod("createPatternHeWithParam", int[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                    if (!c) {
                        int[] iArr = new int[length + 1];
                        iArr[0] = r.a() < 24 ? 1 : 3;
                        System.arraycopy(b2, 0, iArr, 1, b2.length);
                        b2 = iArr;
                    }
                    this.c.vibrate((VibrationEffect) method.invoke(null, b2, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w(b, "for createPatternHe, The system doesn't integrate richTap software");
                    return;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        } else {
            str2 = "The minimum count of loop pattern is 1";
        }
        Log.e(b, str2);
    }

    int[] a(int i, int i2, int i3, int i4, int i5, int i6, i[] iVarArr) {
        int i7 = 0;
        for (int i8 = 0; i8 < i3; i8++) {
            i7 += iVarArr[i8].b();
        }
        int i9 = 5;
        int[] iArr = new int[i7 + 5];
        Arrays.fill(iArr, 0);
        iArr[0] = i;
        iArr[1] = i2;
        iArr[2] = i4;
        iArr[3] = i5;
        iArr[4] = iArr[4] | (65535 & i3);
        iArr[4] = ((iVarArr.length << 16) & (-65536)) | iArr[4];
        for (int i10 = 0; i10 < i3; i10++) {
            int[] a2 = iVarArr[i10].a(i6);
            System.arraycopy(a2, 0, iArr, i9, a2.length);
            i9 += a2.length;
            i6++;
        }
        return iArr;
    }

    @Override // com.sysaac.haptic.base.d
    public void b(int i) {
        a(i, -1, -1);
    }

    @Override // com.sysaac.haptic.base.d
    public void b(int i, int i2) {
        int[] a2 = new k(4097, 0, 0, i, i2).a();
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                this.c.vibrate((VibrationEffect) this.e.getMethod("createPatternHeWithParam", int[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, a2, 1, 0, 255, 0));
            } else {
                Log.e(b, "The system is low than 26,does not support richTap!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(b, "The system doesn't integrate richTap software");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [com.sysaac.haptic.base.e] */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.io.BufferedReader] */
    @Override // com.sysaac.haptic.base.d
    public void b(File file, int i, int i2, int i3, int i4) {
        if (!r.a(file.getPath(), r.c)) {
            return;
        }
        Log.d(b, "looper:" + i + " interval:" + i2 + " amplitude:" + i3 + " freq:" + i4);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = 0;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    while (true) {
                        try {
                            String readLine = bufferedReader3.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                        } catch (Exception e) {
                            e = e;
                            bufferedReader2 = bufferedReader3;
                            e.printStackTrace();
                            bufferedReader2.close();
                            bufferedReader = this;
                            bufferedReader.c(sb.toString(), i, i2, i3, i4);
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader3;
                            try {
                                bufferedReader.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            throw th;
                        }
                    }
                    bufferedReader3.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            } catch (Exception e4) {
                e = e4;
            }
            bufferedReader = this;
            bufferedReader.c(sb.toString(), i, i2, i3, i4);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.sysaac.haptic.base.d
    public void b(String str, int i, int i2, int i3, int i4) {
        int i5;
        int a2;
        String str2;
        if (this.c == null) {
            str2 = "Please call the init method";
        } else {
            Log.d(b, "play new he api applyPatternHeWithString(String patternString, int loop,int interval,int amplitude,int freq)");
            if (i >= 1) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (!r.c()) {
                        int i6 = jSONObject.getJSONObject(r.X).getInt("Version");
                        if (!b(r.a(), r.b(), i6)) {
                            Log.e(b, "richtap version check failed, richTapMajorVersion:" + String.format("%x02", Integer.valueOf(a2)) + " heVersion:" + i6);
                            return;
                        }
                        i5 = i6;
                    } else {
                        i5 = 0;
                    }
                    i[] d = d(str);
                    if (d != null && d.length != 0) {
                        int andIncrement = a.getAndIncrement();
                        int myPid = Process.myPid();
                        int i7 = 0;
                        int i8 = 0;
                        while (i7 < ((int) Math.ceil(d.length / 4.0d))) {
                            int i9 = i7 + 1;
                            int length = d.length < i9 * 4 ? d.length - (i7 * 4) : 4;
                            i[] iVarArr = new i[length];
                            System.arraycopy(d, i7 * 4, iVarArr, 0, length);
                            int i10 = length;
                            int[] a3 = a(2, i5, length, myPid, andIncrement, i8, iVarArr);
                            try {
                                if (Build.VERSION.SDK_INT >= 26) {
                                    this.c.vibrate((VibrationEffect) this.e.getMethod("createPatternHeWithParam", int[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, a3, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)));
                                } else {
                                    Log.e(b, "The system is low than 26,does not support richTap!!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.w(b, "for createPatternHe, The system doesn't integrate richTap software");
                            }
                            i7 = i9;
                            i8 = i10;
                        }
                        return;
                    }
                    Log.e(b, "serialize he failed!!, heVersion:" + i5);
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            str2 = "The minimum count of loop pattern is 1";
        }
        Log.e(b, str2);
    }

    @Override // com.sysaac.haptic.base.d
    public void c(int i) {
        a(-1, i, -1);
    }

    @Override // com.sysaac.haptic.base.d
    public void c(String str, int i, int i2, int i3, int i4) {
        String str2;
        int i5;
        int i6;
        String str3;
        int i7;
        boolean z;
        int i8;
        String str4;
        Vibrator vibrator = this.c;
        String str5 = b;
        if (vibrator == null) {
            str4 = "Please call the init method";
        } else {
            Log.d(str5, "play new he api, applyPatternHeWithStringOnNoRichTap");
            if (i >= 1) {
                try {
                    JSONArray jSONArray = new JSONObject(str).getJSONArray("Pattern");
                    int min = Math.min(jSONArray.length(), 16);
                    int i9 = min * 2;
                    long[] jArr = new long[i9];
                    int[] iArr = new int[i9];
                    int i10 = 0;
                    Arrays.fill(iArr, 0);
                    int i11 = 0;
                    int i12 = 0;
                    int i13 = 0;
                    while (true) {
                        if (i11 >= min) {
                            str2 = str5;
                            i5 = i9;
                            i6 = i10;
                            break;
                        }
                        JSONObject jSONObject = jSONArray.getJSONObject(i11).getJSONObject("Event");
                        String string = jSONObject.getString("Type");
                        JSONArray jSONArray2 = jSONArray;
                        int i14 = min;
                        i5 = i9;
                        if (!TextUtils.equals("continuous", string)) {
                            str3 = str5;
                            if (!TextUtils.equals("transient", string)) {
                                i6 = 0;
                                str2 = str3;
                                Log.e(str2, "haven't get type value");
                                break;
                            }
                            int i15 = i11 * 2;
                            jArr[i15] = (jSONObject.getInt("RelativeTime") - i12) - i13;
                            if (jArr[i15] < 0) {
                                jArr[i15] = 50;
                            }
                            i7 = 0;
                            iArr[i15] = 0;
                            JSONObject jSONObject2 = jSONObject.getJSONObject("Parameters");
                            int i16 = jSONObject2.getInt("Intensity");
                            int a2 = a(i16, jSONObject2.getInt("Frequency"));
                            int i17 = i15 + 1;
                            jArr[i17] = a2;
                            z = true;
                            iArr[i17] = Math.max(1, Math.min((int) (((i3 * 1.0d) * i16) / 100.0d), 255));
                            i13 = a2;
                            i8 = jSONObject.getInt("RelativeTime");
                        } else {
                            JSONObject jSONObject3 = jSONObject.getJSONObject("Parameters");
                            int[] a3 = a(jSONObject3.getJSONArray("Curve"));
                            int i18 = i11 * 2;
                            jArr[i18] = (jSONObject.getInt("RelativeTime") - i12) - i13;
                            if (jArr[i18] < 0) {
                                jArr[i18] = 50;
                            }
                            iArr[i18] = 0;
                            int i19 = jSONObject.getInt("Duration");
                            if (i19 > 50 && i19 < 100) {
                                i19 = 50;
                            } else if (i19 > 100) {
                                i19 -= 50;
                            }
                            int i20 = i18 + 1;
                            jArr[i20] = i19;
                            int max = Math.max(Math.min((a3[4] * 255) / 100, 255), Math.min((a3[7] * 255) / 100, 255));
                            int i21 = jSONObject3.getInt("Intensity");
                            int i22 = jSONObject3.getInt("Frequency");
                            str3 = str5;
                            int max2 = Math.max(1, (int) (((max * (i21 / 100.0d)) * i3) / 255.0d));
                            if (i22 < 30) {
                                max2 = 0;
                            }
                            iArr[i20] = max2;
                            i8 = jSONObject.getInt("RelativeTime");
                            i13 = i19;
                            z = true;
                            i7 = 0;
                        }
                        i11++;
                        jSONArray = jSONArray2;
                        min = i14;
                        i9 = i5;
                        str5 = str3;
                        int i23 = i7;
                        i12 = i8;
                        i10 = i23;
                    }
                    Log.d(str2, "times:" + Arrays.toString(jArr));
                    int i24 = i5;
                    for (int i25 = i6; i25 < i24; i25++) {
                        long j = jArr[i25];
                    }
                    if (Build.VERSION.SDK_INT >= 26) {
                        this.c.vibrate(VibrationEffect.createWaveform(jArr, iArr, -1));
                        return;
                    } else {
                        this.c.vibrate(jArr, -1);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            str4 = "The minimum count of loop pattern is 1";
        }
        Log.e(str5, str4);
    }

    @Override // com.sysaac.haptic.base.d
    public void d(String str, int i, int i2, int i3, int i4) {
    }
}
