package com.android.systemui.recents;

import android.graphics.Rect;
import com.android.systemui.recents.OverviewProxyService;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda17 implements Function {
    public static final /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda17 INSTANCE = new OverviewProxyService$1$$ExternalSyntheticLambda17();

    private /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda17() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Rect lambda$getNonMinimizedSplitScreenSecondaryBounds$9;
        lambda$getNonMinimizedSplitScreenSecondaryBounds$9 = OverviewProxyService.AnonymousClass1.lambda$getNonMinimizedSplitScreenSecondaryBounds$9((LegacySplitScreen) obj);
        return lambda$getNonMinimizedSplitScreenSecondaryBounds$9;
    }
}
