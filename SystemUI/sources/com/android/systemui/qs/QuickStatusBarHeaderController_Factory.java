package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QuickStatusBarHeaderController_Factory implements Factory<QuickStatusBarHeaderController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<SysuiColorExtractor> colorExtractorProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<PrivacyDialogController> privacyDialogControllerProvider;
    private final Provider<PrivacyItemController> privacyItemControllerProvider;
    private final Provider<PrivacyLogger> privacyLoggerProvider;
    private final Provider<QSCarrierGroupController.Builder> qsCarrierGroupControllerBuilderProvider;
    private final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    private final Provider<StatusBarIconController> statusBarIconControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<VariableDateViewController.Factory> variableDateViewControllerFactoryProvider;
    private final Provider<QuickStatusBarHeader> viewProvider;

    public QuickStatusBarHeaderController_Factory(Provider<QuickStatusBarHeader> provider, Provider<PrivacyItemController> provider2, Provider<ActivityStarter> provider3, Provider<UiEventLogger> provider4, Provider<StatusBarIconController> provider5, Provider<DemoModeController> provider6, Provider<QuickQSPanelController> provider7, Provider<QSCarrierGroupController.Builder> provider8, Provider<PrivacyLogger> provider9, Provider<SysuiColorExtractor> provider10, Provider<PrivacyDialogController> provider11, Provider<QSExpansionPathInterpolator> provider12, Provider<FeatureFlags> provider13, Provider<VariableDateViewController.Factory> provider14) {
        this.viewProvider = provider;
        this.privacyItemControllerProvider = provider2;
        this.activityStarterProvider = provider3;
        this.uiEventLoggerProvider = provider4;
        this.statusBarIconControllerProvider = provider5;
        this.demoModeControllerProvider = provider6;
        this.quickQSPanelControllerProvider = provider7;
        this.qsCarrierGroupControllerBuilderProvider = provider8;
        this.privacyLoggerProvider = provider9;
        this.colorExtractorProvider = provider10;
        this.privacyDialogControllerProvider = provider11;
        this.qsExpansionPathInterpolatorProvider = provider12;
        this.featureFlagsProvider = provider13;
        this.variableDateViewControllerFactoryProvider = provider14;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QuickStatusBarHeaderController mo1933get() {
        return newInstance(this.viewProvider.mo1933get(), this.privacyItemControllerProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get(), this.statusBarIconControllerProvider.mo1933get(), this.demoModeControllerProvider.mo1933get(), this.quickQSPanelControllerProvider.mo1933get(), this.qsCarrierGroupControllerBuilderProvider.mo1933get(), this.privacyLoggerProvider.mo1933get(), this.colorExtractorProvider.mo1933get(), this.privacyDialogControllerProvider.mo1933get(), this.qsExpansionPathInterpolatorProvider.mo1933get(), this.featureFlagsProvider.mo1933get(), this.variableDateViewControllerFactoryProvider.mo1933get());
    }

    public static QuickStatusBarHeaderController_Factory create(Provider<QuickStatusBarHeader> provider, Provider<PrivacyItemController> provider2, Provider<ActivityStarter> provider3, Provider<UiEventLogger> provider4, Provider<StatusBarIconController> provider5, Provider<DemoModeController> provider6, Provider<QuickQSPanelController> provider7, Provider<QSCarrierGroupController.Builder> provider8, Provider<PrivacyLogger> provider9, Provider<SysuiColorExtractor> provider10, Provider<PrivacyDialogController> provider11, Provider<QSExpansionPathInterpolator> provider12, Provider<FeatureFlags> provider13, Provider<VariableDateViewController.Factory> provider14) {
        return new QuickStatusBarHeaderController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static QuickStatusBarHeaderController newInstance(QuickStatusBarHeader quickStatusBarHeader, PrivacyItemController privacyItemController, ActivityStarter activityStarter, UiEventLogger uiEventLogger, StatusBarIconController statusBarIconController, DemoModeController demoModeController, QuickQSPanelController quickQSPanelController, QSCarrierGroupController.Builder builder, PrivacyLogger privacyLogger, SysuiColorExtractor sysuiColorExtractor, PrivacyDialogController privacyDialogController, QSExpansionPathInterpolator qSExpansionPathInterpolator, FeatureFlags featureFlags, VariableDateViewController.Factory factory) {
        return new QuickStatusBarHeaderController(quickStatusBarHeader, privacyItemController, activityStarter, uiEventLogger, statusBarIconController, demoModeController, quickQSPanelController, builder, privacyLogger, sysuiColorExtractor, privacyDialogController, qSExpansionPathInterpolator, featureFlags, factory);
    }
}
