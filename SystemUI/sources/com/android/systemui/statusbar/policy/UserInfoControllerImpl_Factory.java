package com.android.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UserInfoControllerImpl_Factory implements Factory<UserInfoControllerImpl> {
    private final Provider<Context> contextProvider;

    public UserInfoControllerImpl_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public UserInfoControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static UserInfoControllerImpl_Factory create(Provider<Context> provider) {
        return new UserInfoControllerImpl_Factory(provider);
    }

    public static UserInfoControllerImpl newInstance(Context context) {
        return new UserInfoControllerImpl(context);
    }
}
