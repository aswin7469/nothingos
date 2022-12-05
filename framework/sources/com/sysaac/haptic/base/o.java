package com.sysaac.haptic.base;

import android.os.Handler;
import android.os.HandlerThread;
/* loaded from: classes4.dex */
public class o {
    private static final int i = 101;
    private static final int j = 102;
    private static final String k = "RepeatExecutor";
    private int a;
    private int b;
    private int c = 255;
    private int d = 0;
    private int e;
    private HandlerThread f;
    private Handler g;
    private q h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int d(o oVar) {
        int i2 = oVar.b - 1;
        oVar.b = i2;
        return i2;
    }

    public synchronized int a() {
        return this.c;
    }

    public synchronized void a(int i2) {
        this.a = i2;
    }

    public synchronized void a(int i2, int i3, int i4) {
        if (i2 >= 0) {
            try {
                this.a = i2;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (i3 >= 0 && i3 <= 255) {
            this.c = i3;
        }
        this.d = i4;
    }

    public synchronized void a(int i2, int i3, int i4, q qVar) {
        if (i2 < 1 || i4 < 0 || qVar == null) {
            return;
        }
        try {
            this.b = i2;
            this.a = i3;
            this.e = i4;
            this.h = qVar;
            HandlerThread handlerThread = new HandlerThread(k);
            this.f = handlerThread;
            handlerThread.start();
            p pVar = new p(this, this.f.getLooper());
            this.g = pVar;
            pVar.sendEmptyMessage(101);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public synchronized int b() {
        return this.d;
    }

    public synchronized void c() {
        try {
            HandlerThread handlerThread = this.f;
            if (handlerThread != null) {
                handlerThread.quit();
                this.f = null;
            }
            this.g = null;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
