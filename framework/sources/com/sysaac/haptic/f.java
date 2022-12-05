package com.sysaac.haptic;

import android.util.Log;
import com.sysaac.haptic.sync.SyncCallback;
/* loaded from: classes4.dex */
class f implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ SyncCallback d;
    final /* synthetic */ AACHapticUtils e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(AACHapticUtils aACHapticUtils, String str, int i, int i2, SyncCallback syncCallback) {
        this.e = aACHapticUtils;
        this.a = str;
        this.b = i;
        this.c = i2;
        this.d = syncCallback;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.sysaac.haptic.player.f fVar;
        com.sysaac.haptic.player.f fVar2;
        fVar = this.e.mPlayer;
        if (fVar == null) {
            Log.d("AACHapticUtils", "mPlayer == null");
            return;
        }
        try {
            fVar2 = this.e.mPlayer;
            fVar2.a(this.a, this.b, this.c, this.d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
