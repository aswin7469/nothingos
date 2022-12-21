package com.nothing.gamemode;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NTGameModeHelper_Factory implements Factory<NTGameModeHelper> {
    private final Provider<Context> contextProvider;

    public NTGameModeHelper_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public NTGameModeHelper get() {
        return newInstance(this.contextProvider.get());
    }

    public static NTGameModeHelper_Factory create(Provider<Context> provider) {
        return new NTGameModeHelper_Factory(provider);
    }

    public static NTGameModeHelper newInstance(Context context) {
        return new NTGameModeHelper(context);
    }
}
