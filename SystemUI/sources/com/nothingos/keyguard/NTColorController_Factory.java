package com.nothingos.keyguard;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NTColorController_Factory implements Factory<NTColorController> {
    private final Provider<Context> contextProvider;

    public NTColorController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NTColorController mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static NTColorController_Factory create(Provider<Context> provider) {
        return new NTColorController_Factory(provider);
    }

    public static NTColorController newInstance(Context context) {
        return new NTColorController(context);
    }
}
