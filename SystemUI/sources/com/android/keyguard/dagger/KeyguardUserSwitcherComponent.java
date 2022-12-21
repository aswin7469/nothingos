package com.android.keyguard.dagger;

import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = {KeyguardUserSwitcherModule.class})
@KeyguardUserSwitcherScope
public interface KeyguardUserSwitcherComponent {

    @Subcomponent.Factory
    public interface Factory {
        KeyguardUserSwitcherComponent build(@BindsInstance KeyguardUserSwitcherView keyguardUserSwitcherView);
    }

    KeyguardUserSwitcherController getKeyguardUserSwitcherController();
}
