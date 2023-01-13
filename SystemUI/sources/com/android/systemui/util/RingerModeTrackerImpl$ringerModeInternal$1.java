package com.android.systemui.util;

import android.media.AudioManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RingerModeTrackerImpl.kt */
/* synthetic */ class RingerModeTrackerImpl$ringerModeInternal$1 extends FunctionReferenceImpl implements Function0<Integer> {
    RingerModeTrackerImpl$ringerModeInternal$1(Object obj) {
        super(0, obj, AudioManager.class, "getRingerModeInternal", "getRingerModeInternal()I", 0);
    }

    public final Integer invoke() {
        return Integer.valueOf(((AudioManager) this.receiver).getRingerModeInternal());
    }
}
