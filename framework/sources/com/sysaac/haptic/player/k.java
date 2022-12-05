package com.sysaac.haptic.player;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import com.sysaac.haptic.sync.SyncCallback;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class k extends Handler {
    int a = 0;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ g d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k(g gVar, Looper looper, int i, int i2) {
        super(looper);
        this.d = gVar;
        this.b = i;
        this.c = i2;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Handler handler;
        HandlerThread handlerThread;
        com.sysaac.haptic.sync.d dVar;
        SyncCallback syncCallback;
        com.sysaac.haptic.sync.d dVar2;
        com.sysaac.haptic.sync.d dVar3;
        SyncCallback syncCallback2;
        Handler handler2;
        a aVar;
        com.sysaac.haptic.sync.d dVar4;
        com.sysaac.haptic.sync.d dVar5;
        SyncCallback syncCallback3;
        Context context;
        com.sysaac.haptic.base.o oVar;
        int i;
        com.sysaac.haptic.base.o oVar2;
        Context context2;
        com.sysaac.haptic.base.o oVar3;
        int i2;
        com.sysaac.haptic.base.o oVar4;
        SyncCallback syncCallback4;
        a aVar2;
        a aVar3;
        PlayerEventCallback playerEventCallback;
        PlayerEventCallback playerEventCallback2;
        SyncCallback syncCallback5;
        SyncCallback syncCallback6;
        a aVar4;
        Handler handler3;
        try {
            handler = this.d.f;
            if (handler != null) {
                handlerThread = this.d.g;
                if (handlerThread != null) {
                    dVar = this.d.h;
                    if (dVar != null) {
                        switch (message.what) {
                            case 100:
                                long longValue = ((Long) message.obj).longValue();
                                syncCallback = this.d.i;
                                if (syncCallback == null) {
                                    dVar2 = this.d.h;
                                    dVar2.b(longValue);
                                    dVar3 = this.d.h;
                                    dVar3.a(longValue, longValue);
                                    return;
                                }
                                syncCallback2 = this.d.i;
                                int currentPosition = syncCallback2.getCurrentPosition();
                                if (currentPosition <= 0 || currentPosition < this.a) {
                                    if (currentPosition >= 0 && currentPosition < this.a) {
                                        aVar = this.d.j;
                                        if (aVar.c > 0) {
                                            this.d.a(currentPosition);
                                        }
                                    }
                                    handler2 = this.d.f;
                                    handler2.sendMessage(Message.obtain(message));
                                } else {
                                    dVar4 = this.d.h;
                                    long j = currentPosition;
                                    dVar4.b(j);
                                    dVar5 = this.d.h;
                                    dVar5.a(j, longValue);
                                }
                                this.a = currentPosition;
                                return;
                            case 101:
                                if (!(message.obj instanceof Parcel)) {
                                    return;
                                }
                                Parcel parcel = (Parcel) message.obj;
                                syncCallback3 = this.d.i;
                                if (syncCallback3 != null) {
                                    syncCallback4 = this.d.i;
                                    syncCallback4.getCurrentPosition();
                                }
                                com.sysaac.haptic.sync.b bVar = new com.sysaac.haptic.sync.b(parcel);
                                Log.d("RichtapPlayer", "current pattern:" + bVar.a);
                                if (com.sysaac.haptic.base.r.a() >= 24) {
                                    String replace = com.sysaac.haptic.base.r.a.replace("ReplaceMe", bVar.a);
                                    context2 = this.d.d;
                                    com.sysaac.haptic.base.d a = com.sysaac.haptic.base.d.a(context2);
                                    oVar3 = this.d.l;
                                    if (oVar3 != null) {
                                        oVar4 = this.d.l;
                                        i2 = oVar4.a();
                                    } else {
                                        i2 = this.b;
                                    }
                                    a.b(replace, 1, 0, i2, this.c);
                                } else {
                                    String str = "{\"Metadata\": {\"Version\": 1}," + bVar.a + "}";
                                    context = this.d.d;
                                    com.sysaac.haptic.base.d a2 = com.sysaac.haptic.base.d.a(context);
                                    oVar = this.d.l;
                                    if (oVar != null) {
                                        oVar2 = this.d.l;
                                        i = oVar2.a();
                                    } else {
                                        i = this.b;
                                    }
                                    a2.a(str, 1, 0, i, this.c);
                                }
                                parcel.recycle();
                                return;
                            case 102:
                                aVar2 = this.d.j;
                                if (aVar2.c <= 0) {
                                    aVar3 = this.d.j;
                                    aVar3.k = 9;
                                    playerEventCallback = this.d.k;
                                    if (playerEventCallback == null) {
                                        return;
                                    }
                                    playerEventCallback2 = this.d.k;
                                    playerEventCallback2.onPlayerStateChanged(9);
                                    return;
                                }
                                syncCallback5 = this.d.i;
                                if (syncCallback5 != null) {
                                    syncCallback6 = this.d.i;
                                    int currentPosition2 = syncCallback6.getCurrentPosition();
                                    aVar4 = this.d.j;
                                    if (currentPosition2 > com.sysaac.haptic.base.r.j(aVar4.a)) {
                                        handler3 = this.d.f;
                                        handler3.sendEmptyMessageDelayed(102, 10L);
                                        return;
                                    }
                                }
                                this.d.a(0);
                                return;
                            default:
                                return;
                        }
                    }
                }
            }
            Log.d("RichtapPlayer", "after stopPatternListIfNeeded ...");
        } catch (Exception e) {
            Log.w("RichtapPlayer", "ex in handleMessage:" + e.toString());
        }
    }
}
