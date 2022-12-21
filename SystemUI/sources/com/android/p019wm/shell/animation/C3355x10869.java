package com.android.p019wm.shell.animation;

import com.android.p019wm.shell.animation.PhysicsAnimatorTestUtils;
import java.util.concurrent.CountDownLatch;

/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$AnimatorTestHelper$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3355x10869 implements Runnable {
    public final /* synthetic */ PhysicsAnimatorTestUtils.AnimatorTestHelper f$0;
    public final /* synthetic */ CountDownLatch f$1;

    public /* synthetic */ C3355x10869(PhysicsAnimatorTestUtils.AnimatorTestHelper animatorTestHelper, CountDownLatch countDownLatch) {
        this.f$0 = animatorTestHelper;
        this.f$1 = countDownLatch;
    }

    public final void run() {
        PhysicsAnimatorTestUtils.AnimatorTestHelper.m3399startForTest$lambda0(this.f$0, this.f$1);
    }
}
