package com.sysaac.haptic;

import android.util.Log;
/* loaded from: classes4.dex */
class b implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ int b;
    final /* synthetic */ AACHapticUtils c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(AACHapticUtils aACHapticUtils, int i, int i2) {
        this.c = aACHapticUtils;
        this.a = i;
        this.b = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.sysaac.haptic.player.f fVar;
        com.sysaac.haptic.player.f fVar2;
        fVar = this.c.mPlayer;
        if (fVar == null) {
            Log.w("AACHapticUtils", "mPlayer == null");
            return;
        }
        try {
            fVar2 = this.c.mPlayer;
            fVar2.b(this.a, this.b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
