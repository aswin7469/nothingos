package com.sysaac.haptic.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.constraintlayout.widget.R$styleable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class p extends Handler {
    final /* synthetic */ o a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(o oVar, Looper looper) {
        super(looper);
        this.a = oVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        q qVar;
        Handler handler;
        int i;
        Handler handler2;
        int i2;
        try {
            int i3 = message.what;
            if (i3 == 101) {
                qVar = this.a.h;
                qVar.a();
                handler = this.a.g;
                i = this.a.e;
                handler.sendEmptyMessageDelayed(R$styleable.Constraint_layout_goneMarginStart, i);
            } else if (i3 == 102 && o.d(this.a) > 0) {
                handler2 = this.a.g;
                i2 = this.a.a;
                handler2.sendEmptyMessageDelayed(R$styleable.Constraint_layout_goneMarginRight, i2);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
