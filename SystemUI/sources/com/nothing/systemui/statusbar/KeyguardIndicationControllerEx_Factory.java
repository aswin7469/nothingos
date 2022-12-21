package com.nothing.systemui.statusbar;

import android.content.Context;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.keyguard.KeyguardViewMediator;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardIndicationControllerEx_Factory implements Factory<KeyguardIndicationControllerEx> {
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;

    public KeyguardIndicationControllerEx_Factory(Provider<Context> provider, Provider<KeyguardSecurityModel> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewMediator> provider4) {
        this.contextProvider = provider;
        this.keyguardSecurityModelProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.keyguardViewMediatorProvider = provider4;
    }

    public KeyguardIndicationControllerEx get() {
        return newInstance(this.contextProvider.get(), this.keyguardSecurityModelProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.keyguardViewMediatorProvider.get());
    }

    public static KeyguardIndicationControllerEx_Factory create(Provider<Context> provider, Provider<KeyguardSecurityModel> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewMediator> provider4) {
        return new KeyguardIndicationControllerEx_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardIndicationControllerEx newInstance(Context context, KeyguardSecurityModel keyguardSecurityModel, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewMediator keyguardViewMediator) {
        return new KeyguardIndicationControllerEx(context, keyguardSecurityModel, keyguardUpdateMonitor, keyguardViewMediator);
    }
}
