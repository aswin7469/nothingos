package com.sysaac.haptic.player;

import android.os.DynamicEffect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.HapticPlayer;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
import com.sysaac.haptic.sync.SyncCallback;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class r extends Handler {
    int a = 0;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ l d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(l lVar, Looper looper, int i, int i2) {
        super(looper);
        this.d = lVar;
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
        com.sysaac.haptic.base.o oVar;
        int i;
        com.sysaac.haptic.base.o oVar2;
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
                            case R$styleable.Constraint_layout_goneMarginLeft /* 100 */:
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
                            case R$styleable.Constraint_layout_goneMarginRight /* 101 */:
                                Object obj = message.obj;
                                if (!(obj instanceof Parcel)) {
                                    return;
                                }
                                Parcel parcel = (Parcel) obj;
                                syncCallback3 = this.d.i;
                                if (syncCallback3 != null) {
                                    syncCallback4 = this.d.i;
                                    syncCallback4.getCurrentPosition();
                                }
                                com.sysaac.haptic.sync.b bVar = new com.sysaac.haptic.sync.b(parcel);
                                String str = "{\"Metadata\": {\"Version\": 1}," + bVar.a + "}";
                                if (HapticPlayer.isAvailable()) {
                                    this.d.a = new HapticPlayer(DynamicEffect.create(str));
                                    try {
                                        l lVar = this.d;
                                        HapticPlayer hapticPlayer = lVar.a;
                                        oVar = lVar.l;
                                        if (oVar != null) {
                                            oVar2 = this.d.l;
                                            i = oVar2.a();
                                        } else {
                                            i = this.b;
                                        }
                                        hapticPlayer.start(1, 0, i, this.c);
                                    } catch (NoSuchMethodError unused) {
                                        Log.w("TencentPlayer", "[patternString, looper,interval,amplitude,freq],haven't integrate Haptic player 1.1 !!!!!!!now we use Haptic player 1.0 to start vibrate");
                                        this.d.a.start(1);
                                    }
                                } else {
                                    Log.e("TencentPlayer", "The system does not support HapticsPlayer");
                                }
                                parcel.recycle();
                                return;
                            case R$styleable.Constraint_layout_goneMarginStart /* 102 */:
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
                                        handler3.sendEmptyMessageDelayed(R$styleable.Constraint_layout_goneMarginStart, 10L);
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
            Log.d("TencentPlayer", "after stopPatternListIfNeeded ...");
        } catch (Exception e) {
            Log.w("TencentPlayer", "ex in handleMessage:" + e.toString());
        }
    }
}
