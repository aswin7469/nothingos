package com.android.systemui.media.dialog;

import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputAdapter;

/* renamed from: com.android.systemui.media.dialog.MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2213xea444da2 implements CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ MediaOutputAdapter.MediaDeviceViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ C2213xea444da2(MediaOutputAdapter.MediaDeviceViewHolder mediaDeviceViewHolder, MediaDevice mediaDevice) {
        this.f$0 = mediaDeviceViewHolder;
        this.f$1 = mediaDevice;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.f$0.mo34250x2de61aea(this.f$1, compoundButton, z);
    }
}
