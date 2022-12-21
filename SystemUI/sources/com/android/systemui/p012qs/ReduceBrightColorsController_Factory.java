package com.android.systemui.p012qs;

import android.hardware.display.ColorDisplayManager;
import android.os.Handler;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.ReduceBrightColorsController_Factory */
public final class ReduceBrightColorsController_Factory implements Factory<ReduceBrightColorsController> {
    private final Provider<ColorDisplayManager> colorDisplayManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public ReduceBrightColorsController_Factory(Provider<UserTracker> provider, Provider<Handler> provider2, Provider<ColorDisplayManager> provider3, Provider<SecureSettings> provider4) {
        this.userTrackerProvider = provider;
        this.handlerProvider = provider2;
        this.colorDisplayManagerProvider = provider3;
        this.secureSettingsProvider = provider4;
    }

    public ReduceBrightColorsController get() {
        return newInstance(this.userTrackerProvider.get(), this.handlerProvider.get(), this.colorDisplayManagerProvider.get(), this.secureSettingsProvider.get());
    }

    public static ReduceBrightColorsController_Factory create(Provider<UserTracker> provider, Provider<Handler> provider2, Provider<ColorDisplayManager> provider3, Provider<SecureSettings> provider4) {
        return new ReduceBrightColorsController_Factory(provider, provider2, provider3, provider4);
    }

    public static ReduceBrightColorsController newInstance(UserTracker userTracker, Handler handler, ColorDisplayManager colorDisplayManager, SecureSettings secureSettings) {
        return new ReduceBrightColorsController(userTracker, handler, colorDisplayManager, secureSettings);
    }
}
