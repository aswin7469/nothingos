package com.android.wm.shell.animation;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public /* synthetic */ class PhysicsAnimator$startAction$1 extends FunctionReferenceImpl implements Function0<Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PhysicsAnimator$startAction$1(PhysicsAnimator<T> physicsAnimator) {
        super(0, physicsAnimator, PhysicsAnimator.class, "startInternal", "startInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        ((PhysicsAnimator) this.receiver).startInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
    }
}
