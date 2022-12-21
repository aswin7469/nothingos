package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\t\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0010\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000¨\u0006\f"}, mo64987d2 = {"accentPrimaryFromScheme", "", "scheme", "Lcom/android/systemui/monet/ColorScheme;", "accentSecondaryFromScheme", "backgroundEndFromScheme", "backgroundStartFromScheme", "surfaceFromScheme", "textPrimaryFromScheme", "textPrimaryInverseFromScheme", "textSecondaryFromScheme", "textTertiaryFromScheme", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaColorSchemes.kt */
public final class MediaColorSchemesKt {
    public static final int surfaceFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getAccent2().get(9).intValue();
    }

    public static final int accentPrimaryFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getAccent1().get(2).intValue();
    }

    public static final int accentSecondaryFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getAccent1().get(3).intValue();
    }

    public static final int textPrimaryFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getNeutral1().get(1).intValue();
    }

    public static final int textPrimaryInverseFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getNeutral1().get(10).intValue();
    }

    public static final int textSecondaryFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getNeutral2().get(3).intValue();
    }

    public static final int textTertiaryFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getNeutral2().get(5).intValue();
    }

    public static final int backgroundStartFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getAccent2().get(8).intValue();
    }

    public static final int backgroundEndFromScheme(ColorScheme colorScheme) {
        Intrinsics.checkNotNullParameter(colorScheme, "scheme");
        return colorScheme.getAccent1().get(8).intValue();
    }
}
