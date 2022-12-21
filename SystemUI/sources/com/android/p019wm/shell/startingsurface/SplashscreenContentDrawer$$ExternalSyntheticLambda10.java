package com.android.p019wm.shell.startingsurface;

import android.graphics.drawable.Drawable;
import java.util.function.IntSupplier;

/* renamed from: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda10 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SplashscreenContentDrawer$$ExternalSyntheticLambda10 implements IntSupplier {
    public final /* synthetic */ Drawable f$0;

    public /* synthetic */ SplashscreenContentDrawer$$ExternalSyntheticLambda10(Drawable drawable) {
        this.f$0 = drawable;
    }

    public final int getAsInt() {
        return SplashscreenContentDrawer.estimateWindowBGColor(this.f$0);
    }
}
