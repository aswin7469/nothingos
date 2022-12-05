package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.view.WindowManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProvider_Factory implements Factory<StatusBarContentInsetsProvider> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public StatusBarContentInsetsProvider_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<WindowManager> provider3, Provider<DumpManager> provider4) {
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.windowManagerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public StatusBarContentInsetsProvider mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.configurationControllerProvider.mo1933get(), this.windowManagerProvider.mo1933get(), this.dumpManagerProvider.mo1933get());
    }

    public static StatusBarContentInsetsProvider_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<WindowManager> provider3, Provider<DumpManager> provider4) {
        return new StatusBarContentInsetsProvider_Factory(provider, provider2, provider3, provider4);
    }

    public static StatusBarContentInsetsProvider newInstance(Context context, ConfigurationController configurationController, WindowManager windowManager, DumpManager dumpManager) {
        return new StatusBarContentInsetsProvider(context, configurationController, windowManager, dumpManager);
    }
}
