package com.android.systemui.util;

import androidx.lifecycle.LiveData;
import org.jetbrains.annotations.NotNull;
/* compiled from: RingerModeTracker.kt */
/* loaded from: classes2.dex */
public interface RingerModeTracker {
    @NotNull
    LiveData<Integer> getRingerMode();

    @NotNull
    LiveData<Integer> getRingerModeInternal();
}
