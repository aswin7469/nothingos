package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RowContentBindStage_Factory implements Factory<RowContentBindStage> {
    private final Provider<NotificationRowContentBinder> binderProvider;
    private final Provider<NotifInflationErrorManager> errorManagerProvider;
    private final Provider<RowContentBindStageLogger> loggerProvider;

    public RowContentBindStage_Factory(Provider<NotificationRowContentBinder> provider, Provider<NotifInflationErrorManager> provider2, Provider<RowContentBindStageLogger> provider3) {
        this.binderProvider = provider;
        this.errorManagerProvider = provider2;
        this.loggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public RowContentBindStage mo1933get() {
        return newInstance(this.binderProvider.mo1933get(), this.errorManagerProvider.mo1933get(), this.loggerProvider.mo1933get());
    }

    public static RowContentBindStage_Factory create(Provider<NotificationRowContentBinder> provider, Provider<NotifInflationErrorManager> provider2, Provider<RowContentBindStageLogger> provider3) {
        return new RowContentBindStage_Factory(provider, provider2, provider3);
    }

    public static RowContentBindStage newInstance(NotificationRowContentBinder notificationRowContentBinder, NotifInflationErrorManager notifInflationErrorManager, RowContentBindStageLogger rowContentBindStageLogger) {
        return new RowContentBindStage(notificationRowContentBinder, notifInflationErrorManager, rowContentBindStageLogger);
    }
}
