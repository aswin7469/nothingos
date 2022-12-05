package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationInteractionTracker.kt */
/* loaded from: classes.dex */
public final class NotificationInteractionTracker implements NotifCollectionListener, NotificationInteractionListener {
    @NotNull
    private final NotificationClickNotifier clicker;
    @NotNull
    private final NotificationEntryManager entryManager;
    @NotNull
    private final Map<String, Boolean> interactions = new LinkedHashMap();

    public NotificationInteractionTracker(@NotNull NotificationClickNotifier clicker, @NotNull NotificationEntryManager entryManager) {
        Intrinsics.checkNotNullParameter(clicker, "clicker");
        Intrinsics.checkNotNullParameter(entryManager, "entryManager");
        this.clicker = clicker;
        this.entryManager = entryManager;
        clicker.addNotificationInteractionListener(this);
        entryManager.addCollectionListener(this);
    }

    public final boolean hasUserInteractedWith(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        Boolean bool = this.interactions.get(key);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryAdded(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Map<String, Boolean> map = this.interactions;
        String key = entry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        map.put(key, Boolean.FALSE);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryCleanUp(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        this.interactions.remove(entry.getKey());
    }

    @Override // com.android.systemui.statusbar.NotificationInteractionListener
    public void onNotificationInteraction(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.interactions.put(key, Boolean.TRUE);
    }
}
