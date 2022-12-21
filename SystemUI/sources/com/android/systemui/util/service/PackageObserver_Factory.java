package com.android.systemui.util.service;

import android.content.ComponentName;
import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PackageObserver_Factory implements Factory<PackageObserver> {
    private final Provider<ComponentName> componentProvider;
    private final Provider<Context> contextProvider;

    public PackageObserver_Factory(Provider<Context> provider, Provider<ComponentName> provider2) {
        this.contextProvider = provider;
        this.componentProvider = provider2;
    }

    public PackageObserver get() {
        return newInstance(this.contextProvider.get(), this.componentProvider.get());
    }

    public static PackageObserver_Factory create(Provider<Context> provider, Provider<ComponentName> provider2) {
        return new PackageObserver_Factory(provider, provider2);
    }

    public static PackageObserver newInstance(Context context, ComponentName componentName) {
        return new PackageObserver(context, componentName);
    }
}
