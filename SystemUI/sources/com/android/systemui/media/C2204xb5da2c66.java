package com.android.systemui.media;

import com.android.systemui.media.MediaTimeoutListener;

/* renamed from: com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2204xb5da2c66 implements Runnable {
    public final /* synthetic */ MediaTimeoutListener.PlaybackStateListener f$0;

    public /* synthetic */ C2204xb5da2c66(MediaTimeoutListener.PlaybackStateListener playbackStateListener) {
        this.f$0 = playbackStateListener;
    }

    public final void run() {
        MediaTimeoutListener.PlaybackStateListener.m2832processState$lambda0(this.f$0);
    }
}
