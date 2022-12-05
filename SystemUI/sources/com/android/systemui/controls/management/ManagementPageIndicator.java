package com.android.systemui.controls.management;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.qs.PageIndicator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ManagementPageIndicator.kt */
/* loaded from: classes.dex */
public final class ManagementPageIndicator extends PageIndicator {
    @NotNull
    private Function1<? super Integer, Unit> visibilityListener = ManagementPageIndicator$visibilityListener$1.INSTANCE;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ManagementPageIndicator(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
    }

    @Override // com.android.systemui.qs.PageIndicator
    public void setLocation(float f) {
        if (getLayoutDirection() == 1) {
            super.setLocation((getChildCount() - 1) - f);
        } else {
            super.setLocation(f);
        }
    }

    public final void setVisibilityListener(@NotNull Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.visibilityListener = function1;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(@NotNull View changedView, int i) {
        Intrinsics.checkNotNullParameter(changedView, "changedView");
        super.onVisibilityChanged(changedView, i);
        if (Intrinsics.areEqual(changedView, this)) {
            this.visibilityListener.mo1949invoke(Integer.valueOf(i));
        }
    }
}
