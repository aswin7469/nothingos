package com.android.systemui.globalactions;

import android.content.Context;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ExtensionController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalActionsComponent_Factory implements Factory<GlobalActionsComponent> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ExtensionController> extensionControllerProvider;
    private final Provider<GlobalActions> globalActionsProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;

    public GlobalActionsComponent_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ExtensionController> provider3, Provider<GlobalActions> provider4, Provider<StatusBarKeyguardViewManager> provider5) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.extensionControllerProvider = provider3;
        this.globalActionsProvider = provider4;
        this.statusBarKeyguardViewManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public GlobalActionsComponent mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.commandQueueProvider.mo1933get(), this.extensionControllerProvider.mo1933get(), this.globalActionsProvider, this.statusBarKeyguardViewManagerProvider.mo1933get());
    }

    public static GlobalActionsComponent_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ExtensionController> provider3, Provider<GlobalActions> provider4, Provider<StatusBarKeyguardViewManager> provider5) {
        return new GlobalActionsComponent_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static GlobalActionsComponent newInstance(Context context, CommandQueue commandQueue, ExtensionController extensionController, Provider<GlobalActions> provider, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        return new GlobalActionsComponent(context, commandQueue, extensionController, provider, statusBarKeyguardViewManager);
    }
}
