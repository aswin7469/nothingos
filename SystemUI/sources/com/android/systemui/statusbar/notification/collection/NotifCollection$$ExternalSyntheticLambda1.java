package com.android.systemui.statusbar.notification.collection;

import java.util.Objects;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return Objects.equals(((NotificationEntry) obj).getSbn().getGroupKey(), this.f$0);
    }
}
