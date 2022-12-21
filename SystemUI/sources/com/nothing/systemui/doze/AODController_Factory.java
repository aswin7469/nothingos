package com.nothing.systemui.doze;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AODController_Factory implements Factory<AODController> {
    private final Provider<Context> contextProvider;

    public AODController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public AODController get() {
        return newInstance(this.contextProvider.get());
    }

    public static AODController_Factory create(Provider<Context> provider) {
        return new AODController_Factory(provider);
    }

    public static AODController newInstance(Context context) {
        return new AODController(context);
    }
}
