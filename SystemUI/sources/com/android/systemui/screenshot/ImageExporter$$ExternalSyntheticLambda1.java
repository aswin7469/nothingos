package com.android.systemui.screenshot;

import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ImageExporter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ImageExporter f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ CallbackToFutureAdapter.Completer f$2;

    public /* synthetic */ ImageExporter$$ExternalSyntheticLambda1(ImageExporter imageExporter, Uri uri, CallbackToFutureAdapter.Completer completer) {
        this.f$0 = imageExporter;
        this.f$1 = uri;
        this.f$2 = completer;
    }

    public final void run() {
        this.f$0.m2995lambda$delete$4$comandroidsystemuiscreenshotImageExporter(this.f$1, this.f$2);
    }
}
