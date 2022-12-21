package com.android.systemui.statusbar.phone;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationPresenter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StatusBarNotificationPresenter f$0;

    public /* synthetic */ StatusBarNotificationPresenter$$ExternalSyntheticLambda0(StatusBarNotificationPresenter statusBarNotificationPresenter) {
        this.f$0 = statusBarNotificationPresenter;
    }

    public final void run() {
        this.f$0.maybeClosePanelForShadeEmptied();
    }
}
