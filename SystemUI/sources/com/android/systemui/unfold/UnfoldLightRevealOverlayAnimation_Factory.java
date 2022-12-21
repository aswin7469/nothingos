package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.view.IWindowManager;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelper;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class UnfoldLightRevealOverlayAnimation_Factory implements Factory<UnfoldLightRevealOverlayAnimation> {
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceStateManager> deviceStateManagerProvider;
    private final Provider<Optional<DisplayAreaHelper>> displayAreaHelperProvider;
    private final Provider<DisplayManager> displayManagerProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<UnfoldTransitionProgressProvider> unfoldTransitionProgressProvider;
    private final Provider<IWindowManager> windowManagerInterfaceProvider;

    public UnfoldLightRevealOverlayAnimation_Factory(Provider<Context> provider, Provider<DeviceStateManager> provider2, Provider<DisplayManager> provider3, Provider<UnfoldTransitionProgressProvider> provider4, Provider<Optional<DisplayAreaHelper>> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<IWindowManager> provider8) {
        this.contextProvider = provider;
        this.deviceStateManagerProvider = provider2;
        this.displayManagerProvider = provider3;
        this.unfoldTransitionProgressProvider = provider4;
        this.displayAreaHelperProvider = provider5;
        this.executorProvider = provider6;
        this.backgroundExecutorProvider = provider7;
        this.windowManagerInterfaceProvider = provider8;
    }

    public UnfoldLightRevealOverlayAnimation get() {
        return newInstance(this.contextProvider.get(), this.deviceStateManagerProvider.get(), this.displayManagerProvider.get(), this.unfoldTransitionProgressProvider.get(), this.displayAreaHelperProvider.get(), this.executorProvider.get(), this.backgroundExecutorProvider.get(), this.windowManagerInterfaceProvider.get());
    }

    public static UnfoldLightRevealOverlayAnimation_Factory create(Provider<Context> provider, Provider<DeviceStateManager> provider2, Provider<DisplayManager> provider3, Provider<UnfoldTransitionProgressProvider> provider4, Provider<Optional<DisplayAreaHelper>> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<IWindowManager> provider8) {
        return new UnfoldLightRevealOverlayAnimation_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static UnfoldLightRevealOverlayAnimation newInstance(Context context, DeviceStateManager deviceStateManager, DisplayManager displayManager, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2, Optional<DisplayAreaHelper> optional, Executor executor, Executor executor2, IWindowManager iWindowManager) {
        return new UnfoldLightRevealOverlayAnimation(context, deviceStateManager, displayManager, unfoldTransitionProgressProvider2, optional, executor, executor2, iWindowManager);
    }
}
