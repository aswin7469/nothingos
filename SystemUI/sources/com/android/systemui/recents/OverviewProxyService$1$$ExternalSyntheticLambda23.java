package com.android.systemui.recents;

import android.view.MotionEvent;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.phone.CentralSurfaces;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda23 implements Runnable {
    public final /* synthetic */ OverviewProxyService.C24211 f$0;
    public final /* synthetic */ MotionEvent f$1;
    public final /* synthetic */ CentralSurfaces f$2;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda23(OverviewProxyService.C24211 r1, MotionEvent motionEvent, CentralSurfaces centralSurfaces) {
        this.f$0 = r1;
        this.f$1 = motionEvent;
        this.f$2 = centralSurfaces;
    }

    public final void run() {
        this.f$0.mo37162x4e40ddac(this.f$1, this.f$2);
    }
}
