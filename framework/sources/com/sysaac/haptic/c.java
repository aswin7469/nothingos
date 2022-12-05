package com.sysaac.haptic;

import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class c implements Runnable {
    final /* synthetic */ int[] a;
    final /* synthetic */ int[] b;
    final /* synthetic */ int[] c;
    final /* synthetic */ boolean d;
    final /* synthetic */ int e;
    final /* synthetic */ AACHapticUtils f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(AACHapticUtils aACHapticUtils, int[] iArr, int[] iArr2, int[] iArr3, boolean z, int i) {
        this.f = aACHapticUtils;
        this.a = iArr;
        this.b = iArr2;
        this.c = iArr3;
        this.d = z;
        this.e = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.sysaac.haptic.player.f fVar;
        com.sysaac.haptic.player.f fVar2;
        fVar = this.f.mPlayer;
        if (fVar == null) {
            Log.w("AACHapticUtils", "mPlayer == null");
            return;
        }
        try {
            fVar2 = this.f.mPlayer;
            fVar2.a(this.a, this.b, this.c, this.d, this.e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
