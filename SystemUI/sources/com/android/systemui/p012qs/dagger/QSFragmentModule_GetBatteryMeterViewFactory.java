package com.android.systemui.p012qs.dagger;

import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.p012qs.QuickStatusBarHeader;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_GetBatteryMeterViewFactory */
public final class QSFragmentModule_GetBatteryMeterViewFactory implements Factory<BatteryMeterView> {
    private final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;

    public QSFragmentModule_GetBatteryMeterViewFactory(Provider<QuickStatusBarHeader> provider) {
        this.quickStatusBarHeaderProvider = provider;
    }

    public BatteryMeterView get() {
        return getBatteryMeterView(this.quickStatusBarHeaderProvider.get());
    }

    public static QSFragmentModule_GetBatteryMeterViewFactory create(Provider<QuickStatusBarHeader> provider) {
        return new QSFragmentModule_GetBatteryMeterViewFactory(provider);
    }

    public static BatteryMeterView getBatteryMeterView(QuickStatusBarHeader quickStatusBarHeader) {
        return (BatteryMeterView) Preconditions.checkNotNullFromProvides(QSFragmentModule.getBatteryMeterView(quickStatusBarHeader));
    }
}
