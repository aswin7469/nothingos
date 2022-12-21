package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.SplitShadeLockScreenOverScroller_Factory  reason: case insensitive filesystem */
public final class C4825SplitShadeLockScreenOverScroller_Factory {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public C4825SplitShadeLockScreenOverScroller_Factory(Provider<ConfigurationController> provider, Provider<Context> provider2, Provider<ScrimController> provider3, Provider<SysuiStatusBarStateController> provider4) {
        this.configurationControllerProvider = provider;
        this.contextProvider = provider2;
        this.scrimControllerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
    }

    public SplitShadeLockScreenOverScroller get(C2301QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return newInstance(this.configurationControllerProvider.get(), this.contextProvider.get(), this.scrimControllerProvider.get(), this.statusBarStateControllerProvider.get(), qs, notificationStackScrollLayoutController);
    }

    public static C4825SplitShadeLockScreenOverScroller_Factory create(Provider<ConfigurationController> provider, Provider<Context> provider2, Provider<ScrimController> provider3, Provider<SysuiStatusBarStateController> provider4) {
        return new C4825SplitShadeLockScreenOverScroller_Factory(provider, provider2, provider3, provider4);
    }

    public static SplitShadeLockScreenOverScroller newInstance(ConfigurationController configurationController, Context context, ScrimController scrimController, SysuiStatusBarStateController sysuiStatusBarStateController, C2301QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return new SplitShadeLockScreenOverScroller(configurationController, context, scrimController, sysuiStatusBarStateController, qs, notificationStackScrollLayoutController);
    }
}
