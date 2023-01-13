package com.android.p019wm.shell.bubbles;

import android.graphics.drawable.GradientDrawable;
import android.icu.text.DateFormat;
import android.util.IntProperty;
import androidx.constraintlayout.motion.widget.Key;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0002¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u0004H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/wm/shell/bubbles/DismissView$GRADIENT_ALPHA$1", "Landroid/util/IntProperty;", "Landroid/graphics/drawable/GradientDrawable;", "get", "", "d", "(Landroid/graphics/drawable/GradientDrawable;)Ljava/lang/Integer;", "setValue", "", "percent", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.DismissView$GRADIENT_ALPHA$1 */
/* compiled from: DismissView.kt */
public final class DismissView$GRADIENT_ALPHA$1 extends IntProperty<GradientDrawable> {
    DismissView$GRADIENT_ALPHA$1() {
        super(Key.ALPHA);
    }

    public void setValue(GradientDrawable gradientDrawable, int i) {
        Intrinsics.checkNotNullParameter(gradientDrawable, DateFormat.DAY);
        gradientDrawable.setAlpha(i);
    }

    public Integer get(GradientDrawable gradientDrawable) {
        Intrinsics.checkNotNullParameter(gradientDrawable, DateFormat.DAY);
        return Integer.valueOf(gradientDrawable.getAlpha());
    }
}
