package com.android.systemui.statusbar.notification.collection;

import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda4 implements Predicate {
    public final boolean test(Object obj) {
        return ((NotificationEntry) obj).getSbn().getNotification().isGroupSummary();
    }
}
