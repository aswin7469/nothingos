package com.android.systemui.p012qs.dagger;

import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.p012qs.QuickStatusBarHeader;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_ProvidesBatteryMeterViewFactory */
public final class QSFragmentModule_ProvidesBatteryMeterViewFactory implements Factory<BatteryMeterView> {
    private final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;

    public QSFragmentModule_ProvidesBatteryMeterViewFactory(Provider<QuickStatusBarHeader> provider) {
        this.quickStatusBarHeaderProvider = provider;
    }

    public BatteryMeterView get() {
        return providesBatteryMeterView(this.quickStatusBarHeaderProvider.get());
    }

    public static QSFragmentModule_ProvidesBatteryMeterViewFactory create(Provider<QuickStatusBarHeader> provider) {
        return new QSFragmentModule_ProvidesBatteryMeterViewFactory(provider);
    }

    public static BatteryMeterView providesBatteryMeterView(QuickStatusBarHeader quickStatusBarHeader) {
        return (BatteryMeterView) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesBatteryMeterView(quickStatusBarHeader));
    }
}
