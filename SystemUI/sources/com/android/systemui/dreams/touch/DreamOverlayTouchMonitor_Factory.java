package com.android.systemui.dreams.touch;

import androidx.lifecycle.Lifecycle;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import dagger.internal.Factory;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamOverlayTouchMonitor_Factory implements Factory<DreamOverlayTouchMonitor> {
    private final Provider<Executor> executorProvider;
    private final Provider<Set<DreamTouchHandler>> handlersProvider;
    private final Provider<InputSessionComponent.Factory> inputSessionFactoryProvider;
    private final Provider<Lifecycle> lifecycleProvider;

    public DreamOverlayTouchMonitor_Factory(Provider<Executor> provider, Provider<Lifecycle> provider2, Provider<InputSessionComponent.Factory> provider3, Provider<Set<DreamTouchHandler>> provider4) {
        this.executorProvider = provider;
        this.lifecycleProvider = provider2;
        this.inputSessionFactoryProvider = provider3;
        this.handlersProvider = provider4;
    }

    public DreamOverlayTouchMonitor get() {
        return newInstance(this.executorProvider.get(), this.lifecycleProvider.get(), this.inputSessionFactoryProvider.get(), this.handlersProvider.get());
    }

    public static DreamOverlayTouchMonitor_Factory create(Provider<Executor> provider, Provider<Lifecycle> provider2, Provider<InputSessionComponent.Factory> provider3, Provider<Set<DreamTouchHandler>> provider4) {
        return new DreamOverlayTouchMonitor_Factory(provider, provider2, provider3, provider4);
    }

    public static DreamOverlayTouchMonitor newInstance(Executor executor, Lifecycle lifecycle, InputSessionComponent.Factory factory, Set<DreamTouchHandler> set) {
        return new DreamOverlayTouchMonitor(executor, lifecycle, factory, set);
    }
}
