package com.android.systemui.util.view;

import android.view.View;
import com.android.systemui.dagger.SysUISingleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/util/view/ViewUtil;", "", "()V", "touchIsWithinView", "", "view", "Landroid/view/View;", "x", "", "y", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ViewUtil.kt */
public final class ViewUtil {
    public final boolean touchIsWithinView(View view, float f, float f2) {
        Intrinsics.checkNotNullParameter(view, "view");
        int i = view.getLocationOnScreen()[0];
        int i2 = view.getLocationOnScreen()[1];
        if (((float) i) > f || f > ((float) (i + view.getWidth())) || ((float) i2) > f2 || f2 > ((float) (i2 + view.getHeight()))) {
            return false;
        }
        return true;
    }
}
