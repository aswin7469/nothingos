package com.android.systemui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Triple;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\"\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\n2\u0006\u0010\f\u001a\u00020\u0007H\u0002J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007H\u0014¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/util/NeverExactlyLinearLayout;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "getNonExactlyMeasureSpec", "Lkotlin/Triple;", "", "measureSpec", "onMeasure", "", "widthMeasureSpec", "heightMeasureSpec", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NeverExactlyLinearLayout.kt */
public final class NeverExactlyLinearLayout extends LinearLayout {
    public Map<Integer, View> _$_findViewCache;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public NeverExactlyLinearLayout(Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public NeverExactlyLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
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
    public NeverExactlyLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ NeverExactlyLinearLayout(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        Triple<Boolean, Integer, Integer> nonExactlyMeasureSpec = getNonExactlyMeasureSpec(i);
        boolean booleanValue = nonExactlyMeasureSpec.component1().booleanValue();
        int intValue = nonExactlyMeasureSpec.component2().intValue();
        int intValue2 = nonExactlyMeasureSpec.component3().intValue();
        Triple<Boolean, Integer, Integer> nonExactlyMeasureSpec2 = getNonExactlyMeasureSpec(i2);
        boolean booleanValue2 = nonExactlyMeasureSpec2.component1().booleanValue();
        int intValue3 = nonExactlyMeasureSpec2.component2().intValue();
        int intValue4 = nonExactlyMeasureSpec2.component3().intValue();
        super.onMeasure(intValue, intValue3);
        if (booleanValue || booleanValue2) {
            if (!booleanValue) {
                intValue2 = getMeasuredWidth();
            }
            if (!booleanValue2) {
                intValue4 = getMeasuredHeight();
            }
            setMeasuredDimension(intValue2, intValue4);
        }
    }

    private final Triple<Boolean, Integer, Integer> getNonExactlyMeasureSpec(int i) {
        boolean z = View.MeasureSpec.getMode(i) == 1073741824;
        int size = View.MeasureSpec.getSize(i);
        if (z) {
            i = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
        }
        return new Triple<>(Boolean.valueOf(z), Integer.valueOf(i), Integer.valueOf(size));
    }
}
