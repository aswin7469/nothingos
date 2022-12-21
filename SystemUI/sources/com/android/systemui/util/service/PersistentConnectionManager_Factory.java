package com.android.systemui.util.service;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PersistentConnectionManager_Factory<T> implements Factory<PersistentConnectionManager<T>> {
    private final Provider<Integer> baseReconnectDelayMsProvider;
    private final Provider<SystemClock> clockProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<Integer> maxReconnectAttemptsProvider;
    private final Provider<Integer> minConnectionDurationMsProvider;
    private final Provider<Observer> observerProvider;
    private final Provider<ObservableServiceConnection<T>> serviceConnectionProvider;

    public PersistentConnectionManager_Factory(Provider<SystemClock> provider, Provider<DelayableExecutor> provider2, Provider<ObservableServiceConnection<T>> provider3, Provider<Integer> provider4, Provider<Integer> provider5, Provider<Integer> provider6, Provider<Observer> provider7) {
        this.clockProvider = provider;
        this.mainExecutorProvider = provider2;
        this.serviceConnectionProvider = provider3;
        this.maxReconnectAttemptsProvider = provider4;
        this.baseReconnectDelayMsProvider = provider5;
        this.minConnectionDurationMsProvider = provider6;
        this.observerProvider = provider7;
    }

    public PersistentConnectionManager<T> get() {
        return newInstance(this.clockProvider.get(), this.mainExecutorProvider.get(), this.serviceConnectionProvider.get(), this.maxReconnectAttemptsProvider.get().intValue(), this.baseReconnectDelayMsProvider.get().intValue(), this.minConnectionDurationMsProvider.get().intValue(), this.observerProvider.get());
    }

    public static <T> PersistentConnectionManager_Factory<T> create(Provider<SystemClock> provider, Provider<DelayableExecutor> provider2, Provider<ObservableServiceConnection<T>> provider3, Provider<Integer> provider4, Provider<Integer> provider5, Provider<Integer> provider6, Provider<Observer> provider7) {
        return new PersistentConnectionManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static <T> PersistentConnectionManager<T> newInstance(SystemClock systemClock, DelayableExecutor delayableExecutor, ObservableServiceConnection<T> observableServiceConnection, int i, int i2, int i3, Observer observer) {
        return new PersistentConnectionManager(systemClock, delayableExecutor, observableServiceConnection, i, i2, i3, observer);
    }
}
