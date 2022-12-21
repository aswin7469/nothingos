package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProvider;
import javax.inject.Inject;

@CoordinatorScope
public class KeyguardCoordinator implements Coordinator {
    private static final String TAG = "KeyguardCoordinator";
    private final HighPriorityProvider mHighPriorityProvider;
    /* access modifiers changed from: private */
    public final KeyguardNotificationVisibilityProvider mKeyguardNotificationVisibilityProvider;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final SharedCoordinatorLogger mLogger;
    private final NotifFilter mNotifFilter = new NotifFilter(TAG) {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return KeyguardCoordinator.this.mKeyguardNotificationVisibilityProvider.shouldHideNotification(notificationEntry);
        }
    };
    private final SectionHeaderVisibilityProvider mSectionHeaderVisibilityProvider;
    private final StatusBarStateController mStatusBarStateController;

    private void setupInvalidateNotifListCallbacks() {
    }

    @Inject
    public KeyguardCoordinator(StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, HighPriorityProvider highPriorityProvider, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider, KeyguardNotificationVisibilityProvider keyguardNotificationVisibilityProvider, SharedCoordinatorLogger sharedCoordinatorLogger) {
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mSectionHeaderVisibilityProvider = sectionHeaderVisibilityProvider;
        this.mKeyguardNotificationVisibilityProvider = keyguardNotificationVisibilityProvider;
        this.mLogger = sharedCoordinatorLogger;
    }

    public void attach(NotifPipeline notifPipeline) {
        setupInvalidateNotifListCallbacks();
        notifPipeline.addFinalizeFilter(this.mNotifFilter);
        this.mKeyguardNotificationVisibilityProvider.addOnStateChangedListener(new KeyguardCoordinator$$ExternalSyntheticLambda0(this));
        updateSectionHeadersVisibility();
    }

    /* access modifiers changed from: private */
    public void invalidateListFromFilter(String str) {
        this.mLogger.logKeyguardCoordinatorInvalidated(str);
        updateSectionHeadersVisibility();
        this.mNotifFilter.invalidateList();
    }

    private void updateSectionHeadersVisibility() {
        boolean z = false;
        boolean z2 = this.mStatusBarStateController.getState() == 1;
        boolean neverShowSectionHeaders = this.mSectionHeaderVisibilityProvider.getNeverShowSectionHeaders();
        if (!z2 && !neverShowSectionHeaders) {
            z = true;
        }
        this.mSectionHeaderVisibilityProvider.setSectionHeadersVisible(z);
    }
}
