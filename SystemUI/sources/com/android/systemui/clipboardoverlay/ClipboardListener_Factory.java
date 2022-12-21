package com.android.systemui.clipboardoverlay;

import android.content.ClipboardManager;
import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ClipboardListener_Factory implements Factory<ClipboardListener> {
    private final Provider<ClipboardManager> clipboardManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    private final Provider<ClipboardOverlayControllerFactory> overlayFactoryProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public ClipboardListener_Factory(Provider<Context> provider, Provider<DeviceConfigProxy> provider2, Provider<ClipboardOverlayControllerFactory> provider3, Provider<ClipboardManager> provider4, Provider<UiEventLogger> provider5) {
        this.contextProvider = provider;
        this.deviceConfigProxyProvider = provider2;
        this.overlayFactoryProvider = provider3;
        this.clipboardManagerProvider = provider4;
        this.uiEventLoggerProvider = provider5;
    }

    public ClipboardListener get() {
        return newInstance(this.contextProvider.get(), this.deviceConfigProxyProvider.get(), this.overlayFactoryProvider.get(), this.clipboardManagerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static ClipboardListener_Factory create(Provider<Context> provider, Provider<DeviceConfigProxy> provider2, Provider<ClipboardOverlayControllerFactory> provider3, Provider<ClipboardManager> provider4, Provider<UiEventLogger> provider5) {
        return new ClipboardListener_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ClipboardListener newInstance(Context context, DeviceConfigProxy deviceConfigProxy, ClipboardOverlayControllerFactory clipboardOverlayControllerFactory, ClipboardManager clipboardManager, UiEventLogger uiEventLogger) {
        return new ClipboardListener(context, deviceConfigProxy, clipboardOverlayControllerFactory, clipboardManager, uiEventLogger);
    }
}
