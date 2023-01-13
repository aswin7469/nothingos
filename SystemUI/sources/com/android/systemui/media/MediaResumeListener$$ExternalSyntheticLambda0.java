package com.android.systemui.media;

import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaResumeListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MediaResumeListener f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ MediaResumeListener$$ExternalSyntheticLambda0(MediaResumeListener mediaResumeListener, String str, List list) {
        this.f$0 = mediaResumeListener;
        this.f$1 = str;
        this.f$2 = list;
    }

    public final void run() {
        MediaResumeListener.m2818onMediaDataLoaded$lambda4(this.f$0, this.f$1, this.f$2);
    }
}
