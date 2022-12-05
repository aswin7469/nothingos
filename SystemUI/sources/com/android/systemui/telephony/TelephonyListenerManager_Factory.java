package com.android.systemui.telephony;

import android.telephony.TelephonyManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TelephonyListenerManager_Factory implements Factory<TelephonyListenerManager> {
    private final Provider<Executor> executorProvider;
    private final Provider<TelephonyCallback> telephonyCallbackProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;

    public TelephonyListenerManager_Factory(Provider<TelephonyManager> provider, Provider<Executor> provider2, Provider<TelephonyCallback> provider3) {
        this.telephonyManagerProvider = provider;
        this.executorProvider = provider2;
        this.telephonyCallbackProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TelephonyListenerManager mo1933get() {
        return newInstance(this.telephonyManagerProvider.mo1933get(), this.executorProvider.mo1933get(), this.telephonyCallbackProvider.mo1933get());
    }

    public static TelephonyListenerManager_Factory create(Provider<TelephonyManager> provider, Provider<Executor> provider2, Provider<TelephonyCallback> provider3) {
        return new TelephonyListenerManager_Factory(provider, provider2, provider3);
    }

    public static TelephonyListenerManager newInstance(TelephonyManager telephonyManager, Executor executor, Object obj) {
        return new TelephonyListenerManager(telephonyManager, executor, (TelephonyCallback) obj);
    }
}
