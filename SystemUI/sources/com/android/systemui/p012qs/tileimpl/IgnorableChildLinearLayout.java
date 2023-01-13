package com.android.systemui.p012qs.tileimpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.nothing.systemui.p024qs.CircleTileTextView;
import com.nothing.systemui.util.NTLogUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0003\u0018\u0000 *2\u00020\u0001:\u0001*B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\b\u0010\u001e\u001a\u00020\u001fH\u0014J\u0018\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\u0007H\u0014J\u000e\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u000bJ\u000e\u0010%\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u000bJ\u0016\u0010&\u001a\u00020\u001f2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u000bR\u001a\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\r\"\u0004\b\u0012\u0010\u000fR\u001a\u0010\u0013\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\r\"\u0004\b\u0014\u0010\u000fR\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0018\"\u0004\b\u001d\u0010\u001a¨\u0006+"}, mo65043d2 = {"Lcom/android/systemui/qs/tileimpl/IgnorableChildLinearLayout;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attributeSet", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "forceUnspecifiedMeasure", "", "getForceUnspecifiedMeasure", "()Z", "setForceUnspecifiedMeasure", "(Z)V", "ignoreLastView", "getIgnoreLastView", "setIgnoreLastView", "isSingleMode", "setSingleMode", "label", "Landroid/widget/TextView;", "getLabel", "()Landroid/widget/TextView;", "setLabel", "(Landroid/widget/TextView;)V", "secondLabel", "getSecondLabel", "setSecondLabel", "onFinishInflate", "", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "setLabelSingleMode", "singleMode", "setLabelSingleModeNonFirst", "setPosition", "position", "", "onFirst", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout */
/* compiled from: IgnorableChildLinearLayout.kt */
public final class IgnorableChildLinearLayout extends LinearLayout {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG = false;
    private static final String TAG = "IgnorableChildLinearLayout";
    public Map<Integer, View> _$_findViewCache;
    private boolean forceUnspecifiedMeasure;
    private boolean ignoreLastView;
    private boolean isSingleMode;
    private TextView label;
    private TextView secondLabel;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public IgnorableChildLinearLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
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
    public IgnorableChildLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ IgnorableChildLinearLayout(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public final boolean getIgnoreLastView() {
        return this.ignoreLastView;
    }

    public final void setIgnoreLastView(boolean z) {
        this.ignoreLastView = z;
    }

    public final boolean getForceUnspecifiedMeasure() {
        return this.forceUnspecifiedMeasure;
    }

    public final void setForceUnspecifiedMeasure(boolean z) {
        this.forceUnspecifiedMeasure = z;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.forceUnspecifiedMeasure && getOrientation() == 0) {
            i = View.MeasureSpec.makeMeasureSpec(i, 0);
        }
        if (this.forceUnspecifiedMeasure && getOrientation() == 1) {
            i2 = View.MeasureSpec.makeMeasureSpec(i2, 0);
        }
        super.onMeasure(i, i2);
        if (this.ignoreLastView && getChildCount() > 0) {
            View childAt = getChildAt(getChildCount() - 1);
            if (childAt.getVisibility() != 8) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    if (getOrientation() == 1) {
                        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() - ((childAt.getMeasuredHeight() + marginLayoutParams.bottomMargin) + marginLayoutParams.topMargin));
                        return;
                    }
                    setMeasuredDimension(getMeasuredWidth() - ((childAt.getMeasuredWidth() + marginLayoutParams.leftMargin) + marginLayoutParams.rightMargin), getMeasuredHeight());
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/qs/tileimpl/IgnorableChildLinearLayout$Companion;", "", "()V", "DEBUG", "", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout$Companion */
    /* compiled from: IgnorableChildLinearLayout.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean isSingleMode() {
        return this.isSingleMode;
    }

    public final void setSingleMode(boolean z) {
        this.isSingleMode = z;
    }

    public final TextView getLabel() {
        return this.label;
    }

    public final void setLabel(TextView textView) {
        this.label = textView;
    }

    public final TextView getSecondLabel() {
        return this.secondLabel;
    }

    public final void setSecondLabel(TextView textView) {
        this.secondLabel = textView;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.label = (TextView) findViewById(C1894R.C1898id.tile_label);
        this.secondLabel = (TextView) findViewById(C1894R.C1898id.app_label);
    }

    public final void setLabelSingleMode(boolean z) {
        NTLogUtil.m1686d(TAG, "setLabelSingleMode: singleMode " + z);
        this.isSingleMode = z;
        View findViewById = findViewById(C1894R.C1898id.tile_label);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.tile_label)");
        TextView textView = (TextView) findViewById;
        View findViewById2 = findViewById(C1894R.C1898id.app_label);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.app_label)");
        TextView textView2 = (TextView) findViewById2;
        if (z) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                layoutParams2.width = -2;
                layoutParams2.weight = 0.0f;
                layoutParams2.topMargin = getResources().getDimensionPixelSize(C1894R.dimen.circle_qs_tile_label_top_margin);
                layoutParams2.leftMargin = 0;
                setLayoutParams(layoutParams2);
                ViewGroup.LayoutParams layoutParams3 = textView.getLayoutParams();
                if (layoutParams3 != null) {
                    LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
                    layoutParams4.gravity = 1;
                    textView.setLayoutParams(layoutParams4);
                    textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    textView.setMarqueeRepeatLimit(1);
                    textView.setSingleLine(true);
                    ViewGroup.LayoutParams layoutParams5 = textView2.getLayoutParams();
                    if (layoutParams5 != null) {
                        LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) layoutParams5;
                        layoutParams6.gravity = 1;
                        textView2.setLayoutParams(layoutParams6);
                        textView.setText(textView.getText());
                        textView2.setText(textView2.getText());
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
                }
                throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        }
        ViewGroup.LayoutParams layoutParams7 = getLayoutParams();
        if (layoutParams7 != null) {
            LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) layoutParams7;
            layoutParams8.width = 0;
            layoutParams8.weight = 1.0f;
            layoutParams8.gravity = 8388627;
            layoutParams8.topMargin = 0;
            layoutParams8.leftMargin = getResources().getDimensionPixelSize(C1894R.dimen.qs_label_container_margin);
            setLayoutParams(layoutParams8);
            ViewGroup.LayoutParams layoutParams9 = textView.getLayoutParams();
            if (layoutParams9 != null) {
                LinearLayout.LayoutParams layoutParams10 = (LinearLayout.LayoutParams) layoutParams9;
                layoutParams10.gravity = 8388611;
                textView.setLayoutParams(layoutParams10);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setMarqueeRepeatLimit(1);
                textView.setSingleLine(false);
                textView.setMaxLines(2);
                ViewGroup.LayoutParams layoutParams11 = textView2.getLayoutParams();
                if (layoutParams11 != null) {
                    LinearLayout.LayoutParams layoutParams12 = (LinearLayout.LayoutParams) layoutParams11;
                    layoutParams12.gravity = 8388611;
                    textView2.setLayoutParams(layoutParams12);
                    textView.setText(textView.getText());
                    textView2.setText(textView2.getText());
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
    }

    public final void setLabelSingleModeNonFirst(boolean z) {
        NTLogUtil.m1686d(TAG, "setCircleLabelSingleMode: singleMode " + z);
        this.isSingleMode = z;
        View findViewById = findViewById(C1894R.C1898id.tile_label);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.tile_label)");
        TextView textView = (TextView) findViewById;
        View findViewById2 = findViewById(C1894R.C1898id.app_label);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.app_label)");
        TextView textView2 = (TextView) findViewById2;
        if (z) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                layoutParams2.width = -2;
                layoutParams2.topMargin = getResources().getDimensionPixelSize(C1894R.dimen.circle_qs_tile_label_top_margin);
                setLayoutParams(layoutParams2);
                textView.setText(textView.getText());
                textView2.setText(textView2.getText());
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        }
        ViewGroup.LayoutParams layoutParams3 = getLayoutParams();
        if (layoutParams3 != null) {
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
            layoutParams4.width = getResources().getDimensionPixelSize(C1894R.dimen.circle_qs_tile_label_max_width);
            layoutParams4.topMargin = 0;
            setLayoutParams(layoutParams4);
            textView.setText(textView.getText());
            textView2.setText(textView2.getText());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
    }

    public final void setPosition(float f, boolean z) {
        TextView textView = this.label;
        if (textView != null) {
            if (textView != null) {
                ((CircleTileTextView) textView).setPosition(f);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type com.nothing.systemui.qs.CircleTileTextView");
            }
        }
        TextView textView2 = this.secondLabel;
        if (textView2 != null) {
            if (textView2 != null) {
                ((CircleTileTextView) textView2).setPosition(f);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type com.nothing.systemui.qs.CircleTileTextView");
            }
        }
        double d = (double) f;
        if (d <= 0.01d) {
            if (z && this.isSingleMode) {
                this.isSingleMode = false;
                setLabelSingleMode(false);
            } else if (!z && !this.isSingleMode) {
                this.isSingleMode = true;
                setLabelSingleModeNonFirst(true);
            }
        } else if (d <= 0.01d || f > 0.5f) {
            if (f > 0.5f && !this.isSingleMode) {
                this.isSingleMode = true;
                if (z) {
                    setLabelSingleMode(true);
                    return;
                }
                setLabelSingleModeNonFirst(true);
                setTranslationX(0.0f);
            }
        } else if (this.isSingleMode) {
            this.isSingleMode = false;
            if (z) {
                setLabelSingleMode(false);
                setTranslationX(0.0f);
                setTranslationY(0.0f);
                return;
            }
            setLabelSingleModeNonFirst(false);
        }
    }
}
