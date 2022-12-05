package com.android.systemui.util.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: UniqueObjectHostView.kt */
/* loaded from: classes2.dex */
public final class UniqueObjectHostView extends FrameLayout {
    public MeasurementManager measurementManager;

    /* compiled from: UniqueObjectHostView.kt */
    /* loaded from: classes2.dex */
    public interface MeasurementManager {
        @NotNull
        MeasurementOutput onMeasure(@NotNull MeasurementInput measurementInput);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UniqueObjectHostView(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @NotNull
    public final MeasurementManager getMeasurementManager() {
        MeasurementManager measurementManager = this.measurementManager;
        if (measurementManager != null) {
            return measurementManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("measurementManager");
        throw null;
    }

    public final void setMeasurementManager(@NotNull MeasurementManager measurementManager) {
        Intrinsics.checkNotNullParameter(measurementManager, "<set-?>");
        this.measurementManager = measurementManager;
    }

    @Override // android.widget.FrameLayout, android.view.View
    @SuppressLint({"DrawAllocation"})
    protected void onMeasure(int i, int i2) {
        int paddingStart = getPaddingStart() + getPaddingEnd();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        MeasurementOutput onMeasure = getMeasurementManager().onMeasure(new MeasurementInput(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) - paddingStart, View.MeasureSpec.getMode(i)), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2) - paddingTop, View.MeasureSpec.getMode(i2))));
        int component1 = onMeasure.component1();
        int component2 = onMeasure.component2();
        if (isCurrentHost()) {
            super.onMeasure(i, i2);
            View childAt = getChildAt(0);
            if (childAt != null) {
                UniqueObjectHostViewKt.setRequiresRemeasuring(childAt, false);
            }
        }
        setMeasuredDimension(component1 + paddingStart, component2 + paddingTop);
    }

    @Override // android.view.ViewGroup
    public void addView(@Nullable View view, int i, @Nullable ViewGroup.LayoutParams layoutParams) {
        if (view == null) {
            throw new IllegalArgumentException("child must be non-null");
        }
        if (view.getMeasuredWidth() == 0 || getMeasuredWidth() == 0 || UniqueObjectHostViewKt.getRequiresRemeasuring(view)) {
            super.addView(view, i, layoutParams);
            return;
        }
        invalidate();
        addViewInLayout(view, i, layoutParams, true);
        view.resolveRtlPropertiesIfNeeded();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        view.layout(paddingLeft, paddingTop, (getMeasuredWidth() + paddingLeft) - (getPaddingStart() + getPaddingEnd()), (getMeasuredHeight() + paddingTop) - (getPaddingTop() + getPaddingBottom()));
    }

    private final boolean isCurrentHost() {
        return getChildCount() != 0;
    }
}
