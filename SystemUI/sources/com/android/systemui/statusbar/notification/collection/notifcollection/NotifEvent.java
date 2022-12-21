package com.android.systemui.statusbar.notification.collection.notifcollection;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001B\u0007\b\u0004¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0007H&\u0001\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "", "()V", "dispatchTo", "", "listeners", "", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "dispatchToListener", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/BindEntryEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/InitEntryEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/EntryAddedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/EntryUpdatedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/EntryRemovedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CleanUpEntryEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/RankingUpdatedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/RankingAppliedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/ChannelChangedEvent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifEvent.kt */
public abstract class NotifEvent {
    public /* synthetic */ NotifEvent(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract void dispatchToListener(NotifCollectionListener notifCollectionListener);

    private NotifEvent() {
    }

    public final void dispatchTo(List<? extends NotifCollectionListener> list) {
        Intrinsics.checkNotNullParameter(list, "listeners");
        int size = list.size();
        for (int i = 0; i < size; i++) {
            dispatchToListener((NotifCollectionListener) list.get(i));
        }
    }
}
