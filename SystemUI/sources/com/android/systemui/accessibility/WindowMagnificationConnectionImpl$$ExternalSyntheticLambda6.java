package com.android.systemui.accessibility;

import android.view.accessibility.IRemoteMagnificationAnimationCallback;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WindowMagnificationConnectionImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ WindowMagnificationConnectionImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ IRemoteMagnificationAnimationCallback f$2;

    public /* synthetic */ WindowMagnificationConnectionImpl$$ExternalSyntheticLambda6(WindowMagnificationConnectionImpl windowMagnificationConnectionImpl, int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.f$0 = windowMagnificationConnectionImpl;
        this.f$1 = i;
        this.f$2 = iRemoteMagnificationAnimationCallback;
    }

    public final void run() {
        this.f$0.mo29986x31bfb220(this.f$1, this.f$2);
    }
}
