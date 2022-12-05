package com.android.systemui.statusbar.tv.notifications;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvNotificationPanelActivity_Factory implements Factory<TvNotificationPanelActivity> {
    private final Provider<TvNotificationHandler> tvNotificationHandlerProvider;

    public TvNotificationPanelActivity_Factory(Provider<TvNotificationHandler> provider) {
        this.tvNotificationHandlerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TvNotificationPanelActivity mo1933get() {
        return newInstance(this.tvNotificationHandlerProvider.mo1933get());
    }

    public static TvNotificationPanelActivity_Factory create(Provider<TvNotificationHandler> provider) {
        return new TvNotificationPanelActivity_Factory(provider);
    }

    public static TvNotificationPanelActivity newInstance(TvNotificationHandler tvNotificationHandler) {
        return new TvNotificationPanelActivity(tvNotificationHandler);
    }
}
