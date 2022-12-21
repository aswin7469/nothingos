package com.android.systemui.util.service;

import android.content.Context;
import android.content.Intent;
import com.android.systemui.util.service.ObservableServiceConnection;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ObservableServiceConnection_Factory<T> implements Factory<ObservableServiceConnection<T>> {
    private final Provider<Context> contextProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<Intent> serviceIntentProvider;
    private final Provider<ObservableServiceConnection.ServiceTransformer<T>> transformerProvider;

    public ObservableServiceConnection_Factory(Provider<Context> provider, Provider<Intent> provider2, Provider<Executor> provider3, Provider<ObservableServiceConnection.ServiceTransformer<T>> provider4) {
        this.contextProvider = provider;
        this.serviceIntentProvider = provider2;
        this.executorProvider = provider3;
        this.transformerProvider = provider4;
    }

    public ObservableServiceConnection<T> get() {
        return newInstance(this.contextProvider.get(), this.serviceIntentProvider.get(), this.executorProvider.get(), this.transformerProvider.get());
    }

    public static <T> ObservableServiceConnection_Factory<T> create(Provider<Context> provider, Provider<Intent> provider2, Provider<Executor> provider3, Provider<ObservableServiceConnection.ServiceTransformer<T>> provider4) {
        return new ObservableServiceConnection_Factory<>(provider, provider2, provider3, provider4);
    }

    public static <T> ObservableServiceConnection<T> newInstance(Context context, Intent intent, Executor executor, ObservableServiceConnection.ServiceTransformer<T> serviceTransformer) {
        return new ObservableServiceConnection<>(context, intent, executor, serviceTransformer);
    }
}
