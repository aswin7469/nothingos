package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: StackEducationView.kt */
/* loaded from: classes2.dex */
public final class StackEducationView extends LinearLayout {
    private boolean isHiding;
    @NotNull
    private final String TAG = "Bubbles";
    private final long ANIMATE_DURATION = 200;
    private final long ANIMATE_DURATION_SHORT = 40;
    @NotNull
    private final Lazy view$delegate = LazyKt.lazy(new StackEducationView$view$2(this));
    @NotNull
    private final Lazy titleTextView$delegate = LazyKt.lazy(new StackEducationView$titleTextView$2(this));
    @NotNull
    private final Lazy descTextView$delegate = LazyKt.lazy(new StackEducationView$descTextView$2(this));

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StackEducationView(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        LayoutInflater.from(context).inflate(R.layout.bubble_stack_user_education, this);
        setVisibility(8);
        setElevation(getResources().getDimensionPixelSize(R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final View getView() {
        return (View) this.view$delegate.getValue();
    }

    private final TextView getTitleTextView() {
        return (TextView) this.titleTextView$delegate.getValue();
    }

    private final TextView getDescTextView() {
        return (TextView) this.descTextView$delegate.getValue();
    }

    @Override // android.view.View
    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        setTextColor();
    }

    private final void setTextColor() {
        TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{16843829, 16842809});
        int color = obtainStyledAttributes.getColor(0, -16777216);
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
            i = R.drawable.bubble_stack_user_education_bg;
        } else {
            i = R.drawable.bubble_stack_user_education_bg_rtl;
        }
        view.setBackgroundResource(i);
    }

    public final boolean show(@NotNull final PointF stackPosition) {
        Intrinsics.checkNotNullParameter(stackPosition, "stackPosition");
        if (getVisibility() == 0) {
            return false;
        }
        setAlpha(0.0f);
        setVisibility(0);
        post(new Runnable() { // from class: com.android.wm.shell.bubbles.StackEducationView$show$1
            @Override // java.lang.Runnable
            public final void run() {
                View view;
                long j;
                view = StackEducationView.this.getView();
                view.setTranslationY((stackPosition.y + (view.getContext().getResources().getDimensionPixelSize(R.dimen.bubble_size) / 2)) - (view.getHeight() / 2));
                ViewPropertyAnimator animate = StackEducationView.this.animate();
                j = StackEducationView.this.ANIMATE_DURATION;
                animate.setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
            }
        });
        setShouldShow(false);
        return true;
    }

    public final void hide(boolean z) {
        if (getVisibility() != 0 || this.isHiding) {
            return;
        }
        animate().alpha(0.0f).setDuration(z ? this.ANIMATE_DURATION_SHORT : this.ANIMATE_DURATION).withEndAction(new Runnable() { // from class: com.android.wm.shell.bubbles.StackEducationView$hide$1
            @Override // java.lang.Runnable
            public final void run() {
                StackEducationView.this.setVisibility(8);
            }
        });
    }

    private final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesOnboarding", !z).apply();
    }
}
