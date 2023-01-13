package com.android.systemui.media;

import com.android.systemui.media.MediaDeviceManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDeviceManager$Entry$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MediaDeviceManager f$0;
    public final /* synthetic */ MediaDeviceManager.Entry f$1;
    public final /* synthetic */ MediaDeviceData f$2;

    public /* synthetic */ MediaDeviceManager$Entry$$ExternalSyntheticLambda1(MediaDeviceManager mediaDeviceManager, MediaDeviceManager.Entry entry, MediaDeviceData mediaDeviceData) {
        this.f$0 = mediaDeviceManager;
        this.f$1 = entry;
        this.f$2 = mediaDeviceData;
    }

    public final void run() {
        MediaDeviceManager.Entry.m2809_set_current_$lambda0(this.f$0, this.f$1, this.f$2);
    }
}
