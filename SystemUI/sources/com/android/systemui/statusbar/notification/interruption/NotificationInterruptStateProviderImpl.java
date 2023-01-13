package com.android.systemui.statusbar.notification.interruption;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.util.EventLog;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@SysUISingleton
public class NotificationInterruptStateProviderImpl implements NotificationInterruptStateProvider {
    private static final boolean ENABLE_HEADS_UP = true;
    private static final String SETTING_HEADS_UP_TICKER = "ticker_gets_heads_up";
    private static final String TAG = "InterruptionStateProvider";
    private final AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    private final BatteryController mBatteryController;
    /* access modifiers changed from: private */
    public final ContentResolver mContentResolver;
    private final IDreamManager mDreamManager;
    private final NotifPipelineFlags mFlags;
    /* access modifiers changed from: private */
    public final HeadsUpManager mHeadsUpManager;
    private final ContentObserver mHeadsUpObserver;
    private final KeyguardNotificationVisibilityProvider mKeyguardNotificationVisibilityProvider;
    /* access modifiers changed from: private */
    public final NotificationInterruptLogger mLogger;
    private final NotificationFilter mNotificationFilter;
    private final PowerManager mPowerManager;
    private final StatusBarStateController mStatusBarStateController;
    private final List<NotificationInterruptSuppressor> mSuppressors = new ArrayList();
    protected boolean mUseHeadsUp = false;

    @Inject
    public NotificationInterruptStateProviderImpl(ContentResolver contentResolver, PowerManager powerManager, IDreamManager iDreamManager, AmbientDisplayConfiguration ambientDisplayConfiguration, NotificationFilter notificationFilter, BatteryController batteryController, StatusBarStateController statusBarStateController, HeadsUpManager headsUpManager, NotificationInterruptLogger notificationInterruptLogger, @Main Handler handler, NotifPipelineFlags notifPipelineFlags, KeyguardNotificationVisibilityProvider keyguardNotificationVisibilityProvider) {
        this.mContentResolver = contentResolver;
        this.mPowerManager = powerManager;
        this.mDreamManager = iDreamManager;
        this.mBatteryController = batteryController;
        this.mAmbientDisplayConfiguration = ambientDisplayConfiguration;
        this.mNotificationFilter = notificationFilter;
        this.mStatusBarStateController = statusBarStateController;
        this.mHeadsUpManager = headsUpManager;
        this.mLogger = notificationInterruptLogger;
        this.mFlags = notifPipelineFlags;
        this.mKeyguardNotificationVisibilityProvider = keyguardNotificationVisibilityProvider;
        C27301 r3 = new ContentObserver(handler) {
            public void onChange(boolean z) {
                boolean z2 = NotificationInterruptStateProviderImpl.this.mUseHeadsUp;
                NotificationInterruptStateProviderImpl notificationInterruptStateProviderImpl = NotificationInterruptStateProviderImpl.this;
                boolean z3 = false;
                if (Settings.Global.getInt(notificationInterruptStateProviderImpl.mContentResolver, "heads_up_notifications_enabled", 0) != 0) {
                    z3 = true;
                }
                notificationInterruptStateProviderImpl.mUseHeadsUp = z3;
                NotificationInterruptStateProviderImpl.this.mLogger.logHeadsUpFeatureChanged(NotificationInterruptStateProviderImpl.this.mUseHeadsUp);
                if (z2 != NotificationInterruptStateProviderImpl.this.mUseHeadsUp) {
                    if (((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).shouldShowLightweightHeadsup()) {
                        if (NotificationInterruptStateProviderImpl.this.mUseHeadsUp) {
                            ((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).addToWindow();
                        } else {
                            ((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).removeFromWindow();
                        }
                    }
                    if (!NotificationInterruptStateProviderImpl.this.mUseHeadsUp) {
                        NotificationInterruptStateProviderImpl.this.mLogger.logWillDismissAll();
                        NotificationInterruptStateProviderImpl.this.mHeadsUpManager.releaseAllImmediately();
                    }
                }
            }
        };
        this.mHeadsUpObserver = r3;
        contentResolver.registerContentObserver(Settings.Global.getUriFor("heads_up_notifications_enabled"), true, r3);
        contentResolver.registerContentObserver(Settings.Global.getUriFor(SETTING_HEADS_UP_TICKER), true, r3);
        r3.onChange(true);
    }

    public void addSuppressor(NotificationInterruptSuppressor notificationInterruptSuppressor) {
        this.mSuppressors.add(notificationInterruptSuppressor);
    }

    public boolean shouldBubbleUp(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!canAlertCommon(notificationEntry) || !canAlertAwakeCommon(notificationEntry)) {
            return false;
        }
        if (!notificationEntry.canBubble()) {
            this.mLogger.logNoBubbleNotAllowed(sbn);
            return false;
        } else if (notificationEntry.getBubbleMetadata() != null && (notificationEntry.getBubbleMetadata().getShortcutId() != null || notificationEntry.getBubbleMetadata().getIntent() != null)) {
            return true;
        } else {
            this.mLogger.logNoBubbleNoMetadata(sbn);
            return false;
        }
    }

    public boolean shouldHeadsUp(NotificationEntry notificationEntry) {
        if (this.mStatusBarStateController.isDozing()) {
            return shouldHeadsUpWhenDozing(notificationEntry);
        }
        return shouldHeadsUpWhenAwake(notificationEntry);
    }

    public boolean shouldLaunchFullScreenIntentWhenAdded(NotificationEntry notificationEntry) {
        if (notificationEntry.getSbn().getNotification().fullScreenIntent == null) {
            return false;
        }
        if (notificationEntry.shouldSuppressFullScreenIntent()) {
            this.mLogger.logNoFullscreen(notificationEntry, "Suppressed by DND");
            return false;
        } else if (notificationEntry.getImportance() < 4) {
            this.mLogger.logNoFullscreen(notificationEntry, "Not important enough");
            return false;
        } else {
            StatusBarNotification sbn = notificationEntry.getSbn();
            if (sbn.isGroup() && sbn.getNotification().suppressAlertingDueToGrouping()) {
                EventLog.writeEvent(1397638484, new Object[]{"231322873", Integer.valueOf(notificationEntry.getSbn().getUid()), "groupAlertBehavior"});
                this.mLogger.logNoFullscreenWarning(notificationEntry, "GroupAlertBehavior will prevent HUN");
                return false;
            } else if (!this.mPowerManager.isInteractive()) {
                this.mLogger.logFullscreen(notificationEntry, "Device is not interactive");
                return true;
            } else if (isDreaming()) {
                this.mLogger.logFullscreen(notificationEntry, "Device is dreaming");
                return true;
            } else if (this.mStatusBarStateController.getState() == 1) {
                this.mLogger.logFullscreen(notificationEntry, "Keyguard is showing");
                return true;
            } else if (shouldHeadsUp(notificationEntry)) {
                this.mLogger.logNoFullscreen(notificationEntry, "Expected to HUN");
                return false;
            } else {
                this.mLogger.logFullscreen(notificationEntry, "Expected not to HUN");
                return true;
            }
        }
    }

    private boolean isDreaming() {
        try {
            return this.mDreamManager.isDreaming();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to query dream manager.", e);
            return false;
        }
    }

    private boolean shouldHeadsUpWhenAwake(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!this.mUseHeadsUp) {
            this.mLogger.logNoHeadsUpFeatureDisabled();
            return false;
        } else if (!canAlertCommon(notificationEntry) || !canAlertAwakeCommon(notificationEntry)) {
            return false;
        } else {
            if (isSnoozedPackage(sbn)) {
                this.mLogger.logNoHeadsUpPackageSnoozed(sbn);
                return false;
            }
            boolean z = this.mStatusBarStateController.getState() == 0;
            if (notificationEntry.isBubble() && z) {
                this.mLogger.logNoHeadsUpAlreadyBubbled(sbn);
                return false;
            } else if (notificationEntry.shouldSuppressPeek()) {
                this.mLogger.logNoHeadsUpSuppressedByDnd(sbn);
                return false;
            } else if (notificationEntry.getImportance() < 4) {
                this.mLogger.logNoHeadsUpNotImportant(sbn);
                return false;
            } else {
                if (!(this.mPowerManager.isScreenOn() && !isDreaming())) {
                    this.mLogger.logNoHeadsUpNotInUse(sbn);
                    return false;
                }
                for (int i = 0; i < this.mSuppressors.size(); i++) {
                    if (this.mSuppressors.get(i).suppressAwakeHeadsUp(notificationEntry)) {
                        this.mLogger.logNoHeadsUpSuppressedBy(sbn, this.mSuppressors.get(i));
                        return false;
                    }
                }
                this.mLogger.logHeadsUp(sbn);
                return true;
            }
        }
    }

    private boolean shouldHeadsUpWhenDozing(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!this.mAmbientDisplayConfiguration.pulseOnNotificationEnabled(-2)) {
            this.mLogger.logNoPulsingSettingDisabled(sbn);
            return false;
        } else if (this.mBatteryController.isAodPowerSave()) {
            this.mLogger.logNoPulsingBatteryDisabled(sbn);
            return false;
        } else if (!canAlertCommon(notificationEntry)) {
            this.mLogger.logNoPulsingNoAlert(sbn);
            return false;
        } else if (notificationEntry.shouldSuppressAmbient()) {
            this.mLogger.logNoPulsingNoAmbientEffect(sbn);
            return false;
        } else if (notificationEntry.getImportance() < 3) {
            this.mLogger.logNoPulsingNotImportant(sbn);
            return false;
        } else {
            this.mLogger.logPulsing(sbn);
            return true;
        }
    }

