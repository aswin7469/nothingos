package com.sysaac.haptic.base;

import android.os.Handler;
import android.os.HandlerThread;
import androidx.constraintlayout.widget.R$styleable;
/* loaded from: classes2.dex */
public class o {
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
        int i = oVar.b - 1;
        oVar.b = i;
        return i;
    }

    public synchronized int a() {
        return this.c;
    }

    public synchronized void a(int i, int i2, int i3) {
        if (i >= 0) {
            try {
                this.a = i;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (i2 >= 0 && i2 <= 255) {
            this.c = i2;
        }
        this.d = i3;
    }

    public synchronized void a(int i, int i2, int i3, q qVar) {
        if (i < 1 || i3 < 0 || qVar == null) {
            return;
        }
        try {
            this.b = i;
            this.a = i2;
            this.e = i3;
            this.h = qVar;
            HandlerThread handlerThread = new HandlerThread("RepeatExecutor");
            this.f = handlerThread;
            handlerThread.start();
            p pVar = new p(this, this.f.getLooper());
            this.g = pVar;
            pVar.sendEmptyMessage(R$styleable.Constraint_layout_goneMarginRight);
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
