package com.android.systemui.media;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u0015\u0010\u0002\u001a\u00020\u0003*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0005¨\u0006\u0006"}, mo64987d2 = {"EMPTY_RECT", "Landroid/graphics/Rect;", "isShownNotFaded", "", "Landroid/view/View;", "(Landroid/view/View;)Z", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManagerKt {
    /* access modifiers changed from: private */
    public static final Rect EMPTY_RECT = new Rect();

    public static final boolean isShownNotFaded(View view) {
        ViewParent parent;
        Intrinsics.checkNotNullParameter(view, "<this>");
        while (view.getVisibility() == 0) {
            if ((view.getAlpha() == 0.0f) || (parent = view.getParent()) == null) {
                return false;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            view = (View) parent;
        }
        return false;
    }
}
