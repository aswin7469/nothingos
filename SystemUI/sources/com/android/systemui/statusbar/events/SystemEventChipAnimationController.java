package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import com.android.systemui.statusbar.phone.StatusBarWindowView;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SystemEventChipAnimationController.kt */
/* loaded from: classes.dex */
public final class SystemEventChipAnimationController {
    private View animationDotView;
    private FrameLayout animationWindowView;
    @NotNull
    private final Context context;
    @Nullable
    private View currentAnimatedView;
    private boolean initialized;
    @NotNull
    private final StatusBarLocationPublisher locationPublisher;
    @NotNull
    private final SuperStatusBarViewFactory statusBarViewFactory;
    @NotNull
    private final StatusBarWindowController statusBarWindowController;
    private StatusBarWindowView statusBarWindowView;

    public SystemEventChipAnimationController(@NotNull Context context, @NotNull SuperStatusBarViewFactory statusBarViewFactory, @NotNull StatusBarWindowController statusBarWindowController, @NotNull StatusBarLocationPublisher locationPublisher) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(statusBarViewFactory, "statusBarViewFactory");
        Intrinsics.checkNotNullParameter(statusBarWindowController, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(locationPublisher, "locationPublisher");
        this.context = context;
        this.statusBarViewFactory = statusBarViewFactory;
        this.statusBarWindowController = statusBarWindowController;
        this.locationPublisher = locationPublisher;
    }

    public void onChipAnimationStart(@NotNull Function1<? super Context, ? extends View> viewCreator, int i) {
        Intrinsics.checkNotNullParameter(viewCreator, "viewCreator");
        if (!this.initialized) {
            init();
        }
        if (i == 1) {
            View mo1949invoke = viewCreator.mo1949invoke(this.context);
            this.currentAnimatedView = mo1949invoke;
            FrameLayout frameLayout = this.animationWindowView;
            if (frameLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
                throw null;
            }
            frameLayout.addView(mo1949invoke, layoutParamsDefault());
            View view = this.currentAnimatedView;
            if (view == null) {
                return;
            }
            float width = view.getWidth();
            if (view.isLayoutRtl()) {
                width = -width;
            }
            view.setTranslationX(width);
            view.setAlpha(0.0f);
            view.setVisibility(0);
            view.setPadding(this.locationPublisher.getMarginLeft(), 0, this.locationPublisher.getMarginRight(), 0);
            return;
        }
        View view2 = this.currentAnimatedView;
        if (view2 == null) {
            return;
        }
        view2.setTranslationX(0.0f);
        view2.setAlpha(1.0f);
    }

    public void onChipAnimationEnd(int i) {
        if (i == 1) {
            View view = this.currentAnimatedView;
            if (view == null) {
                return;
            }
            view.setTranslationX(0.0f);
            view.setAlpha(1.0f);
            return;
        }
        View view2 = this.currentAnimatedView;
        if (view2 != null) {
            view2.setVisibility(4);
        }
        FrameLayout frameLayout = this.animationWindowView;
        if (frameLayout != null) {
            frameLayout.removeView(this.currentAnimatedView);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            throw null;
        }
    }

    public void onChipAnimationUpdate(@NotNull ValueAnimator animator, int i) {
        Intrinsics.checkNotNullParameter(animator, "animator");
        View view = this.currentAnimatedView;
        if (view == null) {
            return;
        }
        Object animatedValue = animator.getAnimatedValue();
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float floatValue = ((Float) animatedValue).floatValue();
        view.setAlpha(floatValue);
        float width = (1 - floatValue) * view.getWidth();
        if (view.isLayoutRtl()) {
            width = -width;
        }
        view.setTranslationX(width);
    }

    private final void init() {
        this.initialized = true;
        StatusBarWindowView statusBarWindowView = this.statusBarViewFactory.getStatusBarWindowView();
        Intrinsics.checkNotNullExpressionValue(statusBarWindowView, "statusBarViewFactory.statusBarWindowView");
        this.statusBarWindowView = statusBarWindowView;
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.system_event_animation_window, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.FrameLayout");
        FrameLayout frameLayout = (FrameLayout) inflate;
        this.animationWindowView = frameLayout;
        View findViewById = frameLayout.findViewById(R$id.dot_view);
        Intrinsics.checkNotNullExpressionValue(findViewById, "animationWindowView.findViewById(R.id.dot_view)");
        this.animationDotView = findViewById;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 8388629;
        StatusBarWindowView statusBarWindowView2 = this.statusBarWindowView;
        if (statusBarWindowView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusBarWindowView");
            throw null;
        }
        FrameLayout frameLayout2 = this.animationWindowView;
        if (frameLayout2 != null) {
            statusBarWindowView2.addView(frameLayout2, layoutParams);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            throw null;
        }
    }

    private final int start() {
        FrameLayout frameLayout = this.animationWindowView;
        if (frameLayout != null) {
            return frameLayout.isLayoutRtl() ? right() : left();
        }
        Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
        throw null;
    }

    private final int right() {
        return this.locationPublisher.getMarginRight();
    }

    private final int left() {
        return this.locationPublisher.getMarginLeft();
    }

    private final FrameLayout.LayoutParams layoutParamsDefault() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 8388629;
        layoutParams.setMarginStart(start());
        return layoutParams;
    }
}
