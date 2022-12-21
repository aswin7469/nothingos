package com.android.systemui.dreams.complication;

import android.content.Context;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ComplicationTypesUpdater_Factory implements Factory<ComplicationTypesUpdater> {
    private final Provider<Context> contextProvider;
    private final Provider<DreamBackend> dreamBackendProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<SecureSettings> secureSettingsProvider;

    public ComplicationTypesUpdater_Factory(Provider<Context> provider, Provider<DreamBackend> provider2, Provider<Executor> provider3, Provider<SecureSettings> provider4, Provider<DreamOverlayStateController> provider5) {
        this.contextProvider = provider;
        this.dreamBackendProvider = provider2;
        this.executorProvider = provider3;
        this.secureSettingsProvider = provider4;
        this.dreamOverlayStateControllerProvider = provider5;
    }

    public ComplicationTypesUpdater get() {
        return newInstance(this.contextProvider.get(), this.dreamBackendProvider.get(), this.executorProvider.get(), this.secureSettingsProvider.get(), this.dreamOverlayStateControllerProvider.get());
    }

    public static ComplicationTypesUpdater_Factory create(Provider<Context> provider, Provider<DreamBackend> provider2, Provider<Executor> provider3, Provider<SecureSettings> provider4, Provider<DreamOverlayStateController> provider5) {
        return new ComplicationTypesUpdater_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ComplicationTypesUpdater newInstance(Context context, DreamBackend dreamBackend, Executor executor, SecureSettings secureSettings, DreamOverlayStateController dreamOverlayStateController) {
        return new ComplicationTypesUpdater(context, dreamBackend, executor, secureSettings, dreamOverlayStateController);
    }
}
