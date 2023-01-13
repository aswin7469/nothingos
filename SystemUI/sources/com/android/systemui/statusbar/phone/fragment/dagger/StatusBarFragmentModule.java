package com.android.systemui.statusbar.phone.fragment.dagger;

import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.dagger.qualifiers.RootView;
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Named;

@Module
public interface StatusBarFragmentModule {
    public static final String LIGHTS_OUT_NOTIF_VIEW = "lights_out_notif_view";
    public static final String OPERATOR_NAME_FRAME_VIEW = "operator_name_frame_view";
    public static final String OPERATOR_NAME_VIEW = "operator_name_view";

    @Binds
    @StatusBarFragmentScope
    StatusBarUserSwitcherController bindStatusBarUserSwitcherController(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl);

    @RootView
    @StatusBarFragmentScope
    @Provides
    static PhoneStatusBarView providePhoneStatusBarView(CollapsedStatusBarFragment collapsedStatusBarFragment) {
        return (PhoneStatusBarView) collapsedStatusBarFragment.getView();
    }

    @StatusBarFragmentScope
    @Provides
    static BatteryMeterView provideBatteryMeterView(@RootView PhoneStatusBarView phoneStatusBarView) {
        return (BatteryMeterView) phoneStatusBarView.findViewById(C1894R.C1898id.battery);
    }

    @StatusBarFragmentScope
    @Provides
    @Named("lights_out_notif_view")
    static View provideLightsOutNotifView(@RootView PhoneStatusBarView phoneStatusBarView) {
        return phoneStatusBarView.findViewById(C1894R.C1898id.notification_lights_out);
    }

    @StatusBarFragmentScope
    @Provides
    @Named("operator_name_view")
    static View provideOperatorNameView(@RootView PhoneStatusBarView phoneStatusBarView) {
        return phoneStatusBarView.findViewById(C1894R.C1898id.operator_name);
    }

    @Named("operator_name_frame_view")
    @StatusBarFragmentScope
    @Provides
    static Optional<View> provideOperatorFrameNameView(@RootView PhoneStatusBarView phoneStatusBarView) {
        return Optional.ofNullable(phoneStatusBarView.findViewById(C1894R.C1898id.operator_name_frame));
    }

    @StatusBarFragmentScope
    @Provides
    static Clock provideClock(@RootView PhoneStatusBarView phoneStatusBarView) {
        return (Clock) phoneStatusBarView.findViewById(C1894R.C1898id.clock);
    }

    @StatusBarFragmentScope
    @Provides
    static StatusBarUserSwitcherContainer provideStatusBarUserSwitcherContainer(@RootView PhoneStatusBarView phoneStatusBarView) {
        return (StatusBarUserSwitcherContainer) phoneStatusBarView.findViewById(C1894R.C1898id.user_switcher_container);
    }

    @StatusBarFragmentScope
    @Provides
    static PhoneStatusBarViewController providePhoneStatusBarViewController(PhoneStatusBarViewController.Factory factory, @RootView PhoneStatusBarView phoneStatusBarView, NotificationPanelViewController notificationPanelViewController) {
        return factory.create(phoneStatusBarView, notificationPanelViewController.getStatusBarTouchEventHandler());
    }

    @StatusBarFragmentScope
    @Provides
    static PhoneStatusBarTransitions providePhoneStatusBarTransitions(@RootView PhoneStatusBarView phoneStatusBarView, StatusBarWindowController statusBarWindowController) {
        return new PhoneStatusBarTransitions(phoneStatusBarView, statusBarWindowController.getBackgroundView());
    }

    @StatusBarFragmentScope
    @Provides
    static HeadsUpStatusBarView providesHeasdUpStatusBarView(@RootView PhoneStatusBarView phoneStatusBarView) {
        return (HeadsUpStatusBarView) phoneStatusBarView.findViewById(C1894R.C1898id.heads_up_status_bar_view);
    }
}
