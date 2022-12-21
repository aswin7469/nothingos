package com.android.systemui.p012qs;

import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.p012qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QuickStatusBarHeaderController_Factory */
public final class QuickStatusBarHeaderController_Factory implements Factory<QuickStatusBarHeaderController> {
    private final Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
    private final Provider<SysuiColorExtractor> colorExtractorProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<HeaderPrivacyIconsController> headerPrivacyIconsControllerProvider;
    private final Provider<QSCarrierGroupController.Builder> qsCarrierGroupControllerBuilderProvider;
    private final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    private final Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
    private final Provider<StatusBarIconController> statusBarIconControllerProvider;
    private final Provider<VariableDateViewController.Factory> variableDateViewControllerFactoryProvider;
    private final Provider<QuickStatusBarHeader> viewProvider;

    public QuickStatusBarHeaderController_Factory(Provider<QuickStatusBarHeader> provider, Provider<HeaderPrivacyIconsController> provider2, Provider<StatusBarIconController> provider3, Provider<DemoModeController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSCarrierGroupController.Builder> provider6, Provider<SysuiColorExtractor> provider7, Provider<QSExpansionPathInterpolator> provider8, Provider<FeatureFlags> provider9, Provider<VariableDateViewController.Factory> provider10, Provider<BatteryMeterViewController> provider11, Provider<StatusBarContentInsetsProvider> provider12) {
        this.viewProvider = provider;
        this.headerPrivacyIconsControllerProvider = provider2;
        this.statusBarIconControllerProvider = provider3;
        this.demoModeControllerProvider = provider4;
        this.quickQSPanelControllerProvider = provider5;
        this.qsCarrierGroupControllerBuilderProvider = provider6;
        this.colorExtractorProvider = provider7;
        this.qsExpansionPathInterpolatorProvider = provider8;
        this.featureFlagsProvider = provider9;
        this.variableDateViewControllerFactoryProvider = provider10;
        this.batteryMeterViewControllerProvider = provider11;
        this.statusBarContentInsetsProvider = provider12;
    }

    public QuickStatusBarHeaderController get() {
        return newInstance(this.viewProvider.get(), this.headerPrivacyIconsControllerProvider.get(), this.statusBarIconControllerProvider.get(), this.demoModeControllerProvider.get(), this.quickQSPanelControllerProvider.get(), this.qsCarrierGroupControllerBuilderProvider.get(), this.colorExtractorProvider.get(), this.qsExpansionPathInterpolatorProvider.get(), this.featureFlagsProvider.get(), this.variableDateViewControllerFactoryProvider.get(), this.batteryMeterViewControllerProvider.get(), this.statusBarContentInsetsProvider.get());
    }

    public static QuickStatusBarHeaderController_Factory create(Provider<QuickStatusBarHeader> provider, Provider<HeaderPrivacyIconsController> provider2, Provider<StatusBarIconController> provider3, Provider<DemoModeController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSCarrierGroupController.Builder> provider6, Provider<SysuiColorExtractor> provider7, Provider<QSExpansionPathInterpolator> provider8, Provider<FeatureFlags> provider9, Provider<VariableDateViewController.Factory> provider10, Provider<BatteryMeterViewController> provider11, Provider<StatusBarContentInsetsProvider> provider12) {
        return new QuickStatusBarHeaderController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static QuickStatusBarHeaderController newInstance(QuickStatusBarHeader quickStatusBarHeader, HeaderPrivacyIconsController headerPrivacyIconsController, StatusBarIconController statusBarIconController, DemoModeController demoModeController, QuickQSPanelController quickQSPanelController, QSCarrierGroupController.Builder builder, SysuiColorExtractor sysuiColorExtractor, QSExpansionPathInterpolator qSExpansionPathInterpolator, FeatureFlags featureFlags, VariableDateViewController.Factory factory, BatteryMeterViewController batteryMeterViewController, StatusBarContentInsetsProvider statusBarContentInsetsProvider2) {
        return new QuickStatusBarHeaderController(quickStatusBarHeader, headerPrivacyIconsController, statusBarIconController, demoModeController, quickQSPanelController, builder, sysuiColorExtractor, qSExpansionPathInterpolator, featureFlags, factory, batteryMeterViewController, statusBarContentInsetsProvider2);
    }
}
