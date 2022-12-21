package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.dagger.qualifiers.RootView;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.StatusBarDemoMode;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = {StatusBarFragmentModule.class})
@StatusBarFragmentScope
public interface StatusBarFragmentComponent {

    @Subcomponent.Factory
    public interface Factory {
        StatusBarFragmentComponent create(@BindsInstance CollapsedStatusBarFragment collapsedStatusBarFragment);
    }

    @StatusBarFragmentScope
    BatteryMeterViewController getBatteryMeterViewController();

    @StatusBarFragmentScope
    HeadsUpAppearanceController getHeadsUpAppearanceController();

    @StatusBarFragmentScope
    LightsOutNotifController getLightsOutNotifController();

    @StatusBarFragmentScope
    PhoneStatusBarTransitions getPhoneStatusBarTransitions();

    @RootView
    @StatusBarFragmentScope
    PhoneStatusBarView getPhoneStatusBarView();

    @StatusBarFragmentScope
    PhoneStatusBarViewController getPhoneStatusBarViewController();

    @StatusBarFragmentScope
    StatusBarDemoMode getStatusBarDemoMode();

    void init() {
        getBatteryMeterViewController().init();
        getHeadsUpAppearanceController().init();
        getPhoneStatusBarViewController().init();
        getLightsOutNotifController().init();
        getStatusBarDemoMode().init();
    }
}
