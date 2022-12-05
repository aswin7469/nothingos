package com.android.settings.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import com.android.settings.R;
import com.android.settings.widget.SeekBarPreference;
/* loaded from: classes.dex */
public class BADeviceVolumePreference extends SeekBarPreference {
    public BADeviceVolumePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.preference_ba_device_volume_slider);
    }

    public BADeviceVolumePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.preference_ba_device_volume_slider);
    }

    public BADeviceVolumePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.preference_ba_device_volume_slider);
    }

    public BADeviceVolumePreference(Context context) {
        super(context);
        setLayoutResource(R.layout.preference_ba_device_volume_slider);
    }
}
