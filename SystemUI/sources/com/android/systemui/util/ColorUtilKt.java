package com.android.systemui.util;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0016\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004\u001a.\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u0001¨\u0006\r"}, mo64987d2 = {"getColorWithAlpha", "", "color", "alpha", "", "getPrivateAttrColorIfUnset", "ctw", "Landroid/view/ContextThemeWrapper;", "attrArray", "Landroid/content/res/TypedArray;", "attrIndex", "defColor", "privAttrId", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorUtil.kt */
public final class ColorUtilKt {
    public static final int getColorWithAlpha(int i, float f) {
        return Color.argb((int) (f * ((float) 255)), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static final int getPrivateAttrColorIfUnset(ContextThemeWrapper contextThemeWrapper, TypedArray typedArray, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(contextThemeWrapper, "ctw");
        Intrinsics.checkNotNullParameter(typedArray, "attrArray");
        if (typedArray.hasValue(i)) {
            return typedArray.getColor(i, i2);
        }
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{i3});
        Intrinsics.checkNotNullExpressionValue(obtainStyledAttributes, "ctw.obtainStyledAttributes(customAttrs)");
        int color = obtainStyledAttributes.getColor(0, i2);
        obtainStyledAttributes.recycle();
        return color;
    }
}
