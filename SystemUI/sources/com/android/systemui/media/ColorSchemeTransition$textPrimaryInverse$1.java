package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorSchemeTransition.kt */
/* synthetic */ class ColorSchemeTransition$textPrimaryInverse$1 extends FunctionReferenceImpl implements Function1<ColorScheme, Integer> {
    public static final ColorSchemeTransition$textPrimaryInverse$1 INSTANCE = new ColorSchemeTransition$textPrimaryInverse$1();

    ColorSchemeTransition$textPrimaryInverse$1() {
        super(1, MediaColorSchemesKt.class, "textPrimaryInverseFromScheme", "textPrimaryInverseFromScheme(Lcom/android/systemui/monet/ColorScheme;)I", 1);
    }

    public final Integer invoke(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "p0");
        return Integer.valueOf(MediaColorSchemesKt.textPrimaryInverseFromScheme(colorScheme));
    }
}
