package com.android.systemui.keyguard;

import android.animation.ValueAnimator;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import com.android.systemui.keyguard.KeyguardViewMediator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$6$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ KeyguardViewMediator.C21726 f$0;
    public final /* synthetic */ RemoteAnimationTarget f$1;
    public final /* synthetic */ SyncRtSurfaceTransactionApplier f$2;

    public /* synthetic */ KeyguardViewMediator$6$$ExternalSyntheticLambda0(KeyguardViewMediator.C21726 r1, RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier) {
        this.f$0 = r1;
        this.f$1 = remoteAnimationTarget;
        this.f$2 = syncRtSurfaceTransactionApplier;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo33300x178a2bf5(this.f$1, this.f$2, valueAnimator);
    }
}
