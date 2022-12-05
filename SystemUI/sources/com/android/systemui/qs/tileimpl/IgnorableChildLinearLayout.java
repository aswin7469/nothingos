package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.nothingos.systemui.qs.CircleTileTextView;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: IgnorableChildLinearLayout.kt */
/* loaded from: classes.dex */
public final class IgnorableChildLinearLayout extends LinearLayout {
    private boolean forceUnspecifiedMeasure;
    private boolean ignoreLastView;
    private boolean isSingleMode;
    @Nullable
    private TextView label;
    @Nullable
    private TextView secondLabel;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(@NotNull Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ IgnorableChildLinearLayout(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(@NotNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public final void setIgnoreLastView(boolean z) {
        this.ignoreLastView = z;
    }

    public final void setForceUnspecifiedMeasure(boolean z) {
        this.forceUnspecifiedMeasure = z;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.forceUnspecifiedMeasure && getOrientation() == 0) {
            i = View.MeasureSpec.makeMeasureSpec(i, 0);
        }
        if (this.forceUnspecifiedMeasure && getOrientation() == 1) {
            i2 = View.MeasureSpec.makeMeasureSpec(i2, 0);
        }
        super.onMeasure(i, i2);
        if (!this.ignoreLastView || getChildCount() <= 0) {
            return;
        }
        View childAt = getChildAt(getChildCount() - 1);
        if (childAt.getVisibility() == 8) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        if (getOrientation() == 1) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() - ((childAt.getMeasuredHeight() + marginLayoutParams.bottomMargin) + marginLayoutParams.topMargin));
            return;
        }
        setMeasuredDimension(getMeasuredWidth() - ((childAt.getMeasuredWidth() + marginLayoutParams.leftMargin) + marginLayoutParams.rightMargin), getMeasuredHeight());
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.label = (TextView) findViewById(R$id.tile_label);
        this.secondLabel = (TextView) findViewById(R$id.app_label);
    }

