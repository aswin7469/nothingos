package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorSchemeTransition.kt */
/* synthetic */ class ColorSchemeTransition$textSecondary$1 extends FunctionReferenceImpl implements Function1<ColorScheme, Integer> {
    public static final ColorSchemeTransition$textSecondary$1 INSTANCE = new ColorSchemeTransition$textSecondary$1();

    ColorSchemeTransition$textSecondary$1() {
        super(1, MediaColorSchemesKt.class, "textSecondaryFromScheme", "textSecondaryFromScheme(Lcom/android/systemui/monet/ColorScheme;)I", 1);
    }

    public final Integer invoke(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "p0");
        return Integer.valueOf(MediaColorSchemesKt.textSecondaryFromScheme(colorScheme));
    }
}