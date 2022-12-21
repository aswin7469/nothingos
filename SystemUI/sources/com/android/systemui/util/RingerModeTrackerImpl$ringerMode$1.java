package com.android.systemui.util;

import android.media.AudioManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RingerModeTrackerImpl.kt */
/* synthetic */ class RingerModeTrackerImpl$ringerMode$1 extends FunctionReferenceImpl implements Function0<Integer> {
    RingerModeTrackerImpl$ringerMode$1(Object obj) {
        super(0, obj, AudioManager.class, "getRingerMode", "getRingerMode()I", 0);
    }

    public final Integer invoke() {
        return Integer.valueOf(((AudioManager) this.receiver).getRingerMode());
    }
}
