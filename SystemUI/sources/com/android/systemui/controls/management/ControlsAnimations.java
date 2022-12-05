package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
public final class ControlsAnimations {
    @NotNull
    public static final ControlsAnimations INSTANCE = new ControlsAnimations();
    private static float translationY = -1.0f;

    private ControlsAnimations() {
    }

    @NotNull
    public final LifecycleObserver observerForAnimations(@NotNull final ViewGroup view, @NotNull final Window window, @NotNull final Intent intent) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(window, "window");
        Intrinsics.checkNotNullParameter(intent, "intent");
        return new LifecycleObserver(intent, view, window) { // from class: com.android.systemui.controls.management.ControlsAnimations$observerForAnimations$1
            final /* synthetic */ Intent $intent;
            final /* synthetic */ ViewGroup $view;
            final /* synthetic */ Window $window;
            private boolean showAnimation;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                float f;
                this.$intent = intent;
                this.$view = view;
                this.$window = window;
                boolean z = false;
                this.showAnimation = intent.getBooleanExtra("extra_animate", false);
                view.setTransitionGroup(true);
                view.setTransitionAlpha(0.0f);
                f = ControlsAnimations.translationY;
                if (f == -1.0f ? true : z) {
                    ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                    ControlsAnimations.translationY = view.getContext().getResources().getDimensionPixelSize(R$dimen.global_actions_controls_y_translation);
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public final void setup() {
                Window window2 = this.$window;
                ViewGroup viewGroup = this.$view;
                window2.setAllowEnterTransitionOverlap(true);
                ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                window2.setEnterTransition(controlsAnimations.enterWindowTransition(viewGroup.getId()));
                window2.setExitTransition(controlsAnimations.exitWindowTransition(viewGroup.getId()));
                window2.setReenterTransition(controlsAnimations.enterWindowTransition(viewGroup.getId()));
                window2.setReturnTransition(controlsAnimations.exitWindowTransition(viewGroup.getId()));
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public final void enterAnimation() {
                if (this.showAnimation) {
                    ControlsAnimations.INSTANCE.enterAnimation(this.$view).start();
                    this.showAnimation = false;
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public final void resetAnimation() {
                this.$view.setTranslationY(0.0f);
            }
        };
    }

    @NotNull
    public final Animator enterAnimation(@NotNull View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        Log.d("ControlsUiController", Intrinsics.stringPlus("Enter animation for ", view));
        view.setTransitionAlpha(0.0f);
        view.setAlpha(1.0f);
        view.setTranslationY(translationY);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", 0.0f, 1.0f);
        Interpolator interpolator = Interpolators.DECELERATE_QUINT;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setStartDelay(183L);
        ofFloat.setDuration(167L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", 0.0f);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setStartDelay(217L);
        ofFloat2.setDuration(217L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        return animatorSet;
    }

    public static /* synthetic */ Animator exitAnimation$default(View view, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        return exitAnimation(view, runnable);
    }

    @NotNull
    public static final Animator exitAnimation(@NotNull View view, @Nullable final Runnable runnable) {
        Intrinsics.checkNotNullParameter(view, "view");
        Log.d("ControlsUiController", Intrinsics.stringPlus("Exit animation for ", view));
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", 0.0f);
        Interpolator interpolator = Interpolators.ACCELERATE;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration(183L);
        view.setTranslationY(0.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", -translationY);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setDuration(183L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        if (runnable != null) {
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.management.ControlsAnimations$exitAnimation$1$1$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@NotNull Animator animation) {
                    Intrinsics.checkNotNullParameter(animation, "animation");
                    runnable.run();
                }
            });
        }
        return animatorSet;
    }

    @NotNull
    public final WindowTransition enterWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$enterWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }

    @NotNull
    public final WindowTransition exitWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$exitWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }
}
