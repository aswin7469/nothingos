package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: VariableDateView.kt */
/* loaded from: classes2.dex */
public final class VariableDateView extends TextView {
    private boolean freezeSwitching;
    @NotNull
    private final String longerPattern;
    @Nullable
    private OnMeasureListener onMeasureListener;
    @NotNull
    private final String shorterPattern;

    /* compiled from: VariableDateView.kt */
    /* loaded from: classes2.dex */
    public interface OnMeasureListener {
        void onMeasureAction(int i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VariableDateView(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R$styleable.VariableDateView, 0, 0);
        String string = obtainStyledAttributes.getString(R$styleable.VariableDateView_longDatePattern);
        if (string == null) {
            string = context.getString(R$string.system_ui_date_pattern);
            Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.string.system_ui_date_pattern)");
        }
        this.longerPattern = string;
        String string2 = obtainStyledAttributes.getString(R$styleable.VariableDateView_shortDatePattern);
        if (string2 == null) {
            string2 = context.getString(R$string.abbrev_month_day_no_year);
            Intrinsics.checkNotNullExpressionValue(string2, "context.getString(R.string.abbrev_month_day_no_year)");
        }
        this.shorterPattern = string2;
        obtainStyledAttributes.recycle();
    }

    @NotNull
    public final String getLongerPattern() {
        return this.longerPattern;
    }

    @NotNull
    public final String getShorterPattern() {
        return this.shorterPattern;
    }

    public final boolean getFreezeSwitching() {
        return this.freezeSwitching;
    }

    public final void setFreezeSwitching(boolean z) {
        this.freezeSwitching = z;
    }

    public final void onAttach(@Nullable OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        OnMeasureListener onMeasureListener;
        int size = (View.MeasureSpec.getSize(i) - getPaddingStart()) - getPaddingEnd();
        if (View.MeasureSpec.getMode(i) != 0 && !this.freezeSwitching && (onMeasureListener = this.onMeasureListener) != null) {
            onMeasureListener.onMeasureAction(size);
        }
        super.onMeasure(i, i2);
    }

    public final float getDesiredWidthForText(@NotNull CharSequence text) {
        Intrinsics.checkNotNullParameter(text, "text");
        return StaticLayout.getDesiredWidth(text, getPaint());
    }
}
