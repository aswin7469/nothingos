package com.android.p019wm.shell.animation;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator$Companion$instanceConstructor$1 */
/* compiled from: PhysicsAnimator.kt */
/* synthetic */ class PhysicsAnimator$Companion$instanceConstructor$1 extends FunctionReferenceImpl implements Function1<Object, PhysicsAnimator<Object>> {
    public static final PhysicsAnimator$Companion$instanceConstructor$1 INSTANCE = new PhysicsAnimator$Companion$instanceConstructor$1();

    PhysicsAnimator$Companion$instanceConstructor$1() {
        super(1, PhysicsAnimator.class, "<init>", "<init>(Ljava/lang/Object;)V", 0);
    }

    public final PhysicsAnimator<Object> invoke(Object obj) {
        Intrinsics.checkNotNullParameter(obj, "p0");
        return new PhysicsAnimator<>(obj, (DefaultConstructorMarker) null);
    }
}
