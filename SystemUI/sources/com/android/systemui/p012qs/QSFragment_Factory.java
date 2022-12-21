package com.android.systemui.p012qs;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.p012qs.dagger.QSFragmentComponent;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSFragment_Factory */
public final class QSFragment_Factory implements Factory<QSFragment> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<MediaHost> qqsMediaHostProvider;
    private final Provider<QSFragmentComponent.Factory> qsComponentFactoryProvider;
    private final Provider<QSFragmentDisableFlagsLogger> qsFragmentDisableFlagsLoggerProvider;
    private final Provider<MediaHost> qsMediaHostProvider;
    private final Provider<QSTileHost> qsTileHostProvider;
    private final Provider<RemoteInputQuickSettingsDisabler> remoteInputQsDisablerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public QSFragment_Factory(Provider<RemoteInputQuickSettingsDisabler> provider, Provider<QSTileHost> provider2, Provider<StatusBarStateController> provider3, Provider<CommandQueue> provider4, Provider<MediaHost> provider5, Provider<MediaHost> provider6, Provider<KeyguardBypassController> provider7, Provider<QSFragmentComponent.Factory> provider8, Provider<QSFragmentDisableFlagsLogger> provider9, Provider<FalsingManager> provider10, Provider<DumpManager> provider11) {
        this.remoteInputQsDisablerProvider = provider;
        this.qsTileHostProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.commandQueueProvider = provider4;
        this.qsMediaHostProvider = provider5;
        this.qqsMediaHostProvider = provider6;
        this.keyguardBypassControllerProvider = provider7;
        this.qsComponentFactoryProvider = provider8;
        this.qsFragmentDisableFlagsLoggerProvider = provider9;
        this.falsingManagerProvider = provider10;
        this.dumpManagerProvider = provider11;
    }

    public QSFragment get() {
        return newInstance(this.remoteInputQsDisablerProvider.get(), this.qsTileHostProvider.get(), this.statusBarStateControllerProvider.get(), this.commandQueueProvider.get(), this.qsMediaHostProvider.get(), this.qqsMediaHostProvider.get(), this.keyguardBypassControllerProvider.get(), this.qsComponentFactoryProvider.get(), this.qsFragmentDisableFlagsLoggerProvider.get(), this.falsingManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static QSFragment_Factory create(Provider<RemoteInputQuickSettingsDisabler> provider, Provider<QSTileHost> provider2, Provider<StatusBarStateController> provider3, Provider<CommandQueue> provider4, Provider<MediaHost> provider5, Provider<MediaHost> provider6, Provider<KeyguardBypassController> provider7, Provider<QSFragmentComponent.Factory> provider8, Provider<QSFragmentDisableFlagsLogger> provider9, Provider<FalsingManager> provider10, Provider<DumpManager> provider11) {
        return new QSFragment_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static QSFragment newInstance(RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, QSTileHost qSTileHost, StatusBarStateController statusBarStateController, CommandQueue commandQueue, MediaHost mediaHost, MediaHost mediaHost2, KeyguardBypassController keyguardBypassController, QSFragmentComponent.Factory factory, QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger, FalsingManager falsingManager, DumpManager dumpManager) {
        return new QSFragment(remoteInputQuickSettingsDisabler, qSTileHost, statusBarStateController, commandQueue, mediaHost, mediaHost2, keyguardBypassController, factory, qSFragmentDisableFlagsLogger, falsingManager, dumpManager);
    }
}
