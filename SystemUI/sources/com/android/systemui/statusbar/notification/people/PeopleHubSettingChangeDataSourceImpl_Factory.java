package com.android.systemui.statusbar.notification.people;

import android.content.Context;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PeopleHubSettingChangeDataSourceImpl_Factory implements Factory<PeopleHubSettingChangeDataSourceImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;

    public PeopleHubSettingChangeDataSourceImpl_Factory(Provider<Handler> provider, Provider<Context> provider2) {
        this.handlerProvider = provider;
        this.contextProvider = provider2;
    }

    public PeopleHubSettingChangeDataSourceImpl get() {
        return newInstance(this.handlerProvider.get(), this.contextProvider.get());
    }

    public static PeopleHubSettingChangeDataSourceImpl_Factory create(Provider<Handler> provider, Provider<Context> provider2) {
        return new PeopleHubSettingChangeDataSourceImpl_Factory(provider, provider2);
    }

    public static PeopleHubSettingChangeDataSourceImpl newInstance(Handler handler, Context context) {
        return new PeopleHubSettingChangeDataSourceImpl(handler, context);
    }
}
