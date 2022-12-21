package com.android.systemui.controls.p010ui;

import android.os.VibrationEffect;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\u0004H\u0002J\b\u0010\n\u001a\u00020\u0004H\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/Vibrations;", "", "()V", "rangeEdgeEffect", "Landroid/os/VibrationEffect;", "getRangeEdgeEffect", "()Landroid/os/VibrationEffect;", "rangeMiddleEffect", "getRangeMiddleEffect", "initRangeEdgeEffect", "initRangeMiddleEffect", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.Vibrations */
/* compiled from: Vibrations.kt */
public final class Vibrations {
    public static final Vibrations INSTANCE;
    private static final VibrationEffect rangeEdgeEffect;
    private static final VibrationEffect rangeMiddleEffect;

    private Vibrations() {
    }

    static {
        Vibrations vibrations = new Vibrations();
        INSTANCE = vibrations;
        rangeEdgeEffect = vibrations.initRangeEdgeEffect();
        rangeMiddleEffect = vibrations.initRangeMiddleEffect();
    }

    public final VibrationEffect getRangeEdgeEffect() {
        return rangeEdgeEffect;
    }

    public final VibrationEffect getRangeMiddleEffect() {
        return rangeMiddleEffect;
    }

    private final VibrationEffect initRangeEdgeEffect() {
        VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
        startComposition.addPrimitive(7, 0.5f);
        VibrationEffect compose = startComposition.compose();
        Intrinsics.checkNotNullExpressionValue(compose, "composition.compose()");
        return compose;
    }

    private final VibrationEffect initRangeMiddleEffect() {
        VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
        startComposition.addPrimitive(7, 0.1f);
        VibrationEffect compose = startComposition.compose();
        Intrinsics.checkNotNullExpressionValue(compose, "composition.compose()");
        return compose;
    }
}
