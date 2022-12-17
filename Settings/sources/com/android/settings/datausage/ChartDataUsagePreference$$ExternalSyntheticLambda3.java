package com.android.settings.datausage;

import com.android.settings.datausage.ChartDataUsagePreference;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ChartDataUsagePreference$$ExternalSyntheticLambda3 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((ChartDataUsagePreference.DataUsageSummaryNode) obj).getDataUsagePercentage();
    }
}
