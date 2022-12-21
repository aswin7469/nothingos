package com.nothing.systemui.statusbar.notification;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NTLightweightHeadsupManager_Factory implements Factory<NTLightweightHeadsupManager> {
    private final Provider<Context> contextProvider;

    public NTLightweightHeadsupManager_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public NTLightweightHeadsupManager get() {
        return newInstance(this.contextProvider.get());
    }

    public static NTLightweightHeadsupManager_Factory create(Provider<Context> provider) {
        return new NTLightweightHeadsupManager_Factory(provider);
    }

    public static NTLightweightHeadsupManager newInstance(Context context) {
        return new NTLightweightHeadsupManager(context);
    }
}
