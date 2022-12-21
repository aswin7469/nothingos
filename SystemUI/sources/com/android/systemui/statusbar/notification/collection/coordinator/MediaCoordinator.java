package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.media.MediaDataManagerKt;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.icon.IconManager;
import javax.inject.Inject;

@CoordinatorScope
public class MediaCoordinator implements Coordinator {
    private static final int STATE_ICONS_ERROR = 2;
    private static final int STATE_ICONS_INFLATED = 1;
    private static final int STATE_ICONS_UNINFLATED = 0;
    private static final String TAG = "MediaCoordinator";
    private final NotifCollectionListener mCollectionListener = new NotifCollectionListener() {
        public void onEntryInit(NotificationEntry notificationEntry) {
            MediaCoordinator.this.mIconsState.put(notificationEntry, 0);
        }

        public void onEntryUpdated(NotificationEntry notificationEntry) {
            if (((Integer) MediaCoordinator.this.mIconsState.getOrDefault(notificationEntry, 0)).intValue() == 2) {
                MediaCoordinator.this.mIconsState.put(notificationEntry, 0);
            }
        }

        public void onEntryCleanUp(NotificationEntry notificationEntry) {
            MediaCoordinator.this.mIconsState.remove(notificationEntry);
        }
    };
    /* access modifiers changed from: private */
    public final IconManager mIconManager;
    /* access modifiers changed from: private */
    public final ArrayMap<NotificationEntry, Integer> mIconsState = new ArrayMap<>();
    /* access modifiers changed from: private */
    public final Boolean mIsMediaFeatureEnabled;
    private final NotifFilter mMediaFilter = new NotifFilter(TAG) {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (!MediaCoordinator.this.mIsMediaFeatureEnabled.booleanValue() || !MediaDataManagerKt.isMediaNotification(notificationEntry.getSbn())) {
                return false;
            }
            int intValue = ((Integer) MediaCoordinator.this.mIconsState.getOrDefault(notificationEntry, 0)).intValue();
            if (intValue == 0) {
                try {
                    MediaCoordinator.this.mIconManager.createIcons(notificationEntry);
                    MediaCoordinator.this.mIconsState.put(notificationEntry, 1);
                } catch (InflationException e) {
                    MediaCoordinator.this.reportInflationError(notificationEntry, e);
                    MediaCoordinator.this.mIconsState.put(notificationEntry, 2);
                }
            } else if (intValue == 1) {
                try {
                    MediaCoordinator.this.mIconManager.updateIcons(notificationEntry);
                } catch (InflationException e2) {
                    MediaCoordinator.this.reportInflationError(notificationEntry, e2);
                    MediaCoordinator.this.mIconsState.put(notificationEntry, 2);
                }
            }
            return true;
        }
    };
    private final IStatusBarService mStatusBarService;

    /* access modifiers changed from: private */
    public void reportInflationError(NotificationEntry notificationEntry, Exception exc) {
        try {
            StatusBarNotification sbn = notificationEntry.getSbn();
            this.mStatusBarService.onNotificationError(sbn.getPackageName(), sbn.getTag(), sbn.getId(), sbn.getUid(), sbn.getInitialPid(), exc.getMessage(), sbn.getUser().getIdentifier());
        } catch (RemoteException unused) {
        }
    }

    @Inject
    public MediaCoordinator(MediaFeatureFlag mediaFeatureFlag, IStatusBarService iStatusBarService, IconManager iconManager) {
        this.mIsMediaFeatureEnabled = Boolean.valueOf(mediaFeatureFlag.getEnabled());
        this.mStatusBarService = iStatusBarService;
        this.mIconManager = iconManager;
    }

    public void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.mMediaFilter);
        notifPipeline.addCollectionListener(this.mCollectionListener);
    }
}
