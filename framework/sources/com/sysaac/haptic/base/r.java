package com.sysaac.haptic.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes4.dex */
public class r {
    public static final int A = 0;
    public static final int B = 0;
    public static final int C = 1;
    public static final int D = 2;
    public static final int E = 3;
    public static final int F = 4;
    public static final int G = 5;
    public static final int H = 6;
    public static final int I = 17;
    public static final int J = 55;
    public static final int K = 26;
    public static final int L = 29;
    public static final int M = 65;
    public static final int N = 50;
    public static final int O = 2;
    public static final int P = 1;
    public static final int Q = 255;
    public static final int R = 0;
    public static final int S = 100;
    public static final int T = 30000;
    public static final int U = 17;
    public static final int V = 1;
    public static final int W = 100;
    public static final String X = "Metadata";
    public static final String Y = "Version";
    public static final int Z = 2;
    public static final String a = "{\n    \"Metadata\": {\n        \"Created\": \"2020-08-10\",\n        \"Description\": \"Haptic editor design\",\n        \"Version\": 2\n    },\n    \"PatternList\": [\n       {\n        \"AbsoluteTime\": 0,\n          ReplaceMe\n       }\n    ]\n}";
    public static final int aa = 16;
    private static final String ab = "Util";
    private static final String ac = "swap_left_right";
    private static int ad = 0;
    private static int ae = 0;
    private static boolean af = false;
    private static int ag = 0;
    public static final boolean b = false;
    public static final String c = ".he";
    public static final String d = "Pattern";
    public static final String e = "PatternList";
    public static final String f = "PatternDesc";
    public static final String g = "AbsoluteTime";
    public static final String h = "Index";
    public static final String i = "continuous";
    public static final String j = "transient";
    public static final String k = "Event";
    public static final String l = "RelativeTime";
    public static final String m = "Duration";
    public static final String n = "Type";
    public static final String o = "Parameters";
    public static final String p = "Intensity";
    public static final String q = "Frequency";
    public static final String r = "Curve";
    public static final String s = "Time";
    public static final String t = "Created";
    public static final String u = "Description";
    public static final String v = "Version";
    public static final int w = 16;
    public static final int x = 4096;
    public static final int y = 4097;
    public static final int z = 400;

    public static int a() {
        return ad;
    }

    public static int a(int i2) {
        ad = i2;
        return i2;
    }

    public static long a(byte[] bArr) {
        int i2;
        int length = bArr.length;
        if (length == 1) {
            i2 = bArr[0] & 255;
        } else if (length == 2) {
            i2 = (short) ((bArr[0] & 255) | ((bArr[1] & 255) << 8));
        } else if (length != 4) {
            if (length != 8) {
                return 0L;
            }
            long j2 = bArr[7] & 255;
            long j3 = bArr[6] & 255;
            long j4 = bArr[5] & 255;
            long j5 = bArr[4] & 255;
            return (bArr[0] & 255) | ((bArr[2] & 255) << 16) | ((bArr[3] & 255) << 24) | (j2 << 56) | (j3 << 48) | (j4 << 40) | (j5 << 32) | ((bArr[1] & 255) << 8);
        } else {
            i2 = (bArr[0] & 255) | ((bArr[3] & 255) << 24) | ((bArr[2] & 255) << 16) | ((bArr[1] & 255) << 8);
        }
        return i2;
    }

    public static com.sysaac.haptic.b.a.c a(String str) {
        switch (e(str)) {
            case 1:
                try {
                    return f(str);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return null;
                }
            case 2:
                try {
                    return g(str);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    return null;
                }
            default:
                return null;
        }
    }

    public static String a(int i2, int i3) {
        return "{ \"Metadata\":{\"Created\": \"2021-01-01\",\"Description\": \"Haptic editor design, for getting transient signal\",        \"Version\": 1},\"Pattern\":[{    \"Event\": {       \"Parameters\": {           \"Frequency\": " + i3 + ",\"Intensity\": " + i2 + "       },       \"Type\": \"transient\",       \"RelativeTime\": 0   }}]}";
    }

