package com.android.systemui.theme;

import android.content.om.OverlayInfo;
import java.util.Set;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda3(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return this.f$0.contains(((OverlayInfo) obj).category);
    }
}
