package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ MediaController f$0;
    public final /* synthetic */ PlaybackState.CustomAction f$1;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda4(MediaController mediaController, PlaybackState.CustomAction customAction) {
        this.f$0 = mediaController;
        this.f$1 = customAction;
    }

    public final void run() {
        MediaDataManager.m2793getCustomAction$lambda32(this.f$0, this.f$1);
    }
}
