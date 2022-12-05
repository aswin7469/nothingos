package com.android.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class FlashlightControllerImpl_Factory implements Factory<FlashlightControllerImpl> {
    private final Provider<Context> contextProvider;

    public FlashlightControllerImpl_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public FlashlightControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static FlashlightControllerImpl_Factory create(Provider<Context> provider) {
        return new FlashlightControllerImpl_Factory(provider);
    }

    public static FlashlightControllerImpl newInstance(Context context) {
        return new FlashlightControllerImpl(context);
    }
}
