package com.nothing.systemui.biometrics;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AuthRippleControllerEx_Factory implements Factory<AuthRippleControllerEx> {
    private final Provider<Context> contextProvider;

    public AuthRippleControllerEx_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public AuthRippleControllerEx get() {
        return newInstance(this.contextProvider.get());
    }

    public static AuthRippleControllerEx_Factory create(Provider<Context> provider) {
        return new AuthRippleControllerEx_Factory(provider);
    }

    public static AuthRippleControllerEx newInstance(Context context) {
        return new AuthRippleControllerEx(context);
    }
}
