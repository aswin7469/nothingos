package com.android.systemui.qs;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessSlider;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSPanelController_Factory implements Factory<QSPanelController> {
    private final Provider<BrightnessController.Factory> brightnessControllerFactoryProvider;
    private final Provider<BrightnessSlider.Factory> brightnessSliderFactoryProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<MediaHost> mediaHostProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSCustomizerController> qsCustomizerControllerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<QSSecurityFooter> qsSecurityFooterProvider;
    private final Provider<QSTileRevealController.Factory> qsTileRevealControllerFactoryProvider;
    private final Provider<QSTileHost> qstileHostProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<Boolean> usingMediaPlayerProvider;
    private final Provider<QSPanel> viewProvider;

    public QSPanelController_Factory(Provider<QSPanel> provider, Provider<QSSecurityFooter> provider2, Provider<TunerService> provider3, Provider<QSTileHost> provider4, Provider<QSCustomizerController> provider5, Provider<Boolean> provider6, Provider<MediaHost> provider7, Provider<QSTileRevealController.Factory> provider8, Provider<DumpManager> provider9, Provider<MetricsLogger> provider10, Provider<UiEventLogger> provider11, Provider<QSLogger> provider12, Provider<BrightnessController.Factory> provider13, Provider<BrightnessSlider.Factory> provider14, Provider<FalsingManager> provider15, Provider<FeatureFlags> provider16) {
        this.viewProvider = provider;
        this.qsSecurityFooterProvider = provider2;
        this.tunerServiceProvider = provider3;
        this.qstileHostProvider = provider4;
        this.qsCustomizerControllerProvider = provider5;
        this.usingMediaPlayerProvider = provider6;
        this.mediaHostProvider = provider7;
        this.qsTileRevealControllerFactoryProvider = provider8;
        this.dumpManagerProvider = provider9;
        this.metricsLoggerProvider = provider10;
        this.uiEventLoggerProvider = provider11;
        this.qsLoggerProvider = provider12;
        this.brightnessControllerFactoryProvider = provider13;
        this.brightnessSliderFactoryProvider = provider14;
        this.falsingManagerProvider = provider15;
        this.featureFlagsProvider = provider16;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QSPanelController mo1933get() {
        return newInstance(this.viewProvider.mo1933get(), this.qsSecurityFooterProvider.mo1933get(), this.tunerServiceProvider.mo1933get(), this.qstileHostProvider.mo1933get(), this.qsCustomizerControllerProvider.mo1933get(), this.usingMediaPlayerProvider.mo1933get().booleanValue(), this.mediaHostProvider.mo1933get(), this.qsTileRevealControllerFactoryProvider.mo1933get(), this.dumpManagerProvider.mo1933get(), this.metricsLoggerProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.qsLoggerProvider.mo1933get(), this.brightnessControllerFactoryProvider.mo1933get(), this.brightnessSliderFactoryProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.featureFlagsProvider.mo1933get());
    }

    public static QSPanelController_Factory create(Provider<QSPanel> provider, Provider<QSSecurityFooter> provider2, Provider<TunerService> provider3, Provider<QSTileHost> provider4, Provider<QSCustomizerController> provider5, Provider<Boolean> provider6, Provider<MediaHost> provider7, Provider<QSTileRevealController.Factory> provider8, Provider<DumpManager> provider9, Provider<MetricsLogger> provider10, Provider<UiEventLogger> provider11, Provider<QSLogger> provider12, Provider<BrightnessController.Factory> provider13, Provider<BrightnessSlider.Factory> provider14, Provider<FalsingManager> provider15, Provider<FeatureFlags> provider16) {
        return new QSPanelController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static QSPanelController newInstance(QSPanel qSPanel, Object obj, TunerService tunerService, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, Object obj2, DumpManager dumpManager, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, BrightnessController.Factory factory, BrightnessSlider.Factory factory2, FalsingManager falsingManager, FeatureFlags featureFlags) {
        return new QSPanelController(qSPanel, (QSSecurityFooter) obj, tunerService, qSTileHost, qSCustomizerController, z, mediaHost, (QSTileRevealController.Factory) obj2, dumpManager, metricsLogger, uiEventLogger, qSLogger, factory, factory2, falsingManager, featureFlags);
    }
}
