package com.android.systemui.keyguard;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardLifecyclesDispatcher_Factory implements Factory<KeyguardLifecyclesDispatcher> {
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public KeyguardLifecyclesDispatcher_Factory(Provider<ScreenLifecycle> provider, Provider<WakefulnessLifecycle> provider2) {
        this.screenLifecycleProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardLifecyclesDispatcher mo1933get() {
        return newInstance(this.screenLifecycleProvider.mo1933get(), this.wakefulnessLifecycleProvider.mo1933get());
    }

    public static KeyguardLifecyclesDispatcher_Factory create(Provider<ScreenLifecycle> provider, Provider<WakefulnessLifecycle> provider2) {
        return new KeyguardLifecyclesDispatcher_Factory(provider, provider2);
    }

    public static KeyguardLifecyclesDispatcher newInstance(ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle) {
        return new KeyguardLifecyclesDispatcher(screenLifecycle, wakefulnessLifecycle);
    }
}
