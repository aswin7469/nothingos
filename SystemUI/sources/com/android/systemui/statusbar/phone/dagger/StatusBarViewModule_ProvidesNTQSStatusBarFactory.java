package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.nothing.systemui.p024qs.NTQSStatusBar;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_ProvidesNTQSStatusBarFactory implements Factory<NTQSStatusBar> {
    private final Provider<NotificationPanelView> npvProvider;

    public StatusBarViewModule_ProvidesNTQSStatusBarFactory(Provider<NotificationPanelView> provider) {
        this.npvProvider = provider;
    }

    public NTQSStatusBar get() {
        return providesNTQSStatusBar(this.npvProvider.get());
    }

    public static StatusBarViewModule_ProvidesNTQSStatusBarFactory create(Provider<NotificationPanelView> provider) {
        return new StatusBarViewModule_ProvidesNTQSStatusBarFactory(provider);
    }

    public static NTQSStatusBar providesNTQSStatusBar(NotificationPanelView notificationPanelView) {
        return (NTQSStatusBar) Preconditions.checkNotNullFromProvides(StatusBarViewModule.providesNTQSStatusBar(notificationPanelView));
    }
}
