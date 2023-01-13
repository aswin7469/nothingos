package com.android.systemui.media;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaSessionBasedFilter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MediaSessionBasedFilter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ MediaSessionBasedFilter$$ExternalSyntheticLambda0(MediaSessionBasedFilter mediaSessionBasedFilter, String str) {
        this.f$0 = mediaSessionBasedFilter;
        this.f$1 = str;
    }

    public final void run() {
        MediaSessionBasedFilter.m2824dispatchMediaDataRemoved$lambda13(this.f$0, this.f$1);
    }
}
