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
import java.util.Arrays;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class b implements f {
    private static final String a = "GooglePlayer";
    private Vibrator c;
    private Context d;
    private com.sysaac.haptic.base.c e;
    private Handler f;
    private HandlerThread g;
    private com.sysaac.haptic.sync.d h;
    private SyncCallback i;
    private PlayerEventCallback k;
    private com.sysaac.haptic.base.o l;
    private final boolean b = false;
    private a j = new a();

    public b(Context context) {
        this.d = context;
        this.c = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        com.sysaac.haptic.base.c cVar = new com.sysaac.haptic.base.c(this.d);
        this.e = cVar;
        cVar.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.sync.d dVar;
        int i3;
        this.i = syncCallback;
        HandlerThread handlerThread = new HandlerThread("Richtap-Sync");
        this.g = handlerThread;
        handlerThread.start();
        d dVar2 = new d(this, this.g.getLooper(), i, i2);
        this.f = dVar2;
        this.h = new com.sysaac.haptic.sync.d(dVar2, str, this.j);
        if (this.j.h == null) {
            com.sysaac.haptic.sync.d dVar3 = this.h;
            if (syncCallback == null) {
                dVar3.a(0L);
                return;
            }
            dVar3.b(syncCallback.getCurrentPosition());
            this.h.a(syncCallback.getCurrentPosition(), 0L);
            return;
        }
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
    }

    private void j() {
        com.sysaac.haptic.base.c cVar = this.e;
        if (cVar != null) {
            cVar.b();
        }
        this.c.cancel();
    }

    private void k() {
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
        k();
        j();
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int i, int i2) {
        int i3 = (i2 * 255) / 100;
        this.j.a();
        if (this.c == null) {
            Log.e(a, "Please call the init method");
            return;
        }
        a();
        if (Build.VERSION.SDK_INT >= 26) {
            this.c.vibrate(VibrationEffect.createOneShot(65, Math.max(1, Math.min(i3, 255))));
        } else {
            this.c.vibrate(65);
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int i, int i2, int i3) {
        com.sysaac.haptic.base.o oVar = this.l;
        if (oVar != null) {
            oVar.a(i2, i, i3);
        }
        com.sysaac.haptic.base.b bVar = new com.sysaac.haptic.base.b(null, -1, i2, i, i3);
        com.sysaac.haptic.base.c cVar = this.e;
        if (cVar != null) {
            cVar.b(bVar);
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(PlayerEventCallback playerEventCallback) {
        this.k = playerEventCallback;
    }

    @Override // com.sysaac.haptic.player.f
    public void a(File file, int i, int i2, int i3, int i4) {
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
                        } catch (Exception e) {
                            e = e;
                            bufferedReader = bufferedReader2;
                            e.printStackTrace();
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            a(sb.toString(), i, i2, i3, i4);
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                    bufferedReader2.close();
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        a(sb.toString(), i, i2, i3, i4);
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
        try {
            int i5 = new JSONObject(str).getJSONObject(com.sysaac.haptic.base.r.X).getInt("Version");
            if (i5 == 1) {
                j();
                this.e.a(new com.sysaac.haptic.base.b(str, i, i2, i3, i4));
            } else if (i5 == 2) {
                k();
                String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                com.sysaac.haptic.base.o oVar = new com.sysaac.haptic.base.o();
                this.l = oVar;
                oVar.a(i2, i3, i4);
                this.l.a(i, i2, com.sysaac.haptic.base.r.i(h), new c(this, h, i3, i4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(String str, int i, int i2, SyncCallback syncCallback) {
        k();
        if (2 == com.sysaac.haptic.base.r.e(str)) {
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
        int i2 = Arrays.copyOfRange(iArr, 0, 4)[3];
        this.j.a();
        if (this.c == null) {
            Log.e(a, "Please call the init method");
            return;
        }
        a();
        int max = (int) ((Math.max(iArr2[1], iArr2[2]) / 100.0f) * (i / 255.0f) * 255.0f);
        if (Build.VERSION.SDK_INT >= 26) {
            this.c.vibrate(VibrationEffect.createOneShot(i2, Math.max(1, Math.min(max, 255))));
        } else {
            this.c.vibrate(i2);
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
            k();
            j();
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
        k();
        com.sysaac.haptic.base.c cVar = this.e;
        if (cVar != null) {
            cVar.c();
            this.e = null;
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void b(int i, int i2) {
        int a2 = com.sysaac.haptic.base.d.a(i, i2);
        int i3 = (i * 255) / 100;
        this.j.a();
        if (this.c == null) {
            Log.e(a, "Please call the init method");
            return;
        }
        a();
        if (Build.VERSION.SDK_INT >= 26) {
            this.c.vibrate(VibrationEffect.createOneShot(a2, Math.max(1, Math.min(i3, 255))));
        } else {
            this.c.vibrate(a2);
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
        k();
        j();
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
        a aVar3 = this.j;
        aVar3.a = com.sysaac.haptic.base.r.d(aVar3.a);
        a aVar4 = this.j;
        aVar4.a = com.sysaac.haptic.base.r.h(aVar4.a);
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
