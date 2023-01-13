package com.android.systemui.statusbar;

import kotlin.Metadata;
import kotlin.ranges.RangesKt;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000 \b2\u00020\u0001:\u0001\bJ\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/LightRevealEffect;", "", "setRevealAmountOnScrim", "", "amount", "", "scrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightRevealScrim.kt */
public interface LightRevealEffect {
    public static final Companion Companion = Companion.$$INSTANCE;

    void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim);

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/LightRevealEffect$Companion;", "", "()V", "getPercentPastThreshold", "", "value", "threshold", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: LightRevealScrim.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }

        public final float getPercentPastThreshold(float f, float f2) {
            return RangesKt.coerceAtLeast(f - f2, 0.0f) * (1.0f / (1.0f - f2));
        }
    }
}
