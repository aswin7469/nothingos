package com.android.systemui.media.dialog;

import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputAdapter;

/* renamed from: com.android.systemui.media.dialog.MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2216xea444da5 implements CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ MediaOutputAdapter.MediaDeviceViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ C2216xea444da5(MediaOutputAdapter.MediaDeviceViewHolder mediaDeviceViewHolder, MediaDevice mediaDevice) {
        this.f$0 = mediaDeviceViewHolder;
        this.f$1 = mediaDevice;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.f$0.mo34253xf42ba307(this.f$1, compoundButton, z);
    }
}
