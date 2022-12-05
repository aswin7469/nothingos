package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public /* synthetic */ class PhysicsAnimator$cancelAction$1 extends FunctionReferenceImpl implements Function1<Set<? extends FloatPropertyCompat<? super T>>, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PhysicsAnimator$cancelAction$1(PhysicsAnimator<T> physicsAnimator) {
        super(1, physicsAnimator, PhysicsAnimator.class, "cancelInternal", "cancelInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(Ljava/util/Set;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Object obj) {
        invoke((Set) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull Set<? extends FloatPropertyCompat<? super T>> p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((PhysicsAnimator) this.receiver).cancelInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(p0);
    }
}
