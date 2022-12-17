package com.android.settings.datausage;

import com.android.settingslib.net.NetworkCycleData;
import java.util.function.ToLongFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ChartDataUsagePreference$$ExternalSyntheticLambda0 implements ToLongFunction {
    public final long applyAsLong(Object obj) {
        return ((NetworkCycleData) obj).getTotalUsage();
    }
}
