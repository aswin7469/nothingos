package com.nothingos.systemui.doze;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AODController_Factory implements Factory<AODController> {
    private final Provider<Context> contextProvider;

    public AODController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AODController mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static AODController_Factory create(Provider<Context> provider) {
        return new AODController_Factory(provider);
    }

    public static AODController newInstance(Context context) {
        return new AODController(context);
    }
}
