package com.android.p019wm.shell.onehanded;

import android.view.SurfaceControl;
import com.android.p019wm.shell.onehanded.OneHandedAnimationController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.onehanded.OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3502x55a9da49 implements Consumer {
    public final /* synthetic */ OneHandedAnimationController.OneHandedTransitionAnimator f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;

    public /* synthetic */ C3502x55a9da49(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator, SurfaceControl.Transaction transaction) {
        this.f$0 = oneHandedTransitionAnimator;
        this.f$1 = transaction;
    }

    public final void accept(Object obj) {
        this.f$0.mo49844xb3944cf7(this.f$1, (OneHandedAnimationCallback) obj);
    }
}