    public static String a(com.sysaac.haptic.b.b.a aVar) {
        try {
            JSONStringer jSONStringer = new JSONStringer();
            jSONStringer.object();
            jSONStringer.key(X).object().key(t).value(aVar.a.b).key(u).value(aVar.a.c).key("Version").value(aVar.a.a).endObject();
            jSONStringer.key("Pattern").array();
            Iterator<com.sysaac.haptic.b.a.e> it = aVar.b.iterator();
            while (it.hasNext()) {
                com.sysaac.haptic.b.a.e next = it.next();
                jSONStringer.object();
                jSONStringer.key("Event").object().key("Type").value(next.a.a).key("RelativeTime").value(next.a.b);
                if ("continuous".equals(next.a.a)) {
                    jSONStringer.key("Duration").value(next.a.c);
                }
                jSONStringer.key("Parameters").object().key("Frequency").value(next.a.e.b).key("Intensity").value(next.a.e.a);
                if ("continuous".equals(next.a.a)) {
                    jSONStringer.key("Curve").array();
                    Iterator<com.sysaac.haptic.b.a.a> it2 = next.a.e.c.iterator();
                    while (it2.hasNext()) {
                        com.sysaac.haptic.b.a.a next2 = it2.next();
                        jSONStringer.object().key("Frequency").value(next2.c).key("Intensity").value(next2.b).key("Time").value(next2.a).endObject();
                    }
                    jSONStringer.endArray();
                }
                jSONStringer.endObject().endObject().endObject();
            }
            jSONStringer.endArray().endObject();
            return jSONStringer.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String a(com.sysaac.haptic.b.c.a aVar) {
        try {
            JSONStringer jSONStringer = new JSONStringer();
            jSONStringer.object();
            jSONStringer.key(X).object().key(t).value(aVar.a.b).key(u).value(aVar.a.c).key("Version").value(aVar.a.a).endObject();
            jSONStringer.key("PatternList").array();
            Iterator<com.sysaac.haptic.b.c.c> it = aVar.b.iterator();
            while (it.hasNext()) {
                com.sysaac.haptic.b.c.c next = it.next();
                jSONStringer.object().key("AbsoluteTime").value(next.a).key("Pattern").array();
                Iterator<com.sysaac.haptic.b.a.e> it2 = next.b.iterator();
                while (it2.hasNext()) {
                    com.sysaac.haptic.b.a.e next2 = it2.next();
                    jSONStringer.object();
                    jSONStringer.key("Event").object().key("Index").value(next2.a.d).key("RelativeTime").value(next2.a.b).key("Type").value(next2.a.a);
                    if ("continuous".equals(next2.a.a)) {
                        jSONStringer.key("Duration").value(next2.a.c);
                    }
                    jSONStringer.key("Parameters").object().key("Frequency").value(next2.a.e.b).key("Intensity").value(next2.a.e.a);
                    if ("continuous".equals(next2.a.a)) {
                        jSONStringer.key("Curve").array();
                        Iterator<com.sysaac.haptic.b.a.a> it3 = next2.a.e.c.iterator();
                        while (it3.hasNext()) {
                            com.sysaac.haptic.b.a.a next3 = it3.next();
                            jSONStringer.object().key("Frequency").value(next3.c).key("Intensity").value(next3.b).key("Time").value(next3.a).endObject();
                        }
                        jSONStringer.endArray();
                    }
                    jSONStringer.endObject().endObject().endObject();
                }
                jSONStringer.endArray().endObject();
            }
            jSONStringer.endArray().endObject();
            return jSONStringer.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String a(String str, int i2) {
        switch (e(str)) {
            case 1:
                return c(str, i2);
            case 2:
                return d(str, i2);
            default:
                return "";
        }
    }

    private static ArrayList<com.sysaac.haptic.b.a.a> a(ArrayList<com.sysaac.haptic.b.a.a> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        int size = arrayList.size();
        if (size > 0 && size <= 4) {
            return arrayList;
        }
        com.sysaac.haptic.b.a.a aVar = new com.sysaac.haptic.b.a.a();
        int i2 = size - 2;
        int i3 = i2 / 2;
        for (int i4 = 1; i4 <= i3; i4++) {
            aVar.a += arrayList.get(i4).a;
            aVar.b += arrayList.get(i4).b;
            aVar.c += arrayList.get(i4).c;
        }
        aVar.a /= i3;
        aVar.b /= i3;
        aVar.b = Math.round(aVar.b * 10.0d) / 10.0d;
        aVar.c /= i3;
        com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
        for (int i5 = i3 + 1; i5 <= i2; i5++) {
            aVar2.a += arrayList.get(i5).a;
            aVar2.b += arrayList.get(i5).b;
            aVar2.c += arrayList.get(i5).c;
        }
        int i6 = i2 - i3;
        aVar2.a /= i6;
        aVar2.b /= i6;
        aVar2.b = Math.round(aVar2.b * 10.0d) / 10.0d;
        aVar2.c /= i6;
        arrayList.subList(1, size - 1).clear();
        arrayList.add(1, aVar);
        arrayList.add(2, aVar2);
        return arrayList;
    }

    public static void a(String str, ArrayList<Long> arrayList, ArrayList<Integer> arrayList2) {
        Iterator<com.sysaac.haptic.b.a.e> it;
        String str2;
        String str3;
        int i2;
        String str4;
        String str5;
        Iterator<com.sysaac.haptic.b.c.c> it2;
        if (str == null || str.length() == 0 || arrayList == null || arrayList2 == null) {
            Log.e(ab, "convertHeStringToWaveformParams(), invalid parameters.");
            return;
        }
        com.sysaac.haptic.b.a.c a2 = a(str);
        if (!com.sysaac.haptic.player.a.a(a2)) {
            return;
        }
        arrayList.clear();
        arrayList2.clear();
        long j2 = 0;
        arrayList.add(new Long(0L));
        int i3 = 0;
        arrayList2.add(0);
        String str6 = "continuous";
        String str7 = "transient";
        switch (a2.a()) {
            case 1:
                String str8 = str6;
                String str9 = str7;
                Iterator<com.sysaac.haptic.b.a.e> it3 = ((com.sysaac.haptic.b.b.a) a2).b.iterator();
                while (it3.hasNext()) {
                    com.sysaac.haptic.b.a.e next = it3.next();
                    if (next == null || next.a == null || next.a.e == null) {
                        it = it3;
                        str2 = str8;
                        str3 = str9;
                    } else {
                        str3 = str9;
                        if (str3.equals(next.a.a)) {
                            if (next.a.b > j2) {
                                arrayList.add(Long.valueOf(new Long(next.a.b).longValue() - j2));
                                arrayList2.add(0);
                                j2 += arrayList.get(arrayList.size() - 1).longValue();
                            }
                            arrayList.add(new Long(65L));
                            arrayList2.add(Integer.valueOf((int) (((next.a.e.a * 1.0d) / 100.0d) * 255.0d)));
                            j2 += arrayList.get(arrayList.size() - 1).longValue();
                            it = it3;
                            str2 = str8;
                        } else {
                            str2 = str8;
                            if (str2.equals(next.a.a)) {
                                if (next.a.b > j2) {
                                    arrayList.add(Long.valueOf(new Long(next.a.b).longValue() - j2));
                                    i2 = 0;
                                    arrayList2.add(0);
                                    j2 += arrayList.get(arrayList.size() - 1).longValue();
                                } else {
                                    i2 = 0;
                                }
                                if (next.a.e.c != null && 4 <= next.a.e.c.size()) {
                                    int i4 = i2;
                                    while (i4 < next.a.e.c.size() - 2) {
                                        int i5 = i4 + 1;
                                        arrayList.add(new Long(next.a.e.c.get(i5).a - next.a.e.c.get(i4).a));
                                        arrayList2.add(Integer.valueOf((int) (((next.a.e.c.get(i5).b + next.a.e.c.get(i4).b) / 2.0d) * 255.0d)));
                                        j2 += arrayList.get(arrayList.size() - 1).longValue();
                                        it3 = it3;
                                        i4 = i5;
                                        next = next;
                                    }
                                    it = it3;
                                }
                                str9 = str3;
                                str8 = str2;
                            } else {
                                it = it3;
                                Log.e(ab, "unknown type!");
                            }
                        }
                    }
                    str9 = str3;
                    it3 = it;
                    str8 = str2;
                }
                return;
            case 2:
                Iterator<com.sysaac.haptic.b.c.c> it4 = ((com.sysaac.haptic.b.c.a) a2).b.iterator();
                while (it4.hasNext()) {
                    com.sysaac.haptic.b.c.c next2 = it4.next();
                    Iterator<com.sysaac.haptic.b.a.e> it5 = next2.b.iterator();
                    while (it5.hasNext()) {
                        com.sysaac.haptic.b.a.e next3 = it5.next();
                        if (next3 != null && next3.a != null && next3.a.e != null) {
                            if (str7.equals(next3.a.a)) {
                                if (next3.a.b + next2.a > j2) {
                                    arrayList.add(Long.valueOf(new Long(next3.a.b + next2.a).longValue() - j2));
                                    arrayList2.add(Integer.valueOf(i3));
                                    j2 += arrayList.get(arrayList.size() - 1).longValue();
                                }
                                arrayList.add(new Long(65L));
                                arrayList2.add(Integer.valueOf((int) (((next3.a.e.a * 255) * 1.0f) / 100.0f)));
                                j2 += arrayList.get(arrayList.size() - 1).longValue();
                            } else if (str6.equals(next3.a.a)) {
                                if (next3.a.b + next2.a > j2) {
                                    arrayList.add(Long.valueOf(new Long(next3.a.b).longValue() - j2));
                                    arrayList2.add(Integer.valueOf(i3));
                                    j2 += arrayList.get(arrayList.size() - 1).longValue();
                                }
                                if (next3.a.e.c != null && 4 <= next3.a.e.c.size()) {
                                    int i6 = i3;
                                    while (i6 < next3.a.e.c.size() - 2) {
                                        int i7 = i6 + 1;
                                        arrayList.add(new Long(next3.a.e.c.get(i7).a - next3.a.e.c.get(i6).a));
                                        arrayList2.add(Integer.valueOf((int) (((((next3.a.e.c.get(i7).b + next3.a.e.c.get(i6).b) * 0.5d) * next3.a.e.a) / 100.0d) * 255.0d)));
                                        j2 += arrayList.get(arrayList.size() - 1).longValue();
                                        i6 = i7;
                                        it4 = it4;
                                        str6 = str6;
                                        str7 = str7;
                                    }
                                }
                            } else {
                                it2 = it4;
                                str5 = str6;
                                str4 = str7;
                                Log.e(ab, "unknown type!");
                                it4 = it2;
                                str6 = str5;
                                str7 = str4;
                                i3 = 0;
                            }
                        }
                        it2 = it4;
                        str5 = str6;
                        str4 = str7;
                        it4 = it2;
                        str6 = str5;
                        str7 = str4;
                        i3 = 0;
                    }
                }
                return;
            default:
                return;
        }
    }

    public static void a(boolean z2) {
        af = z2;
    }

    public static void a(boolean z2, Context context) {
        if (context == null) {
            Log.e(ab, "swapLR null==context");
            return;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences(ac, 0).edit();
        edit.putBoolean(ac, z2);
        edit.commit();
    }

    public static boolean a(Context context) {
        if (context == null) {
            Log.e(ab, "isLRSwapped null==context");
            return false;
        }
        return context.getSharedPreferences(ac, 0).getBoolean(ac, false);
    }

    public static boolean a(File file) {
        return a(file.getPath(), c);
    }

    public static boolean a(String str, String str2) {
        if (!b(str, str2)) {
            return false;
        }
        return new File(str).exists();
    }

    public static byte[] a(long j2) {
        return new byte[]{(byte) (j2 & 255), (byte) ((j2 >> 8) & 255), (byte) ((j2 >> 16) & 255), (byte) ((j2 >> 24) & 255), (byte) ((j2 >> 32) & 255), (byte) ((j2 >> 40) & 255), (byte) ((j2 >> 48) & 255), (byte) ((j2 >> 56) & 255)};
    }

    public static byte[] a(short s2) {
        return new byte[]{(byte) (s2 & 255), (byte) ((s2 >> 8) & 255)};
    }

    public static int b() {
        return ae;
    }

    public static int b(int i2) {
        ae = i2;
        return i2;
    }

    public static com.sysaac.haptic.sync.b b(com.sysaac.haptic.b.c.a aVar) {
        if (!com.sysaac.haptic.player.a.a(aVar)) {
            return null;
        }
        com.sysaac.haptic.b.c.a aVar2 = new com.sysaac.haptic.b.c.a();
        aVar2.a = new com.sysaac.haptic.b.c.b();
        aVar2.b = new ArrayList<>();
        aVar2.b.add(aVar.b.get(0));
        StringBuilder sb = new StringBuilder(a(aVar2));
        return new com.sysaac.haptic.sync.b(sb.substring(sb.indexOf("\"Pattern\""), sb.lastIndexOf("}", sb.lastIndexOf("}"))), 1, 0);
    }

    public static String b(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    while (true) {
                        try {
                            String readLine = bufferedReader2.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                        } catch (Exception e2) {
                            e = e2;
                            bufferedReader = bufferedReader2;
                            e.printStackTrace();
                            bufferedReader.close();
                            return sb.toString();
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            try {
                                bufferedReader.close();
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                            throw th;
                        }
                    }
                    bufferedReader2.close();
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        return sb.toString();
    }

    public static String b(String str) {
        com.sysaac.haptic.b.b.a aVar;
        try {
            aVar = f(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            aVar = null;
        }
        if (aVar == null || aVar.b == null || aVar.b.size() == 0) {
            Log.w(ab, "pause_start_seek, convertHe10ToHe20, invalid HE1.0 string!");
            return "";
        }
        com.sysaac.haptic.b.c.a aVar2 = new com.sysaac.haptic.b.c.a();
        aVar2.a = new com.sysaac.haptic.b.c.b();
        aVar2.b = new ArrayList<>();
        com.sysaac.haptic.b.c.c cVar = new com.sysaac.haptic.b.c.c();
        cVar.b = aVar.b;
        cVar.a = 0;
        aVar2.b.add(cVar);
        return a(aVar2);
    }

    public static String b(String str, int i2) {
        if (i2 == 0) {
            return str;
        }
        com.sysaac.haptic.b.c.a g2 = g(str);
        if (!com.sysaac.haptic.player.a.a(g2)) {
            return str;
        }
        Iterator<com.sysaac.haptic.b.c.c> it = g2.b.iterator();
        while (it.hasNext()) {
            Iterator<com.sysaac.haptic.b.a.e> it2 = it.next().b.iterator();
            while (it2.hasNext()) {
                com.sysaac.haptic.b.a.e next = it2.next();
                next.a.e.b += i2;
                int i3 = 0;
                if ("transient".equals(next.a.a)) {
                    if (a() >= 24) {
                        i3 = 150;
                        if (next.a.e.b <= 150) {
                            i3 = -50;
                            if (next.a.e.b < -50) {
                            }
                        }
                        next.a.e.b = i3;
                    } else if (next.a.e.b > 100) {
                        next.a.e.b = 100;
                    } else if (next.a.e.b < 0) {
                        next.a.e.b = i3;
                    }
                } else if ("continuous".equals(next.a.a)) {
                    if (next.a.e.b > 100) {
                        next.a.e.b = 100;
                    } else if (next.a.e.b < 0) {
                        next.a.e.b = i3;
                    }
                }
            }
        }
        return a(g2);
    }

    public static void b(String str, ArrayList<Long> arrayList, ArrayList<Integer> arrayList2) {
        int i2;
        int i3;
        if (str == null || str.length() == 0 || arrayList == null || arrayList2 == null) {
            Log.e(ab, "convertM2VHeStringToWaveformParams(), invalid parameters.");
            return;
        }
        com.sysaac.haptic.b.a.c a2 = a(str);
        if (!com.sysaac.haptic.player.a.a(a2)) {
            return;
        }
        arrayList.clear();
        arrayList2.clear();
        long j2 = 0;
        arrayList.add(new Long(0L));
        arrayList2.add(0);
        switch (a2.a()) {
            case 1:
                Log.i(ab, "convertM2VHeStringToWaveformParams, HE VERSION == 1, NOT A M2V HE, do nothing!");
                break;
            case 2:
                Iterator<com.sysaac.haptic.b.c.c> it = ((com.sysaac.haptic.b.c.a) a2).b.iterator();
                while (it.hasNext()) {
                    com.sysaac.haptic.b.c.c next = it.next();
                    Iterator<com.sysaac.haptic.b.a.e> it2 = next.b.iterator();
                    while (it2.hasNext()) {
                        com.sysaac.haptic.b.a.e next2 = it2.next();
                        if (next2 != null && next2.a != null && next2.a.e != null && 2 != next2.a.d) {
                            if ("transient".equals(next2.a.a)) {
                                if (next2.a.b + next.a > j2) {
                                    arrayList.add(Long.valueOf(new Long(next2.a.b + next.a).longValue() - j2));
                                    arrayList2.add(0);
                                    j2 += arrayList.get(arrayList.size() - 1).longValue();
                                }
                                arrayList.add(100 == next2.a.e.a ? new Long(75L) : new Long(30L));
                                i2 = 255;
                            } else if ("continuous".equals(next2.a.a)) {
                                if (next2.a.b + next.a > j2) {
                                    arrayList.add(Long.valueOf(new Long(next2.a.b + next.a).longValue() - j2));
                                    arrayList2.add(0);
                                    j2 += arrayList.get(arrayList.size() - 1).longValue();
                                }
                                if (next2.a.e.c != null && 4 <= next2.a.e.c.size()) {
                                    arrayList.add(new Long(next2.a.c));
                                    if (4 == next2.a.e.c.size()) {
                                        i3 = 153;
                                    } else if (6 == next2.a.e.c.size()) {
                                        i2 = 255;
                                    } else {
                                        i3 = 127;
                                    }
                                    i2 = Integer.valueOf(i3);
                                }
                            } else {
                                Log.e(ab, "unknown type!");
                            }
                            arrayList2.add(i2);
                            j2 += arrayList.get(arrayList.size() - 1).longValue();
                        }
                    }
                }
                break;
        }
        arrayList.remove(0);
        arrayList2.remove(0);
    }

    public static boolean b(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())) {
            return false;
        }
        return str.trim().toLowerCase(Locale.getDefault()).endsWith(str2);
    }

    public static int c(com.sysaac.haptic.b.c.a aVar) {
        if (com.sysaac.haptic.player.a.a(aVar)) {
            return aVar.b.size();
        }
        return -1;
    }

    public static String c(String str) {
        com.sysaac.haptic.b.c.a aVar;
        try {
            aVar = g(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            aVar = null;
        }
        if (!com.sysaac.haptic.player.a.a(aVar)) {
            Log.w(ab, "pause_start_seek, cutUpLongPatternOfHe20String, invalid HE2.0 string!");
            return "";
        }
        com.sysaac.haptic.b.c.a aVar2 = new com.sysaac.haptic.b.c.a();
        aVar2.a = aVar.a;
        aVar2.b = new ArrayList<>();
        Iterator<com.sysaac.haptic.b.c.c> it = aVar.b.iterator();
        while (it.hasNext()) {
            com.sysaac.haptic.b.c.c next = it.next();
            if (next.b.size() != 0) {
                if (1 == next.b.size()) {
                    aVar2.b.add(next);
                } else if (1 < next.b.size()) {
                    Iterator<com.sysaac.haptic.b.a.e> it2 = next.b.iterator();
                    while (it2.hasNext()) {
                        com.sysaac.haptic.b.a.e next2 = it2.next();
                        com.sysaac.haptic.b.c.c cVar = new com.sysaac.haptic.b.c.c();
                        cVar.a = next.a + next2.a.b;
                        cVar.b = new ArrayList<>();
                        cVar.b.add(next2);
                        cVar.b.get(0).a.b = 0;
                        aVar2.b.add(cVar);
                    }
                }
            }
        }
        return a(aVar2);
    }

    private static String c(String str, int i2) {
        com.sysaac.haptic.b.b.a aVar;
        try {
            aVar = f(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            aVar = null;
        }
        if (aVar == null || aVar.b == null || aVar.b.size() == 0) {
            Log.w(ab, "pause_start_seek generatePartialHe10String, source HE invalid!");
            return "";
        }
        int i3 = -1;
        Iterator<com.sysaac.haptic.b.a.e> it = aVar.b.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.sysaac.haptic.b.a.e next = it.next();
            if (next.a != null && next.a.b >= i2) {
                i3 = aVar.b.indexOf(next);
                break;
            }
        }
        if (i3 < 0) {
            return "";
        }
        aVar.b.subList(0, i3).clear();
        Iterator<com.sysaac.haptic.b.a.e> it2 = aVar.b.iterator();
        while (it2.hasNext()) {
            com.sysaac.haptic.b.a.e next2 = it2.next();
            if (next2.a != null) {
                next2.a.b -= i2;
            }
        }
        return a(aVar);
    }

    public static void c(String str, String str2) {
        FileOutputStream fileOutputStream;
        Throwable th;
        Exception e2;
        try {
            try {
                try {
                    fileOutputStream = new FileOutputStream(new File(str));
                    try {
                        fileOutputStream.write(str2.getBytes());
                        fileOutputStream.close();
                    } catch (Exception e3) {
                        e2 = e3;
                        Log.e(ab, e2.toString());
                        fileOutputStream.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fileOutputStream.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                fileOutputStream = null;
                e2 = e5;
            } catch (Throwable th3) {
                fileOutputStream = null;
                th = th3;
                fileOutputStream.close();
                throw th;
            }
        } catch (Exception e6) {
            e6.printStackTrace();
        }
    }

    public static boolean c() {
        return af;
    }

    public static byte[] c(int i2) {
        return new byte[]{(byte) (i2 & 255), (byte) ((i2 >> 8) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 24) & 255)};
    }

    public static String d(String str) {
        com.sysaac.haptic.b.c.a aVar;
        try {
            aVar = g(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            aVar = null;
        }
        if (!com.sysaac.haptic.player.a.a(aVar)) {
            Log.w(ab, "pause_start_seek, trim16pTo4p, invalid HE2.0 string!");
            return "";
        }
        Iterator<com.sysaac.haptic.b.c.c> it = aVar.b.iterator();
        while (it.hasNext()) {
            Iterator<com.sysaac.haptic.b.a.e> it2 = it.next().b.iterator();
            while (it2.hasNext()) {
                com.sysaac.haptic.b.a.e next = it2.next();
                next.a.e.c = a(next.a.e.c);
                if (next.a.a.equals("transient")) {
                    if (next.a.e.b < 0) {
                        next.a.e.b = 0;
                    } else if (next.a.e.b > 100) {
                        next.a.e.b = 100;
                    }
                }
            }
        }
        return a(aVar);
    }

    private static String d(String str, int i2) {
        com.sysaac.haptic.b.c.a aVar;
        int i3;
        int i4;
        try {
            aVar = g(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            aVar = null;
        }
        if (aVar == null || aVar.b == null || aVar.b.size() == 0) {
            Log.w(ab, "pause_start_seek generatePartialHe20String, source HE invalid!");
            return "";
        }
        Iterator<com.sysaac.haptic.b.c.c> it = aVar.b.iterator();
        loop0: while (true) {
            i3 = -1;
            if (!it.hasNext()) {
                i4 = -1;
                break;
            }
            com.sysaac.haptic.b.c.c next = it.next();
            if (next.b != null) {
                Iterator<com.sysaac.haptic.b.a.e> it2 = next.b.iterator();
                while (it2.hasNext()) {
                    com.sysaac.haptic.b.a.e next2 = it2.next();
                    if (next2.a != null && next2.a.b + next.a >= i2) {
                        int indexOf = next.b.indexOf(next2);
                        i3 = aVar.b.indexOf(next);
                        i4 = indexOf;
                        break loop0;
                    }
                }
                continue;
            }
        }
        if (i3 < 0 || i4 < 0) {
            return "";
        }
        aVar.b.subList(0, i3).clear();
        aVar.b.get(0).b.subList(0, i4).clear();
        Iterator<com.sysaac.haptic.b.c.c> it3 = aVar.b.iterator();
        while (it3.hasNext()) {
            com.sysaac.haptic.b.c.c next3 = it3.next();
            if (next3.b != null) {
                if (next3.a < i2) {
                    Iterator<com.sysaac.haptic.b.a.e> it4 = next3.b.iterator();
                    while (it4.hasNext()) {
                        com.sysaac.haptic.b.a.e next4 = it4.next();
                        if (next4.a != null) {
                            next4.a.b = (next4.a.b + next3.a) - i2;
                        }
                    }
                    next3.a = 0;
                } else {
                    next3.a -= i2;
                }
            }
        }
        return a(aVar);
    }

    public static int e(String str) {
        try {
            return new JSONObject(str).getJSONObject(X).getInt("Version");
        } catch (Exception e2) {
            Log.e(ab, "getHeVersion ERROR, heString:" + str);
            e2.printStackTrace();
            return 0;
        }
    }

    public static com.sysaac.haptic.b.b.a f(String str) {
        if (1 != e(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            com.sysaac.haptic.b.b.a aVar = new com.sysaac.haptic.b.b.a();
            aVar.a = new com.sysaac.haptic.b.b.b();
            aVar.b = new ArrayList<>();
            JSONArray jSONArray = jSONObject.getJSONArray("Pattern");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                com.sysaac.haptic.b.a.e eVar = new com.sysaac.haptic.b.a.e();
                eVar.a = new com.sysaac.haptic.b.a.b();
                JSONObject jSONObject2 = ((JSONObject) jSONArray.get(i2)).getJSONObject("Event");
                eVar.a.a = jSONObject2.getString("Type");
                if ("continuous".equals(eVar.a.a)) {
                    eVar.a.c = jSONObject2.getInt("Duration");
                }
                eVar.a.b = jSONObject2.getInt("RelativeTime");
                JSONObject jSONObject3 = jSONObject2.getJSONObject("Parameters");
                eVar.a.e = new com.sysaac.haptic.b.a.d();
                eVar.a.e.b = jSONObject3.getInt("Frequency");
                eVar.a.e.a = jSONObject3.getInt("Intensity");
                eVar.a.e.c = new ArrayList<>();
                if ("continuous".equals(eVar.a.a)) {
                    JSONArray jSONArray2 = jSONObject3.getJSONArray("Curve");
                    for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                        JSONObject jSONObject4 = (JSONObject) jSONArray2.get(i3);
                        com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
                        aVar2.c = jSONObject4.getInt("Frequency");
                        aVar2.b = jSONObject4.getDouble("Intensity");
                        aVar2.a = jSONObject4.getInt("Time");
                        eVar.a.e.c.add(aVar2);
                    }
                }
                aVar.b.add(eVar);
            }
            return aVar;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static com.sysaac.haptic.b.c.a g(String str) {
        if (2 != e(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            com.sysaac.haptic.b.c.a aVar = new com.sysaac.haptic.b.c.a();
            aVar.a = new com.sysaac.haptic.b.c.b();
            aVar.b = new ArrayList<>();
            JSONArray jSONArray = jSONObject.getJSONArray("PatternList");
            int i2 = 0;
            while (i2 < jSONArray.length()) {
                JSONObject jSONObject2 = (JSONObject) jSONArray.get(i2);
                com.sysaac.haptic.b.c.c cVar = new com.sysaac.haptic.b.c.c();
                cVar.a = jSONObject2.getInt("AbsoluteTime");
                cVar.b = new ArrayList<>();
                JSONArray jSONArray2 = jSONObject2.getJSONArray("Pattern");
                int i3 = 0;
                while (i3 < jSONArray2.length()) {
                    com.sysaac.haptic.b.a.e eVar = new com.sysaac.haptic.b.a.e();
                    eVar.a = new com.sysaac.haptic.b.a.b();
                    JSONObject jSONObject3 = ((JSONObject) jSONArray2.get(i3)).getJSONObject("Event");
                    eVar.a.a = jSONObject3.getString("Type");
                    if ("continuous".equals(eVar.a.a)) {
                        eVar.a.c = jSONObject3.getInt("Duration");
                    }
                    eVar.a.b = jSONObject3.getInt("RelativeTime");
                    eVar.a.d = jSONObject3.getInt("Index");
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("Parameters");
                    eVar.a.e = new com.sysaac.haptic.b.a.d();
                    eVar.a.e.b = jSONObject4.getInt("Frequency");
                    eVar.a.e.a = jSONObject4.getInt("Intensity");
                    eVar.a.e.c = new ArrayList<>();
                    if ("continuous".equals(eVar.a.a)) {
                        JSONArray jSONArray3 = jSONObject4.getJSONArray("Curve");
                        int i4 = 0;
                        while (i4 < jSONArray3.length()) {
                            JSONObject jSONObject5 = (JSONObject) jSONArray3.get(i4);
                            com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
                            aVar2.c = jSONObject5.getInt("Frequency");
                            aVar2.b = jSONObject5.getDouble("Intensity");
                            aVar2.a = jSONObject5.getInt("Time");
                            eVar.a.e.c.add(aVar2);
                            i4++;
                            aVar = aVar;
                        }
                    }
                    cVar.b.add(eVar);
                    i3++;
                    aVar = aVar;
                }
                com.sysaac.haptic.b.c.a aVar3 = aVar;
                aVar3.b.add(cVar);
                i2++;
                aVar = aVar3;
            }
            return aVar;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String h(String str) {
        int i2;
        e(str);
        com.sysaac.haptic.b.c.a g2 = g(str);
        if (!com.sysaac.haptic.player.a.a(g2)) {
            return str;
        }
        try {
            Iterator<com.sysaac.haptic.b.c.c> it = g2.b.iterator();
            while (it.hasNext()) {
                com.sysaac.haptic.b.c.c next = it.next();
                if (next.b != null) {
                    Iterator<com.sysaac.haptic.b.a.e> it2 = next.b.iterator();
                    while (it2.hasNext()) {
                        if (2 == it2.next().a.d) {
                            it2.remove();
                        }
                    }
                    if (1 != next.b.size()) {
                        Collections.sort(next.b, new s());
                        int i3 = 0;
                        int i4 = 0;
                        int i5 = 1;
                        for (int i6 = 1; i4 <= next.b.size() - i6 && i5 <= next.b.size() - i6; i6 = 1) {
                            com.sysaac.haptic.b.a.b bVar = next.b.get(i4).a;
                            com.sysaac.haptic.b.a.b bVar2 = next.b.get(i5).a;
                            int i7 = "transient".equals(bVar.a) ? 48 : bVar.c;
                            int i8 = "transient".equals(bVar2.a) ? 48 : bVar2.c;
                            if (bVar2.b < bVar.b + i7) {
                                if (!"continuous".equals(bVar2.a)) {
                                    i2 = i3;
                                    bVar2.d = -1;
                                } else if (bVar2.b + i8 <= bVar.b + i7) {
                                    bVar2.d = -1;
                                    i5++;
                                    i2 = i3;
                                } else {
                                    int i9 = (bVar2.b + i8) - (bVar.b + i7);
                                    if (i9 <= 48) {
                                        bVar2.d = -1;
                                        i2 = i3;
                                    } else {
                                        ArrayList<com.sysaac.haptic.b.a.a> arrayList = new ArrayList<>();
                                        com.sysaac.haptic.b.a.a aVar = new com.sysaac.haptic.b.a.a();
                                        aVar.a = i3;
                                        aVar.b = 0.0d;
                                        aVar.c = i3;
                                        com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
                                        aVar2.a = i9 / 3;
                                        aVar2.b = 1.0d;
                                        aVar2.c = i3;
                                        com.sysaac.haptic.b.a.a aVar3 = new com.sysaac.haptic.b.a.a();
                                        aVar3.a = (i9 / 3) * 2;
                                        aVar3.b = 1.0d;
                                        i2 = 0;
                                        aVar3.c = 0;
                                        com.sysaac.haptic.b.a.a aVar4 = new com.sysaac.haptic.b.a.a();
                                        aVar4.a = i9;
                                        aVar4.b = 0.0d;
                                        aVar4.c = 0;
                                        arrayList.add(aVar);
                                        arrayList.add(aVar2);
                                        arrayList.add(aVar3);
                                        arrayList.add(aVar4);
                                        bVar2.c = i9;
                                        bVar2.b = bVar.b + i7;
                                        bVar2.e.c = arrayList;
                                        i4 = i5;
                                    }
                                }
                                i5++;
                            } else {
                                i2 = i3;
                                i4 = i5;
                                i5++;
                            }
                            i3 = i2;
                        }
                        Iterator<com.sysaac.haptic.b.a.e> it3 = next.b.iterator();
                        while (it3.hasNext()) {
                            if (it3.next().a.d < 0) {
                                it3.remove();
                            }
                        }
                    }
                }
            }
            return a(g2);
        } catch (Throwable th) {
            Log.e(ab, "trimOverlapEvent " + th.toString());
            return str;
        }
    }

    public static int i(String str) {
        com.sysaac.haptic.b.c.a g2 = g(str);
        if (g2 != null) {
            return g2.b();
        }
        return 0;
    }

    public static int j(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        com.sysaac.haptic.b.a.c a2 = a(str);
        if (com.sysaac.haptic.player.a.a(a2)) {
            return a2.b();
        }
        return 0;
    }

    public int[] b(byte[] bArr) {
        int i2;
        int length = bArr.length;
        int[] iArr = new int[length % 4 == 0 ? length / 4 : (length / 4) + 1];
        int i3 = 4;
        while (i3 <= length) {
            iArr[(i3 / 4) - 1] = (bArr[i3 - 4] << 24) | ((bArr[i3 - 3] & 255) << 16) | ((bArr[i3 - 2] & 255) << 8) | (bArr[i3 - 1] & 255);
            i3 += 4;
        }
        int i4 = 0;
        while (true) {
            if (i4 < (length + 4) - i3) {
                int i5 = (i3 / 4) - 1;
                if (i4 == 0) {
                    iArr[i5] = (bArr[(i3 - 4) + i4] << (((i2 - i4) - 1) * 8)) | iArr[i5];
                } else {
                    iArr[i5] = ((bArr[(i3 - 4) + i4] & 255) << (((i2 - i4) - 1) * 8)) | iArr[i5];
                }
                i4++;
            } else {
                return iArr;
            }
        }
    }
}
