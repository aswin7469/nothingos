package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.view.IWindowManager;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.AutoHideController_Factory  reason: case insensitive filesystem */
public final class C4826AutoHideController_Factory implements Factory<AutoHideController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<IWindowManager> iWindowManagerProvider;

    public C4826AutoHideController_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<IWindowManager> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.iWindowManagerProvider = provider3;
    }

    public AutoHideController get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.iWindowManagerProvider.get());
    }

    public static C4826AutoHideController_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<IWindowManager> provider3) {
        return new C4826AutoHideController_Factory(provider, provider2, provider3);
    }

    public static AutoHideController newInstance(Context context, Handler handler, IWindowManager iWindowManager) {
        return new AutoHideController(context, handler, iWindowManager);
    }
}
