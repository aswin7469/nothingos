package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import javax.inject.Inject;

@CentralSurfacesComponent.CentralSurfacesScope
public class StatusBarHeadsUpChangeListener implements OnHeadsUpChangedListener {
    private final DozeScrimController mDozeScrimController;
    private final DozeServiceHost mDozeServiceHost;
    private final HeadsUpManagerPhone mHeadsUpManager;
    private final KeyguardBypassController mKeyguardBypassController;
    private final NotificationPanelViewController mNotificationPanelViewController;
    private final NotificationRemoteInputManager mNotificationRemoteInputManager;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private final NotificationsController mNotificationsController;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarWindowController mStatusBarWindowController;

    @Inject
    StatusBarHeadsUpChangeListener(NotificationShadeWindowController notificationShadeWindowController, StatusBarWindowController statusBarWindowController, NotificationPanelViewController notificationPanelViewController, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, StatusBarStateController statusBarStateController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationsController notificationsController, DozeServiceHost dozeServiceHost, DozeScrimController dozeScrimController) {
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mStatusBarWindowController = statusBarWindowController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mStatusBarStateController = statusBarStateController;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mNotificationsController = notificationsController;
        this.mDozeServiceHost = dozeServiceHost;
        this.mDozeScrimController = dozeScrimController;
    }

    public void onHeadsUpPinnedModeChanged(boolean z) {
        if (z) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(true);
            this.mStatusBarWindowController.setForceStatusBarVisible(true);
            if (this.mNotificationPanelViewController.isFullyCollapsed()) {
                this.mNotificationPanelViewController.getView().requestLayout();
                this.mNotificationShadeWindowController.setForceWindowCollapsed(true);
                this.mNotificationPanelViewController.getView().post(new StatusBarHeadsUpChangeListener$$ExternalSyntheticLambda0(this));
                return;
            }
            return;
        }
        boolean z2 = this.mKeyguardBypassController.getBypassEnabled() && this.mStatusBarStateController.getState() == 1;
        if (!this.mNotificationPanelViewController.isFullyCollapsed() || this.mNotificationPanelViewController.isTracking() || z2) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(false);
            if (z2) {
                this.mStatusBarWindowController.setForceStatusBarVisible(false);
                return;
            }
            return;
        }
        this.mHeadsUpManager.setHeadsUpGoingAway(true);
        this.mNotificationPanelViewController.runAfterAnimationFinished(new StatusBarHeadsUpChangeListener$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onHeadsUpPinnedModeChanged$0$com-android-systemui-statusbar-phone-StatusBarHeadsUpChangeListener */
    public /* synthetic */ void mo45163x590764eb() {
        this.mNotificationShadeWindowController.setForceWindowCollapsed(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onHeadsUpPinnedModeChanged$1$com-android-systemui-statusbar-phone-StatusBarHeadsUpChangeListener */
    public /* synthetic */ void mo45164x4a58f46c() {
        if (!this.mHeadsUpManager.hasPinnedHeadsUp()) {
            this.mNotificationShadeWindowController.setHeadsUpShowing(false);
            this.mHeadsUpManager.setHeadsUpGoingAway(false);
        }
        this.mNotificationRemoteInputManager.onPanelCollapsed();
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        this.mNotificationsController.requestNotificationUpdate("onHeadsUpStateChanged");
        if (this.mStatusBarStateController.isDozing() && z) {
            notificationEntry.setPulseSuppressed(false);
            if (!((AODController) NTDependencyEx.get(AODController.class)).shouldShowAODView()) {
                ((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).setNotificationPanelViewAlpha(1.0f);
            }
            this.mDozeServiceHost.fireNotificationPulse(notificationEntry);
            if (this.mDozeServiceHost.isPulsing()) {
                this.mDozeScrimController.cancelPendingPulseTimeout();
            }
        }
        if (!z && !this.mHeadsUpManager.hasNotifications()) {
            this.mDozeScrimController.pulseOutNow();
        }
    }
}
