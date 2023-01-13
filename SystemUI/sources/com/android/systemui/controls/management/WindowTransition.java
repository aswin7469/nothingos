package com.android.systemui.controls.management;

import android.animation.Animator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B(\u0012!\u0010\u0002\u001a\u001d\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003¢\u0006\u0002\u0010\tJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J&\u0010\u0011\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000fH\u0016R,\u0010\u0002\u001a\u001d\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/controls/management/WindowTransition;", "Landroid/transition/Transition;", "animator", "Lkotlin/Function1;", "Landroid/view/View;", "Lkotlin/ParameterName;", "name", "view", "Landroid/animation/Animator;", "(Lkotlin/jvm/functions/Function1;)V", "getAnimator", "()Lkotlin/jvm/functions/Function1;", "captureEndValues", "", "tv", "Landroid/transition/TransitionValues;", "captureStartValues", "createAnimator", "sceneRoot", "Landroid/view/ViewGroup;", "startValues", "endValues", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsAnimations.kt */
public final class WindowTransition extends Transition {
    private final Function1<View, Animator> animator;

    public final Function1<View, Animator> getAnimator() {
        return this.animator;
    }

    public WindowTransition(Function1<? super View, ? extends Animator> function1) {
        Intrinsics.checkNotNullParameter(function1, "animator");
        this.animator = function1;
    }

    public void captureStartValues(TransitionValues transitionValues) {
        Intrinsics.checkNotNullParameter(transitionValues, "tv");
        Map map = transitionValues.values;
        Intrinsics.checkNotNullExpressionValue(map, "tv.values");
        map.put("item", Float.valueOf(0.0f));
    }

    public void captureEndValues(TransitionValues transitionValues) {
        Intrinsics.checkNotNullParameter(transitionValues, "tv");
        Map map = transitionValues.values;
        Intrinsics.checkNotNullExpressionValue(map, "tv.values");
        map.put("item", Float.valueOf(1.0f));
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Intrinsics.checkNotNullParameter(viewGroup, "sceneRoot");
        Function1<View, Animator> function1 = this.animator;
        Intrinsics.checkNotNull(transitionValues);
        View view = transitionValues.view;
        Intrinsics.checkNotNullExpressionValue(view, "startValues!!.view");
        return function1.invoke(view);
    }
}
