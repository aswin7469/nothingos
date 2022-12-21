package com.android.systemui.statusbar.phone.dagger;

import android.content.ContentResolver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import com.android.keyguard.LockIconView;
import com.android.systemui.C1893R;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.SecureSettings;
import com.nothing.systemui.p024qs.NTQSStatusBar;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Named;

@Module(subcomponents = {StatusBarFragmentComponent.class})
public abstract class StatusBarViewModule {
    public static final String LARGE_SCREEN_BATTERY_CONTROLLER = "split_shade_battery_controller";
    public static final String LARGE_SCREEN_SHADE_HEADER = "large_screen_shade_header";
    private static final String SPLIT_SHADE_BATTERY_VIEW = "split_shade_battery_view";
    public static final String STATUS_BAR_FRAGMENT = "status_bar_fragment";

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static NotificationShadeWindowView providesNotificationShadeWindowView(LayoutInflater layoutInflater) {
        NotificationShadeWindowView notificationShadeWindowView = (NotificationShadeWindowView) layoutInflater.inflate(C1893R.layout.super_notification_shade, (ViewGroup) null);
        if (notificationShadeWindowView != null) {
            return notificationShadeWindowView;
        }
        throw new IllegalStateException("R.layout.super_notification_shade could not be properly inflated");
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static NotificationStackScrollLayout providesNotificationStackScrollLayout(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationStackScrollLayout) notificationShadeWindowView.findViewById(C1893R.C1897id.notification_stack_scroller);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static NotificationShelf providesNotificationShelf(LayoutInflater layoutInflater, NotificationStackScrollLayout notificationStackScrollLayout) {
        NotificationShelf notificationShelf = (NotificationShelf) layoutInflater.inflate(C1893R.layout.status_bar_notification_shelf, notificationStackScrollLayout, false);
        if (notificationShelf != null) {
            return notificationShelf;
        }
        throw new IllegalStateException("R.layout.status_bar_notification_shelf could not be properly inflated");
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static NotificationShelfController providesStatusBarWindowView(NotificationShelfComponent.Builder builder, NotificationShelf notificationShelf) {
        NotificationShelfController notificationShelfController = builder.notificationShelf(notificationShelf).build().getNotificationShelfController();
        notificationShelfController.init();
        return notificationShelfController;
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static NotificationPanelView getNotificationPanelView(NotificationShadeWindowView notificationShadeWindowView) {
        return notificationShadeWindowView.getNotificationPanelView();
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static LockIconView getLockIconView(NotificationShadeWindowView notificationShadeWindowView) {
        return (LockIconView) notificationShadeWindowView.findViewById(C1893R.C1897id.lock_icon_view);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static AuthRippleView getAuthRippleView(NotificationShadeWindowView notificationShadeWindowView) {
        return (AuthRippleView) notificationShadeWindowView.findViewById(C1893R.C1897id.auth_ripple);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    @Named("large_screen_shade_header")
    public static View getLargeScreenShadeHeaderBarView(NotificationShadeWindowView notificationShadeWindowView, FeatureFlags featureFlags) {
        ViewStub viewStub = (ViewStub) notificationShadeWindowView.findViewById(C1893R.C1897id.qs_header_stub);
        viewStub.setLayoutResource(featureFlags.isEnabled(Flags.COMBINED_QS_HEADERS) ? C1893R.layout.combined_qs_header : C1893R.layout.large_screen_shade_header);
        return viewStub.inflate();
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static OngoingPrivacyChip getSplitShadeOngoingPrivacyChip(@Named("large_screen_shade_header") View view) {
        return (OngoingPrivacyChip) view.findViewById(C1893R.C1897id.privacy_chip);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    static StatusIconContainer providesStatusIconContainer(@Named("large_screen_shade_header") View view) {
        return (StatusIconContainer) view.findViewById(C1893R.C1897id.statusIcons);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    @Named("split_shade_battery_view")
    static BatteryMeterView getBatteryMeterView(@Named("large_screen_shade_header") View view) {
        return (BatteryMeterView) view.findViewById(C1893R.C1897id.batteryRemainingIcon);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    @Named("split_shade_battery_controller")
    static BatteryMeterViewController getBatteryMeterViewController(@Named("split_shade_battery_view") BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, @Main Handler handler, ContentResolver contentResolver, BatteryController batteryController) {
        return new BatteryMeterViewController(batteryMeterView, configurationController, tunerService, broadcastDispatcher, handler, contentResolver, batteryController);
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static TapAgainView getTapAgainView(NotificationPanelView notificationPanelView) {
        return notificationPanelView.getTapAgainView();
    }

    @CentralSurfacesComponent.CentralSurfacesScope
    @Provides
    public static NotificationsQuickSettingsContainer getNotificationsQuickSettingsContainer(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationsQuickSettingsContainer) notificationShadeWindowView.findViewById(C1893R.C1897id.notification_container_parent);
    }

    @Provides
    @Named("status_bar_fragment")
    public static CollapsedStatusBarFragment createCollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CarrierConfigTracker carrierConfigTracker, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2, SecureSettings secureSettings, @Main Executor executor) {
        return new CollapsedStatusBarFragment(factory, ongoingCallController, systemStatusAnimationScheduler, statusBarLocationPublisher, notificationIconAreaController, panelExpansionStateManager, featureFlags, statusBarIconController, statusBarHideIconsForBouncerManager, keyguardStateController, notificationPanelViewController, networkController, statusBarStateController, commandQueue, carrierConfigTracker, collapsedStatusBarFragmentLogger, factory2, secureSettings, executor);
    }

    @Provides
    static NTQSStatusBar providesNTQSStatusBar(NotificationPanelView notificationPanelView) {
        return (NTQSStatusBar) notificationPanelView.findViewById(C1893R.C1897id.qs_status_bar_layout);
    }

    @Provides
    @Named("nt_qs_header_battery_meter_view")
    static BatteryMeterView createBatteryMeterView(NotificationPanelView notificationPanelView) {
        return (BatteryMeterView) notificationPanelView.findViewById(C1893R.C1897id.qs_batteryRemainingIcon);
    }

    @Provides
    @Named("nt_qs_header_battery_controller")
    static BatteryMeterViewController createBatteryMeterViewController(@Named("nt_qs_header_battery_meter_view") BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, @Main Handler handler, ContentResolver contentResolver, BatteryController batteryController) {
        return new BatteryMeterViewController(batteryMeterView, configurationController, tunerService, broadcastDispatcher, handler, contentResolver, batteryController);
    }
}
