package com.android.systemui;

import android.service.notification.StatusBarNotification;
import com.android.systemui.ForegroundServiceController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ForegroundServiceNotificationListener$$ExternalSyntheticLambda0 implements ForegroundServiceController.UserStateUpdateCallback {
    public final /* synthetic */ ForegroundServiceNotificationListener f$0;
    public final /* synthetic */ StatusBarNotification f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ForegroundServiceNotificationListener$$ExternalSyntheticLambda0(ForegroundServiceNotificationListener foregroundServiceNotificationListener, StatusBarNotification statusBarNotification, int i) {
        this.f$0 = foregroundServiceNotificationListener;
        this.f$1 = statusBarNotification;
        this.f$2 = i;
    }

    public final boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState) {
        return this.f$0.mo29647x855961b8(this.f$1, this.f$2, foregroundServicesUserState);
    }
}
