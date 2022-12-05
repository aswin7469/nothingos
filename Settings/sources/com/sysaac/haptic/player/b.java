package com.sysaac.haptic.player;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class b implements f {
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
        this.c = (Vibrator) context.getSystemService("vibrator");
        com.sysaac.haptic.base.c cVar = new com.sysaac.haptic.base.c(this.d);
        this.e = cVar;
        cVar.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.sync.d dVar;
        int i3;
        long j;
        this.i = syncCallback;
        HandlerThread handlerThread = new HandlerThread("Richtap-Sync");
        this.g = handlerThread;
        handlerThread.start();
        d dVar2 = new d(this, this.g.getLooper(), i, i2);
        this.f = dVar2;
        com.sysaac.haptic.sync.d dVar3 = new com.sysaac.haptic.sync.d(dVar2, str, this.j);
        this.h = dVar3;
        SyncCallback syncCallback2 = this.j.h;
        if (syncCallback2 == null) {
            if (syncCallback == null) {
                dVar3.a(0L);
                return;
            }
            dVar3.b(syncCallback.getCurrentPosition());
            this.h.a(syncCallback.getCurrentPosition(), 0L);
            return;
        }
        if (syncCallback2.getCurrentPosition() < 0) {
            this.h.b(this.j.h.getCurrentPosition());
            dVar = this.h;
            j = this.j.h.getCurrentPosition();
            i3 = this.j.i;
        } else {
            this.h.b(this.j.i);
            dVar = this.h;
            i3 = this.j.i;
            j = i3;
        }
        dVar.a(j, i3);
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

    public void a(String str, int i, int i2, int i3, int i4) {
        try {
            int i5 = new JSONObject(str).getJSONObject("Metadata").getInt("Version");
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

    public boolean a(int i) {
        if (!a.a(this.j.g)) {
            Log.e("GooglePlayer", "pause_start_seek seekTo() return - HE invalid or prepare() not be called.");
            return false;
        }
        if (i >= 0 && i <= this.j.g.b()) {
            k();
            j();
            a aVar = this.j;
            aVar.i = i;
            String a = com.sysaac.haptic.base.r.a(aVar.a, i);
            if (a == null || "".equals(a)) {
                a aVar2 = this.j;
                if (aVar2.c > 0) {
                    String str = aVar2.a;
                    aVar2.i = 0;
                    a = str;
                } else {
                    aVar2.k = 9;
                    PlayerEventCallback playerEventCallback = this.k;
                    if (playerEventCallback != null) {
                        playerEventCallback.onPlayerStateChanged(9);
                    }
                }
            }
            a aVar3 = this.j;
            if (6 != aVar3.k) {
                return true;
            }
            aVar3.b = SystemClock.elapsedRealtime();
            a aVar4 = this.j;
            SyncCallback syncCallback = aVar4.h;
            if (syncCallback != null) {
                c(aVar4.a, aVar4.d, aVar4.e, syncCallback);
            } else {
                c(a, aVar4.d, aVar4.e, null);
            }
            return true;
        }
        return false;
    }
}
