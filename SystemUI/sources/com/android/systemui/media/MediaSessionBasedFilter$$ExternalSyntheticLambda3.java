package com.android.systemui.media;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaSessionBasedFilter$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ MediaSessionBasedFilter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ SmartspaceMediaData f$2;

    public /* synthetic */ MediaSessionBasedFilter$$ExternalSyntheticLambda3(MediaSessionBasedFilter mediaSessionBasedFilter, String str, SmartspaceMediaData smartspaceMediaData) {
        this.f$0 = mediaSessionBasedFilter;
        this.f$1 = str;
        this.f$2 = smartspaceMediaData;
    }

    public final void run() {
        MediaSessionBasedFilter.m2824onSmartspaceMediaDataLoaded$lambda7(this.f$0, this.f$1, this.f$2);
    }
}
