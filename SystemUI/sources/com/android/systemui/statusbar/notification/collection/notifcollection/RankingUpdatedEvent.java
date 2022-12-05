package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.NotificationListenerService;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class RankingUpdatedEvent extends NotifEvent {
    @NotNull
    private final NotificationListenerService.RankingMap rankingMap;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RankingUpdatedEvent) && Intrinsics.areEqual(this.rankingMap, ((RankingUpdatedEvent) obj).rankingMap);
    }

    public int hashCode() {
        return this.rankingMap.hashCode();
    }

    @NotNull
    public String toString() {
        return "RankingUpdatedEvent(rankingMap=" + this.rankingMap + ')';
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RankingUpdatedEvent(@NotNull NotificationListenerService.RankingMap rankingMap) {
        super(null);
        Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
        this.rankingMap = rankingMap;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(@NotNull NotifCollectionListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        listener.onRankingUpdate(this.rankingMap);
    }
}
