package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class RemoteInputQuickSettingsDisabler_Factory implements Factory<RemoteInputQuickSettingsDisabler> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<ConfigurationController> configControllerProvider;
    private final Provider<Context> contextProvider;

    public RemoteInputQuickSettingsDisabler_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<CommandQueue> provider3) {
        this.contextProvider = provider;
        this.configControllerProvider = provider2;
        this.commandQueueProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public RemoteInputQuickSettingsDisabler mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.configControllerProvider.mo1933get(), this.commandQueueProvider.mo1933get());
    }

    public static RemoteInputQuickSettingsDisabler_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<CommandQueue> provider3) {
        return new RemoteInputQuickSettingsDisabler_Factory(provider, provider2, provider3);
    }

    public static RemoteInputQuickSettingsDisabler newInstance(Context context, ConfigurationController configurationController, CommandQueue commandQueue) {
        return new RemoteInputQuickSettingsDisabler(context, configurationController, commandQueue);
    }
}
