package com.android.systemui.media;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaSessionBasedFilter$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ MediaSessionBasedFilter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ MediaSessionBasedFilter$$ExternalSyntheticLambda5(MediaSessionBasedFilter mediaSessionBasedFilter, String str, boolean z) {
        this.f$0 = mediaSessionBasedFilter;
        this.f$1 = str;
        this.f$2 = z;
    }

    public final void run() {
        MediaSessionBasedFilter.m2821dispatchSmartspaceMediaDataRemoved$lambda17(this.f$0, this.f$1, this.f$2);
    }
}
