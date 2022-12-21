package com.android.systemui.p012qs;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.p012qs.QSTileRevealController;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSPanelController_Factory */
public final class QSPanelController_Factory implements Factory<QSPanelController> {
    private final Provider<BrightnessController.Factory> brightnessControllerFactoryProvider;
    private final Provider<BrightnessSliderController.Factory> brightnessSliderFactoryProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<MediaHost> mediaHostProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSCustomizerController> qsCustomizerControllerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<QSTileRevealController.Factory> qsTileRevealControllerFactoryProvider;
    private final Provider<QSTileHost> qstileHostProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<Boolean> usingMediaPlayerProvider;
    private final Provider<QSPanel> viewProvider;

    public QSPanelController_Factory(Provider<QSPanel> provider, Provider<TunerService> provider2, Provider<QSTileHost> provider3, Provider<QSCustomizerController> provider4, Provider<Boolean> provider5, Provider<MediaHost> provider6, Provider<QSTileRevealController.Factory> provider7, Provider<DumpManager> provider8, Provider<MetricsLogger> provider9, Provider<UiEventLogger> provider10, Provider<QSLogger> provider11, Provider<BrightnessController.Factory> provider12, Provider<BrightnessSliderController.Factory> provider13, Provider<FalsingManager> provider14, Provider<StatusBarKeyguardViewManager> provider15) {
        this.viewProvider = provider;
        this.tunerServiceProvider = provider2;
        this.qstileHostProvider = provider3;
        this.qsCustomizerControllerProvider = provider4;
        this.usingMediaPlayerProvider = provider5;
        this.mediaHostProvider = provider6;
        this.qsTileRevealControllerFactoryProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.metricsLoggerProvider = provider9;
        this.uiEventLoggerProvider = provider10;
        this.qsLoggerProvider = provider11;
        this.brightnessControllerFactoryProvider = provider12;
        this.brightnessSliderFactoryProvider = provider13;
        this.falsingManagerProvider = provider14;
        this.statusBarKeyguardViewManagerProvider = provider15;
    }

    public QSPanelController get() {
        return newInstance(this.viewProvider.get(), this.tunerServiceProvider.get(), this.qstileHostProvider.get(), this.qsCustomizerControllerProvider.get(), this.usingMediaPlayerProvider.get().booleanValue(), this.mediaHostProvider.get(), this.qsTileRevealControllerFactoryProvider.get(), this.dumpManagerProvider.get(), this.metricsLoggerProvider.get(), this.uiEventLoggerProvider.get(), this.qsLoggerProvider.get(), this.brightnessControllerFactoryProvider.get(), this.brightnessSliderFactoryProvider.get(), this.falsingManagerProvider.get(), this.statusBarKeyguardViewManagerProvider.get());
    }

    public static QSPanelController_Factory create(Provider<QSPanel> provider, Provider<TunerService> provider2, Provider<QSTileHost> provider3, Provider<QSCustomizerController> provider4, Provider<Boolean> provider5, Provider<MediaHost> provider6, Provider<QSTileRevealController.Factory> provider7, Provider<DumpManager> provider8, Provider<MetricsLogger> provider9, Provider<UiEventLogger> provider10, Provider<QSLogger> provider11, Provider<BrightnessController.Factory> provider12, Provider<BrightnessSliderController.Factory> provider13, Provider<FalsingManager> provider14, Provider<StatusBarKeyguardViewManager> provider15) {
        return new QSPanelController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static QSPanelController newInstance(QSPanel qSPanel, TunerService tunerService, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, Object obj, DumpManager dumpManager, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, BrightnessController.Factory factory, BrightnessSliderController.Factory factory2, FalsingManager falsingManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        return new QSPanelController(qSPanel, tunerService, qSTileHost, qSCustomizerController, z, mediaHost, (QSTileRevealController.Factory) obj, dumpManager, metricsLogger, uiEventLogger, qSLogger, factory, factory2, falsingManager, statusBarKeyguardViewManager);
    }
}
