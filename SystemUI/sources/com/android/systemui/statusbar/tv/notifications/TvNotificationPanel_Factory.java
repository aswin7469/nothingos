package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvNotificationPanel_Factory implements Factory<TvNotificationPanel> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;

    public TvNotificationPanel_Factory(Provider<Context> provider, Provider<CommandQueue> provider2) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public TvNotificationPanel mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.commandQueueProvider.mo1933get());
    }

    public static TvNotificationPanel_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2) {
        return new TvNotificationPanel_Factory(provider, provider2);
    }

    public static TvNotificationPanel newInstance(Context context, CommandQueue commandQueue) {
        return new TvNotificationPanel(context, commandQueue);
    }
}
