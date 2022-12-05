package com.android.systemui.doze;

import android.hardware.display.AmbientDisplayConfiguration;
import com.android.systemui.dock.DockManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeDockHandler_Factory implements Factory<DozeDockHandler> {
    private final Provider<AmbientDisplayConfiguration> configProvider;
    private final Provider<DockManager> dockManagerProvider;

    public DozeDockHandler_Factory(Provider<AmbientDisplayConfiguration> provider, Provider<DockManager> provider2) {
        this.configProvider = provider;
        this.dockManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DozeDockHandler mo1933get() {
        return newInstance(this.configProvider.mo1933get(), this.dockManagerProvider.mo1933get());
    }

    public static DozeDockHandler_Factory create(Provider<AmbientDisplayConfiguration> provider, Provider<DockManager> provider2) {
        return new DozeDockHandler_Factory(provider, provider2);
    }

    public static DozeDockHandler newInstance(AmbientDisplayConfiguration ambientDisplayConfiguration, DockManager dockManager) {
        return new DozeDockHandler(ambientDisplayConfiguration, dockManager);
    }
}
