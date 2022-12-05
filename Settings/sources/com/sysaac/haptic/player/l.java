package com.sysaac.haptic.player;

import android.content.Context;
import android.os.DynamicEffect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.HapticPlayer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
/* loaded from: classes2.dex */
public class l implements f {
    HapticPlayer a;
    private Vibrator d;
    private Context e;
    private Handler f;
    private HandlerThread g;
    private com.sysaac.haptic.sync.d h;
    private SyncCallback i;
    private PlayerEventCallback k;
    private com.sysaac.haptic.base.o l;
    private final boolean c = false;
    private a j = new a();

    public l(Context context) {
        Log.i("TencentPlayer", "TencentPlayer initialized!");
        this.e = context;
        this.d = (Vibrator) context.getSystemService("vibrator");
    }

    private String a(File file) {
        BufferedReader bufferedReader = null;
        if (!com.sysaac.haptic.base.r.a(file.getPath(), ".he")) {
            Log.d("TencentPlayer", "Wrong parameter {patternFile: " + file.getPath() + "} doesn't exist or has wrong file format!");
            return null;
        }
        StringBuilder sb = new StringBuilder();
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
                            return sb.toString();
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
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.sync.d dVar;
        int i3;
        long j;
        this.i = syncCallback;
        HandlerThread handlerThread = new HandlerThread("Tencent-Sync");
        this.g = handlerThread;
        handlerThread.start();
        r rVar = new r(this, this.g.getLooper(), i, i2);
        this.f = rVar;
        com.sysaac.haptic.sync.d dVar2 = new com.sysaac.haptic.sync.d(rVar, str, this.j);
        this.h = dVar2;
        SyncCallback syncCallback2 = this.j.h;
        if (syncCallback2 == null) {
            if (syncCallback == null) {
                dVar2.a(0L);
                return;
            }
            dVar2.b(syncCallback.getCurrentPosition());
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

    public static boolean j() {
        try {
            return HapticPlayer.isAvailable();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
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
        try {
            k();
            if (!HapticPlayer.isAvailable()) {
                Log.e("TencentPlayer", "The system does not support HapticsPlayer");
                return;
            }
            String a = a(file);
            if (TextUtils.isEmpty(a)) {
                Log.e("TencentPlayer", "empty pattern,do nothing");
            } else if (2 == com.sysaac.haptic.base.r.e(a)) {
                String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(a));
                com.sysaac.haptic.base.o oVar = new com.sysaac.haptic.base.o();
                this.l = oVar;
                oVar.a(i2, i3, i4);
                this.l.a(i, i2, com.sysaac.haptic.base.r.i(h), new o(this, h, i3, i4));
            } else {
                HapticPlayer hapticPlayer = new HapticPlayer(DynamicEffect.create(a));
                this.a = hapticPlayer;
                try {
                    hapticPlayer.start(i, i2, i3, i4);
                } catch (NoSuchMethodError unused) {
                    Log.w("TencentPlayer", "[file, looper,interval,amplitude,freq],haven't integrate Haptic player 1.1 !!!!!!!now we use Haptic player 1.0 to start vibrate");
                    this.a.start(i);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public boolean a(int i) {
        if (!a.a(this.j.g)) {
            Log.e("TencentPlayer", "pause_start_seek seekTo() return - HE invalid or prepare() not be called.");
            return false;
        }
        if (i >= 0 && i <= this.j.g.b()) {
            HapticPlayer hapticPlayer = this.a;
            if (hapticPlayer != null) {
                hapticPlayer.stop();
            }
            k();
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
