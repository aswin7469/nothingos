package com.android.systemui.statusbar.notification.row.wrapper;

import android.app.PendingIntent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationTemplateViewWrapper$$ExternalSyntheticLambda1 implements PendingIntent.CancelListener {
    public final /* synthetic */ NotificationTemplateViewWrapper f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ NotificationTemplateViewWrapper$$ExternalSyntheticLambda1(NotificationTemplateViewWrapper notificationTemplateViewWrapper, PendingIntent pendingIntent, Runnable runnable) {
        this.f$0 = notificationTemplateViewWrapper;
        this.f$1 = pendingIntent;
        this.f$2 = runnable;
    }

    public final void onCanceled(PendingIntent pendingIntent) {
        this.f$0.mo41788x8c7348a3(this.f$1, this.f$2, pendingIntent);
    }
}
