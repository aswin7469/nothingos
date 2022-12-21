package com.android.systemui.screenshot;

import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ImageExporter$$ExternalSyntheticLambda0 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ ImageExporter f$0;
    public final /* synthetic */ Executor f$1;
    public final /* synthetic */ Uri f$2;

    public /* synthetic */ ImageExporter$$ExternalSyntheticLambda0(ImageExporter imageExporter, Executor executor, Uri uri) {
        this.f$0 = imageExporter;
        this.f$1 = executor;
        this.f$2 = uri;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return this.f$0.m2996lambda$delete$5$comandroidsystemuiscreenshotImageExporter(this.f$1, this.f$2, completer);
    }
}
