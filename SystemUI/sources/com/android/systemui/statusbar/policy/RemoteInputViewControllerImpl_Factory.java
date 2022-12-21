package com.android.systemui.statusbar.policy;

import android.content.pm.ShortcutManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RemoteInputViewControllerImpl_Factory implements Factory<RemoteInputViewControllerImpl> {
    private final Provider<NotificationEntry> entryProvider;
    private final Provider<RemoteInputController> remoteInputControllerProvider;
    private final Provider<RemoteInputQuickSettingsDisabler> remoteInputQuickSettingsDisablerProvider;
    private final Provider<ShortcutManager> shortcutManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<RemoteInputView> viewProvider;

    public RemoteInputViewControllerImpl_Factory(Provider<RemoteInputView> provider, Provider<NotificationEntry> provider2, Provider<RemoteInputQuickSettingsDisabler> provider3, Provider<RemoteInputController> provider4, Provider<ShortcutManager> provider5, Provider<UiEventLogger> provider6) {
        this.viewProvider = provider;
        this.entryProvider = provider2;
        this.remoteInputQuickSettingsDisablerProvider = provider3;
        this.remoteInputControllerProvider = provider4;
        this.shortcutManagerProvider = provider5;
        this.uiEventLoggerProvider = provider6;
    }

    public RemoteInputViewControllerImpl get() {
        return newInstance(this.viewProvider.get(), this.entryProvider.get(), this.remoteInputQuickSettingsDisablerProvider.get(), this.remoteInputControllerProvider.get(), this.shortcutManagerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static RemoteInputViewControllerImpl_Factory create(Provider<RemoteInputView> provider, Provider<NotificationEntry> provider2, Provider<RemoteInputQuickSettingsDisabler> provider3, Provider<RemoteInputController> provider4, Provider<ShortcutManager> provider5, Provider<UiEventLogger> provider6) {
        return new RemoteInputViewControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static RemoteInputViewControllerImpl newInstance(RemoteInputView remoteInputView, NotificationEntry notificationEntry, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, RemoteInputController remoteInputController, ShortcutManager shortcutManager, UiEventLogger uiEventLogger) {
        return new RemoteInputViewControllerImpl(remoteInputView, notificationEntry, remoteInputQuickSettingsDisabler, remoteInputController, shortcutManager, uiEventLogger);
    }
}
