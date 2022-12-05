package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class BindEntryEvent extends NotifEvent {
    @NotNull
    private final NotificationEntry entry;
    @NotNull
    private final StatusBarNotification sbn;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BindEntryEvent)) {
            return false;
        }
        BindEntryEvent bindEntryEvent = (BindEntryEvent) obj;
        return Intrinsics.areEqual(this.entry, bindEntryEvent.entry) && Intrinsics.areEqual(this.sbn, bindEntryEvent.sbn);
    }

    public int hashCode() {
        return (this.entry.hashCode() * 31) + this.sbn.hashCode();
    }

    @NotNull
    public String toString() {
        return "BindEntryEvent(entry=" + this.entry + ", sbn=" + this.sbn + ')';
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BindEntryEvent(@NotNull NotificationEntry entry, @NotNull StatusBarNotification sbn) {
        super(null);
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        this.entry = entry;
        this.sbn = sbn;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(@NotNull NotifCollectionListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        listener.onEntryBind(this.entry, this.sbn);
    }
}
