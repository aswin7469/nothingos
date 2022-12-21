package com.android.systemui.theme;

import android.content.om.OverlayInfo;
import java.util.Map;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda4(Map map) {
        this.f$0 = map;
    }

    public final boolean test(Object obj) {
        return ThemeOverlayApplier.lambda$applyCurrentUserOverlays$4(this.f$0, (OverlayInfo) obj);
    }
}
