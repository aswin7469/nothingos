package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$startInternal$2 */
/* compiled from: PhysicsAnimator.kt */
/* synthetic */ class PhysicsAnimator$startInternal$2 extends FunctionReferenceImpl implements Function0<Unit> {
    PhysicsAnimator$startInternal$2(Object obj) {
        super(0, obj, SpringAnimation.class, "start", "start()V", 0);
    }

    public final void invoke() {
        ((SpringAnimation) this.receiver).start();
    }
}
