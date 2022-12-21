package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/EntryRemovedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "reason", "", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;I)V", "getEntry", "()Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getReason", "()I", "component1", "component2", "copy", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "equals", "", "other", "", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifEvent.kt */
public final class EntryRemovedEvent extends NotifEvent {
    private final NotificationEntry entry;
    private final int reason;

    public static /* synthetic */ EntryRemovedEvent copy$default(EntryRemovedEvent entryRemovedEvent, NotificationEntry notificationEntry, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            notificationEntry = entryRemovedEvent.entry;
        }
        if ((i2 & 2) != 0) {
            i = entryRemovedEvent.reason;
        }
        return entryRemovedEvent.copy(notificationEntry, i);
    }

    public final NotificationEntry component1() {
        return this.entry;
    }

    public final int component2() {
        return this.reason;
    }

    public final EntryRemovedEvent copy(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return new EntryRemovedEvent(notificationEntry, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntryRemovedEvent)) {
            return false;
        }
        EntryRemovedEvent entryRemovedEvent = (EntryRemovedEvent) obj;
        return Intrinsics.areEqual((Object) this.entry, (Object) entryRemovedEvent.entry) && this.reason == entryRemovedEvent.reason;
    }

    public int hashCode() {
        return (this.entry.hashCode() * 31) + Integer.hashCode(this.reason);
    }

    public String toString() {
        return "EntryRemovedEvent(entry=" + this.entry + ", reason=" + this.reason + ')';
    }

    public final NotificationEntry getEntry() {
        return this.entry;
    }

    public final int getReason() {
        return this.reason;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public EntryRemovedEvent(NotificationEntry notificationEntry, int i) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.entry = notificationEntry;
        this.reason = i;
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryRemoved(this.entry, this.reason);
    }
}
