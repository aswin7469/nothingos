package com.android.systemui.statusbar.policy;

import com.android.internal.statusbar.IStatusBarService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class RemoteInputUriController_Factory implements Factory<RemoteInputUriController> {
    private final Provider<IStatusBarService> statusBarServiceProvider;

    public RemoteInputUriController_Factory(Provider<IStatusBarService> provider) {
        this.statusBarServiceProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public RemoteInputUriController mo1933get() {
        return newInstance(this.statusBarServiceProvider.mo1933get());
    }

    public static RemoteInputUriController_Factory create(Provider<IStatusBarService> provider) {
        return new RemoteInputUriController_Factory(provider);
    }

    public static RemoteInputUriController newInstance(IStatusBarService iStatusBarService) {
        return new RemoteInputUriController(iStatusBarService);
    }
}
