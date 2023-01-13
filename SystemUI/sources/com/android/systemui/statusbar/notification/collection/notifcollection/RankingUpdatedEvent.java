package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.NotificationListenerService;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/RankingUpdatedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "rankingMap", "Landroid/service/notification/NotificationListenerService$RankingMap;", "(Landroid/service/notification/NotificationListenerService$RankingMap;)V", "getRankingMap", "()Landroid/service/notification/NotificationListenerService$RankingMap;", "component1", "copy", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifEvent.kt */
public final class RankingUpdatedEvent extends NotifEvent {
    private final NotificationListenerService.RankingMap rankingMap;

    public static /* synthetic */ RankingUpdatedEvent copy$default(RankingUpdatedEvent rankingUpdatedEvent, NotificationListenerService.RankingMap rankingMap2, int i, Object obj) {
        if ((i & 1) != 0) {
            rankingMap2 = rankingUpdatedEvent.rankingMap;
        }
        return rankingUpdatedEvent.copy(rankingMap2);
    }

    public final NotificationListenerService.RankingMap component1() {
        return this.rankingMap;
    }

    public final RankingUpdatedEvent copy(NotificationListenerService.RankingMap rankingMap2) {
        Intrinsics.checkNotNullParameter(rankingMap2, "rankingMap");
        return new RankingUpdatedEvent(rankingMap2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RankingUpdatedEvent) && Intrinsics.areEqual((Object) this.rankingMap, (Object) ((RankingUpdatedEvent) obj).rankingMap);
    }

    public int hashCode() {
        return this.rankingMap.hashCode();
    }

    public String toString() {
        return "RankingUpdatedEvent(rankingMap=" + this.rankingMap + ')';
    }

    public final NotificationListenerService.RankingMap getRankingMap() {
        return this.rankingMap;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public RankingUpdatedEvent(NotificationListenerService.RankingMap rankingMap2) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(rankingMap2, "rankingMap");
        this.rankingMap = rankingMap2;
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onRankingUpdate(this.rankingMap);
    }
}
