package com.sysaac.haptic.sync;

import android.util.Log;
import com.sysaac.haptic.b.a.e;
import com.sysaac.haptic.base.r;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class a {
    public boolean a = false;
    private long q = -1;
    private JSONArray r;
    private JSONObject s;
    private String t;
    private com.sysaac.haptic.player.a u;
    private com.sysaac.haptic.b.c.a v;

    public a(String str, com.sysaac.haptic.player.a aVar) {
        this.r = null;
        this.s = null;
        try {
            this.t = str;
            JSONObject jSONObject = new JSONObject(str);
            try {
                this.s = jSONObject.getJSONObject("Pattern");
            } catch (JSONException unused) {
                this.r = jSONObject.getJSONArray("PatternList");
            }
        } catch (Exception e) {
            Log.e("VibrationParser", e.getMessage(), e);
        }
        this.u = aVar;
        int i = aVar.i;
        if (i <= 0 || aVar.h == null) {
            return;
        }
        String a = r.a(aVar.a, i);
        if (a != null && a.length() > 0) {
            com.sysaac.haptic.player.a aVar2 = this.u;
            this.v = r.g(r.a(aVar2.a, aVar2.i));
        }
        if (!com.sysaac.haptic.player.a.a(this.v)) {
            return;
        }
        this.v.b.get(0).a = this.u.i;
    }

    public long a() {
        String message;
        JSONException jSONException;
        JSONObject jSONObject;
        long j;
        if (this.s != null) {
            return -1L;
        }
        try {
            try {
                JSONArray jSONArray = this.r;
                if (jSONArray != null) {
                    int length = jSONArray.length();
                    for (int i = 0; i < length; i++) {
                        try {
                            j = this.r.getJSONObject(i).getLong("AbsoluteTime");
                        } catch (JSONException unused) {
                            j = jSONObject.getJSONObject("PatternDesc").getLong("AbsoluteTime");
                        }
                        if (this.q < j) {
                            return j;
                        }
                    }
                }
            } catch (JSONException e) {
                message = e.getMessage();
                jSONException = e;
                Log.e("VibrationParser", message, jSONException);
                return -1L;
            }
        } catch (Exception e2) {
            message = e2.getMessage();
            jSONException = e2;
            Log.e("VibrationParser", message, jSONException);
            return -1L;
        }
        return -1L;
    }

    public b a(long j) {
        long j2;
        if (j < 0) {
            Log.i("VibrationParser", "timeUs shouldn't be less than 0, which means no media played!");
            return null;
        }
        this.q = j;
        if (this.s != null && this.r == null) {
            return new b("\"Pattern\":" + this.s.toString(), 1, 0);
        }
        if (this.r != null) {
            com.sysaac.haptic.player.a aVar = this.u;
            if (aVar != null && aVar.i > 0 && com.sysaac.haptic.player.a.a(this.v) && this.v.b.get(0).a >= j) {
                Log.d("VibrationParser", "use paused pattern!");
                return r.b(this.v);
            }
            int length = this.r.length();
            int i = 0;
            while (i < length) {
                JSONObject jSONObject = this.r.getJSONObject(i);
                try {
                    j2 = jSONObject.getLong("AbsoluteTime");
                } catch (JSONException unused) {
                    j2 = jSONObject.getJSONObject("PatternDesc").getLong("AbsoluteTime");
                }
                if (j < j2) {
                    break;
                }
                i++;
            }
            if (i >= 1) {
                JSONArray jSONArray = this.r.getJSONObject(i - 1).getJSONArray("Pattern");
                return new b("\"Pattern\":" + jSONArray.toString(), 1, 0);
            }
        }
        return null;
    }

    public int b() {
        com.sysaac.haptic.b.c.a g;
        int i;
        boolean z = -1 == a() && com.sysaac.haptic.player.a.a(this.v) && 1 == r.c(this.v);
        int e = r.e(this.t);
        if (1 == e) {
            com.sysaac.haptic.b.b.a f = r.f(this.t);
            if (!com.sysaac.haptic.player.a.a(f)) {
                return 0;
            }
            return f.b();
        } else if (2 != e) {
            return 0;
        } else {
            if (z) {
                Log.d("VibrationParser", "Utils.getHe20PatternCount(mRemainderHe20):" + r.c(this.v) + "\n getNextScheduledTimeMs():" + a() + "\n mRemainderHe20:" + r.a(this.v));
                g = this.v;
            } else {
                g = r.g(this.t);
            }
            if (!com.sysaac.haptic.player.a.a(g)) {
                return 0;
            }
            try {
                ArrayList<com.sysaac.haptic.b.c.c> arrayList = g.b;
                Iterator<e> it = arrayList.get(arrayList.size() - 1).b.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    e next = it.next();
                    if (next.a.a.equals("continuous")) {
                        com.sysaac.haptic.b.a.b bVar = next.a;
                        i = bVar.b + bVar.c;
                    } else {
                        i = next.a.b + 48;
                    }
                    if (i > i2) {
                        i2 = i;
                    }
                }
                return i2;
            } catch (Exception e2) {
                e2.printStackTrace();
                return 0;
            }
        }
    }
}
