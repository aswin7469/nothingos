package com.android.systemui.util;

import androidx.lifecycle.LiveData;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\u0006ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/util/RingerModeTracker;", "", "ringerMode", "Landroidx/lifecycle/LiveData;", "", "getRingerMode", "()Landroidx/lifecycle/LiveData;", "ringerModeInternal", "getRingerModeInternal", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RingerModeTracker.kt */
public interface RingerModeTracker {
    LiveData<Integer> getRingerMode();

    LiveData<Integer> getRingerModeInternal();
}
