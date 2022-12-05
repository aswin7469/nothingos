package com.android.systemui.statusbar.notification.collection;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifPipeline_Factory implements Factory<NotifPipeline> {
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<ShadeListBuilder> shadeListBuilderProvider;

    public NotifPipeline_Factory(Provider<NotifCollection> provider, Provider<ShadeListBuilder> provider2) {
        this.notifCollectionProvider = provider;
        this.shadeListBuilderProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotifPipeline mo1933get() {
        return newInstance(this.notifCollectionProvider.mo1933get(), this.shadeListBuilderProvider.mo1933get());
    }

    public static NotifPipeline_Factory create(Provider<NotifCollection> provider, Provider<ShadeListBuilder> provider2) {
        return new NotifPipeline_Factory(provider, provider2);
    }

    public static NotifPipeline newInstance(NotifCollection notifCollection, ShadeListBuilder shadeListBuilder) {
        return new NotifPipeline(notifCollection, shadeListBuilder);
    }
}
