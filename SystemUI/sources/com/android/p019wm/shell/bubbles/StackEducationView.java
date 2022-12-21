package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.android.internal.util.ContrastColorUtil;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.animation.Interpolators;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0016J\b\u0010\"\u001a\u00020 H\u0014J\b\u0010#\u001a\u00020 H\u0014J\b\u0010$\u001a\u00020 H\u0014J\b\u0010%\u001a\u00020 H\u0002J\u0010\u0010&\u001a\u00020 2\u0006\u0010'\u001a\u00020(H\u0016J\u0010\u0010)\u001a\u00020 2\u0006\u0010*\u001a\u00020\u0016H\u0002J\b\u0010+\u001a\u00020 H\u0002J\u000e\u0010,\u001a\u00020\u00162\u0006\u0010-\u001a\u00020.R\u000e\u0010\t\u001a\u00020\nXD¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nXD¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R#\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000f8BX\u0002¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R#\u0010\u0017\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000f8BX\u0002¢\u0006\f\n\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0012R#\u0010\u001a\u001a\n \u0010*\u0004\u0018\u00010\u001b0\u001b8BX\u0002¢\u0006\f\n\u0004\b\u001e\u0010\u0014\u001a\u0004\b\u001c\u0010\u001d¨\u0006/"}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/StackEducationView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "positioner", "Lcom/android/wm/shell/bubbles/BubblePositioner;", "controller", "Lcom/android/wm/shell/bubbles/BubbleController;", "(Landroid/content/Context;Lcom/android/wm/shell/bubbles/BubblePositioner;Lcom/android/wm/shell/bubbles/BubbleController;)V", "ANIMATE_DURATION", "", "ANIMATE_DURATION_SHORT", "TAG", "", "descTextView", "Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "getDescTextView", "()Landroid/widget/TextView;", "descTextView$delegate", "Lkotlin/Lazy;", "isHiding", "", "titleTextView", "getTitleTextView", "titleTextView$delegate", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "view$delegate", "hide", "", "isExpanding", "onAttachedToWindow", "onDetachedFromWindow", "onFinishInflate", "setDrawableDirection", "setLayoutDirection", "layoutDirection", "", "setShouldShow", "shouldShow", "setTextColor", "show", "stackPosition", "Landroid/graphics/PointF;", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.StackEducationView */
/* compiled from: StackEducationView.kt */
public final class StackEducationView extends LinearLayout {
    private final long ANIMATE_DURATION;
    private final long ANIMATE_DURATION_SHORT;
    private final String TAG;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final BubbleController controller;
    private final Lazy descTextView$delegate;
    /* access modifiers changed from: private */
    public boolean isHiding;
    private final BubblePositioner positioner;
    private final Lazy titleTextView$delegate;
    private final Lazy view$delegate;

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
    public StackEducationView(Context context, BubblePositioner bubblePositioner, BubbleController bubbleController) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bubblePositioner, "positioner");
        Intrinsics.checkNotNullParameter(bubbleController, "controller");
        this.TAG = BubbleDebugConfig.TAG_BUBBLES;
        this.ANIMATE_DURATION = 200;
        this.ANIMATE_DURATION_SHORT = 40;
        this.positioner = bubblePositioner;
        this.controller = bubbleController;
        this.view$delegate = LazyKt.lazy(new StackEducationView$view$2(this));
        this.titleTextView$delegate = LazyKt.lazy(new StackEducationView$titleTextView$2(this));
        this.descTextView$delegate = LazyKt.lazy(new StackEducationView$descTextView$2(this));
        LayoutInflater.from(context).inflate(C3343R.layout.bubble_stack_user_education, this);
        setVisibility(8);
        setElevation((float) getResources().getDimensionPixelSize(C3343R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    private final View getView() {
        return (View) this.view$delegate.getValue();
    }

    private final TextView getTitleTextView() {
        return (TextView) this.titleTextView$delegate.getValue();
    }

    private final TextView getDescTextView() {
        return (TextView) this.descTextView$delegate.getValue();
    }

    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        setTextColor();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFocusableInTouchMode(true);
        setOnKeyListener(new StackEducationView$onAttachedToWindow$1(this));
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnKeyListener((View.OnKeyListener) null);
        this.controller.updateWindowFlagsForBackpress(false);
    }

    private final void setTextColor() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16843829, 16842809});
        int color = obtainStyledAttributes.getColor(0, ViewCompat.MEASURED_STATE_MASK);
        int color2 = obtainStyledAttributes.getColor(1, -1);
        obtainStyledAttributes.recycle();
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(color2, color, true);
        getTitleTextView().setTextColor(ensureTextContrast);
        getDescTextView().setTextColor(ensureTextContrast);
    }

    private final void setDrawableDirection() {
        int i;
        View view = getView();
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            i = C3343R.C3345drawable.bubble_stack_user_education_bg;
        } else {
            i = C3343R.C3345drawable.bubble_stack_user_education_bg_rtl;
        }
        view.setBackgroundResource(i);
    }

    public final boolean show(PointF pointF) {
        int i;
        Intrinsics.checkNotNullParameter(pointF, "stackPosition");
        this.isHiding = false;
        if (getVisibility() == 0) {
            return false;
        }
        this.controller.updateWindowFlagsForBackpress(true);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.positioner.isLargeScreen() || this.positioner.isLandscape()) {
            i = getContext().getResources().getDimensionPixelSize(C3343R.dimen.bubbles_user_education_width);
        } else {
            i = -1;
        }
        layoutParams.width = i;
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(C3343R.dimen.bubble_user_education_stack_padding);
        setAlpha(0.0f);
        setVisibility(0);
        post(new StackEducationView$$ExternalSyntheticLambda0(this, dimensionPixelSize, pointF));
        setShouldShow(false);
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-1  reason: not valid java name */
    public static final void m3438show$lambda1(StackEducationView stackEducationView, int i, PointF pointF) {
        Intrinsics.checkNotNullParameter(stackEducationView, "this$0");
        Intrinsics.checkNotNullParameter(pointF, "$stackPosition");
        stackEducationView.requestFocus();
        View view = stackEducationView.getView();
        if (view.getResources().getConfiguration().getLayoutDirection() == 0) {
            view.setPadding(stackEducationView.positioner.getBubbleSize() + i, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        } else {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), stackEducationView.positioner.getBubbleSize() + i, view.getPaddingBottom());
            if (stackEducationView.positioner.isLargeScreen() || stackEducationView.positioner.isLandscape()) {
                view.setTranslationX((float) ((stackEducationView.positioner.getScreenRect().right - view.getWidth()) - i));
            } else {
                view.setTranslationX(0.0f);
            }
        }
        view.setTranslationY((pointF.y + ((float) (stackEducationView.positioner.getBubbleSize() / 2))) - ((float) (view.getHeight() / 2)));
        stackEducationView.animate().setDuration(stackEducationView.ANIMATE_DURATION).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
    }

    public final void hide(boolean z) {
        if (getVisibility() == 0 && !this.isHiding) {
            this.isHiding = true;
            this.controller.updateWindowFlagsForBackpress(false);
            animate().alpha(0.0f).setDuration(z ? this.ANIMATE_DURATION_SHORT : this.ANIMATE_DURATION).withEndAction(new StackEducationView$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: hide$lambda-2  reason: not valid java name */
    public static final void m3437hide$lambda2(StackEducationView stackEducationView) {
        Intrinsics.checkNotNullParameter(stackEducationView, "this$0");
        stackEducationView.setVisibility(8);
    }

    private final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean(StackEducationViewKt.PREF_STACK_EDUCATION, !z).apply();
    }
}
