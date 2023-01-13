package com.android.p019wm.shell.animation;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$cancelAction$1 */
/* compiled from: PhysicsAnimator.kt */
/* synthetic */ class PhysicsAnimator$cancelAction$1 extends FunctionReferenceImpl implements Function1<Set<? extends FloatPropertyCompat<? super T>>, Unit> {
    PhysicsAnimator$cancelAction$1(Object obj) {
        super(1, obj, PhysicsAnimator.class, "cancelInternal", "cancelInternal$WMShell_release(Ljava/util/Set;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Set) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Set<? extends FloatPropertyCompat<? super T>> set) {
        Intrinsics.checkNotNullParameter(set, "p0");
        ((PhysicsAnimator) this.receiver).cancelInternal$WMShell_release(set);
    }
}
