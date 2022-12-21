package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_CreateBatteryMeterViewFactory implements Factory<BatteryMeterView> {
    private final Provider<NotificationPanelView> npvProvider;

    public StatusBarViewModule_CreateBatteryMeterViewFactory(Provider<NotificationPanelView> provider) {
        this.npvProvider = provider;
    }

    public BatteryMeterView get() {
        return createBatteryMeterView(this.npvProvider.get());
    }

    public static StatusBarViewModule_CreateBatteryMeterViewFactory create(Provider<NotificationPanelView> provider) {
        return new StatusBarViewModule_CreateBatteryMeterViewFactory(provider);
    }

    public static BatteryMeterView createBatteryMeterView(NotificationPanelView notificationPanelView) {
        return (BatteryMeterView) Preconditions.checkNotNullFromProvides(StatusBarViewModule.createBatteryMeterView(notificationPanelView));
    }
}
