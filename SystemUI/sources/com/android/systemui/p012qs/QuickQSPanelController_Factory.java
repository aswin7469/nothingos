package com.android.systemui.p012qs;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.logging.QSLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QuickQSPanelController_Factory */
public final class QuickQSPanelController_Factory implements Factory<QuickQSPanelController> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<MediaHost> mediaHostProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSCustomizerController> qsCustomizerControllerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<QSTileHost> qsTileHostProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<Boolean> usingCollapsedLandscapeMediaProvider;
    private final Provider<Boolean> usingMediaPlayerProvider;
    private final Provider<QuickQSPanel> viewProvider;

    public QuickQSPanelController_Factory(Provider<QuickQSPanel> provider, Provider<QSTileHost> provider2, Provider<QSCustomizerController> provider3, Provider<Boolean> provider4, Provider<MediaHost> provider5, Provider<Boolean> provider6, Provider<MetricsLogger> provider7, Provider<UiEventLogger> provider8, Provider<QSLogger> provider9, Provider<DumpManager> provider10) {
        this.viewProvider = provider;
        this.qsTileHostProvider = provider2;
        this.qsCustomizerControllerProvider = provider3;
        this.usingMediaPlayerProvider = provider4;
        this.mediaHostProvider = provider5;
        this.usingCollapsedLandscapeMediaProvider = provider6;
        this.metricsLoggerProvider = provider7;
        this.uiEventLoggerProvider = provider8;
        this.qsLoggerProvider = provider9;
        this.dumpManagerProvider = provider10;
    }

    public QuickQSPanelController get() {
        return newInstance(this.viewProvider.get(), this.qsTileHostProvider.get(), this.qsCustomizerControllerProvider.get(), this.usingMediaPlayerProvider.get().booleanValue(), this.mediaHostProvider.get(), this.usingCollapsedLandscapeMediaProvider.get().booleanValue(), this.metricsLoggerProvider.get(), this.uiEventLoggerProvider.get(), this.qsLoggerProvider.get(), this.dumpManagerProvider.get());
    }

    public static QuickQSPanelController_Factory create(Provider<QuickQSPanel> provider, Provider<QSTileHost> provider2, Provider<QSCustomizerController> provider3, Provider<Boolean> provider4, Provider<MediaHost> provider5, Provider<Boolean> provider6, Provider<MetricsLogger> provider7, Provider<UiEventLogger> provider8, Provider<QSLogger> provider9, Provider<DumpManager> provider10) {
        return new QuickQSPanelController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static QuickQSPanelController newInstance(QuickQSPanel quickQSPanel, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, boolean z2, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        return new QuickQSPanelController(quickQSPanel, qSTileHost, qSCustomizerController, z, mediaHost, z2, metricsLogger, uiEventLogger, qSLogger, dumpManager);
    }
}
