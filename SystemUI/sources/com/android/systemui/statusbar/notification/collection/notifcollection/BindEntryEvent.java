package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u001a"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/BindEntryEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "sbn", "Landroid/service/notification/StatusBarNotification;", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;Landroid/service/notification/StatusBarNotification;)V", "getEntry", "()Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getSbn", "()Landroid/service/notification/StatusBarNotification;", "component1", "component2", "copy", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifEvent.kt */
public final class BindEntryEvent extends NotifEvent {
    private final NotificationEntry entry;
    private final StatusBarNotification sbn;

    public static /* synthetic */ BindEntryEvent copy$default(BindEntryEvent bindEntryEvent, NotificationEntry notificationEntry, StatusBarNotification statusBarNotification, int i, Object obj) {
        if ((i & 1) != 0) {
            notificationEntry = bindEntryEvent.entry;
        }
        if ((i & 2) != 0) {
            statusBarNotification = bindEntryEvent.sbn;
        }
        return bindEntryEvent.copy(notificationEntry, statusBarNotification);
    }

    public final NotificationEntry component1() {
        return this.entry;
    }

    public final StatusBarNotification component2() {
        return this.sbn;
    }

    public final BindEntryEvent copy(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        return new BindEntryEvent(notificationEntry, statusBarNotification);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BindEntryEvent)) {
            return false;
        }
        BindEntryEvent bindEntryEvent = (BindEntryEvent) obj;
        return Intrinsics.areEqual((Object) this.entry, (Object) bindEntryEvent.entry) && Intrinsics.areEqual((Object) this.sbn, (Object) bindEntryEvent.sbn);
    }

    public int hashCode() {
        return (this.entry.hashCode() * 31) + this.sbn.hashCode();
    }

    public String toString() {
        return "BindEntryEvent(entry=" + this.entry + ", sbn=" + this.sbn + ')';
    }

    public final NotificationEntry getEntry() {
        return this.entry;
    }

    public final StatusBarNotification getSbn() {
        return this.sbn;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BindEntryEvent(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        this.entry = notificationEntry;
        this.sbn = statusBarNotification;
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryBind(this.entry, this.sbn);
    }
}
