package com.android.p019wm.shell.bubbles;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.IntProperty;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.p019wm.shell.common.DismissCircleView;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u001c\u001a\u00020\u001dJ\b\u0010\u001e\u001a\u00020\tH\u0002J\u0006\u0010\u001f\u001a\u00020\u001dJ\u0006\u0010 \u001a\u00020\u001dJ\b\u0010!\u001a\u00020\u001dH\u0002J\u0006\u0010\"\u001a\u00020\u001dR\u000e\u0010\u0005\u001a\u00020\u0006XD¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000¨\u0006#"}, mo65043d2 = {"Lcom/android/wm/shell/bubbles/DismissView;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "DISMISS_SCRIM_FADE_MS", "", "GRADIENT_ALPHA", "Landroid/util/IntProperty;", "Landroid/graphics/drawable/GradientDrawable;", "animator", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "Lcom/android/wm/shell/common/DismissCircleView;", "circle", "getCircle", "()Lcom/android/wm/shell/common/DismissCircleView;", "setCircle", "(Lcom/android/wm/shell/common/DismissCircleView;)V", "gradientDrawable", "isShowing", "", "()Z", "setShowing", "(Z)V", "spring", "Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "wm", "Landroid/view/WindowManager;", "cancelAnimators", "", "createGradient", "hide", "show", "updatePadding", "updateResources", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.DismissView */
/* compiled from: DismissView.kt */
public final class DismissView extends FrameLayout {
    private final long DISMISS_SCRIM_FADE_MS;
    private final IntProperty<GradientDrawable> GRADIENT_ALPHA;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final PhysicsAnimator<DismissCircleView> animator;
    private DismissCircleView circle;
    private GradientDrawable gradientDrawable;
    private boolean isShowing;
    private final PhysicsAnimator.SpringConfig spring;

    /* renamed from: wm */
    private WindowManager f403wm;

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
    public DismissView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        this.circle = new DismissCircleView(context);
        this.animator = PhysicsAnimator.Companion.getInstance(this.circle);
        this.spring = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
        this.DISMISS_SCRIM_FADE_MS = 200;
        Object systemService = context.getSystemService("window");
        if (systemService != null) {
            this.f403wm = (WindowManager) systemService;
            this.gradientDrawable = createGradient();
            this.GRADIENT_ALPHA = new DismissView$GRADIENT_ALPHA$1();
            setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(C3353R.dimen.floating_dismiss_gradient_height), 80));
            updatePadding();
            setClipToPadding(false);
            setClipChildren(false);
            setVisibility(4);
            setBackgroundDrawable(this.gradientDrawable);
            int dimensionPixelSize = getResources().getDimensionPixelSize(C3353R.dimen.dismiss_circle_size);
            addView(this.circle, new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 81));
            this.circle.setTranslationY((float) getResources().getDimensionPixelSize(C3353R.dimen.floating_dismiss_gradient_height));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.WindowManager");
    }

    public final DismissCircleView getCircle() {
        return this.circle;
    }

    public final void setCircle(DismissCircleView dismissCircleView) {
        Intrinsics.checkNotNullParameter(dismissCircleView, "<set-?>");
        this.circle = dismissCircleView;
    }

    public final boolean isShowing() {
        return this.isShowing;
    }

    public final void setShowing(boolean z) {
        this.isShowing = z;
    }

    public final void show() {
        if (!this.isShowing) {
            this.isShowing = true;
            setVisibility(0);
            GradientDrawable gradientDrawable2 = this.gradientDrawable;
            ObjectAnimator ofInt = ObjectAnimator.ofInt(gradientDrawable2, this.GRADIENT_ALPHA, new int[]{gradientDrawable2.getAlpha(), 255});
            ofInt.setDuration(this.DISMISS_SCRIM_FADE_MS);
            ofInt.start();
            this.animator.cancel();
            PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_Y;
            Intrinsics.checkNotNullExpressionValue(viewProperty, "TRANSLATION_Y");
            physicsAnimator.spring(viewProperty, 0.0f, this.spring).start();
        }
    }

    public final void hide() {
        if (this.isShowing) {
            this.isShowing = false;
            GradientDrawable gradientDrawable2 = this.gradientDrawable;
            ObjectAnimator ofInt = ObjectAnimator.ofInt(gradientDrawable2, this.GRADIENT_ALPHA, new int[]{gradientDrawable2.getAlpha(), 0});
            ofInt.setDuration(this.DISMISS_SCRIM_FADE_MS);
            ofInt.start();
            PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_Y;
            Intrinsics.checkNotNullExpressionValue(viewProperty, "TRANSLATION_Y");
            physicsAnimator.spring(viewProperty, (float) getHeight(), this.spring).withEndActions((Function0<Unit>[]) new Function0[]{new DismissView$hide$1(this)}).start();
        }
    }

    public final void cancelAnimators() {
        this.animator.cancel();
    }

    public final void updateResources() {
        updatePadding();
        getLayoutParams().height = getResources().getDimensionPixelSize(C3353R.dimen.floating_dismiss_gradient_height);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C3353R.dimen.dismiss_circle_size);
        this.circle.getLayoutParams().width = dimensionPixelSize;
        this.circle.getLayoutParams().height = dimensionPixelSize;
        this.circle.requestLayout();
    }

    private final GradientDrawable createGradient() {
        int color = getContext().getResources().getColor(17170472);
        int argb = Color.argb((int) 178.5f, Color.red(color), Color.green(color), Color.blue(color));
        GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{argb, 0});
        gradientDrawable2.setAlpha(0);
        return gradientDrawable2;
    }

    private final void updatePadding() {
        WindowInsets windowInsets = this.f403wm.getCurrentWindowMetrics().getWindowInsets();
        Intrinsics.checkNotNullExpressionValue(windowInsets, "wm.getCurrentWindowMetrics().getWindowInsets()");
        setPadding(0, 0, 0, windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom + getResources().getDimensionPixelSize(C3353R.dimen.floating_dismiss_bottom_margin));
    }
}
