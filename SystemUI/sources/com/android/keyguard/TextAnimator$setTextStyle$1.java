package com.android.keyguard;

import android.graphics.Typeface;
import com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationModule;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Landroid/graphics/Typeface;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TextAnimator.kt */
final class TextAnimator$setTextStyle$1 extends Lambda implements Function0<Typeface> {
    final /* synthetic */ int $weight;
    final /* synthetic */ TextAnimator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    TextAnimator$setTextStyle$1(TextAnimator textAnimator, int i) {
        super(0);
        this.this$0 = textAnimator;
        this.$weight = i;
    }

    public final Typeface invoke() {
        this.this$0.getTextInterpolator$SystemUI_nothingRelease().getTargetPaint().setFontVariationSettings(DreamClockTimeComplicationModule.TAG_WEIGHT + this.$weight);
        return this.this$0.getTextInterpolator$SystemUI_nothingRelease().getTargetPaint().getTypeface();
    }
}
