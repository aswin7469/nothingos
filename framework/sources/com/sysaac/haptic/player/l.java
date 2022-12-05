package com.sysaac.haptic.player;

import android.content.Context;
import android.os.Build;
import android.os.DynamicEffect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.HapticPlayer;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
/* loaded from: classes4.dex */
public class l implements f {
    private static final String b = "TencentPlayer";
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
        Log.i(b, "TencentPlayer initialized!");
        this.e = context;
        this.d = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    private String a(File file) {
        BufferedReader bufferedReader = null;
        if (!com.sysaac.haptic.base.r.a(file.getPath(), com.sysaac.haptic.base.r.c)) {
            Log.d(b, "Wrong parameter {patternFile: " + file.getPath() + "} doesn't exist or has wrong file format!");
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

    private void b(int i) {
        String str;
        try {
            if (HapticPlayer.isAvailable()) {
                HapticPlayer hapticPlayer = this.a;
                if (hapticPlayer == null) {
                    Log.e(b, "HapticsPlayer is null");
                    return;
                }
                try {
                    hapticPlayer.updateInterval(i);
                    return;
                } catch (NoSuchMethodError e) {
                    str = "[interval,amplitude,freq],haven't integrate Haptic player 1.1";
                }
            } else {
                str = "The system does not support HapticsPlayer";
            }
            Log.e(b, str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void c(int i) {
        String str;
        try {
            if (HapticPlayer.isAvailable()) {
                HapticPlayer hapticPlayer = this.a;
                if (hapticPlayer == null) {
                    Log.e(b, "HapticsPlayer is null");
                    return;
                }
                try {
                    hapticPlayer.updateAmplitude(i);
                    return;
                } catch (NoSuchMethodError e) {
                    str = "[interval,amplitude,freq],haven't integrate Haptic player 1.1";
                }
            } else {
                str = "The system does not support HapticsPlayer";
            }
            Log.e(b, str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.sync.d dVar;
        int i3;
        this.i = syncCallback;
        HandlerThread handlerThread = new HandlerThread("Tencent-Sync");
        this.g = handlerThread;
        handlerThread.start();
        r rVar = new r(this, this.g.getLooper(), i, i2);
        this.f = rVar;
        this.h = new com.sysaac.haptic.sync.d(rVar, str, this.j);
        if (this.j.h == null) {
            com.sysaac.haptic.sync.d dVar2 = this.h;
            if (syncCallback == null) {
                dVar2.a(0L);
                return;
            }
            dVar2.b(syncCallback.getCurrentPosition());
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

    private void d(int i) {
        String str;
        try {
            if (HapticPlayer.isAvailable()) {
                HapticPlayer hapticPlayer = this.a;
                if (hapticPlayer == null) {
                    Log.e(b, "HapticsPlayer is null");
                    return;
                }
                try {
                    hapticPlayer.updateFrequency(i);
                    return;
                } catch (NoSuchMethodError e) {
                    str = "[interval,amplitude,freq],haven't integrate Haptic player 1.1";
                }
            } else {
                str = "The system does not support HapticsPlayer";
            }
            Log.e(b, str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
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
    public void a() {
        try {
            this.j.a();
            if (this.a == null) {
                Log.e(b, "HapticsPlayer is null");
                return;
            }
            k();
            this.a.stop();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int i, int i2) {
        int i3 = (i2 * 255) / 100;
        k();
        this.j.a();
        if (this.d == null) {
            Log.e(b, "Please call the init method");
            return;
        }
        a();
        if (Build.VERSION.SDK_INT >= 26) {
            this.d.vibrate(VibrationEffect.createOneShot(65, Math.max(1, Math.min(i3, 255))));
        } else {
            this.d.vibrate(65);
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(int i, int i2, int i3) {
        try {
            if (!HapticPlayer.isAvailable()) {
                Log.e(b, "The system does not support HapticsPlayer");
                return;
            }
            HapticPlayer hapticPlayer = this.a;
            if (hapticPlayer == null) {
                Log.e(b, "HapticsPlayer is null");
                return;
            }
            try {
                hapticPlayer.updateParameter(i2, i, i3);
            } catch (NoSuchMethodError e) {
                Log.e(b, "[interval,amplitude,freq],haven't integrate Haptic player 1.1");
            }
            com.sysaac.haptic.base.o oVar = this.l;
            if (oVar == null) {
                return;
            }
            oVar.a(i2, i, i3);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(PlayerEventCallback playerEventCallback) {
        this.k = playerEventCallback;
    }

    public void a(File file, int i) {
        try {
            k();
            if (!HapticPlayer.isAvailable()) {
                Log.e(b, "The system does not support HapticsPlayer");
                return;
            }
            String a = a(file);
            if (TextUtils.isEmpty(a)) {
                Log.e(b, "empty pattern,do nothing");
            } else if (2 != com.sysaac.haptic.base.r.e(a)) {
                HapticPlayer hapticPlayer = new HapticPlayer(DynamicEffect.create(a));
                this.a = hapticPlayer;
                hapticPlayer.start(i);
            } else {
                String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(a));
                com.sysaac.haptic.base.o oVar = new com.sysaac.haptic.base.o();
                this.l = oVar;
                oVar.a(0, 255, 0);
                this.l.a(i, 0, com.sysaac.haptic.base.r.i(h), new m(this, h));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void a(File file, int i, int i2, int i3) {
        try {
            k();
            if (!HapticPlayer.isAvailable()) {
                Log.e(b, "[looper, interval, amplitude],The system does not support HapticsPlayer");
                return;
            }
            String a = a(file);
            if (TextUtils.isEmpty(a)) {
                Log.e(b, "empty pattern,do nothing");
            } else if (2 == com.sysaac.haptic.base.r.e(a)) {
                String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(a));
                com.sysaac.haptic.base.o oVar = new com.sysaac.haptic.base.o();
                this.l = oVar;
                oVar.a(i2, i3, 0);
                this.l.a(i, i2, com.sysaac.haptic.base.r.i(h), new n(this, h, i3));
            } else {
                HapticPlayer hapticPlayer = new HapticPlayer(DynamicEffect.create(a));
                this.a = hapticPlayer;
                try {
                    hapticPlayer.start(i, i2, i3);
                } catch (NoSuchMethodError e) {
                    Log.w(b, "haven't integrate Haptic player 1.1 !!!!!!! now we use Haptic player 1.0 to start vibrate");
                    this.a.start(i);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(File file, int i, int i2, int i3, int i4) {
        try {
            k();
            if (!HapticPlayer.isAvailable()) {
                Log.e(b, "The system does not support HapticsPlayer");
                return;
            }
            String a = a(file);
            if (TextUtils.isEmpty(a)) {
                Log.e(b, "empty pattern,do nothing");
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
                } catch (NoSuchMethodError e) {
                    Log.w(b, "[file, looper,interval,amplitude,freq],haven't integrate Haptic player 1.1 !!!!!!!now we use Haptic player 1.0 to start vibrate");
                    this.a.start(i);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(File file, int i, int i2, SyncCallback syncCallback) {
        this.j.a();
        this.j.l = file;
        this.j.d = i;
        this.j.e = i2;
        this.j.h = syncCallback;
    }

    public void a(String str, int i) {
        try {
            k();
            if (2 == com.sysaac.haptic.base.r.e(str)) {
                String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                com.sysaac.haptic.base.o oVar = new com.sysaac.haptic.base.o();
                this.l = oVar;
                oVar.a(0, 255, 0);
                this.l.a(i, 0, com.sysaac.haptic.base.r.i(h), new p(this, h));
            } else if (!HapticPlayer.isAvailable()) {
                Log.e(b, "The system does not support HapticsPlayer");
            } else {
                HapticPlayer hapticPlayer = new HapticPlayer(DynamicEffect.create(str));
                this.a = hapticPlayer;
                hapticPlayer.start(i);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.sysaac.haptic.player.f
    public void a(String str, int i, int i2, int i3, int i4) {
        try {
            k();
            if (2 == com.sysaac.haptic.base.r.e(str)) {
                String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                com.sysaac.haptic.base.o oVar = new com.sysaac.haptic.base.o();
                this.l = oVar;
                oVar.a(i2, i3, i4);
                this.l.a(i, i2, com.sysaac.haptic.base.r.i(h), new q(this, h, i3, i4));
            } else if (!HapticPlayer.isAvailable()) {
                Log.e(b, "The system does not support HapticsPlayer");
            } else {
                HapticPlayer hapticPlayer = new HapticPlayer(DynamicEffect.create(str));
                this.a = hapticPlayer;
                try {
                    hapticPlayer.start(i, i2, i3, i4);
                } catch (NoSuchMethodError e) {
                    Log.w(b, "[patternString, looper,interval,amplitude,freq],haven't integrate Haptic player 1.1 !!!!!!!now we use Haptic player 1.0 to start vibrate");
                    this.a.start(i);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
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
        a();
        this.j.a();
        int max = (int) ((Math.max(iArr2[1], iArr2[2]) / 100.0f) * (i / 255.0f) * 255.0f);
        if (Build.VERSION.SDK_INT >= 26) {
            this.d.vibrate(VibrationEffect.createOneShot(i2, Math.max(1, Math.min(max, 255))));
        } else {
            this.d.vibrate(i2);
        }
    }

    @Override // com.sysaac.haptic.player.f
    public boolean a(int i) {
        int i2;
        int i3;
        SyncCallback syncCallback;
        if (!a.a(this.j.g)) {
            Log.e(b, "pause_start_seek seekTo() return - HE invalid or prepare() not be called.");
            return false;
        }
        if (i >= 0 && i <= this.j.g.b()) {
            HapticPlayer hapticPlayer = this.a;
            if (hapticPlayer != null) {
                hapticPlayer.stop();
            }
            k();
            this.j.i = i;
            String a = com.sysaac.haptic.base.r.a(this.j.a, this.j.i);
            if (a == null || "".equals(a)) {
                if (this.j.c > 0) {
                    a = this.j.a;
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
                a = this.j.a;
                i2 = this.j.d;
                i3 = this.j.e;
                syncCallback = this.j.h;
            } else {
                i2 = this.j.d;
                i3 = this.j.e;
                syncCallback = null;
            }
            c(a, i2, i3, syncCallback);
            return true;
        }
        return false;
    }

    @Override // com.sysaac.haptic.player.f
    public void b() {
        Log.i(b, "TencentPlayer releaseed!");
        this.j.a();
        k();
        HapticPlayer hapticPlayer = this.a;
        if (hapticPlayer != null) {
            hapticPlayer.stop();
        }
        this.a = null;
    }

    @Override // com.sysaac.haptic.player.f
    public void b(int i, int i2) {
        this.j.a();
        if (this.d == null) {
            Log.e(b, "Please call the init method");
        } else {
            a(com.sysaac.haptic.base.r.a(i, i2), 1);
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
        HapticPlayer hapticPlayer = this.a;
        if (hapticPlayer != null) {
            hapticPlayer.stop();
        }
        k();
        this.j.k = 7;
        if (!a.a(this.j.g)) {
            Log.e(b, "pause_start_seek pause() return -  HE invalid or prepare() not be called");
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
            Log.e(b, "pause_start_seek start() return - HE invalid or prepare() not called.");
            return false;
        } else if (this.j.i < 0) {
            return false;
        } else {
            if (9 == this.j.k) {
                this.j.i = 0;
            }
            String a = com.sysaac.haptic.base.r.a(this.j.a, this.j.i);
            if (a == null || "".equals(a)) {
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
                a = this.j.a;
                i = this.j.d;
                i2 = this.j.e;
                syncCallback = this.j.h;
            } else {
                i = this.j.d;
                i2 = this.j.e;
                syncCallback = null;
            }
            c(a, i, i2, syncCallback);
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
        com.sysaac.haptic.b.a.c a = com.sysaac.haptic.base.r.a(this.j.a);
        if (a.a(a)) {
            this.j.g = a;
            return true;
        }
        Log.e(b, "prepare error, invalid HE");
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
