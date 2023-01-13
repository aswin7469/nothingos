package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo65043d2 = {"<anonymous>", "", "colorScheme", "Lcom/android/systemui/monet/ColorScheme;", "invoke", "(Lcom/android/systemui/monet/ColorScheme;)Ljava/lang/Integer;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorSchemeTransition.kt */
final class ColorSchemeTransition$colorSeamless$1 extends Lambda implements Function1<ColorScheme, Integer> {
    final /* synthetic */ ColorSchemeTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ColorSchemeTransition$colorSeamless$1(ColorSchemeTransition colorSchemeTransition) {
        super(1);
        this.this$0 = colorSchemeTransition;
    }

    public final Integer invoke(ColorScheme colorScheme) {
        int i;
        Intrinsics.checkNotNullParameter(colorScheme, "colorScheme");
        if ((this.this$0.context.getResources().getConfiguration().uiMode & 48) == 32) {
            i = colorScheme.getAccent1().get(2).intValue();
        } else {
            i = colorScheme.getAccent1().get(3).intValue();
        }
        return Integer.valueOf(i);
    }
}
