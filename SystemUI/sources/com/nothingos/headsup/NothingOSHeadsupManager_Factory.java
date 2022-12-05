package com.nothingos.headsup;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NothingOSHeadsupManager_Factory implements Factory<NothingOSHeadsupManager> {
    private final Provider<Context> contextProvider;

    public NothingOSHeadsupManager_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NothingOSHeadsupManager mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static NothingOSHeadsupManager_Factory create(Provider<Context> provider) {
        return new NothingOSHeadsupManager_Factory(provider);
    }

    public static NothingOSHeadsupManager newInstance(Context context) {
        return new NothingOSHeadsupManager(context);
    }
}
