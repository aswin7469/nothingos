package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.ActivityStarter;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationPresenter$2$$ExternalSyntheticLambda0 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ StatusBarNotificationPresenter$2$$ExternalSyntheticLambda0(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final boolean onDismiss() {
        return this.f$0.run();
    }
}
