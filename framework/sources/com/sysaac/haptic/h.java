package com.sysaac.haptic;

import android.util.Log;
/* loaded from: classes4.dex */
class h implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ AACHapticUtils d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(AACHapticUtils aACHapticUtils, int i, int i2, int i3) {
        this.d = aACHapticUtils;
        this.a = i;
        this.b = i2;
        this.c = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.sysaac.haptic.player.f fVar;
        com.sysaac.haptic.player.f fVar2;
        fVar = this.d.mPlayer;
        if (fVar == null) {
            Log.d("AACHapticUtils", "mPlayer == null");
            return;
        }
        try {
            fVar2 = this.d.mPlayer;
            fVar2.a(this.a, this.b, this.c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
