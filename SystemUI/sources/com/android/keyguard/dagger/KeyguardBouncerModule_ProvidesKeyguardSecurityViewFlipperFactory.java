package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory implements Factory<KeyguardSecurityViewFlipper> {
    private final Provider<KeyguardSecurityContainer> containerViewProvider;

    public KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory(Provider<KeyguardSecurityContainer> provider) {
        this.containerViewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardSecurityViewFlipper mo1933get() {
        return providesKeyguardSecurityViewFlipper(this.containerViewProvider.mo1933get());
    }

    public static KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory create(Provider<KeyguardSecurityContainer> provider) {
        return new KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory(provider);
    }

    public static KeyguardSecurityViewFlipper providesKeyguardSecurityViewFlipper(KeyguardSecurityContainer keyguardSecurityContainer) {
        return (KeyguardSecurityViewFlipper) Preconditions.checkNotNullFromProvides(KeyguardBouncerModule.providesKeyguardSecurityViewFlipper(keyguardSecurityContainer));
    }
}
