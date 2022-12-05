package com.android.systemui.controls.management;

import android.animation.Animator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
public final class WindowTransition extends Transition {
    @NotNull
    private final Function1<View, Animator> animator;

    /* JADX WARN: Multi-variable type inference failed */
    public WindowTransition(@NotNull Function1<? super View, ? extends Animator> animator) {
        Intrinsics.checkNotNullParameter(animator, "animator");
        this.animator = animator;
    }

    @Override // android.transition.Transition
    public void captureStartValues(@NotNull TransitionValues tv) {
        Intrinsics.checkNotNullParameter(tv, "tv");
        Map map = tv.values;
        Intrinsics.checkNotNullExpressionValue(map, "tv.values");
        map.put("item", Float.valueOf(0.0f));
    }

    @Override // android.transition.Transition
    public void captureEndValues(@NotNull TransitionValues tv) {
        Intrinsics.checkNotNullParameter(tv, "tv");
        Map map = tv.values;
        Intrinsics.checkNotNullExpressionValue(map, "tv.values");
        map.put("item", Float.valueOf(1.0f));
    }

    @Override // android.transition.Transition
    @Nullable
    public Animator createAnimator(@NotNull ViewGroup sceneRoot, @Nullable TransitionValues transitionValues, @Nullable TransitionValues transitionValues2) {
        Intrinsics.checkNotNullParameter(sceneRoot, "sceneRoot");
        Function1<View, Animator> function1 = this.animator;
        Intrinsics.checkNotNull(transitionValues);
        View view = transitionValues.view;
        Intrinsics.checkNotNullExpressionValue(view, "!!.view");
        return function1.mo1949invoke(view);
    }
}
