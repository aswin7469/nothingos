package com.android.systemui;

import com.android.systemui.decor.DecorProvider;
import com.android.systemui.decor.OverlayWindow;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ ScreenDecorations f$0;
    public final /* synthetic */ OverlayWindow f$1;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda3(ScreenDecorations screenDecorations, OverlayWindow overlayWindow) {
        this.f$0 = screenDecorations;
        this.f$1 = overlayWindow;
    }

    public final void accept(Object obj) {
        this.f$0.m2524lambda$initOverlay$3$comandroidsystemuiScreenDecorations(this.f$1, (DecorProvider) obj);
    }
}
