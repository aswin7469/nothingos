package com.android.keyguard;

import android.view.LayoutInflater;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardSecurityViewFlipperController_Factory implements Factory<KeyguardSecurityViewFlipperController> {
    private final Provider<EmergencyButtonController.Factory> emergencyButtonControllerFactoryProvider;
    private final Provider<KeyguardInputViewController.Factory> keyguardSecurityViewControllerFactoryProvider;
    private final Provider<LayoutInflater> layoutInflaterProvider;
    private final Provider<KeyguardSecurityViewFlipper> viewProvider;

    public KeyguardSecurityViewFlipperController_Factory(Provider<KeyguardSecurityViewFlipper> provider, Provider<LayoutInflater> provider2, Provider<KeyguardInputViewController.Factory> provider3, Provider<EmergencyButtonController.Factory> provider4) {
        this.viewProvider = provider;
        this.layoutInflaterProvider = provider2;
        this.keyguardSecurityViewControllerFactoryProvider = provider3;
        this.emergencyButtonControllerFactoryProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardSecurityViewFlipperController mo1933get() {
        return newInstance(this.viewProvider.mo1933get(), this.layoutInflaterProvider.mo1933get(), this.keyguardSecurityViewControllerFactoryProvider.mo1933get(), this.emergencyButtonControllerFactoryProvider.mo1933get());
    }

    public static KeyguardSecurityViewFlipperController_Factory create(Provider<KeyguardSecurityViewFlipper> provider, Provider<LayoutInflater> provider2, Provider<KeyguardInputViewController.Factory> provider3, Provider<EmergencyButtonController.Factory> provider4) {
        return new KeyguardSecurityViewFlipperController_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardSecurityViewFlipperController newInstance(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, LayoutInflater layoutInflater, KeyguardInputViewController.Factory factory, EmergencyButtonController.Factory factory2) {
        return new KeyguardSecurityViewFlipperController(keyguardSecurityViewFlipper, layoutInflater, factory, factory2);
    }
}
