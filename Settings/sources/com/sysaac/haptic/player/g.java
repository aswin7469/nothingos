package com.sysaac.haptic.player;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
import com.sysaac.haptic.base.q;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class g implements f {
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
        this.c = (Vibrator) context.getSystemService("vibrator");
        try {
            this.e = Class.forName("android.os.RichTapVibrationEffect");
            k();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        k kVar = new k(this, this.g.getLooper(), i, i2);
        this.f = kVar;
        this.h = new com.sysaac.haptic.sync.d(kVar, str, this.j);
        SyncCallback syncCallback2 = this.j.h;
        if (syncCallback2 != null) {
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
        } else if (syncCallback == null && com.sysaac.haptic.base.r.a() >= 24) {
            com.sysaac.haptic.base.d.a(this.d).b(str, 1, 0, i, i2);
            this.f.sendEmptyMessageDelayed(R$styleable.Constraint_layout_goneMarginStart, com.sysaac.haptic.base.r.j(str));
        } else if (syncCallback == null) {
            this.h.a(0L);
        } else {
            this.h.b(syncCallback.getCurrentPosition());
            this.h.a(syncCallback.getCurrentPosition(), 0L);
        }
    }

    public static boolean j() {
        try {
            return 2 != ((Integer) Class.forName("android.os.RichTapVibrationEffect").getMethod("checkIfRichTapSupport", new Class[0]).invoke(null, new Object[0])).intValue();
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
                Log.d("RichtapPlayer", "clientCode:" + ((16711680 & intValue) >> 16) + " majorVersion:" + i + " minorVersion:" + i2);
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
        if (!com.sysaac.haptic.base.r.a(file.getPath(), ".he")) {
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
                            if (com.sysaac.haptic.base.r.b(file.getPath(), ".he")) {
                            }
                            Log.e("RichtapPlayer", str);
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
        if (com.sysaac.haptic.base.r.b(file.getPath(), ".he")) {
            str = "Wrong HE file extention!";
        } else if (this.d != null) {
            a(sb.toString(), i, i2, i3, i4);
            return;
        } else {
            str = "mContext is null!";
        }
        Log.e("RichtapPlayer", str);
    }

    public void a(String str, int i, int i2, int i3, int i4) {
        com.sysaac.haptic.base.o oVar;
        int i5;
        q jVar;
        try {
            l();
            if (com.sysaac.haptic.base.r.c()) {
                return;
            }
            int i6 = new JSONObject(str).getJSONObject("Metadata").getInt("Version");
            int a = com.sysaac.haptic.base.r.a();
            if (i6 == 1) {
                com.sysaac.haptic.base.d.a(this.d).a(str, i, i2, i3, i4);
            } else if (i6 != 2) {
                Log.e("RichtapPlayer", "unsupport he version heVersion:" + i6);
            } else {
                if (a == 22) {
                    String h = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                    com.sysaac.haptic.base.o oVar2 = new com.sysaac.haptic.base.o();
                    this.l = oVar2;
                    oVar2.a(i2, i3, i4);
                    oVar = this.l;
                    i5 = com.sysaac.haptic.base.r.i(h);
                    jVar = new h(this, h, i3, i4);
                } else if (a == 23) {
                    String h2 = com.sysaac.haptic.base.r.h(com.sysaac.haptic.base.r.d(str));
                    com.sysaac.haptic.base.o oVar3 = new com.sysaac.haptic.base.o();
                    this.l = oVar3;
                    oVar3.a(i2, i3, i4);
                    oVar = this.l;
                    i5 = com.sysaac.haptic.base.r.i(h2);
                    jVar = new i(this, h2, i3, i4);
                } else if (a < 24) {
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

    public boolean a(int i) {
        if (!a.a(this.j.g)) {
            Log.e("RichtapPlayer", "pause_start_seek seekTo() return - HE invalid or prepare() not be called.");
            return false;
        }
        if (i >= 0 && i <= this.j.g.b()) {
            l();
            com.sysaac.haptic.base.d.a(this.d).a();
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
