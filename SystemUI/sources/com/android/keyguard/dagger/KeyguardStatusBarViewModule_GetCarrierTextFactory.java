package com.android.keyguard.dagger;

import com.android.keyguard.CarrierText;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardStatusBarViewModule_GetCarrierTextFactory implements Factory<CarrierText> {
    private final Provider<KeyguardStatusBarView> viewProvider;

    public KeyguardStatusBarViewModule_GetCarrierTextFactory(Provider<KeyguardStatusBarView> provider) {
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public CarrierText mo1933get() {
        return getCarrierText(this.viewProvider.mo1933get());
    }

    public static KeyguardStatusBarViewModule_GetCarrierTextFactory create(Provider<KeyguardStatusBarView> provider) {
        return new KeyguardStatusBarViewModule_GetCarrierTextFactory(provider);
    }

    public static CarrierText getCarrierText(KeyguardStatusBarView keyguardStatusBarView) {
        return (CarrierText) Preconditions.checkNotNullFromProvides(KeyguardStatusBarViewModule.getCarrierText(keyguardStatusBarView));
    }
}
