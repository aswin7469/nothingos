package com.android.systemui.dreams.touch;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ DreamOverlayTouchMonitor f$0;
    public final /* synthetic */ DreamOverlayTouchMonitor.TouchSessionImpl f$1;
    public final /* synthetic */ CallbackToFutureAdapter.Completer f$2;

    public /* synthetic */ DreamOverlayTouchMonitor$$ExternalSyntheticLambda5(DreamOverlayTouchMonitor dreamOverlayTouchMonitor, DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) {
        this.f$0 = dreamOverlayTouchMonitor;
        this.f$1 = touchSessionImpl;
        this.f$2 = completer;
    }

    public final void run() {
        this.f$0.mo32637x70ba854(this.f$1, this.f$2);
    }
}
