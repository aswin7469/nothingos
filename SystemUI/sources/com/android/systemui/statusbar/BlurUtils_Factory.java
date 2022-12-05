package com.android.systemui.statusbar;

import android.content.res.Resources;
import android.view.CrossWindowBlurListeners;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BlurUtils_Factory implements Factory<BlurUtils> {
    private final Provider<CrossWindowBlurListeners> crossWindowBlurListenersProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Resources> resourcesProvider;

    public BlurUtils_Factory(Provider<Resources> provider, Provider<CrossWindowBlurListeners> provider2, Provider<DumpManager> provider3) {
        this.resourcesProvider = provider;
        this.crossWindowBlurListenersProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public BlurUtils mo1933get() {
        return newInstance(this.resourcesProvider.mo1933get(), this.crossWindowBlurListenersProvider.mo1933get(), this.dumpManagerProvider.mo1933get());
    }

    public static BlurUtils_Factory create(Provider<Resources> provider, Provider<CrossWindowBlurListeners> provider2, Provider<DumpManager> provider3) {
        return new BlurUtils_Factory(provider, provider2, provider3);
    }

    public static BlurUtils newInstance(Resources resources, CrossWindowBlurListeners crossWindowBlurListeners, DumpManager dumpManager) {
        return new BlurUtils(resources, crossWindowBlurListeners, dumpManager);
    }
}
