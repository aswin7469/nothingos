package com.android.p019wm.shell.pip.phone;

import android.graphics.Region;
import android.view.MagnificationSpec;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.p019wm.shell.pip.phone.PipAccessibilityInteractionConnection;

/* renamed from: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3518xdda2519d implements Runnable {
    public final /* synthetic */ PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Region f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ int f$7;
    public final /* synthetic */ long f$8;
    public final /* synthetic */ MagnificationSpec f$9;

    public /* synthetic */ C3518xdda2519d(PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl, long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
        this.f$0 = pipAccessibilityInteractionConnectionImpl;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = region;
        this.f$4 = i2;
        this.f$5 = iAccessibilityInteractionConnectionCallback;
        this.f$6 = i3;
        this.f$7 = i4;
        this.f$8 = j2;
        this.f$9 = magnificationSpec;
    }

    public final void run() {
        this.f$0.mo50310xe838b180(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
    }
}
