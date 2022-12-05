package com.android.systemui.statusbar.notification.interruption;

import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HeadsUpViewBinder_Factory implements Factory<HeadsUpViewBinder> {
    private final Provider<RowContentBindStage> bindStageProvider;
    private final Provider<NotificationMessagingUtil> notificationMessagingUtilProvider;

    public HeadsUpViewBinder_Factory(Provider<NotificationMessagingUtil> provider, Provider<RowContentBindStage> provider2) {
        this.notificationMessagingUtilProvider = provider;
        this.bindStageProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public HeadsUpViewBinder mo1933get() {
        return newInstance(this.notificationMessagingUtilProvider.mo1933get(), this.bindStageProvider.mo1933get());
    }

    public static HeadsUpViewBinder_Factory create(Provider<NotificationMessagingUtil> provider, Provider<RowContentBindStage> provider2) {
        return new HeadsUpViewBinder_Factory(provider, provider2);
    }

    public static HeadsUpViewBinder newInstance(NotificationMessagingUtil notificationMessagingUtil, RowContentBindStage rowContentBindStage) {
        return new HeadsUpViewBinder(notificationMessagingUtil, rowContentBindStage);
    }
}
