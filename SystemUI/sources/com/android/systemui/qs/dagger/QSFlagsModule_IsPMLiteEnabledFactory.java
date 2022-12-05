package com.android.systemui.qs.dagger;

import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFlagsModule_IsPMLiteEnabledFactory implements Factory<Boolean> {
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<GlobalSettings> globalSettingsProvider;

    public QSFlagsModule_IsPMLiteEnabledFactory(Provider<FeatureFlags> provider, Provider<GlobalSettings> provider2) {
        this.featureFlagsProvider = provider;
        this.globalSettingsProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public Boolean mo1933get() {
        return Boolean.valueOf(isPMLiteEnabled(this.featureFlagsProvider.mo1933get(), this.globalSettingsProvider.mo1933get()));
    }

    public static QSFlagsModule_IsPMLiteEnabledFactory create(Provider<FeatureFlags> provider, Provider<GlobalSettings> provider2) {
        return new QSFlagsModule_IsPMLiteEnabledFactory(provider, provider2);
    }

    public static boolean isPMLiteEnabled(FeatureFlags featureFlags, GlobalSettings globalSettings) {
        return QSFlagsModule.isPMLiteEnabled(featureFlags, globalSettings);
    }
}
