package com.android.p019wm.shell.common;

import android.graphics.Rect;
import com.android.p019wm.shell.common.FloatingContentCoordinator;
import kotlin.Lazy;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, mo64987d2 = {"<anonymous>", "", "invoke", "()Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.common.FloatingContentCoordinator$Companion$findAreaForContentVertically$positionAboveInBounds$2 */
/* compiled from: FloatingContentCoordinator.kt */
final class C3435x994e5850 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ Rect $allowedBounds;
    final /* synthetic */ Lazy<Rect> $newContentBoundsAbove$delegate;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C3435x994e5850(Rect rect, Lazy<Rect> lazy) {
        super(0);
        this.$allowedBounds = rect;
        this.$newContentBoundsAbove$delegate = lazy;
    }

    public final Boolean invoke() {
        return Boolean.valueOf(this.$allowedBounds.contains(FloatingContentCoordinator.Companion.m3446findAreaForContentVertically$lambda2(this.$newContentBoundsAbove$delegate)));
    }
}
