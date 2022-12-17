package com.android.settings.dashboard;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DashboardFragment$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ DashboardFragment f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ CountDownLatch f$2;

    public /* synthetic */ DashboardFragment$$ExternalSyntheticLambda7(DashboardFragment dashboardFragment, List list, CountDownLatch countDownLatch) {
        this.f$0 = dashboardFragment;
        this.f$1 = list;
        this.f$2 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$refreshDashboardTiles$10(this.f$1, this.f$2);
    }
}
