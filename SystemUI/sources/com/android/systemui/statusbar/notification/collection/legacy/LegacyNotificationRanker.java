package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J.\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u00032\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H&R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0011À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/legacy/LegacyNotificationRanker;", "", "rankingMap", "Landroid/service/notification/NotificationListenerService$RankingMap;", "getRankingMap", "()Landroid/service/notification/NotificationListenerService$RankingMap;", "isNotificationForCurrentProfiles", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "updateRanking", "", "newRankingMap", "entries", "", "reason", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LegacyNotificationRanker.kt */
public interface LegacyNotificationRanker {
    NotificationListenerService.RankingMap getRankingMap();

    boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry);

    List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap, Collection<NotificationEntry> collection, String str);
}