    public final void setLabelSingleMode(boolean z) {
        Log.d("IgnorableChildLinearLayout", Intrinsics.stringPlus("setLabelSingleMode: singleMode ", Boolean.valueOf(z)));
        this.isSingleMode = z;
        View findViewById = findViewById(R$id.tile_label);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.tile_label)");
        TextView textView = (TextView) findViewById;
        View findViewById2 = findViewById(R$id.app_label);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.app_label)");
        TextView textView2 = (TextView) findViewById2;
        if (z) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.width = -2;
            layoutParams2.weight = 0.0f;
            layoutParams2.topMargin = getResources().getDimensionPixelSize(R$dimen.circle_qs_tile_label_top_margin);
            layoutParams2.leftMargin = 0;
            setLayoutParams(layoutParams2);
            ViewGroup.LayoutParams layoutParams3 = textView.getLayoutParams();
            Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
            layoutParams4.gravity = 1;
            textView.setLayoutParams(layoutParams4);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setMarqueeRepeatLimit(1);
            textView.setSingleLine(true);
            ViewGroup.LayoutParams layoutParams5 = textView2.getLayoutParams();
            Objects.requireNonNull(layoutParams5, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) layoutParams5;
            layoutParams6.gravity = 1;
            textView2.setLayoutParams(layoutParams6);
            textView.setText(textView.getText());
            textView2.setText(textView2.getText());
            return;
        }
        ViewGroup.LayoutParams layoutParams7 = getLayoutParams();
        Objects.requireNonNull(layoutParams7, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) layoutParams7;
        layoutParams8.width = 0;
        layoutParams8.weight = 1.0f;
        layoutParams8.gravity = 8388627;
        layoutParams8.topMargin = 0;
        layoutParams8.leftMargin = getResources().getDimensionPixelSize(R$dimen.qs_label_container_margin);
        setLayoutParams(layoutParams8);
        ViewGroup.LayoutParams layoutParams9 = textView.getLayoutParams();
        Objects.requireNonNull(layoutParams9, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        LinearLayout.LayoutParams layoutParams10 = (LinearLayout.LayoutParams) layoutParams9;
        layoutParams10.gravity = 8388611;
        textView.setLayoutParams(layoutParams10);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(1);
        textView.setSingleLine(false);
        textView.setMaxLines(2);
        ViewGroup.LayoutParams layoutParams11 = textView2.getLayoutParams();
        Objects.requireNonNull(layoutParams11, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        LinearLayout.LayoutParams layoutParams12 = (LinearLayout.LayoutParams) layoutParams11;
        layoutParams12.gravity = 8388611;
        textView2.setLayoutParams(layoutParams12);
        textView.setText(textView.getText());
        textView2.setText(textView2.getText());
    }

    public final void setLabelSingleModeNonFirst(boolean z) {
        Log.d("IgnorableChildLinearLayout", Intrinsics.stringPlus("setCircleLabelSingleMode: singleMode ", Boolean.valueOf(z)));
        this.isSingleMode = z;
        View findViewById = findViewById(R$id.tile_label);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.tile_label)");
        TextView textView = (TextView) findViewById;
        View findViewById2 = findViewById(R$id.app_label);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.app_label)");
        TextView textView2 = (TextView) findViewById2;
        if (z) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.width = -2;
            layoutParams2.topMargin = getResources().getDimensionPixelSize(R$dimen.circle_qs_tile_label_top_margin);
            setLayoutParams(layoutParams2);
            textView.setText(textView.getText());
            textView2.setText(textView2.getText());
            return;
        }
        ViewGroup.LayoutParams layoutParams3 = getLayoutParams();
        Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
        layoutParams4.width = getResources().getDimensionPixelSize(R$dimen.circle_qs_tile_label_max_width);
        layoutParams4.topMargin = 0;
        setLayoutParams(layoutParams4);
        textView.setText(textView.getText());
        textView2.setText(textView2.getText());
    }

    public final void setPosition(float f, boolean z) {
        TextView textView = this.label;
        if (textView != null) {
            Objects.requireNonNull(textView, "null cannot be cast to non-null type com.nothingos.systemui.qs.CircleTileTextView");
            ((CircleTileTextView) textView).setPosition(f);
        }
        TextView textView2 = this.secondLabel;
        if (textView2 != null) {
            Objects.requireNonNull(textView2, "null cannot be cast to non-null type com.nothingos.systemui.qs.CircleTileTextView");
            ((CircleTileTextView) textView2).setPosition(f);
        }
        double d = f;
        if (d <= 0.01d) {
            Log.d("IgnorableChildLinearLayout", "setPosition: position under 0.01 , onFirst = " + z + " position = " + f);
            if (z && this.isSingleMode) {
                this.isSingleMode = false;
                setLabelSingleMode(false);
            } else if (z || this.isSingleMode) {
            } else {
                this.isSingleMode = true;
                setLabelSingleModeNonFirst(true);
            }
        } else if (d > 0.01d && f <= 0.5f) {
            if (!this.isSingleMode) {
                return;
            }
            Log.d("IgnorableChildLinearLayout", Intrinsics.stringPlus("setPosition: make single line disable, onFirst = ", Boolean.valueOf(z)));
            this.isSingleMode = false;
            if (z) {
                setLabelSingleMode(false);
                setTranslationX(0.0f);
                setTranslationY(0.0f);
                return;
            }
            setLabelSingleModeNonFirst(false);
        } else if (f <= 0.5f || this.isSingleMode) {
        } else {
            Log.d("IgnorableChildLinearLayout", Intrinsics.stringPlus("setPosition: make single line enable, onFirst = ", Boolean.valueOf(z)));
            this.isSingleMode = true;
            if (z) {
                setLabelSingleMode(true);
                return;
            }
            setLabelSingleModeNonFirst(true);
            setTranslationX(0.0f);
        }
    }

    public final boolean isSingleMode() {
        return this.isSingleMode;
    }
}
