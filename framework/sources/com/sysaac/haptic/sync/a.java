package com.sysaac.haptic.sync;

import android.util.Log;
import com.sysaac.haptic.b.a.e;
import com.sysaac.haptic.base.r;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class a {
    public static final long b = -1;
    public static final String c = "PatternList";
    public static final String d = "PatternDesc";
    public static final String e = "Pattern";
    public static final String f = "AbsoluteTime";
    public static final String g = "Loop";
    public static final String h = "Interval";
    public static final String i = "Event";
    public static final String j = "Type";
    public static final String k = "Duration";
    public static final String l = "RelativeTime";
    public static final String m = "continuous";
    public static final String n = "transient";
    public static final int o = 48;
    private static final String p = "VibrationParser";
    public boolean a = false;
    private long q = -1;
    private JSONArray r;
    private JSONObject s;
    private String t;
    private com.sysaac.haptic.player.a u;
    private com.sysaac.haptic.b.c.a v;

    public a(FileDescriptor fileDescriptor) {
        this.r = null;
        this.s = null;
        try {
            String a = a(fileDescriptor);
            this.t = a;
            if (this.a) {
                Log.i(p, "configured HE: " + a);
            }
            JSONObject jSONObject = new JSONObject(a);
            try {
                this.s = jSONObject.getJSONObject("Pattern");
            } catch (JSONException e2) {
                this.r = jSONObject.getJSONArray("PatternList");
            }
        } catch (Exception e3) {
            Log.e(p, e3.getMessage(), e3);
        }
    }

    public a(String str) {
        this.r = null;
        this.s = null;
        try {
            this.t = str;
            JSONObject jSONObject = new JSONObject(str);
            try {
                this.s = jSONObject.getJSONObject("Pattern");
            } catch (JSONException e2) {
                this.r = jSONObject.getJSONArray("PatternList");
            }
        } catch (Exception e3) {
            Log.e(p, e3.getMessage(), e3);
        }
    }

    public a(String str, com.sysaac.haptic.player.a aVar) {
        this.r = null;
        this.s = null;
        try {
            this.t = str;
            JSONObject jSONObject = new JSONObject(str);
            try {
                this.s = jSONObject.getJSONObject("Pattern");
            } catch (JSONException e2) {
                this.r = jSONObject.getJSONArray("PatternList");
            }
        } catch (Exception e3) {
            Log.e(p, e3.getMessage(), e3);
        }
        this.u = aVar;
        if (aVar.i <= 0 || this.u.h == null) {
            return;
        }
        String a = r.a(this.u.a, this.u.i);
        if (a != null && a.length() > 0) {
            this.v = r.g(r.a(this.u.a, this.u.i));
        }
        if (!com.sysaac.haptic.player.a.a(this.v)) {
            return;
        }
        this.v.b.get(0).a = this.u.i;
    }

    private long a(JSONArray jSONArray) {
        long j2;
        JSONObject jSONObject = jSONArray.getJSONObject(jSONArray.length() - 1).getJSONObject("Event");
        String string = jSONObject.getString("Type");
        long j3 = jSONObject.getInt("RelativeTime") + 0;
        if ("continuous".equals(string)) {
            j2 = jSONObject.getInt("Duration");
        } else if (!"transient".equals(string)) {
            return j3;
        } else {
            j2 = 48;
        }
        return j3 + j2;
    }

    private String a(FileDescriptor fileDescriptor) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
        byte[] bArr = new byte[4096];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read == -1) {
                fileInputStream.close();
                return byteArrayOutputStream.toString();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public long a() {
        String message;
        Exception exc;
        JSONObject jSONObject;
        long j2;
        if (this.s != null) {
            return -1L;
        }
        try {
            try {
                JSONArray jSONArray = this.r;
                if (jSONArray != null) {
                    int length = jSONArray.length();
                    for (int i2 = 0; i2 < length; i2++) {
                        try {
                            j2 = this.r.getJSONObject(i2).getLong("AbsoluteTime");
                        } catch (JSONException e2) {
                            j2 = jSONObject.getJSONObject("PatternDesc").getLong("AbsoluteTime");
                        }
                        if (this.q < j2) {
                            return j2;
                        }
                    }
                }
            } catch (Exception e3) {
                message = e3.getMessage();
                exc = e3;
                Log.e(p, message, exc);
                return -1L;
            }
        } catch (JSONException e4) {
            message = e4.getMessage();
            exc = e4;
            Log.e(p, message, exc);
            return -1L;
        }
        return -1L;
    }

    public b a(long j2) {
        long j3;
        if (j2 < 0) {
            Log.i(p, "timeUs shouldn't be less than 0, which means no media played!");
            return null;
        }
        this.q = j2;
        if (this.s != null && this.r == null) {
            return new b("\"Pattern\":" + this.s.toString(), 1, 0);
        }
        if (this.r != null) {
            com.sysaac.haptic.player.a aVar = this.u;
            if (aVar != null && aVar.i > 0 && com.sysaac.haptic.player.a.a(this.v) && this.v.b.get(0).a >= j2) {
                Log.d(p, "use paused pattern!");
                return r.b(this.v);
            }
            int length = this.r.length();
            int i2 = 0;
            while (i2 < length) {
                JSONObject jSONObject = this.r.getJSONObject(i2);
                try {
                    j3 = jSONObject.getLong("AbsoluteTime");
                } catch (JSONException e2) {
                    j3 = jSONObject.getJSONObject("PatternDesc").getLong("AbsoluteTime");
                }
                if (j2 < j3) {
                    break;
                }
                i2++;
            }
            if (i2 >= 1) {
                JSONArray jSONArray = this.r.getJSONObject(i2 - 1).getJSONArray("Pattern");
                return new b("\"Pattern\":" + jSONArray.toString(), 1, 0);
            }
        }
        return null;
    }

    public int b() {
        com.sysaac.haptic.b.c.a g2;
        boolean z = -1 == a() && com.sysaac.haptic.player.a.a(this.v) && 1 == r.c(this.v);
        int e2 = r.e(this.t);
        if (1 == e2) {
            com.sysaac.haptic.b.b.a f2 = r.f(this.t);
            if (!com.sysaac.haptic.player.a.a(f2)) {
                return 0;
            }
            return f2.b();
        } else if (2 != e2) {
            return 0;
        } else {
            if (z) {
                Log.d(p, "Utils.getHe20PatternCount(mRemainderHe20):" + r.c(this.v) + "\n getNextScheduledTimeMs():" + a() + "\n mRemainderHe20:" + r.a(this.v));
                g2 = this.v;
            } else {
                g2 = r.g(this.t);
            }
            if (!com.sysaac.haptic.player.a.a(g2)) {
                return 0;
            }
            try {
                Iterator<e> it = g2.b.get(g2.b.size() - 1).b.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    e next = it.next();
                    int i3 = next.a.a.equals("continuous") ? next.a.b + next.a.c : next.a.b + 48;
                    if (i3 > i2) {
                        i2 = i3;
                    }
                }
                return i2;
            } catch (Exception e3) {
                e3.printStackTrace();
                return 0;
            }
        }
    }
}
