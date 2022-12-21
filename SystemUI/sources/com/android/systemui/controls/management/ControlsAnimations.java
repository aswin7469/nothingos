package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.constraintlayout.motion.widget.Key;
import androidx.lifecycle.LifecycleObserver;
import com.android.systemui.animation.Interpolators;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u001c\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0007J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u000e\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsAnimations;", "", "()V", "ALPHA_ENTER_DELAY", "", "ALPHA_ENTER_DURATION", "ALPHA_EXIT_DURATION", "Y_TRANSLATION_ENTER_DELAY", "Y_TRANSLATION_ENTER_DURATION", "Y_TRANSLATION_EXIT_DURATION", "translationY", "", "enterAnimation", "Landroid/animation/Animator;", "view", "Landroid/view/View;", "enterWindowTransition", "Lcom/android/systemui/controls/management/WindowTransition;", "id", "", "exitAnimation", "onEnd", "Ljava/lang/Runnable;", "exitWindowTransition", "observerForAnimations", "Landroidx/lifecycle/LifecycleObserver;", "Landroid/view/ViewGroup;", "window", "Landroid/view/Window;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsAnimations.kt */
public final class ControlsAnimations {
    private static final long ALPHA_ENTER_DELAY = 183;
    private static final long ALPHA_ENTER_DURATION = 167;
    private static final long ALPHA_EXIT_DURATION = 183;
    public static final ControlsAnimations INSTANCE = new ControlsAnimations();
    private static final long Y_TRANSLATION_ENTER_DELAY = 0;
    private static final long Y_TRANSLATION_ENTER_DURATION = 217;
    private static final long Y_TRANSLATION_EXIT_DURATION = 183;
    /* access modifiers changed from: private */
    public static float translationY = -1.0f;

    private ControlsAnimations() {
    }

    public final LifecycleObserver observerForAnimations(ViewGroup viewGroup, Window window, Intent intent) {
        Intrinsics.checkNotNullParameter(viewGroup, "view");
        Intrinsics.checkNotNullParameter(window, "window");
        Intrinsics.checkNotNullParameter(intent, "intent");
        return new ControlsAnimations$observerForAnimations$1(intent, viewGroup, window);
    }

    public final Animator enterAnimation(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        Log.d("ControlsUiController", "Enter animation for " + view);
        view.setTransitionAlpha(0.0f);
        view.setAlpha(1.0f);
        view.setTranslationY(translationY);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(Interpolators.DECELERATE_QUINT);
        ofFloat.setStartDelay(183);
        ofFloat.setDuration(ALPHA_ENTER_DURATION);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, Key.TRANSLATION_Y, new float[]{0.0f});
        ofFloat2.setInterpolator(Interpolators.DECELERATE_QUINT);
        ofFloat2.setStartDelay(Y_TRANSLATION_ENTER_DURATION);
        ofFloat2.setDuration(Y_TRANSLATION_ENTER_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        return animatorSet;
    }

    public static /* synthetic */ Animator exitAnimation$default(View view, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        return exitAnimation(view, runnable);
    }

    @JvmStatic
    public static final Animator exitAnimation(View view, Runnable runnable) {
        Intrinsics.checkNotNullParameter(view, "view");
        Log.d("ControlsUiController", "Exit animation for " + view);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", new float[]{0.0f});
        ofFloat.setInterpolator(Interpolators.ACCELERATE);
        ofFloat.setDuration(183);
        view.setTranslationY(0.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, Key.TRANSLATION_Y, new float[]{-translationY});
        ofFloat2.setInterpolator(Interpolators.ACCELERATE);
        ofFloat2.setDuration(183);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        if (runnable != null) {
            animatorSet.addListener(new ControlsAnimations$exitAnimation$1$1$1(runnable));
        }
        return animatorSet;
    }

    public final WindowTransition enterWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$enterWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }

    public final WindowTransition exitWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$exitWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }
}
