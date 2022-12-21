package com.android.systemui.util.condition;

import com.android.systemui.util.condition.Monitor;
import dagger.internal.Factory;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class Monitor_Factory implements Factory<Monitor> {
    private final Provider<Set<Monitor.Callback>> callbacksProvider;
    private final Provider<Set<Condition>> conditionsProvider;
    private final Provider<Executor> executorProvider;

    public Monitor_Factory(Provider<Executor> provider, Provider<Set<Condition>> provider2, Provider<Set<Monitor.Callback>> provider3) {
        this.executorProvider = provider;
        this.conditionsProvider = provider2;
        this.callbacksProvider = provider3;
    }

    public Monitor get() {
        return newInstance(this.executorProvider.get(), this.conditionsProvider.get(), this.callbacksProvider.get());
    }

    public static Monitor_Factory create(Provider<Executor> provider, Provider<Set<Condition>> provider2, Provider<Set<Monitor.Callback>> provider3) {
        return new Monitor_Factory(provider, provider2, provider3);
    }

    public static Monitor newInstance(Executor executor, Set<Condition> set, Set<Monitor.Callback> set2) {
        return new Monitor(executor, set, set2);
    }
}
