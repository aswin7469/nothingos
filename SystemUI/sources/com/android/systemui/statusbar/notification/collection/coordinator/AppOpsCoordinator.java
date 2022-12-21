package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.util.concurrency.DelayableExecutor;
import javax.inject.Inject;

@CoordinatorScope
public class AppOpsCoordinator implements Coordinator {
    private static final String TAG = "AppOpsCoordinator";
    private final AppOpsController mAppOpsController;
    /* access modifiers changed from: private */
    public final ForegroundServiceController mForegroundServiceController;
    private final DelayableExecutor mMainExecutor;
    private final NotifFilter mNotifFilter = new NotifFilter(TAG) {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            StatusBarNotification sbn = notificationEntry.getSbn();
            return AppOpsCoordinator.this.mForegroundServiceController.isDisclosureNotification(sbn) && !AppOpsCoordinator.this.mForegroundServiceController.isDisclosureNeededForUser(sbn.getUser().getIdentifier());
        }
    };
    private NotifPipeline mNotifPipeline;
    private final NotifSectioner mNotifSectioner = new NotifSectioner("ForegroundService", 3) {
        public boolean isInSection(ListEntry listEntry) {
            NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
            if (representativeEntry == null) {
                return false;
            }
            if (isColorizedForegroundService(representativeEntry) || isCall(representativeEntry)) {
                return true;
            }
            return false;
        }

        private boolean isColorizedForegroundService(NotificationEntry notificationEntry) {
            Notification notification = notificationEntry.getSbn().getNotification();
            return notification.isForegroundService() && notification.isColorized() && notificationEntry.getImportance() > 1;
        }

        private boolean isCall(NotificationEntry notificationEntry) {
            Notification notification = notificationEntry.getSbn().getNotification();
            if (notificationEntry.getImportance() <= 1 || !notification.isStyle(Notification.CallStyle.class)) {
                return false;
            }
            return true;
        }
    };

    @Inject
    public AppOpsCoordinator(ForegroundServiceController foregroundServiceController, AppOpsController appOpsController, @Main DelayableExecutor delayableExecutor) {
        this.mForegroundServiceController = foregroundServiceController;
        this.mAppOpsController = appOpsController;
        this.mMainExecutor = delayableExecutor;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        notifPipeline.addPreGroupFilter(this.mNotifFilter);
    }

    public NotifSectioner getSectioner() {
        return this.mNotifSectioner;
    }
}
