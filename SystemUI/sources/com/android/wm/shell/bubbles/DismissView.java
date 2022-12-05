package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: DismissView.kt */
/* loaded from: classes2.dex */
public final class DismissView extends FrameLayout {
    @NotNull
    private final PhysicsAnimator<DismissCircleView> animator;
    @NotNull
    private DismissCircleView circle;
    private boolean isShowing;
    @NotNull
    private final PhysicsAnimator.SpringConfig spring = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
    private final int DISMISS_SCRIM_FADE_MS = 200;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DismissView(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        DismissCircleView dismissCircleView = new DismissCircleView(context);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
        layoutParams.gravity = 81;
        dismissCircleView.setLayoutParams(layoutParams);
        Resources resources = dismissCircleView.getResources();
        int i = R.dimen.floating_dismiss_gradient_height;
        dismissCircleView.setTranslationY(resources.getDimensionPixelSize(i));
        Unit unit = Unit.INSTANCE;
        this.circle = dismissCircleView;
        this.animator = PhysicsAnimator.Companion.getInstance(dismissCircleView);
        setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(i), 80));
        setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.floating_dismiss_bottom_margin));
        setClipToPadding(false);
        setClipChildren(false);
        setVisibility(4);
        setBackgroundResource(R.drawable.floating_dismiss_gradient_transition);
        addView(this.circle);
    }

    @NotNull
    public final DismissCircleView getCircle() {
        return this.circle;
    }

    public final boolean isShowing() {
        return this.isShowing;
    }

    public final void show() {
        if (this.isShowing) {
            return;
        }
        this.isShowing = true;
        setVisibility(0);
        Drawable background = getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) background).startTransition(this.DISMISS_SCRIM_FADE_MS);
        this.animator.cancel();
        PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
        DynamicAnimation.ViewProperty TRANSLATION_Y = DynamicAnimation.TRANSLATION_Y;
        Intrinsics.checkNotNullExpressionValue(TRANSLATION_Y, "TRANSLATION_Y");
        physicsAnimator.spring(TRANSLATION_Y, 0.0f, this.spring).start();
    }

    public final void hide() {
        if (!this.isShowing) {
            return;
        }
        this.isShowing = false;
        Drawable background = getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
        ((TransitionDrawable) background).reverseTransition(this.DISMISS_SCRIM_FADE_MS);
        PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
        DynamicAnimation.ViewProperty TRANSLATION_Y = DynamicAnimation.TRANSLATION_Y;
        Intrinsics.checkNotNullExpressionValue(TRANSLATION_Y, "TRANSLATION_Y");
        physicsAnimator.spring(TRANSLATION_Y, getHeight(), this.spring).withEndActions(new DismissView$hide$1(this)).start();
    }

    public final void updateResources() {
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size);
        this.circle.getLayoutParams().width = dimensionPixelSize;
        this.circle.getLayoutParams().height = dimensionPixelSize;
        this.circle.requestLayout();
    }
}
