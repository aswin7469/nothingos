package com.android.systemui.statusbar.notification.row;

import android.view.View;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ NotificationMenuRowPlugin.MenuItem f$4;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda0(NotificationGutsManager notificationGutsManager, View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        this.f$0 = notificationGutsManager;
        this.f$1 = view;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = menuItem;
    }

    public final void run() {
        this.f$0.mo41609x864d1971(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
