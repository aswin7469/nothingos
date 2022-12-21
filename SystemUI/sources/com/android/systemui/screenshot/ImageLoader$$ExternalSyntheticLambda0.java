package com.android.systemui.screenshot;

import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ImageLoader$$ExternalSyntheticLambda0 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ ImageLoader f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ ImageLoader$$ExternalSyntheticLambda0(ImageLoader imageLoader, Uri uri) {
        this.f$0 = imageLoader;
        this.f$1 = uri;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return this.f$0.m2997lambda$load$0$comandroidsystemuiscreenshotImageLoader(this.f$1, completer);
    }
}
