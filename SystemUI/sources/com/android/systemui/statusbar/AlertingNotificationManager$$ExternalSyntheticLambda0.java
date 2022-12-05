package com.android.systemui.statusbar;

import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class AlertingNotificationManager$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ AlertingNotificationManager$$ExternalSyntheticLambda0 INSTANCE = new AlertingNotificationManager$$ExternalSyntheticLambda0();

    private /* synthetic */ AlertingNotificationManager$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        NotificationEntry notificationEntry;
        notificationEntry = ((AlertingNotificationManager.AlertEntry) obj).mEntry;
        return notificationEntry;
    }
}
