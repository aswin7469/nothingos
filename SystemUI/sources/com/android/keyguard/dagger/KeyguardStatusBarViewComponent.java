package com.android.keyguard.dagger;

import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = {KeyguardStatusBarViewModule.class})
@KeyguardStatusBarViewScope
public interface KeyguardStatusBarViewComponent {

    @Subcomponent.Factory
    public interface Factory {
        KeyguardStatusBarViewComponent build(@BindsInstance KeyguardStatusBarView keyguardStatusBarView, @BindsInstance NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider);
    }

    KeyguardStatusBarViewController getKeyguardStatusBarViewController();
}
