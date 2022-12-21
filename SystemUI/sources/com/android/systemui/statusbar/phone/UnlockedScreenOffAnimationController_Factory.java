package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UnlockedScreenOffAnimationController_Factory implements Factory<UnlockedScreenOffAnimationController> {
    private final Provider<Context> contextProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<GlobalSettings> globalSettingsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorLazyProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public UnlockedScreenOffAnimationController_Factory(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateControllerImpl> provider3, Provider<KeyguardViewMediator> provider4, Provider<KeyguardStateController> provider5, Provider<DozeParameters> provider6, Provider<GlobalSettings> provider7, Provider<InteractionJankMonitor> provider8, Provider<PowerManager> provider9, Provider<Handler> provider10) {
        this.contextProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
        this.statusBarStateControllerImplProvider = provider3;
        this.keyguardViewMediatorLazyProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.dozeParametersProvider = provider6;
        this.globalSettingsProvider = provider7;
        this.interactionJankMonitorProvider = provider8;
        this.powerManagerProvider = provider9;
        this.handlerProvider = provider10;
    }

    public UnlockedScreenOffAnimationController get() {
        return newInstance(this.contextProvider.get(), this.wakefulnessLifecycleProvider.get(), this.statusBarStateControllerImplProvider.get(), DoubleCheck.lazy(this.keyguardViewMediatorLazyProvider), this.keyguardStateControllerProvider.get(), DoubleCheck.lazy(this.dozeParametersProvider), this.globalSettingsProvider.get(), this.interactionJankMonitorProvider.get(), this.powerManagerProvider.get(), this.handlerProvider.get());
    }

    public static UnlockedScreenOffAnimationController_Factory create(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateControllerImpl> provider3, Provider<KeyguardViewMediator> provider4, Provider<KeyguardStateController> provider5, Provider<DozeParameters> provider6, Provider<GlobalSettings> provider7, Provider<InteractionJankMonitor> provider8, Provider<PowerManager> provider9, Provider<Handler> provider10) {
        return new UnlockedScreenOffAnimationController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static UnlockedScreenOffAnimationController newInstance(Context context, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateControllerImpl statusBarStateControllerImpl, Lazy<KeyguardViewMediator> lazy, KeyguardStateController keyguardStateController, Lazy<DozeParameters> lazy2, GlobalSettings globalSettings, InteractionJankMonitor interactionJankMonitor, PowerManager powerManager, Handler handler) {
        return new UnlockedScreenOffAnimationController(context, wakefulnessLifecycle, statusBarStateControllerImpl, lazy, keyguardStateController, lazy2, globalSettings, interactionJankMonitor, powerManager, handler);
    }
}
