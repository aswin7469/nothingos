package com.android.systemui.media.dialog;

import android.view.View;

/* renamed from: com.android.systemui.media.dialog.MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda8 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2216xea444da8 implements View.OnClickListener {
    public final /* synthetic */ MediaOutputController f$0;

    public /* synthetic */ C2216xea444da8(MediaOutputController mediaOutputController) {
        this.f$0 = mediaOutputController;
    }

    public final void onClick(View view) {
        this.f$0.launchBluetoothPairing(view);
    }
}
