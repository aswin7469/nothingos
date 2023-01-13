package com.android.systemui.controls.management;

import android.animation.Animator;
import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Landroid/animation/Animator;", "view", "Landroid/view/View;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsAnimations.kt */
final class ControlsAnimations$enterWindowTransition$1 extends Lambda implements Function1<View, Animator> {
    public static final ControlsAnimations$enterWindowTransition$1 INSTANCE = new ControlsAnimations$enterWindowTransition$1();

    ControlsAnimations$enterWindowTransition$1() {
        super(1);
    }

    public final Animator invoke(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        return ControlsAnimations.INSTANCE.enterAnimation(view);
    }
}
