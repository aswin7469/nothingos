package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaTttChipControllerReceiver$$ExternalSyntheticLambda0 implements Icon.OnDrawableLoadedListener {
    public final /* synthetic */ MediaTttChipControllerReceiver f$0;
    public final /* synthetic */ MediaRoute2Info f$1;
    public final /* synthetic */ CharSequence f$2;

    public /* synthetic */ MediaTttChipControllerReceiver$$ExternalSyntheticLambda0(MediaTttChipControllerReceiver mediaTttChipControllerReceiver, MediaRoute2Info mediaRoute2Info, CharSequence charSequence) {
        this.f$0 = mediaTttChipControllerReceiver;
        this.f$1 = mediaRoute2Info;
        this.f$2 = charSequence;
    }

    public final void onDrawableLoaded(Drawable drawable) {
        MediaTttChipControllerReceiver.m2849updateMediaTapToTransferReceiverDisplay$lambda0(this.f$0, this.f$1, this.f$2, drawable);
    }
}
