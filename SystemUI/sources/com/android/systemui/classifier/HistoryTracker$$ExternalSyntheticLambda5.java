package com.android.systemui.classifier;

import com.android.systemui.classifier.HistoryTracker;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda5 implements Function {
    public final /* synthetic */ double f$0;

    public /* synthetic */ HistoryTracker$$ExternalSyntheticLambda5(double d) {
        this.f$0 = d;
    }

    public final Object apply(Object obj) {
        return Double.valueOf(Math.pow(((HistoryTracker.CombinedResult) obj).getScore() - this.f$0, 2.0d));
    }
}
