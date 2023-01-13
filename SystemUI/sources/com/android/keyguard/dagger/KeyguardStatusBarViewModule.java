package com.android.keyguard.dagger;

import com.android.keyguard.CarrierText;
import com.android.systemui.C1894R;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class KeyguardStatusBarViewModule {
    /* access modifiers changed from: package-private */
    @Binds
    @KeyguardStatusBarViewScope
    public abstract StatusBarUserSwitcherController bindStatusBarUserSwitcherController(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl);

    @KeyguardStatusBarViewScope
    @Provides
    static CarrierText getCarrierText(KeyguardStatusBarView keyguardStatusBarView) {
        return (CarrierText) keyguardStatusBarView.findViewById(C1894R.C1898id.keyguard_carrier_text);
    }

    @KeyguardStatusBarViewScope
    @Provides
    static BatteryMeterView getBatteryMeterView(KeyguardStatusBarView keyguardStatusBarView) {
        return (BatteryMeterView) keyguardStatusBarView.findViewById(C1894R.C1898id.battery);
    }

    @KeyguardStatusBarViewScope
    @Provides
    static StatusBarUserSwitcherContainer getUserSwitcherContainer(KeyguardStatusBarView keyguardStatusBarView) {
        return (StatusBarUserSwitcherContainer) keyguardStatusBarView.findViewById(C1894R.C1898id.user_switcher_container);
    }
}
