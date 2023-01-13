package com.android.systemui.statusbar.notification.collection.provider;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/provider/NotificationVisibilityProviderImpl;", "Lcom/android/systemui/statusbar/notification/collection/render/NotificationVisibilityProvider;", "notifDataStore", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStore;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "(Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStore;Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;)V", "getCount", "", "getLocation", "Lcom/android/internal/statusbar/NotificationVisibility$NotificationLocation;", "key", "", "obtain", "Lcom/android/internal/statusbar/NotificationVisibility;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "visible", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationVisibilityProviderImpl.kt */
public final class NotificationVisibilityProviderImpl implements NotificationVisibilityProvider {
    private final CommonNotifCollection notifCollection;
    private final NotifLiveDataStore notifDataStore;

    @Inject
    public NotificationVisibilityProviderImpl(NotifLiveDataStore notifLiveDataStore, CommonNotifCollection commonNotifCollection) {
        Intrinsics.checkNotNullParameter(notifLiveDataStore, "notifDataStore");
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        this.notifDataStore = notifLiveDataStore;
        this.notifCollection = commonNotifCollection;
    }

    public NotificationVisibility obtain(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        int count = getCount();
        int rank = notificationEntry.getRanking().getRank();
        boolean z2 = true;
        boolean z3 = notificationEntry.getRow() != null;
        NotificationVisibility.NotificationLocation notificationLocation = NotificationLogger.getNotificationLocation(notificationEntry);
        String key = notificationEntry.getKey();
        if (!z || !z3) {
            z2 = false;
        }
        NotificationVisibility obtain = NotificationVisibility.obtain(key, rank, count, z2, notificationLocation);
        Intrinsics.checkNotNullExpressionValue(obtain, "obtain(entry.key, rank, …ible && hasRow, location)");
        return obtain;
    }

    public NotificationVisibility obtain(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        NotificationEntry entry = this.notifCollection.getEntry(str);
        if (entry != null) {
            return obtain(entry, z);
        }
        NotificationVisibility obtain = NotificationVisibility.obtain(str, -1, getCount(), false);
        Intrinsics.checkNotNullExpressionValue(obtain, "obtain(key, -1, getCount(), false)");
        return obtain;
    }

    public NotificationVisibility.NotificationLocation getLocation(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        NotificationVisibility.NotificationLocation notificationLocation = NotificationLogger.getNotificationLocation(this.notifCollection.getEntry(str));
        Intrinsics.checkNotNullExpressionValue(notificationLocation, "getNotificationLocation(…Collection.getEntry(key))");
        return notificationLocation;
    }

    private final int getCount() {
        return this.notifDataStore.getActiveNotifCount().getValue().intValue();
    }
}
