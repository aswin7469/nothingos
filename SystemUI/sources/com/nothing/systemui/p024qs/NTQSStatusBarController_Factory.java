package com.nothing.systemui.p024qs;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.nothing.systemui.qs.NTQSStatusBarController_Factory */
public final class NTQSStatusBarController_Factory implements Factory<NTQSStatusBarController> {
    private final Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PrivacyDialogController> privacyDialogControllerProvider;
    private final Provider<PrivacyItemController> privacyItemControllerProvider;
    private final Provider<PrivacyLogger> privacyLoggerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<NTQSStatusBar> viewProvider;

    public NTQSStatusBarController_Factory(Provider<Context> provider, Provider<NTQSStatusBar> provider2, Provider<PrivacyDialogController> provider3, Provider<PrivacyItemController> provider4, Provider<UiEventLogger> provider5, Provider<PrivacyLogger> provider6, Provider<ConfigurationController> provider7, Provider<BatteryMeterViewController> provider8) {
        this.contextProvider = provider;
        this.viewProvider = provider2;
        this.privacyDialogControllerProvider = provider3;
        this.privacyItemControllerProvider = provider4;
        this.uiEventLoggerProvider = provider5;
        this.privacyLoggerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.batteryMeterViewControllerProvider = provider8;
    }

    public NTQSStatusBarController get() {
        return newInstance(this.contextProvider.get(), this.viewProvider.get(), this.privacyDialogControllerProvider.get(), this.privacyItemControllerProvider.get(), this.uiEventLoggerProvider.get(), this.privacyLoggerProvider.get(), this.configurationControllerProvider.get(), this.batteryMeterViewControllerProvider.get());
    }

    public static NTQSStatusBarController_Factory create(Provider<Context> provider, Provider<NTQSStatusBar> provider2, Provider<PrivacyDialogController> provider3, Provider<PrivacyItemController> provider4, Provider<UiEventLogger> provider5, Provider<PrivacyLogger> provider6, Provider<ConfigurationController> provider7, Provider<BatteryMeterViewController> provider8) {
        return new NTQSStatusBarController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static NTQSStatusBarController newInstance(Context context, NTQSStatusBar nTQSStatusBar, PrivacyDialogController privacyDialogController, PrivacyItemController privacyItemController, UiEventLogger uiEventLogger, PrivacyLogger privacyLogger, ConfigurationController configurationController, BatteryMeterViewController batteryMeterViewController) {
        return new NTQSStatusBarController(context, nTQSStatusBar, privacyDialogController, privacyItemController, uiEventLogger, privacyLogger, configurationController, batteryMeterViewController);
    }
}
