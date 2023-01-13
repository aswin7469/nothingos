package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.LightBarTransitionsController_Factory  reason: case insensitive filesystem */
public final class C4840LightBarTransitionsController_Factory {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public C4840LightBarTransitionsController_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<KeyguardStateController> provider3, Provider<StatusBarStateController> provider4) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.keyguardStateControllerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
    }

    public LightBarTransitionsController get(LightBarTransitionsController.DarkIntensityApplier darkIntensityApplier) {
        return newInstance(this.contextProvider.get(), darkIntensityApplier, this.commandQueueProvider.get(), this.keyguardStateControllerProvider.get(), this.statusBarStateControllerProvider.get());
    }

    public static C4840LightBarTransitionsController_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<KeyguardStateController> provider3, Provider<StatusBarStateController> provider4) {
        return new C4840LightBarTransitionsController_Factory(provider, provider2, provider3, provider4);
    }

    public static LightBarTransitionsController newInstance(Context context, LightBarTransitionsController.DarkIntensityApplier darkIntensityApplier, CommandQueue commandQueue, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController) {
        return new LightBarTransitionsController(context, darkIntensityApplier, commandQueue, keyguardStateController, statusBarStateController);
    }
}
