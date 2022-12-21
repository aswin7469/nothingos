package com.nothing.systemui.p024qs;

import android.os.Handler;
import com.android.systemui.p012qs.QSExpansionPathInterpolator;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.p012qs.QSSecurityFooter;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.QuickQSPanel;
import com.android.systemui.p012qs.QuickQSPanelController;
import com.android.systemui.p012qs.QuickStatusBarHeader;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.nothing.systemui.qs.NTQSAnimator_Factory */
public final class NTQSAnimator_Factory implements Factory<NTQSAnimator> {
    private final Provider<Executor> executorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    private final Provider<QSPanelController> qsPanelControllerProvider;
    private final Provider<C2301QS> qsProvider;
    private final Provider<QSTileHost> qsTileHostProvider;
    private final Provider<QuickQSPanel> quickPanelProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    private final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;
    private final Provider<QSSecurityFooter> securityFooterProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public NTQSAnimator_Factory(Provider<C2301QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<QSSecurityFooter> provider7, Provider<Executor> provider8, Provider<TunerService> provider9, Provider<QSExpansionPathInterpolator> provider10, Provider<Handler> provider11) {
        this.qsProvider = provider;
        this.quickPanelProvider = provider2;
        this.quickStatusBarHeaderProvider = provider3;
        this.qsPanelControllerProvider = provider4;
        this.quickQSPanelControllerProvider = provider5;
        this.qsTileHostProvider = provider6;
        this.securityFooterProvider = provider7;
        this.executorProvider = provider8;
        this.tunerServiceProvider = provider9;
        this.qsExpansionPathInterpolatorProvider = provider10;
        this.mainHandlerProvider = provider11;
    }

    public NTQSAnimator get() {
        return newInstance(this.qsProvider.get(), this.quickPanelProvider.get(), this.quickStatusBarHeaderProvider.get(), this.qsPanelControllerProvider.get(), this.quickQSPanelControllerProvider.get(), this.qsTileHostProvider.get(), this.securityFooterProvider.get(), this.executorProvider.get(), this.tunerServiceProvider.get(), this.qsExpansionPathInterpolatorProvider.get(), this.mainHandlerProvider.get());
    }

    public static NTQSAnimator_Factory create(Provider<C2301QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<QSSecurityFooter> provider7, Provider<Executor> provider8, Provider<TunerService> provider9, Provider<QSExpansionPathInterpolator> provider10, Provider<Handler> provider11) {
        return new NTQSAnimator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static NTQSAnimator newInstance(C2301QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, QSSecurityFooter qSSecurityFooter, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator, Handler handler) {
        return new NTQSAnimator(qs, quickQSPanel, quickStatusBarHeader, qSPanelController, quickQSPanelController, qSTileHost, qSSecurityFooter, executor, tunerService, qSExpansionPathInterpolator, handler);
    }
}
