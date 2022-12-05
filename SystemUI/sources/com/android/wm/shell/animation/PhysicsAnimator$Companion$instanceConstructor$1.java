package com.android.wm.shell.animation;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
/* synthetic */ class PhysicsAnimator$Companion$instanceConstructor$1 extends FunctionReferenceImpl implements Function1<Object, PhysicsAnimator<Object>> {
    public static final PhysicsAnimator$Companion$instanceConstructor$1 INSTANCE = new PhysicsAnimator$Companion$instanceConstructor$1();

    PhysicsAnimator$Companion$instanceConstructor$1() {
        super(1, PhysicsAnimator.class, "<init>", "<init>(Ljava/lang/Object;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke */
    public final PhysicsAnimator<Object> mo1949invoke(@NotNull Object p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        return new PhysicsAnimator<>(p0, null);
    }
}
