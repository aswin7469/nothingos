package com.android.systemui.dagger;

import android.app.trust.TrustManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideTrustManagerFactory implements Factory<TrustManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideTrustManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TrustManager mo1933get() {
        return provideTrustManager(this.contextProvider.mo1933get());
    }

    public static FrameworkServicesModule_ProvideTrustManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideTrustManagerFactory(provider);
    }

    public static TrustManager provideTrustManager(Context context) {
        return (TrustManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideTrustManager(context));
    }
}
