package com.sysaac.haptic;

import android.util.Log;
import com.sysaac.haptic.player.f;
import java.io.File;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class d implements Runnable {
    final /* synthetic */ File a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ int d;
    final /* synthetic */ int e;
    final /* synthetic */ AACHapticUtils f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(AACHapticUtils aACHapticUtils, File file, int i, int i2, int i3, int i4) {
        this.f = aACHapticUtils;
        this.a = file;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = i4;
    }

    @Override // java.lang.Runnable
    public void run() {
        f fVar;
        f fVar2;
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
