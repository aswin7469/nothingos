package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingMessage;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.Collection;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B'\b\u0001\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0010H\u0016J\b\u0010\u0015\u001a\u00020\u0013H\u0016J\b\u0010\u0016\u001a\u00020\u0013H\u0016J\b\u0010\u0017\u001a\u00020\u0013H\u0016J\b\u0010\u0018\u001a\u00020\u0013H\u0016J\u0010\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0013H\u0002J\b\u0010\u001d\u001a\u00020\u0013H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/ViewConfigCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager$UserChangedListener;", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "mConfigurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "mLockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "mGutsManager", "Lcom/android/systemui/statusbar/notification/row/NotificationGutsManager;", "mKeyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "(Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/statusbar/notification/row/NotificationGutsManager;Lcom/android/keyguard/KeyguardUpdateMonitor;)V", "mDispatchUiModeChangeOnUserSwitched", "", "mPipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "mReinflateNotificationsOnUserSwitched", "attach", "", "pipeline", "onDensityOrFontScaleChanged", "onThemeChanged", "onUiModeChanged", "onUiModeChangedDelayCheck", "onUserChanged", "userId", "", "updateNotificationsOnDensityOrFontScaleChanged", "updateNotificationsOnUiModeChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: ViewConfigCoordinator.kt */
public final class ViewConfigCoordinator implements Coordinator, NotificationLockscreenUserManager.UserChangedListener, ConfigurationController.ConfigurationListener {
    private final ConfigurationController mConfigurationController;
    private boolean mDispatchUiModeChangeOnUserSwitched;
    private final NotificationGutsManager mGutsManager;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    private NotifPipeline mPipeline;
    private boolean mReinflateNotificationsOnUserSwitched;

    @Inject
    public ViewConfigCoordinator(ConfigurationController configurationController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGutsManager notificationGutsManager, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        Intrinsics.checkNotNullParameter(configurationController, "mConfigurationController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "mLockscreenUserManager");
        Intrinsics.checkNotNullParameter(notificationGutsManager, "mGutsManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "mKeyguardUpdateMonitor");
        this.mConfigurationController = configurationController;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mGutsManager = notificationGutsManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        this.mPipeline = notifPipeline;
        if (notifPipeline.isNewPipelineEnabled()) {
            this.mLockscreenUserManager.addUserChangedListener(this);
            this.mConfigurationController.addCallback(this);
        }
    }

    public void onDensityOrFontScaleChanged() {
        MessagingMessage.dropCache();
        MessagingGroup.dropCache();
        if (!this.mKeyguardUpdateMonitor.isSwitchingUser()) {
            updateNotificationsOnDensityOrFontScaleChanged();
        } else {
            this.mReinflateNotificationsOnUserSwitched = true;
        }
    }

    public void onUiModeChanged() {
        if (!this.mKeyguardUpdateMonitor.isSwitchingUser()) {
            updateNotificationsOnUiModeChanged();
        } else {
            this.mDispatchUiModeChangeOnUserSwitched = true;
        }
    }

    public void onThemeChanged() {
        onDensityOrFontScaleChanged();
    }

    public void onUserChanged(int i) {
        if (this.mReinflateNotificationsOnUserSwitched) {
            updateNotificationsOnDensityOrFontScaleChanged();
            this.mReinflateNotificationsOnUserSwitched = false;
        }
        if (this.mDispatchUiModeChangeOnUserSwitched) {
            updateNotificationsOnUiModeChanged();
            this.mDispatchUiModeChangeOnUserSwitched = false;
        }
    }

    private final void updateNotificationsOnUiModeChanged() {
        Collection<NotificationEntry> allNotifs;
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null && (allNotifs = notifPipeline.getAllNotifs()) != null) {
            for (NotificationEntry row : allNotifs) {
                ExpandableNotificationRow row2 = row.getRow();
                if (row2 != null) {
                    row2.onUiModeChanged();
                }
            }
        }
    }

    private final void updateNotificationsOnDensityOrFontScaleChanged() {
        Collection<NotificationEntry> allNotifs;
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null && (allNotifs = notifPipeline.getAllNotifs()) != null) {
            for (NotificationEntry notificationEntry : allNotifs) {
                notificationEntry.onDensityOrFontScaleChanged();
                if (notificationEntry.areGutsExposed()) {
                    this.mGutsManager.onDensityOrFontScaleChanged(notificationEntry);
                }
            }
        }
    }

    public void onUiModeChangedDelayCheck() {
        Collection<NotificationEntry> allNotifs;
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null && (allNotifs = notifPipeline.getAllNotifs()) != null) {
            for (NotificationEntry row : allNotifs) {
                ExpandableNotificationRow row2 = row.getRow();
                if (row2 != null) {
                    row2.onUiModeChangedDelayCheck();
                }
            }
        }
    }
}
