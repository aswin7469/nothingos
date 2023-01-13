package com.android.systemui.statusbar.notification.collection;

import java.util.Comparator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationRankingManager$$ExternalSyntheticLambda0 implements Comparator {
    public final /* synthetic */ NotificationRankingManager f$0;

    public /* synthetic */ NotificationRankingManager$$ExternalSyntheticLambda0(NotificationRankingManager notificationRankingManager) {
        this.f$0 = notificationRankingManager;
    }

    public final int compare(Object obj, Object obj2) {
        return NotificationRankingManager.m3101rankingComparator$lambda0(this.f$0, (NotificationEntry) obj, (NotificationEntry) obj2);
    }
}
