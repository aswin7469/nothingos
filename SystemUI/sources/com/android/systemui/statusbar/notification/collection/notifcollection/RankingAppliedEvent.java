package com.android.systemui.statusbar.notification.collection.notifcollection;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/RankingAppliedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "()V", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifEvent.kt */
public final class RankingAppliedEvent extends NotifEvent {
    public RankingAppliedEvent() {
        super((DefaultConstructorMarker) null);
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onRankingApplied();
    }
}
