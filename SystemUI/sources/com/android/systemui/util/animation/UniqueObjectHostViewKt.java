package com.android.systemui.util.animation;

import android.view.View;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\"(\u0010\u0002\u001a\u00020\u0001*\u00020\u00032\u0006\u0010\u0000\u001a\u00020\u00018F@FX\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo65043d2 = {"value", "", "requiresRemeasuring", "Landroid/view/View;", "getRequiresRemeasuring", "(Landroid/view/View;)Z", "setRequiresRemeasuring", "(Landroid/view/View;Z)V", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UniqueObjectHostView.kt */
public final class UniqueObjectHostViewKt {
    public static final boolean getRequiresRemeasuring(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Object tag = view.getTag(C1894R.C1898id.requires_remeasuring);
        if (tag != null) {
            return tag.equals(true);
        }
        return false;
    }

    public static final void setRequiresRemeasuring(View view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        view.setTag(C1894R.C1898id.requires_remeasuring, Boolean.valueOf(z));
    }
}
