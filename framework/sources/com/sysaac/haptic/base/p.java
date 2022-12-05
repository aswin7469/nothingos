package com.sysaac.haptic.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
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
        int i2;
        try {
            switch (message.what) {
                case 101:
                    qVar = this.a.h;
                    qVar.a();
                    handler = this.a.g;
                    i = 102;
                    i2 = this.a.e;
                    break;
                case 102:
                    if (o.d(this.a) <= 0) {
                        return;
                    }
                    handler = this.a.g;
                    i = 101;
                    i2 = this.a.a;
                    break;
                default:
                    return;
            }
            handler.sendEmptyMessageDelayed(i, i2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
