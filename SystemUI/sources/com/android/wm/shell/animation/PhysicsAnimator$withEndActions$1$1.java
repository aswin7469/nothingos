package com.android.wm.shell.animation;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public /* synthetic */ class PhysicsAnimator$withEndActions$1$1 extends FunctionReferenceImpl implements Function0<Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PhysicsAnimator$withEndActions$1$1(Runnable runnable) {
        super(0, runnable, Runnable.class, "run", "run()V", 0);
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
        ((Runnable) this.receiver).run();
    }
}
