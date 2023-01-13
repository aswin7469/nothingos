package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006J\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u000fR\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifViewBarn;", "", "()V", "rowMap", "", "", "Lcom/android/systemui/statusbar/notification/collection/render/NotifViewController;", "registerViewForEntry", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "controller", "removeViewForEntry", "requireGroupController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGroupController;", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "requireNodeController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "requireRowController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifRowController;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifViewBarn.kt */
public final class NotifViewBarn {
    private final Map<String, NotifViewController> rowMap = new LinkedHashMap();

    public final NodeController requireNodeController(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        NotifViewController notifViewController = this.rowMap.get(listEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(("No view has been registered for entry: " + listEntry.getKey()).toString());
    }

    public final NotifGroupController requireGroupController(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        NotifViewController notifViewController = this.rowMap.get(notificationEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(("No view has been registered for entry: " + notificationEntry.getKey()).toString());
    }

    public final NotifRowController requireRowController(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        NotifViewController notifViewController = this.rowMap.get(notificationEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(("No view has been registered for entry: " + notificationEntry.getKey()).toString());
    }

    public final void registerViewForEntry(ListEntry listEntry, NotifViewController notifViewController) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        Intrinsics.checkNotNullParameter(notifViewController, "controller");
        Map<String, NotifViewController> map = this.rowMap;
        String key = listEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        map.put(key, notifViewController);
    }

    public final void removeViewForEntry(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        this.rowMap.remove(listEntry.getKey());
    }
}