    private boolean canAlertCommon(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!this.mFlags.isNewPipelineEnabled() && this.mNotificationFilter.shouldFilterOut(notificationEntry)) {
            this.mLogger.logNoAlertingFilteredOut(sbn);
            return false;
        } else if (!sbn.isGroup() || !sbn.getNotification().suppressAlertingDueToGrouping()) {
            for (int i = 0; i < this.mSuppressors.size(); i++) {
                if (this.mSuppressors.get(i).suppressInterruptions(notificationEntry)) {
                    this.mLogger.logNoAlertingSuppressedBy(sbn, this.mSuppressors.get(i), false);
                    return false;
                }
            }
            if (notificationEntry.hasJustLaunchedFullScreenIntent()) {
                this.mLogger.logNoAlertingRecentFullscreen(sbn);
                return false;
            } else if (!this.mKeyguardNotificationVisibilityProvider.shouldHideNotification(notificationEntry)) {
                return true;
            } else {
                this.mLogger.keyguardHideNotification(notificationEntry.getKey());
                return false;
            }
        } else {
            this.mLogger.logNoAlertingGroupAlertBehavior(sbn);
            return false;
        }
    }

    private boolean canAlertAwakeCommon(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        for (int i = 0; i < this.mSuppressors.size(); i++) {
            if (this.mSuppressors.get(i).suppressAwakeInterruptions(notificationEntry)) {
                this.mLogger.logNoAlertingSuppressedBy(sbn, this.mSuppressors.get(i), true);
                return false;
            }
        }
        return true;
    }

    private boolean isSnoozedPackage(StatusBarNotification statusBarNotification) {
        return this.mHeadsUpManager.isSnoozed(statusBarNotification.getPackageName());
    }
}
