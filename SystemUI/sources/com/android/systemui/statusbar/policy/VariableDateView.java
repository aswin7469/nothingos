package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.C1894R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001 B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0012J\u0018\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0014R\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010¨\u0006!"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/VariableDateView;", "Landroid/widget/TextView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "freezeSwitching", "", "getFreezeSwitching", "()Z", "setFreezeSwitching", "(Z)V", "longerPattern", "", "getLongerPattern", "()Ljava/lang/String;", "onMeasureListener", "Lcom/android/systemui/statusbar/policy/VariableDateView$OnMeasureListener;", "shorterPattern", "getShorterPattern", "getDesiredWidthForText", "", "text", "", "onAttach", "", "listener", "onMeasure", "widthMeasureSpec", "", "heightMeasureSpec", "OnMeasureListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: VariableDateView.kt */
public final class VariableDateView extends TextView {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private boolean freezeSwitching;
    private final String longerPattern;
    private OnMeasureListener onMeasureListener;
    private final String shorterPattern;

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/VariableDateView$OnMeasureListener;", "", "onMeasureAction", "", "availableWidth", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: VariableDateView.kt */
    public interface OnMeasureListener {
        void onMeasureAction(int i);
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
    public VariableDateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C1894R.styleable.VariableDateView, 0, 0);
        String string = obtainStyledAttributes.getString(0);
        if (string == null) {
            string = context.getString(C1894R.string.system_ui_date_pattern);
            Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.string.system_ui_date_pattern)");
        }
        this.longerPattern = string;
        String string2 = obtainStyledAttributes.getString(1);
        if (string2 == null) {
            string2 = context.getString(C1894R.string.abbrev_month_day_no_year);
            Intrinsics.checkNotNullExpressionValue(string2, "context.getString(R.stri…abbrev_month_day_no_year)");
        }
        this.shorterPattern = string2;
        obtainStyledAttributes.recycle();
    }

    public final String getLongerPattern() {
        return this.longerPattern;
    }

    public final String getShorterPattern() {
        return this.shorterPattern;
    }

    public final boolean getFreezeSwitching() {
        return this.freezeSwitching;
    }

    public final void setFreezeSwitching(boolean z) {
        this.freezeSwitching = z;
    }

    public final void onAttach(OnMeasureListener onMeasureListener2) {
        this.onMeasureListener = onMeasureListener2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        OnMeasureListener onMeasureListener2;
        int size = (View.MeasureSpec.getSize(i) - getPaddingStart()) - getPaddingEnd();
        if (!(View.MeasureSpec.getMode(i) == 0 || this.freezeSwitching || (onMeasureListener2 = this.onMeasureListener) == null)) {
            onMeasureListener2.onMeasureAction(size);
        }
        super.onMeasure(i, i2);
    }

    public final float getDesiredWidthForText(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "text");
        return StaticLayout.getDesiredWidth(charSequence, getPaint());
    }
}
