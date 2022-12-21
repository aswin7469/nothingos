package com.android.p019wm.shell.animation;

import com.android.p019wm.shell.animation.PhysicsAnimatorTestUtils;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$AnimatorTestHelper$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3356x1086a implements Runnable {
    public final /* synthetic */ PhysicsAnimatorTestUtils.AnimatorTestHelper f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ CountDownLatch f$2;

    public /* synthetic */ C3356x1086a(PhysicsAnimatorTestUtils.AnimatorTestHelper animatorTestHelper, Set set, CountDownLatch countDownLatch) {
        this.f$0 = animatorTestHelper;
        this.f$1 = set;
        this.f$2 = countDownLatch;
    }

    public final void run() {
        PhysicsAnimatorTestUtils.AnimatorTestHelper.m3398cancelForTest$lambda1(this.f$0, this.f$1, this.f$2);
    }
}
