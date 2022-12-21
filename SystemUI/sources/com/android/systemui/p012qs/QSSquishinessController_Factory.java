package com.android.systemui.p012qs;

import com.nothing.systemui.p024qs.NTQSAnimator;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSSquishinessController_Factory */
public final class QSSquishinessController_Factory implements Factory<QSSquishinessController> {
    private final Provider<NTQSAnimator> qsAnimatorProvider;
    private final Provider<QSPanelController> qsPanelControllerProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;

    public QSSquishinessController_Factory(Provider<NTQSAnimator> provider, Provider<QSPanelController> provider2, Provider<QuickQSPanelController> provider3) {
        this.qsAnimatorProvider = provider;
        this.qsPanelControllerProvider = provider2;
        this.quickQSPanelControllerProvider = provider3;
    }

    public QSSquishinessController get() {
        return newInstance(this.qsAnimatorProvider.get(), this.qsPanelControllerProvider.get(), this.quickQSPanelControllerProvider.get());
    }

    public static QSSquishinessController_Factory create(Provider<NTQSAnimator> provider, Provider<QSPanelController> provider2, Provider<QuickQSPanelController> provider3) {
        return new QSSquishinessController_Factory(provider, provider2, provider3);
    }

    public static QSSquishinessController newInstance(NTQSAnimator nTQSAnimator, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController) {
        return new QSSquishinessController(nTQSAnimator, qSPanelController, quickQSPanelController);
    }
}
