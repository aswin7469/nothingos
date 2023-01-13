package com.android.systemui.util.animation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0018B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J$\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0018\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0010H\u0015R\u001a\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/util/animation/UniqueObjectHostView;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "measurementManager", "Lcom/android/systemui/util/animation/UniqueObjectHostView$MeasurementManager;", "getMeasurementManager", "()Lcom/android/systemui/util/animation/UniqueObjectHostView$MeasurementManager;", "setMeasurementManager", "(Lcom/android/systemui/util/animation/UniqueObjectHostView$MeasurementManager;)V", "addView", "", "child", "Landroid/view/View;", "index", "", "params", "Landroid/view/ViewGroup$LayoutParams;", "isCurrentHost", "", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "MeasurementManager", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UniqueObjectHostView.kt */
public final class UniqueObjectHostView extends FrameLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    public MeasurementManager measurementManager;

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/util/animation/UniqueObjectHostView$MeasurementManager;", "", "onMeasure", "Lcom/android/systemui/util/animation/MeasurementOutput;", "input", "Lcom/android/systemui/util/animation/MeasurementInput;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UniqueObjectHostView.kt */
    public interface MeasurementManager {
        MeasurementOutput onMeasure(MeasurementInput measurementInput);
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UniqueObjectHostView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public final MeasurementManager getMeasurementManager() {
        MeasurementManager measurementManager2 = this.measurementManager;
        if (measurementManager2 != null) {
            return measurementManager2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("measurementManager");
        return null;
    }

    public final void setMeasurementManager(MeasurementManager measurementManager2) {
        Intrinsics.checkNotNullParameter(measurementManager2, "<set-?>");
        this.measurementManager = measurementManager2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
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

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (view == null) {
            throw new IllegalArgumentException("child must be non-null");
        } else if (view.getMeasuredWidth() == 0 || getMeasuredWidth() == 0 || UniqueObjectHostViewKt.getRequiresRemeasuring(view)) {
            super.addView(view, i, layoutParams);
        } else {
            invalidate();
            addViewInLayout(view, i, layoutParams, true);
            view.resolveRtlPropertiesIfNeeded();
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            view.layout(paddingLeft, paddingTop, (getMeasuredWidth() + paddingLeft) - (getPaddingStart() + getPaddingEnd()), (getMeasuredHeight() + paddingTop) - (getPaddingTop() + getPaddingBottom()));
        }
    }

    private final boolean isCurrentHost() {
        return getChildCount() != 0;
    }
}
