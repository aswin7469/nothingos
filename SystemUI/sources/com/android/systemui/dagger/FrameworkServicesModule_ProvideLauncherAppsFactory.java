package com.android.systemui.dagger;

import android.content.Context;
import android.content.pm.LauncherApps;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideLauncherAppsFactory implements Factory<LauncherApps> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideLauncherAppsFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LauncherApps mo1933get() {
        return provideLauncherApps(this.contextProvider.mo1933get());
    }

    public static FrameworkServicesModule_ProvideLauncherAppsFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideLauncherAppsFactory(provider);
    }

    public static LauncherApps provideLauncherApps(Context context) {
        return (LauncherApps) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideLauncherApps(context));
    }
}
