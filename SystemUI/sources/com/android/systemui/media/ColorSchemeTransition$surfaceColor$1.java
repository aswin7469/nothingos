package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorSchemeTransition.kt */
/* synthetic */ class ColorSchemeTransition$surfaceColor$1 extends FunctionReferenceImpl implements Function1<ColorScheme, Integer> {
    public static final ColorSchemeTransition$surfaceColor$1 INSTANCE = new ColorSchemeTransition$surfaceColor$1();

    ColorSchemeTransition$surfaceColor$1() {
        super(1, MediaColorSchemesKt.class, "surfaceFromScheme", "surfaceFromScheme(Lcom/android/systemui/monet/ColorScheme;)I", 1);
    }

    public final Integer invoke(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "p0");
        return Integer.valueOf(MediaColorSchemesKt.surfaceFromScheme(colorScheme));
    }
}
