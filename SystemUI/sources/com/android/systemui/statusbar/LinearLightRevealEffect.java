package com.android.systemui.statusbar;

import android.util.MathUtils;
import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/LinearLightRevealEffect;", "Lcom/android/systemui/statusbar/LightRevealEffect;", "isVertical", "", "(Z)V", "INTERPOLATOR", "Landroid/view/animation/Interpolator;", "kotlin.jvm.PlatformType", "setRevealAmountOnScrim", "", "amount", "", "scrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightRevealScrim.kt */
public final class LinearLightRevealEffect implements LightRevealEffect {
    private static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Deprecated
    private static final float GRADIENT_START_BOUNDS_PERCENTAGE = 0.3f;
    @Deprecated
    private static final float REVEAL_GRADIENT_END_COLOR_ALPHA_START_PERCENTAGE = 0.6f;
    @Deprecated
    private static final float START_COLOR_REVEAL_PERCENTAGE = 0.3f;
    private final Interpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    private final boolean isVertical;

    public LinearLightRevealEffect(boolean z) {
        this.isVertical = z;
    }

    public void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(lightRevealScrim, "scrim");
        float interpolation = this.INTERPOLATOR.getInterpolation(f);
        lightRevealScrim.setInterpolatedRevealAmount(interpolation);
        lightRevealScrim.setStartColorAlpha(LightRevealEffect.Companion.getPercentPastThreshold(((float) 1) - interpolation, 0.7f));
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - LightRevealEffect.Companion.getPercentPastThreshold(interpolation, 0.6f));
        float lerp = MathUtils.lerp(0.3f, 1.0f, interpolation);
        if (this.isVertical) {
            lightRevealScrim.setRevealGradientBounds(((float) (lightRevealScrim.getWidth() / 2)) - (((float) (lightRevealScrim.getWidth() / 2)) * lerp), 0.0f, ((float) (lightRevealScrim.getWidth() / 2)) + (((float) (lightRevealScrim.getWidth() / 2)) * lerp), (float) lightRevealScrim.getHeight());
        } else {
            lightRevealScrim.setRevealGradientBounds(0.0f, ((float) (lightRevealScrim.getHeight() / 2)) - (((float) (lightRevealScrim.getHeight() / 2)) * lerp), (float) lightRevealScrim.getWidth(), ((float) (lightRevealScrim.getHeight() / 2)) + (((float) (lightRevealScrim.getHeight() / 2)) * lerp));
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/LinearLightRevealEffect$Companion;", "", "()V", "GRADIENT_START_BOUNDS_PERCENTAGE", "", "REVEAL_GRADIENT_END_COLOR_ALPHA_START_PERCENTAGE", "START_COLOR_REVEAL_PERCENTAGE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: LightRevealScrim.kt */
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
