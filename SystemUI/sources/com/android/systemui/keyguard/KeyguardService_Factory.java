package com.android.systemui.keyguard;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardService_Factory implements Factory<KeyguardService> {
    private final Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;

    public KeyguardService_Factory(Provider<KeyguardViewMediator> provider, Provider<KeyguardLifecyclesDispatcher> provider2) {
        this.keyguardViewMediatorProvider = provider;
        this.keyguardLifecyclesDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardService mo1933get() {
        return newInstance(this.keyguardViewMediatorProvider.mo1933get(), this.keyguardLifecyclesDispatcherProvider.mo1933get());
    }

    public static KeyguardService_Factory create(Provider<KeyguardViewMediator> provider, Provider<KeyguardLifecyclesDispatcher> provider2) {
        return new KeyguardService_Factory(provider, provider2);
    }

    public static KeyguardService newInstance(KeyguardViewMediator keyguardViewMediator, KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher) {
        return new KeyguardService(keyguardViewMediator, keyguardLifecyclesDispatcher);
    }
}