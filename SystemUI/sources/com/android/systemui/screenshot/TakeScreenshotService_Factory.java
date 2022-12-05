package com.android.systemui.screenshot;

import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TakeScreenshotService_Factory implements Factory<TakeScreenshotService> {
    private final Provider<ScreenshotNotificationsController> notificationsControllerProvider;
    private final Provider<ScreenshotController> screenshotControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserManager> userManagerProvider;

    public TakeScreenshotService_Factory(Provider<ScreenshotController> provider, Provider<UserManager> provider2, Provider<UiEventLogger> provider3, Provider<ScreenshotNotificationsController> provider4) {
        this.screenshotControllerProvider = provider;
        this.userManagerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.notificationsControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TakeScreenshotService mo1933get() {
        return newInstance(this.screenshotControllerProvider.mo1933get(), this.userManagerProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.notificationsControllerProvider.mo1933get());
    }

    public static TakeScreenshotService_Factory create(Provider<ScreenshotController> provider, Provider<UserManager> provider2, Provider<UiEventLogger> provider3, Provider<ScreenshotNotificationsController> provider4) {
        return new TakeScreenshotService_Factory(provider, provider2, provider3, provider4);
    }

    public static TakeScreenshotService newInstance(ScreenshotController screenshotController, UserManager userManager, UiEventLogger uiEventLogger, ScreenshotNotificationsController screenshotNotificationsController) {
        return new TakeScreenshotService(screenshotController, userManager, uiEventLogger, screenshotNotificationsController);
    }
}
