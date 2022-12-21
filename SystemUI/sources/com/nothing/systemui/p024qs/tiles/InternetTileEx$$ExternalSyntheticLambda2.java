package com.nothing.systemui.p024qs.tiles;

import android.view.Window;

/* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class InternetTileEx$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Window f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ InternetTileEx$$ExternalSyntheticLambda2(Window window, int i, int i2) {
        this.f$0 = window;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        InternetTileEx.m3522updateWindowSize$lambda2(this.f$0, this.f$1, this.f$2);
    }
}
