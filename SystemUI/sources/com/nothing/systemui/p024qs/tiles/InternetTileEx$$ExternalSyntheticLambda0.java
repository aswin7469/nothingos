package com.nothing.systemui.p024qs.tiles;

import android.view.Window;

/* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class InternetTileEx$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Window f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ InternetTileEx$$ExternalSyntheticLambda0(Window window, int i, int i2) {
        this.f$0 = window;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        InternetTileEx.m3538updateWindowSize$lambda6(this.f$0, this.f$1, this.f$2);
    }
}
