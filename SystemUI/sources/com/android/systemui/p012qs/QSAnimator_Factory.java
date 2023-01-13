package com.android.systemui.p012qs;

import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSAnimator_Factory */
public final class QSAnimator_Factory implements Factory<QSAnimator> {
    private final Provider<Executor> executorProvider;
    private final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    private final Provider<QSPanelController> qsPanelControllerProvider;
    private final Provider<C2304QS> qsProvider;
    private final Provider<QSTileHost> qsTileHostProvider;
    private final Provider<QuickQSPanel> quickPanelProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    private final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public QSAnimator_Factory(Provider<C2304QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<Executor> provider7, Provider<TunerService> provider8, Provider<QSExpansionPathInterpolator> provider9) {
        this.qsProvider = provider;
        this.quickPanelProvider = provider2;
        this.quickStatusBarHeaderProvider = provider3;
        this.qsPanelControllerProvider = provider4;
        this.quickQSPanelControllerProvider = provider5;
        this.qsTileHostProvider = provider6;
        this.executorProvider = provider7;
        this.tunerServiceProvider = provider8;
        this.qsExpansionPathInterpolatorProvider = provider9;
    }

    public QSAnimator get() {
        return newInstance(this.qsProvider.get(), this.quickPanelProvider.get(), this.quickStatusBarHeaderProvider.get(), this.qsPanelControllerProvider.get(), this.quickQSPanelControllerProvider.get(), this.qsTileHostProvider.get(), this.executorProvider.get(), this.tunerServiceProvider.get(), this.qsExpansionPathInterpolatorProvider.get());
    }

    public static QSAnimator_Factory create(Provider<C2304QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<Executor> provider7, Provider<TunerService> provider8, Provider<QSExpansionPathInterpolator> provider9) {
        return new QSAnimator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static QSAnimator newInstance(C2304QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator) {
        return new QSAnimator(qs, quickQSPanel, quickStatusBarHeader, qSPanelController, quickQSPanelController, qSTileHost, executor, tunerService, qSExpansionPathInterpolator);
    }
}
