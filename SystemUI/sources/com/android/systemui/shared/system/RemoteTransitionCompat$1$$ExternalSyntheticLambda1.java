package com.android.systemui.shared.system;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RemoteTransitionCompat$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ RemoteTransitionRunner f$0;
    public final /* synthetic */ IBinder f$1;
    public final /* synthetic */ TransitionInfo f$2;
    public final /* synthetic */ SurfaceControl.Transaction f$3;
    public final /* synthetic */ IBinder f$4;
    public final /* synthetic */ Runnable f$5;

    public /* synthetic */ RemoteTransitionCompat$1$$ExternalSyntheticLambda1(RemoteTransitionRunner remoteTransitionRunner, IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Runnable runnable) {
        this.f$0 = remoteTransitionRunner;
        this.f$1 = iBinder;
        this.f$2 = transitionInfo;
        this.f$3 = transaction;
        this.f$4 = iBinder2;
        this.f$5 = runnable;
    }

    public final void run() {
        this.f$0.mergeAnimation(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}