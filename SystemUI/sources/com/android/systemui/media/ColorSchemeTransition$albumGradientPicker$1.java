package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import com.android.systemui.util.ColorUtilKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "scheme", "Lcom/android/systemui/monet/ColorScheme;", "invoke", "(Lcom/android/systemui/monet/ColorScheme;)Ljava/lang/Integer;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorSchemeTransition.kt */
final class ColorSchemeTransition$albumGradientPicker$1 extends Lambda implements Function1<ColorScheme, Integer> {
    final /* synthetic */ Function1<ColorScheme, Integer> $inner;
    final /* synthetic */ float $targetAlpha;
    final /* synthetic */ ColorSchemeTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ColorSchemeTransition$albumGradientPicker$1(ColorSchemeTransition colorSchemeTransition, Function1<? super ColorScheme, Integer> function1, float f) {
        super(1);
        this.this$0 = colorSchemeTransition;
        this.$inner = function1;
        this.$targetAlpha = f;
    }

    public final Integer invoke(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return Integer.valueOf(this.this$0.isGradientEnabled ? ColorUtilKt.getColorWithAlpha(this.$inner.invoke(colorScheme).intValue(), this.$targetAlpha) : 0);
    }
}
