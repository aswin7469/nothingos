package com.android.p019wm.shell.pip.phone;

import android.os.Bundle;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.p019wm.shell.pip.phone.PipAccessibilityInteractionConnection;

/* renamed from: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3529xdda2519e implements Runnable {
    public final /* synthetic */ PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ int f$7;
    public final /* synthetic */ long f$8;

    public /* synthetic */ C3529xdda2519e(PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl, long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) {
        this.f$0 = pipAccessibilityInteractionConnectionImpl;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = bundle;
        this.f$4 = i2;
        this.f$5 = iAccessibilityInteractionConnectionCallback;
        this.f$6 = i3;
        this.f$7 = i4;
        this.f$8 = j2;
    }

    public final void run() {
        this.f$0.mo50320x2a817942(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
    }
}
