package com.android.p019wm.shell.common.magnetictarget;

import android.content.Context;
import androidx.dynamicanimation.animation.DynamicAnimation;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0015\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0005J\u001d\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00028\u00002\u0006\u0010\b\u001a\u00020\tH\u0016¢\u0006\u0002\u0010\nJ\u0015\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0005¨\u0006\f"}, mo65043d2 = {"com/android/wm/shell/common/magnetictarget/MagnetizedObject$Companion$magnetizeView$1", "Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject;", "getHeight", "", "underlyingObject", "(Landroid/view/View;)F", "getLocationOnScreen", "", "loc", "", "(Landroid/view/View;[I)V", "getWidth", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.common.magnetictarget.MagnetizedObject$Companion$magnetizeView$1 */
/* compiled from: MagnetizedObject.kt */
public final class MagnetizedObject$Companion$magnetizeView$1 extends MagnetizedObject<T> {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MagnetizedObject$Companion$magnetizeView$1(T t, Context context, DynamicAnimation.ViewProperty viewProperty, DynamicAnimation.ViewProperty viewProperty2) {
        super(context, t, viewProperty, viewProperty2);
        Intrinsics.checkNotNullExpressionValue(context, "context");
        Intrinsics.checkNotNullExpressionValue(viewProperty, "TRANSLATION_X");
        Intrinsics.checkNotNullExpressionValue(viewProperty2, "TRANSLATION_Y");
    }

    public float getWidth(T t) {
        Intrinsics.checkNotNullParameter(t, "underlyingObject");
        return (float) t.getWidth();
    }

    public float getHeight(T t) {
        Intrinsics.checkNotNullParameter(t, "underlyingObject");
        return (float) t.getHeight();
    }

    public void getLocationOnScreen(T t, int[] iArr) {
        Intrinsics.checkNotNullParameter(t, "underlyingObject");
        Intrinsics.checkNotNullParameter(iArr, "loc");
        t.getLocationOnScreen(iArr);
    }
}
