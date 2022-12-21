package com.android.systemui.screenshot;

import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScrollCaptureController$$ExternalSyntheticLambda4 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ ScrollCaptureController f$0;
    public final /* synthetic */ ScrollCaptureResponse f$1;

    public /* synthetic */ ScrollCaptureController$$ExternalSyntheticLambda4(ScrollCaptureController scrollCaptureController, ScrollCaptureResponse scrollCaptureResponse) {
        this.f$0 = scrollCaptureController;
        this.f$1 = scrollCaptureResponse;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return this.f$0.mo37593xfbe3cca5(this.f$1, completer);
    }
}
