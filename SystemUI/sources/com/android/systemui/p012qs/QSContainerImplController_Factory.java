package com.android.systemui.p012qs;

import com.android.systemui.statusbar.policy.ConfigurationController;
import com.nothing.systemui.p024qs.NTQSStatusBarController;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSContainerImplController_Factory */
public final class QSContainerImplController_Factory implements Factory<QSContainerImplController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<NTQSStatusBarController> ntQSStatusBarControllerProvider;
    private final Provider<QSPanelController> qsPanelControllerProvider;
    private final Provider<QuickStatusBarHeaderController> quickStatusBarHeaderControllerProvider;
    private final Provider<QSContainerImpl> viewProvider;

    public QSContainerImplController_Factory(Provider<QSContainerImpl> provider, Provider<QSPanelController> provider2, Provider<QuickStatusBarHeaderController> provider3, Provider<NTQSStatusBarController> provider4, Provider<ConfigurationController> provider5) {
        this.viewProvider = provider;
        this.qsPanelControllerProvider = provider2;
        this.quickStatusBarHeaderControllerProvider = provider3;
        this.ntQSStatusBarControllerProvider = provider4;
        this.configurationControllerProvider = provider5;
    }

    public QSContainerImplController get() {
        return newInstance(this.viewProvider.get(), this.qsPanelControllerProvider.get(), this.quickStatusBarHeaderControllerProvider.get(), this.ntQSStatusBarControllerProvider.get(), this.configurationControllerProvider.get());
    }

    public static QSContainerImplController_Factory create(Provider<QSContainerImpl> provider, Provider<QSPanelController> provider2, Provider<QuickStatusBarHeaderController> provider3, Provider<NTQSStatusBarController> provider4, Provider<ConfigurationController> provider5) {
        return new QSContainerImplController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static QSContainerImplController newInstance(QSContainerImpl qSContainerImpl, QSPanelController qSPanelController, Object obj, NTQSStatusBarController nTQSStatusBarController, ConfigurationController configurationController) {
        return new QSContainerImplController(qSContainerImpl, qSPanelController, (QuickStatusBarHeaderController) obj, nTQSStatusBarController, configurationController);
    }
}
