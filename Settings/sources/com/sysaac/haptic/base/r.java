package com.sysaac.haptic.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.sysaac.haptic.b.b.a;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes2.dex */
public class r {
    private static int ad = 0;
    private static int ae = 0;
    private static boolean af = false;

    public static int a() {
        return ad;
    }

    public static int a(int i) {
        ad = i;
        return i;
    }

    public static com.sysaac.haptic.b.a.c a(String str) {
        int e = e(str);
        if (e == 1) {
            try {
                return f(str);
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        } else if (e != 2) {
            return null;
        } else {
            try {
                return g(str);
            } catch (Exception e3) {
                e3.printStackTrace();
                return null;
            }
        }
    }

    public static String a(a aVar) {
        try {
            JSONStringer jSONStringer = new JSONStringer();
            jSONStringer.object();
            jSONStringer.key("Metadata").object().key("Created").value(aVar.a.b).key("Description").value(aVar.a.c).key("Version").value(aVar.a.a).endObject();
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String a(com.sysaac.haptic.b.c.a aVar) {
        try {
            JSONStringer jSONStringer = new JSONStringer();
            jSONStringer.object();
            jSONStringer.key("Metadata").object().key("Created").value(aVar.a.b).key("Description").value(aVar.a.c).key("Version").value(aVar.a.a).endObject();
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String a(String str, int i) {
        int e = e(str);
        return e != 1 ? e != 2 ? "" : d(str, i) : c(str, i);
    }

    private static ArrayList<com.sysaac.haptic.b.a.a> a(ArrayList<com.sysaac.haptic.b.a.a> arrayList) {
        double d;
        double d2;
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        int size = arrayList.size();
        if (size > 0 && size <= 4) {
            return arrayList;
        }
        com.sysaac.haptic.b.a.a aVar = new com.sysaac.haptic.b.a.a();
        int i = size - 2;
        int i2 = i / 2;
        for (int i3 = 1; i3 <= i2; i3++) {
            aVar.a += arrayList.get(i3).a;
            aVar.b += arrayList.get(i3).b;
            aVar.c += arrayList.get(i3).c;
        }
        aVar.a /= i2;
        aVar.b = aVar.b / i2;
        aVar.b = Math.round(d * 10.0d) / 10.0d;
        aVar.c /= i2;
        com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
        for (int i4 = i2 + 1; i4 <= i; i4++) {
            aVar2.a += arrayList.get(i4).a;
            aVar2.b += arrayList.get(i4).b;
            aVar2.c += arrayList.get(i4).c;
        }
        int i5 = i - i2;
        aVar2.a /= i5;
        aVar2.b = aVar2.b / i5;
        aVar2.b = Math.round(d2 * 10.0d) / 10.0d;
        aVar2.c /= i5;
        arrayList.subList(1, size - 1).clear();
        arrayList.add(1, aVar);
        arrayList.add(2, aVar2);
        return arrayList;
    }

    public static void a(boolean z) {
        af = z;
    }

    public static boolean a(Context context) {
        if (context == null) {
            Log.e("Util", "isLRSwapped null==context");
            return false;
        }
        return context.getSharedPreferences("swap_left_right", 0).getBoolean("swap_left_right", false);
    }

    public static boolean a(String str, String str2) {
        if (!b(str, str2)) {
            return false;
        }
        return new File(str).exists();
    }

    public static int b() {
        return ae;
    }

    public static int b(int i) {
        ae = i;
        return i;
    }

    public static com.sysaac.haptic.sync.b b(com.sysaac.haptic.b.c.a aVar) {
        if (!com.sysaac.haptic.player.a.a(aVar)) {
            return null;
        }
        com.sysaac.haptic.b.c.a aVar2 = new com.sysaac.haptic.b.c.a();
        aVar2.a = new com.sysaac.haptic.b.c.b();
        ArrayList<com.sysaac.haptic.b.c.c> arrayList = new ArrayList<>();
        aVar2.b = arrayList;
        arrayList.add(aVar.b.get(0));
        StringBuilder sb = new StringBuilder(a(aVar2));
        return new com.sysaac.haptic.sync.b(sb.substring(sb.indexOf("\"Pattern\""), sb.lastIndexOf("}", sb.lastIndexOf("}"))), 1, 0);
    }

    public static String b(String str, int i) {
        com.sysaac.haptic.b.a.d dVar;
        if (i == 0) {
            return str;
        }
        com.sysaac.haptic.b.c.a g = g(str);
        if (!com.sysaac.haptic.player.a.a(g)) {
            return str;
        }
        Iterator<com.sysaac.haptic.b.c.c> it = g.b.iterator();
        while (it.hasNext()) {
            Iterator<com.sysaac.haptic.b.a.e> it2 = it.next().b.iterator();
            while (it2.hasNext()) {
                com.sysaac.haptic.b.a.e next = it2.next();
                com.sysaac.haptic.b.a.b bVar = next.a;
                bVar.e.b += i;
                int i2 = 0;
                if ("transient".equals(bVar.a)) {
                    int a = a();
                    dVar = next.a.e;
                    if (a >= 24) {
                        int i3 = dVar.b;
                        i2 = 150;
                        if (i3 <= 150) {
                            i2 = -50;
                            if (i3 < -50) {
                            }
                        }
                        dVar.b = i2;
                    } else {
                        int i4 = dVar.b;
                        if (i4 > 100) {
                            dVar.b = 100;
                        } else if (i4 < 0) {
                            dVar.b = i2;
                        }
                    }
                } else if ("continuous".equals(next.a.a)) {
                    dVar = next.a.e;
                    int i5 = dVar.b;
                    if (i5 > 100) {
                        dVar.b = 100;
                    } else if (i5 < 0) {
                        dVar.b = i2;
                    }
                }
            }
        }
        return a(g);
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

    private static String c(String str, int i) {
        a aVar;
        ArrayList<com.sysaac.haptic.b.a.e> arrayList;
        try {
            aVar = f(str);
        } catch (Exception e) {
            e.printStackTrace();
            aVar = null;
        }
        if (aVar == null || (arrayList = aVar.b) == null || arrayList.size() == 0) {
            Log.w("Util", "pause_start_seek generatePartialHe10String, source HE invalid!");
            return "";
        }
        int i2 = -1;
        Iterator<com.sysaac.haptic.b.a.e> it = aVar.b.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.sysaac.haptic.b.a.e next = it.next();
            com.sysaac.haptic.b.a.b bVar = next.a;
            if (bVar != null && bVar.b >= i) {
                i2 = aVar.b.indexOf(next);
                break;
            }
        }
        if (i2 < 0) {
            return "";
        }
        aVar.b.subList(0, i2).clear();
        Iterator<com.sysaac.haptic.b.a.e> it2 = aVar.b.iterator();
        while (it2.hasNext()) {
            com.sysaac.haptic.b.a.b bVar2 = it2.next().a;
            if (bVar2 != null) {
                bVar2.b -= i;
            }
        }
        return a(aVar);
    }

    public static boolean c() {
        return af;
    }

    public static String d(String str) {
        com.sysaac.haptic.b.c.a aVar;
        try {
            aVar = g(str);
        } catch (Exception e) {
            e.printStackTrace();
            aVar = null;
        }
        if (!com.sysaac.haptic.player.a.a(aVar)) {
            Log.w("Util", "pause_start_seek, trim16pTo4p, invalid HE2.0 string!");
            return "";
        }
        Iterator<com.sysaac.haptic.b.c.c> it = aVar.b.iterator();
        while (it.hasNext()) {
            Iterator<com.sysaac.haptic.b.a.e> it2 = it.next().b.iterator();
            while (it2.hasNext()) {
                com.sysaac.haptic.b.a.e next = it2.next();
                com.sysaac.haptic.b.a.d dVar = next.a.e;
                dVar.c = a(dVar.c);
                if (next.a.a.equals("transient")) {
                    com.sysaac.haptic.b.a.d dVar2 = next.a.e;
                    int i = dVar2.b;
                    if (i < 0) {
                        dVar2.b = 0;
                    } else if (i > 100) {
                        dVar2.b = 100;
                    }
                }
            }
        }
        return a(aVar);
    }

    private static String d(String str, int i) {
        com.sysaac.haptic.b.c.a aVar;
        ArrayList<com.sysaac.haptic.b.c.c> arrayList;
        int i2;
        int i3;
        try {
            aVar = g(str);
        } catch (Exception e) {
            e.printStackTrace();
            aVar = null;
        }
        if (aVar == null || (arrayList = aVar.b) == null || arrayList.size() == 0) {
            Log.w("Util", "pause_start_seek generatePartialHe20String, source HE invalid!");
            return "";
        }
        Iterator<com.sysaac.haptic.b.c.c> it = aVar.b.iterator();
        loop0: while (true) {
            i2 = -1;
            if (!it.hasNext()) {
                i3 = -1;
                break;
            }
            com.sysaac.haptic.b.c.c next = it.next();
            ArrayList<com.sysaac.haptic.b.a.e> arrayList2 = next.b;
            if (arrayList2 != null) {
                Iterator<com.sysaac.haptic.b.a.e> it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    com.sysaac.haptic.b.a.e next2 = it2.next();
                    com.sysaac.haptic.b.a.b bVar = next2.a;
                    if (bVar != null && bVar.b + next.a >= i) {
                        int indexOf = next.b.indexOf(next2);
                        i2 = aVar.b.indexOf(next);
                        i3 = indexOf;
                        break loop0;
                    }
                }
                continue;
            }
        }
        if (i2 < 0 || i3 < 0) {
            return "";
        }
        aVar.b.subList(0, i2).clear();
        aVar.b.get(0).b.subList(0, i3).clear();
        Iterator<com.sysaac.haptic.b.c.c> it3 = aVar.b.iterator();
        while (it3.hasNext()) {
            com.sysaac.haptic.b.c.c next3 = it3.next();
            ArrayList<com.sysaac.haptic.b.a.e> arrayList3 = next3.b;
            if (arrayList3 != null) {
                int i4 = next3.a;
                if (i4 < i) {
                    Iterator<com.sysaac.haptic.b.a.e> it4 = arrayList3.iterator();
                    while (it4.hasNext()) {
                        com.sysaac.haptic.b.a.b bVar2 = it4.next().a;
                        if (bVar2 != null) {
                            bVar2.b = (bVar2.b + next3.a) - i;
                        }
                    }
                    next3.a = 0;
                } else {
                    next3.a = i4 - i;
                }
            }
        }
        return a(aVar);
    }

    public static int e(String str) {
        try {
            return new JSONObject(str).getJSONObject("Metadata").getInt("Version");
        } catch (Exception e) {
            Log.e("Util", "getHeVersion ERROR, heString:" + str);
            e.printStackTrace();
            return 0;
        }
    }

    public static a f(String str) {
        if (1 != e(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            a aVar = new a();
            aVar.a = new com.sysaac.haptic.b.b.b();
            aVar.b = new ArrayList<>();
            JSONArray jSONArray = jSONObject.getJSONArray("Pattern");
            for (int i = 0; i < jSONArray.length(); i++) {
                com.sysaac.haptic.b.a.e eVar = new com.sysaac.haptic.b.a.e();
                eVar.a = new com.sysaac.haptic.b.a.b();
                JSONObject jSONObject2 = ((JSONObject) jSONArray.get(i)).getJSONObject("Event");
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
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        JSONObject jSONObject4 = (JSONObject) jSONArray2.get(i2);
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
        } catch (Exception e) {
            e.printStackTrace();
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
            int i = 0;
            while (i < jSONArray.length()) {
                JSONObject jSONObject2 = (JSONObject) jSONArray.get(i);
                com.sysaac.haptic.b.c.c cVar = new com.sysaac.haptic.b.c.c();
                cVar.a = jSONObject2.getInt("AbsoluteTime");
                cVar.b = new ArrayList<>();
                JSONArray jSONArray2 = jSONObject2.getJSONArray("Pattern");
                int i2 = 0;
                while (i2 < jSONArray2.length()) {
                    com.sysaac.haptic.b.a.e eVar = new com.sysaac.haptic.b.a.e();
                    eVar.a = new com.sysaac.haptic.b.a.b();
                    JSONObject jSONObject3 = ((JSONObject) jSONArray2.get(i2)).getJSONObject("Event");
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
                        int i3 = 0;
                        while (i3 < jSONArray3.length()) {
                            JSONObject jSONObject5 = (JSONObject) jSONArray3.get(i3);
                            com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
                            aVar2.c = jSONObject5.getInt("Frequency");
                            aVar2.b = jSONObject5.getDouble("Intensity");
                            aVar2.a = jSONObject5.getInt("Time");
                            eVar.a.e.c.add(aVar2);
                            i3++;
                            aVar = aVar;
                        }
                    }
                    cVar.b.add(eVar);
                    i2++;
                    aVar = aVar;
                }
                com.sysaac.haptic.b.c.a aVar3 = aVar;
                aVar3.b.add(cVar);
                i++;
                aVar = aVar3;
            }
            return aVar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String h(String str) {
        char c;
        String str2;
        com.sysaac.haptic.b.c.a aVar;
        char c2;
        String str3 = "transient";
        e(str);
        com.sysaac.haptic.b.c.a g = g(str);
        if (!com.sysaac.haptic.player.a.a(g)) {
            return str;
        }
        try {
            Iterator<com.sysaac.haptic.b.c.c> it = g.b.iterator();
            while (it.hasNext()) {
                com.sysaac.haptic.b.c.c next = it.next();
                ArrayList<com.sysaac.haptic.b.a.e> arrayList = next.b;
                if (arrayList != null) {
                    Iterator<com.sysaac.haptic.b.a.e> it2 = arrayList.iterator();
                    while (true) {
                        c = 2;
                        if (!it2.hasNext()) {
                            break;
                        } else if (2 == it2.next().a.d) {
                            it2.remove();
                        }
                    }
                    if (1 != next.b.size()) {
                        Collections.sort(next.b, new s());
                        int i = 1;
                        int i2 = 0;
                        for (int i3 = 1; i2 <= next.b.size() - i3 && i <= next.b.size() - i3; i3 = 1) {
                            com.sysaac.haptic.b.a.b bVar = next.b.get(i2).a;
                            com.sysaac.haptic.b.a.b bVar2 = next.b.get(i).a;
                            int i4 = str3.equals(bVar.a) ? 48 : bVar.c;
                            int i5 = str3.equals(bVar2.a) ? 48 : bVar2.c;
                            if (bVar2.b < bVar.b + i4) {
                                if ("continuous".equals(bVar2.a)) {
                                    int i6 = bVar2.b;
                                    int i7 = i6 + i5;
                                    int i8 = bVar.b;
                                    if (i7 <= i8 + i4) {
                                        bVar2.d = -1;
                                        i++;
                                        str2 = str3;
                                        aVar = g;
                                        c2 = 2;
                                    } else {
                                        int i9 = (i6 + i5) - (i8 + i4);
                                        if (i9 <= 48) {
                                            bVar2.d = -1;
                                            str2 = str3;
                                            aVar = g;
                                            c2 = 2;
                                        } else {
                                            ArrayList<com.sysaac.haptic.b.a.a> arrayList2 = new ArrayList<>();
                                            com.sysaac.haptic.b.a.a aVar2 = new com.sysaac.haptic.b.a.a();
                                            aVar2.a = 0;
                                            aVar2.b = 0.0d;
                                            aVar2.c = 0;
                                            com.sysaac.haptic.b.a.a aVar3 = new com.sysaac.haptic.b.a.a();
                                            aVar3.a = i9 / 3;
                                            aVar3.b = 1.0d;
                                            aVar3.c = 0;
                                            com.sysaac.haptic.b.a.a aVar4 = new com.sysaac.haptic.b.a.a();
                                            c2 = 2;
                                            aVar4.a = (i9 / 3) * 2;
                                            aVar4.b = 1.0d;
                                            aVar4.c = 0;
                                            com.sysaac.haptic.b.a.a aVar5 = new com.sysaac.haptic.b.a.a();
                                            aVar5.a = i9;
                                            str2 = str3;
                                            aVar = g;
                                            aVar5.b = 0.0d;
                                            aVar5.c = 0;
                                            arrayList2.add(aVar2);
                                            arrayList2.add(aVar3);
                                            arrayList2.add(aVar4);
                                            arrayList2.add(aVar5);
                                            bVar2.c = i9;
                                            bVar2.b = bVar.b + i4;
                                            bVar2.e.c = arrayList2;
                                            i2 = i;
                                        }
                                    }
                                } else {
                                    str2 = str3;
                                    aVar = g;
                                    c2 = c;
                                    bVar2.d = -1;
                                }
                                i++;
                            } else {
                                str2 = str3;
                                aVar = g;
                                c2 = c;
                                i2 = i;
                                i++;
                            }
                            c = c2;
                            str3 = str2;
                            g = aVar;
                        }
                        String str4 = str3;
                        com.sysaac.haptic.b.c.a aVar6 = g;
                        Iterator<com.sysaac.haptic.b.a.e> it3 = next.b.iterator();
                        while (it3.hasNext()) {
                            if (it3.next().a.d < 0) {
                                it3.remove();
                            }
                        }
                        str3 = str4;
                        g = aVar6;
                    }
                }
            }
            return a(g);
        } catch (Throwable th) {
            Log.e("Util", "trimOverlapEvent " + th.toString());
            return str;
        }
    }

    public static int i(String str) {
        com.sysaac.haptic.b.c.a g = g(str);
        if (g != null) {
            return g.b();
        }
        return 0;
    }

    public static int j(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        com.sysaac.haptic.b.a.c a = a(str);
        if (com.sysaac.haptic.player.a.a(a)) {
            return a.b();
        }
        return 0;
    }
}
