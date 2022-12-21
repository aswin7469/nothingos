package com.android.systemui.media;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaSessionBasedFilter$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ MediaSessionBasedFilter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ MediaData f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ MediaSessionBasedFilter$$ExternalSyntheticLambda7(MediaSessionBasedFilter mediaSessionBasedFilter, String str, String str2, MediaData mediaData, boolean z) {
        this.f$0 = mediaSessionBasedFilter;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = mediaData;
        this.f$4 = z;
    }

    public final void run() {
        MediaSessionBasedFilter.m2818dispatchMediaDataLoaded$lambda11(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
