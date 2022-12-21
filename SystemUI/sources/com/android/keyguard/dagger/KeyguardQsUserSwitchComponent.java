package com.android.keyguard.dagger;

import android.widget.FrameLayout;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = {KeyguardUserSwitcherModule.class})
@KeyguardUserSwitcherScope
public interface KeyguardQsUserSwitchComponent {

    @Subcomponent.Factory
    public interface Factory {
        KeyguardQsUserSwitchComponent build(@BindsInstance FrameLayout frameLayout);
    }

    KeyguardQsUserSwitchController getKeyguardQsUserSwitchController();
}
