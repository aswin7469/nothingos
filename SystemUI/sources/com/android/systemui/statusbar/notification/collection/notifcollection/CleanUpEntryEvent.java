package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class CleanUpEntryEvent extends NotifEvent {
    @NotNull
    private final NotificationEntry entry;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CleanUpEntryEvent) && Intrinsics.areEqual(this.entry, ((CleanUpEntryEvent) obj).entry);
    }

    public int hashCode() {
        return this.entry.hashCode();
    }

    @NotNull
    public String toString() {
        return "CleanUpEntryEvent(entry=" + this.entry + ')';
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CleanUpEntryEvent(@NotNull NotificationEntry entry) {
        super(null);
        Intrinsics.checkNotNullParameter(entry, "entry");
        this.entry = entry;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(@NotNull NotifCollectionListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        listener.onEntryCleanUp(this.entry);
    }
}
