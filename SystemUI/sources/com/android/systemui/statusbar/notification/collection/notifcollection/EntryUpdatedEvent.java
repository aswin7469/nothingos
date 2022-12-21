package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0013\u0010\u0012\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/EntryUpdatedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "fromSystem", "", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;Z)V", "getEntry", "()Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getFromSystem", "()Z", "component1", "component2", "copy", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "equals", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifEvent.kt */
public final class EntryUpdatedEvent extends NotifEvent {
    private final NotificationEntry entry;
    private final boolean fromSystem;

    public static /* synthetic */ EntryUpdatedEvent copy$default(EntryUpdatedEvent entryUpdatedEvent, NotificationEntry notificationEntry, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            notificationEntry = entryUpdatedEvent.entry;
        }
        if ((i & 2) != 0) {
            z = entryUpdatedEvent.fromSystem;
        }
        return entryUpdatedEvent.copy(notificationEntry, z);
    }

    public final NotificationEntry component1() {
        return this.entry;
    }

    public final boolean component2() {
        return this.fromSystem;
    }

    public final EntryUpdatedEvent copy(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return new EntryUpdatedEvent(notificationEntry, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntryUpdatedEvent)) {
            return false;
        }
        EntryUpdatedEvent entryUpdatedEvent = (EntryUpdatedEvent) obj;
        return Intrinsics.areEqual((Object) this.entry, (Object) entryUpdatedEvent.entry) && this.fromSystem == entryUpdatedEvent.fromSystem;
    }

    public int hashCode() {
        int hashCode = this.entry.hashCode() * 31;
        boolean z = this.fromSystem;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "EntryUpdatedEvent(entry=" + this.entry + ", fromSystem=" + this.fromSystem + ')';
    }

    public final NotificationEntry getEntry() {
        return this.entry;
    }

    public final boolean getFromSystem() {
        return this.fromSystem;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public EntryUpdatedEvent(NotificationEntry notificationEntry, boolean z) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.entry = notificationEntry;
        this.fromSystem = z;
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryUpdated(this.entry, this.fromSystem);
    }
}
