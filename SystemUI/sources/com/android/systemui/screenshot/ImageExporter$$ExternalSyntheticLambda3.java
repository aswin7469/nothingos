package com.android.systemui.screenshot;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.screenshot.ImageExporter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ImageExporter$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ CallbackToFutureAdapter.Completer f$0;
    public final /* synthetic */ ImageExporter.Task f$1;

    public /* synthetic */ ImageExporter$$ExternalSyntheticLambda3(CallbackToFutureAdapter.Completer completer, ImageExporter.Task task) {
        this.f$0 = completer;
        this.f$1 = task;
    }

    public final void run() {
        ImageExporter.lambda$export$2(this.f$0, this.f$1);
    }
}
