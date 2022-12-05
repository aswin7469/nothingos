package com.sysaac.haptic.player;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class g implements f {
    private static final String a = "RichtapPlayer";
    private Vibrator c;
    private Context d;
    private Class e;
    private Handler f;
    private HandlerThread g;
    private com.sysaac.haptic.sync.d h;
    private SyncCallback i;
    private PlayerEventCallback k;
    private com.sysaac.haptic.base.o l;
    private final boolean b = false;
    private a j = new a();

    public g(Context context) {
        this.d = context;
        this.c = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            Class<?> cls = Class.forName("android.os.RichTapVibrationEffect");
            this.e = cls;
            if (cls == null) {
                this.e = Class.forName("android.os.VibrationEffect");
            }
            k();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.sync.d dVar;
        int i3;
        this.i = syncCallback;
        HandlerThread handlerThread = new HandlerThread("Richtap-Sync");
        this.g = handlerThread;
        handlerThread.start();
        k kVar = new k(this, this.g.getLooper(), i, i2);
        this.f = kVar;
        this.h = new com.sysaac.haptic.sync.d(kVar, str, this.j);
        if (this.j.h != null) {
            if (this.j.h.getCurrentPosition() < 0) {
                this.h.b(this.j.h.getCurrentPosition());
                dVar = this.h;
                i3 = this.j.h.getCurrentPosition();
            } else {
                this.h.b(this.j.i);
                dVar = this.h;
                i3 = this.j.i;
            }
            dVar.a(i3, this.j.i);
        } else if (syncCallback == null && com.sysaac.haptic.base.r.a() >= 24) {
            com.sysaac.haptic.base.d.a(this.d).b(str, 1, 0, i, i2);
            this.f.sendEmptyMessageDelayed(102, com.sysaac.haptic.base.r.j(str));
        } else {
            com.sysaac.haptic.sync.d dVar2 = this.h;
            if (syncCallback == null) {
                dVar2.a(0L);
                return;
            }
            dVar2.b(syncCallback.getCurrentPosition());
            this.h.a(syncCallback.getCurrentPosition(), 0L);
        }
    }

    public static boolean j() {
        try {
            Class<?> cls = Class.forName("android.os.RichTapVibrationEffect");
            if (cls == null) {
                cls = Class.forName("android.os.VibrationEffect");
            }
            return 2 != ((Integer) cls.getMethod("checkIfRichTapSupport", new Class[0]).invoke(null, new Object[0])).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void k() {
        try {
            int intValue = ((Integer) this.e.getMethod("checkIfRichTapSupport", new Class[0]).invoke(null, new Object[0])).intValue();
            if (1 == intValue) {
                com.sysaac.haptic.base.r.a(true);
            } else {
                int i = (65280 & intValue) >> 8;
                int i2 = (intValue & 255) >> 0;
                com.sysaac.haptic.base.r.a(i);
                com.sysaac.haptic.base.r.b(i2);
                com.sysaac.haptic.base.r.a(false);
                Log.d(a, "clientCode:" + ((16711680 & intValue) >> 16) + " majorVersion:" + i + " minorVersion:" + i2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void l() {
        HandlerThread handlerThread = this.g;
        if (handlerThread != null) {
            handlerThread.quit();
            this.g = null;
            this.f = null;
            this.h = null;
        }
        com.sysaac.haptic.base.o oVar = this.l;
        if (oVar != null) {
            oVar.c();
            this.l = null;
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a() {
        this.j.a();
        l();
        com.sysaac.haptic.base.d.a(this.d).a();
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int i, int i2) {
        l();
        this.j.a();
        try {
            Method method = this.e.getMethod("createExtPreBaked", Integer.TYPE, Integer.TYPE);
            if (Build.VERSION.SDK_INT < 26) {
                return;
            }
            this.c.vibrate((VibrationEffect) method.invoke(null, Integer.valueOf(i), Integer.valueOf(i2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int i, int i2, int i3) {
        com.sysaac.haptic.base.d.a(this.d).a(i2, i, i3);
        com.sysaac.haptic.base.o oVar = this.l;
        if (oVar != null) {
            oVar.a(i2, i, i3);
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(PlayerEventCallback playerEventCallback) {
        this.k = playerEventCallback;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.sysaac.haptic.player.f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void a(File file, int i, int i2, int i3, int i4) {
        BufferedReader bufferedReader;
        Exception e;
        String str;
        if (!com.sysaac.haptic.base.r.a(file.getPath(), com.sysaac.haptic.base.r.c)) {
            throw new IllegalArgumentException("Wrong parameter {patternFile: " + file.getPath() + "} doesn't exist or has wrong file format!");
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                while (true) {
                    try {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb.append(readLine);
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            if (com.sysaac.haptic.base.r.b(file.getPath(), com.sysaac.haptic.base.r.c)) {
                            }
                            Log.e(a, str);
                        }
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader2 = bufferedReader;
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
                bufferedReader.close();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } catch (Exception e5) {
            bufferedReader = null;
            e = e5;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader2 != null) {
            }
            throw th;
        }
        if (com.sysaac.haptic.base.r.b(file.getPath(), com.sysaac.haptic.base.r.c)) {
            str = "Wrong HE file extention!";
        } else if (this.d != null) {
            a(sb.toString(), i, i2, i3, i4);
            return;
        } else {
            str = "mContext is null!";
        }
        Log.e(a, str);
    }

    @Override // com.sysaac.haptic.player.f
    public void a(File file, int i, int i2, SyncCallback syncCallback) {
        this.j.a();
        this.j.l = file;
        this.j.d = i;
        this.j.e = i2;
        this.j.h = syncCallback;
    }

    @Override // com.sysaac.haptic.player.f
    public void a(String str, int i, int i2, int i3, int i4) {
        com.sysaac.haptic.base.o oVar;
        int i5;
        com.sysaac.haptic.base.q jVar;
        try {
            l();
            if (com.sysaac.haptic.base.r.c()) {
                return;
            }
            int i6 = new JSONObject(str).getJSONObject(com.sysaac.haptic.base.r.X).getInt("Version");
            int a2 = com.sysaac.haptic.base.r.a();
            if (i6 == 1) {
                com.sysaac.haptic.base.d.a(this.d).a(str, i, i2, i3, i4);
            } else if (i6 != 2) {
                Log.e(a, "unsupport he version heVersion:" + i6);
            } else {
                if (a2 == 22) {
                    String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                    com.sysaac.haptic.base.o oVar2 = new com.sysaac.haptic.base.o();
                    this.l = oVar2;
                    oVar2.a(i2, i3, i4);
                    oVar = this.l;
                    i5 = com.sysaac.haptic.base.r.i(h);
                    jVar = new h(this, h, i3, i4);
                } else if (a2 == 23) {
                    String h2 = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                    com.sysaac.haptic.base.o oVar3 = new com.sysaac.haptic.base.o();
                    this.l = oVar3;
                    oVar3.a(i2, i3, i4);
                    oVar = this.l;
                    i5 = com.sysaac.haptic.base.r.i(h2);
                    jVar = new i(this, h2, i3, i4);
                } else if (a2 < 24) {
                    return;
                } else {
                    com.sysaac.haptic.base.o oVar4 = new com.sysaac.haptic.base.o();
                    this.l = oVar4;
                    oVar4.a(i2, i3, i4);
                    oVar = this.l;
                    i5 = com.sysaac.haptic.base.r.i(str);
                    jVar = new j(this, str, i3, i4);
                }
                oVar.a(i, i2, i5, jVar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(String str, int i, int i2, SyncCallback syncCallback) {
        l();
        if (24 > com.sysaac.haptic.base.r.a() && 2 == com.sysaac.haptic.base.r.e(str)) {
            str = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
        }
        c(str, i, i2, syncCallback);
    }

    @Override // com.sysaac.haptic.player.f
    public void a(boolean z) {
        a aVar;
        int i;
        if (z) {
            aVar = this.j;
            i = Integer.MAX_VALUE;
        } else {
            aVar = this.j;
            i = 0;
        }
        aVar.c = i;
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int[] iArr, int[] iArr2, int[] iArr3, boolean z, int i) {
        l();
        try {
            this.j.a();
            Method method = this.e.getMethod("createEnvelope", int[].class, int[].class, int[].class, Boolean.TYPE, Integer.TYPE);
            if (Build.VERSION.SDK_INT < 26) {
                return;
            }
            this.c.vibrate((VibrationEffect) method.invoke(null, iArr, iArr2, iArr3, Boolean.valueOf(z), Integer.valueOf(i)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public boolean a(int i) {
        int i2;
        int i3;
        SyncCallback syncCallback;
        if (!a.a(this.j.g)) {
            Log.e(a, "pause_start_seek seekTo() return - HE invalid or prepare() not be called.");
            return false;
        }
        if (i >= 0 && i <= this.j.g.b()) {
            l();
            com.sysaac.haptic.base.d.a(this.d).a();
            this.j.i = i;
            String a2 = com.sysaac.haptic.base.r.a(this.j.a, this.j.i);
            if (a2 == null || "".equals(a2)) {
                if (this.j.c > 0) {
                    a2 = this.j.a;
                    this.j.i = 0;
                } else {
                    this.j.k = 9;
                    PlayerEventCallback playerEventCallback = this.k;
                    if (playerEventCallback != null) {
                        playerEventCallback.onPlayerStateChanged(9);
                    }
                }
            }
            if (6 != this.j.k) {
                return true;
            }
            this.j.b = SystemClock.elapsedRealtime();
            if (this.j.h != null) {
                a2 = this.j.a;
                i2 = this.j.d;
                i3 = this.j.e;
                syncCallback = this.j.h;
            } else {
                i2 = this.j.d;
                i3 = this.j.e;
                syncCallback = null;
            }
            c(a2, i2, i3, syncCallback);
            return true;
        }
        return false;
    }

    @Override // com.sysaac.haptic.player.f
    public void b() {
        this.j.a();
        l();
    }

    @Override // com.sysaac.haptic.player.f
    public void b(int i, int i2) {
        l();
        this.j.a();
        Context context = this.d;
        if (context != null) {
            com.sysaac.haptic.base.d.a(context).b(i, i2);
        } else {
            Log.e(a, "mContext is null!");
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void b(String str, int i, int i2, SyncCallback syncCallback) {
        this.j.a();
        this.j.a = str;
        this.j.d = i;
        this.j.e = i2;
        this.j.h = syncCallback;
    }

    @Override // com.sysaac.haptic.player.f
    public void c() {
        if (6 != this.j.k) {
            return;
        }
        l();
        com.sysaac.haptic.base.d.a(this.d).a();
        this.j.k = 7;
        if (!a.a(this.j.g)) {
            Log.e(a, "pause_start_seek pause() return - HE invalid or prepare() not be called");
            this.j.i = 0;
        } else if (this.j.h != null) {
            a aVar = this.j;
            aVar.i = aVar.h.getCurrentPosition();
        } else {
            int elapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.j.b);
            if (elapsedRealtime < 0) {
                this.j.i = 0;
                return;
            }
            this.j.i += elapsedRealtime;
        }
    }

    @Override // com.sysaac.haptic.player.f
    public boolean d() {
        int i;
        int i2;
        SyncCallback syncCallback;
        if (6 == this.j.k) {
            return false;
        }
        if (!a.a(this.j.g)) {
            Log.e(a, "pause_start_seek start() return - HE invalid or prepare() not called.");
            return false;
        } else if (this.j.i < 0) {
            return false;
        } else {
            if (9 == this.j.k) {
                this.j.i = 0;
            }
            String a2 = com.sysaac.haptic.base.r.a(this.j.a, this.j.i);
            if (a2 == null || "".equals(a2)) {
                this.j.k = 9;
                PlayerEventCallback playerEventCallback = this.k;
                if (playerEventCallback != null) {
                    playerEventCallback.onPlayerStateChanged(9);
                }
                return false;
            }
            this.j.b = SystemClock.elapsedRealtime();
            this.j.k = 6;
            if (this.j.h != null) {
                a2 = this.j.a;
                i = this.j.d;
                i2 = this.j.e;
                syncCallback = this.j.h;
            } else {
                i = this.j.d;
                i2 = this.j.e;
                syncCallback = null;
            }
            c(a2, i, i2, syncCallback);
            return true;
        }
    }

    @Override // com.sysaac.haptic.player.f
    public boolean e() {
        if (this.j.l != null) {
            a aVar = this.j;
            aVar.a = com.sysaac.haptic.base.r.b(aVar.l);
        }
        if (1 == com.sysaac.haptic.base.r.e(this.j.a)) {
            a aVar2 = this.j;
            aVar2.a = com.sysaac.haptic.base.r.b(aVar2.a);
        }
        if (24 > com.sysaac.haptic.base.r.a()) {
            a aVar3 = this.j;
            aVar3.a = com.sysaac.haptic.base.r.d(aVar3.a);
            a aVar4 = this.j;
            aVar4.a = com.sysaac.haptic.base.r.h(aVar4.a);
        }
        com.sysaac.haptic.b.a.c a2 = com.sysaac.haptic.base.r.a(this.j.a);
        if (a.a(a2)) {
            this.j.g = a2;
            return true;
        }
        Log.e(a, "prepare error, invalid HE");
        this.j.a();
        return false;
    }

    @Override // com.sysaac.haptic.player.f
    public int f() {
        if (this.j.h != null) {
            return this.j.h.getCurrentPosition();
        }
        switch (this.j.k) {
            case 6:
                return (int) ((SystemClock.elapsedRealtime() - this.j.b) + this.j.i);
            case 7:
                return this.j.i;
            case 8:
            default:
                return 0;
            case 9:
                return g();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public int g() {
        if (this.j.g == null) {
            return 0;
        }
        return this.j.g.b();
    }

    @Override // com.sysaac.haptic.player.f
    public boolean h() {
        return 6 == this.j.k;
    }

    @Override // com.sysaac.haptic.player.f
    public void i() {
        this.k = null;
    }
}
