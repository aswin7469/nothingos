package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = {KeyguardStatusViewModule.class})
@KeyguardStatusViewScope
public interface KeyguardStatusViewComponent {

    @Subcomponent.Factory
    public interface Factory {
        KeyguardStatusViewComponent build(@BindsInstance KeyguardStatusView keyguardStatusView);
    }

    KeyguardClockSwitchController getKeyguardClockSwitchController();

    KeyguardStatusViewController getKeyguardStatusViewController();
}
