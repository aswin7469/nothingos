package com.android.p019wm.shell.startingsurface;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.window.StartingWindowInfo;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplashscreenContentDrawer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SplashscreenContentDrawer f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ StartingWindowInfo f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ Consumer f$4;
    public final /* synthetic */ Drawable f$5;
    public final /* synthetic */ Consumer f$6;

    public /* synthetic */ SplashscreenContentDrawer$$ExternalSyntheticLambda0(SplashscreenContentDrawer splashscreenContentDrawer, Context context, StartingWindowInfo startingWindowInfo, int i, Consumer consumer, Drawable drawable, Consumer consumer2) {
        this.f$0 = splashscreenContentDrawer;
        this.f$1 = context;
        this.f$2 = startingWindowInfo;
        this.f$3 = i;
        this.f$4 = consumer;
        this.f$5 = drawable;
        this.f$6 = consumer2;
    }

    public final void run() {
        this.f$0.mo51119xfe08d91f(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
