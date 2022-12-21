package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/InitEntryEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)V", "getEntry", "()Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "component1", "copy", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifEvent.kt */
public final class InitEntryEvent extends NotifEvent {
    private final NotificationEntry entry;

    public static /* synthetic */ InitEntryEvent copy$default(InitEntryEvent initEntryEvent, NotificationEntry notificationEntry, int i, Object obj) {
        if ((i & 1) != 0) {
            notificationEntry = initEntryEvent.entry;
        }
        return initEntryEvent.copy(notificationEntry);
    }

    public final NotificationEntry component1() {
        return this.entry;
    }

    public final InitEntryEvent copy(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return new InitEntryEvent(notificationEntry);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof InitEntryEvent) && Intrinsics.areEqual((Object) this.entry, (Object) ((InitEntryEvent) obj).entry);
    }

    public int hashCode() {
        return this.entry.hashCode();
    }

    public String toString() {
        return "InitEntryEvent(entry=" + this.entry + ')';
    }

    public final NotificationEntry getEntry() {
        return this.entry;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public InitEntryEvent(NotificationEntry notificationEntry) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.entry = notificationEntry;
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryInit(this.entry);
    }
}
