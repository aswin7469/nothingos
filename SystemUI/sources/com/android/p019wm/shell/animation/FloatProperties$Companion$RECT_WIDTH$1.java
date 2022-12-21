package com.android.p019wm.shell.animation;

import android.graphics.Rect;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0004H\u0016¨\u0006\t"}, mo64987d2 = {"com/android/wm/shell/animation/FloatProperties$Companion$RECT_WIDTH$1", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Landroid/graphics/Rect;", "getValue", "", "rect", "setValue", "", "value", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.animation.FloatProperties$Companion$RECT_WIDTH$1 */
/* compiled from: FloatProperties.kt */
public final class FloatProperties$Companion$RECT_WIDTH$1 extends FloatPropertyCompat<Rect> {
    FloatProperties$Companion$RECT_WIDTH$1() {
        super("RectWidth");
    }

    public float getValue(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        return (float) rect.width();
    }

    public void setValue(Rect rect, float f) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        rect.right = rect.left + ((int) f);
    }
}
