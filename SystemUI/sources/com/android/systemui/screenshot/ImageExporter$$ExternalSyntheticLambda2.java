package com.android.systemui.screenshot;

import android.graphics.Bitmap;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import java.p026io.File;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ImageExporter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ImageExporter f$0;
    public final /* synthetic */ File f$1;
    public final /* synthetic */ Bitmap f$2;
    public final /* synthetic */ CallbackToFutureAdapter.Completer f$3;

    public /* synthetic */ ImageExporter$$ExternalSyntheticLambda2(ImageExporter imageExporter, File file, Bitmap bitmap, CallbackToFutureAdapter.Completer completer) {
        this.f$0 = imageExporter;
        this.f$1 = file;
        this.f$2 = bitmap;
        this.f$3 = completer;
    }

    public final void run() {
        this.f$0.mo37338x3ed16e75(this.f$1, this.f$2, this.f$3);
    }
}
