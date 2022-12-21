package com.android.systemui.statusbar;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0017\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u000e\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\nJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/NotificationInteractionTracker;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "Lcom/android/systemui/statusbar/NotificationInteractionListener;", "clicker", "Lcom/android/systemui/statusbar/NotificationClickNotifier;", "entryManager", "Lcom/android/systemui/statusbar/notification/NotificationEntryManager;", "(Lcom/android/systemui/statusbar/NotificationClickNotifier;Lcom/android/systemui/statusbar/notification/NotificationEntryManager;)V", "interactions", "", "", "", "hasUserInteractedWith", "key", "onEntryAdded", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onEntryCleanUp", "onNotificationInteraction", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationInteractionTracker.kt */
public final class NotificationInteractionTracker implements NotifCollectionListener, NotificationInteractionListener {
    private final NotificationClickNotifier clicker;
    private final NotificationEntryManager entryManager;
    private final Map<String, Boolean> interactions = new LinkedHashMap();

    @Inject
    public NotificationInteractionTracker(NotificationClickNotifier notificationClickNotifier, NotificationEntryManager notificationEntryManager) {
        Intrinsics.checkNotNullParameter(notificationClickNotifier, "clicker");
        Intrinsics.checkNotNullParameter(notificationEntryManager, "entryManager");
        this.clicker = notificationClickNotifier;
        this.entryManager = notificationEntryManager;
        notificationClickNotifier.addNotificationInteractionListener(this);
        notificationEntryManager.addCollectionListener(this);
    }

    public final boolean hasUserInteractedWith(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        Boolean bool = this.interactions.get(str);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Map<String, Boolean> map = this.interactions;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        map.put(key, false);
    }

    public void onEntryCleanUp(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.interactions.remove(notificationEntry.getKey());
    }

    public void onNotificationInteraction(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.interactions.put(str, true);
    }
}
