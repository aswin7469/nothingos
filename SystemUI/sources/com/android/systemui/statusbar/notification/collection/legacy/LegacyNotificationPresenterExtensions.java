package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import javax.inject.Inject;

public class LegacyNotificationPresenterExtensions implements NotifShadeEventSource {
    private static final String TAG = "LegacyNotifPresenter";
    private boolean mEntryListenerAdded;
    /* access modifiers changed from: private */
    public final NotificationEntryManager mEntryManager;
    /* access modifiers changed from: private */
    public Runnable mNotifRemovedByUserCallback;
    /* access modifiers changed from: private */
    public Runnable mShadeEmptiedCallback;

    @Inject
    public LegacyNotificationPresenterExtensions(NotificationEntryManager notificationEntryManager) {
        this.mEntryManager = notificationEntryManager;
    }

    private void ensureEntryListenerAdded() {
        if (!this.mEntryListenerAdded) {
            this.mEntryListenerAdded = true;
            this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
                public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                    if (!(notificationEntry.getSbn() == null || LegacyNotificationPresenterExtensions.this.mEntryManager.hasActiveNotifications() || LegacyNotificationPresenterExtensions.this.mShadeEmptiedCallback == null)) {
                        LegacyNotificationPresenterExtensions.this.mShadeEmptiedCallback.run();
                    }
                    if (z && LegacyNotificationPresenterExtensions.this.mNotifRemovedByUserCallback != null) {
                        LegacyNotificationPresenterExtensions.this.mNotifRemovedByUserCallback.run();
                    }
                }
            });
        }
    }

    public void setNotifRemovedByUserCallback(Runnable runnable) {
        if (this.mNotifRemovedByUserCallback == null) {
            this.mNotifRemovedByUserCallback = runnable;
            ensureEntryListenerAdded();
            return;
        }
        throw new IllegalStateException("mNotifRemovedByUserCallback already set");
    }

    public void setShadeEmptiedCallback(Runnable runnable) {
        if (this.mShadeEmptiedCallback == null) {
            this.mShadeEmptiedCallback = runnable;
            ensureEntryListenerAdded();
            return;
        }
        throw new IllegalStateException("mShadeEmptiedCallback already set");
    }
}
