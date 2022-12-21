package com.android.systemui.dreams.touch;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.google.common.util.concurrent.ListenableFuture;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class HideComplicationTouchHandler$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ HideComplicationTouchHandler f$0;
    public final /* synthetic */ ListenableFuture f$1;
    public final /* synthetic */ DreamTouchHandler.TouchSession f$2;

    public /* synthetic */ HideComplicationTouchHandler$$ExternalSyntheticLambda0(HideComplicationTouchHandler hideComplicationTouchHandler, ListenableFuture listenableFuture, DreamTouchHandler.TouchSession touchSession) {
        this.f$0 = hideComplicationTouchHandler;
        this.f$1 = listenableFuture;
        this.f$2 = touchSession;
    }

    public final void run() {
        this.f$0.mo32654xd71ee26(this.f$1, this.f$2);
    }
}
