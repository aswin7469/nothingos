package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarRemoteInputCallback$$ExternalSyntheticLambda3 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ StatusBarRemoteInputCallback f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ NotificationRemoteInputManager.ClickHandler f$2;

    public /* synthetic */ StatusBarRemoteInputCallback$$ExternalSyntheticLambda3(StatusBarRemoteInputCallback statusBarRemoteInputCallback, PendingIntent pendingIntent, NotificationRemoteInputManager.ClickHandler clickHandler) {
        this.f$0 = statusBarRemoteInputCallback;
        this.f$1 = pendingIntent;
        this.f$2 = clickHandler;
    }

    public final boolean onDismiss() {
        return this.f$0.mo45347xedfc7ef6(this.f$1, this.f$2);
    }
}
