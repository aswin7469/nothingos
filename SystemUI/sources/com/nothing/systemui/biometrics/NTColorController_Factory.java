package com.nothing.systemui.biometrics;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NTColorController_Factory implements Factory<NTColorController> {
    private final Provider<Context> contextProvider;

    public NTColorController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public NTColorController get() {
        return newInstance(this.contextProvider.get());
    }

    public static NTColorController_Factory create(Provider<Context> provider) {
        return new NTColorController_Factory(provider);
    }

    public static NTColorController newInstance(Context context) {
        return new NTColorController(context);
    }
}
