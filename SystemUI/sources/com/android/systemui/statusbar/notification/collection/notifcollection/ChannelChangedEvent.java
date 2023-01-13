package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.app.NotificationChannel;
import android.os.UserHandle;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0016\u001a\u00020\tHÆ\u0003J1\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fHÖ\u0003J\t\u0010 \u001a\u00020\tHÖ\u0001J\t\u0010!\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/ChannelChangedEvent;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifEvent;", "pkgName", "", "user", "Landroid/os/UserHandle;", "channel", "Landroid/app/NotificationChannel;", "modificationType", "", "(Ljava/lang/String;Landroid/os/UserHandle;Landroid/app/NotificationChannel;I)V", "getChannel", "()Landroid/app/NotificationChannel;", "getModificationType", "()I", "getPkgName", "()Ljava/lang/String;", "getUser", "()Landroid/os/UserHandle;", "component1", "component2", "component3", "component4", "copy", "dispatchToListener", "", "listener", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "equals", "", "other", "", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifEvent.kt */
public final class ChannelChangedEvent extends NotifEvent {
    private final NotificationChannel channel;
    private final int modificationType;
    private final String pkgName;
    private final UserHandle user;

    public static /* synthetic */ ChannelChangedEvent copy$default(ChannelChangedEvent channelChangedEvent, String str, UserHandle userHandle, NotificationChannel notificationChannel, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = channelChangedEvent.pkgName;
        }
        if ((i2 & 2) != 0) {
            userHandle = channelChangedEvent.user;
        }
        if ((i2 & 4) != 0) {
            notificationChannel = channelChangedEvent.channel;
        }
        if ((i2 & 8) != 0) {
            i = channelChangedEvent.modificationType;
        }
        return channelChangedEvent.copy(str, userHandle, notificationChannel, i);
    }

    public final String component1() {
        return this.pkgName;
    }

    public final UserHandle component2() {
        return this.user;
    }

    public final NotificationChannel component3() {
        return this.channel;
    }

    public final int component4() {
        return this.modificationType;
    }

    public final ChannelChangedEvent copy(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        Intrinsics.checkNotNullParameter(str, "pkgName");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        Intrinsics.checkNotNullParameter(notificationChannel, "channel");
        return new ChannelChangedEvent(str, userHandle, notificationChannel, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChannelChangedEvent)) {
            return false;
        }
        ChannelChangedEvent channelChangedEvent = (ChannelChangedEvent) obj;
        return Intrinsics.areEqual((Object) this.pkgName, (Object) channelChangedEvent.pkgName) && Intrinsics.areEqual((Object) this.user, (Object) channelChangedEvent.user) && Intrinsics.areEqual((Object) this.channel, (Object) channelChangedEvent.channel) && this.modificationType == channelChangedEvent.modificationType;
    }

    public int hashCode() {
        return (((((this.pkgName.hashCode() * 31) + this.user.hashCode()) * 31) + this.channel.hashCode()) * 31) + Integer.hashCode(this.modificationType);
    }

    public String toString() {
        return "ChannelChangedEvent(pkgName=" + this.pkgName + ", user=" + this.user + ", channel=" + this.channel + ", modificationType=" + this.modificationType + ')';
    }

    public final String getPkgName() {
        return this.pkgName;
    }

    public final UserHandle getUser() {
        return this.user;
    }

    public final NotificationChannel getChannel() {
        return this.channel;
    }

    public final int getModificationType() {
        return this.modificationType;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ChannelChangedEvent(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(str, "pkgName");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        Intrinsics.checkNotNullParameter(notificationChannel, "channel");
        this.pkgName = str;
        this.user = userHandle;
        this.channel = notificationChannel;
        this.modificationType = i;
    }

    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onNotificationChannelModified(this.pkgName, this.user, this.channel, this.modificationType);
    }
}
