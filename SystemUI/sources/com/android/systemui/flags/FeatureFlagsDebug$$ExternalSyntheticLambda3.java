package com.android.systemui.flags;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FeatureFlagsDebug$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ FeatureFlagsDebug f$0;

    public /* synthetic */ FeatureFlagsDebug$$ExternalSyntheticLambda3(FeatureFlagsDebug featureFlagsDebug) {
        this.f$0 = featureFlagsDebug;
    }

    public final void accept(Object obj) {
        this.f$0.restartAndroid(((Boolean) obj).booleanValue());
    }
}
