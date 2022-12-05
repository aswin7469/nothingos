package com.android.systemui.doze;

import android.app.IWallpaperManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeWallpaperState_Factory implements Factory<DozeWallpaperState> {
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<DozeParameters> parametersProvider;
    private final Provider<IWallpaperManager> wallpaperManagerServiceProvider;

    public DozeWallpaperState_Factory(Provider<IWallpaperManager> provider, Provider<BiometricUnlockController> provider2, Provider<DozeParameters> provider3) {
        this.wallpaperManagerServiceProvider = provider;
        this.biometricUnlockControllerProvider = provider2;
        this.parametersProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DozeWallpaperState mo1933get() {
        return newInstance(this.wallpaperManagerServiceProvider.mo1933get(), this.biometricUnlockControllerProvider.mo1933get(), this.parametersProvider.mo1933get());
    }

    public static DozeWallpaperState_Factory create(Provider<IWallpaperManager> provider, Provider<BiometricUnlockController> provider2, Provider<DozeParameters> provider3) {
        return new DozeWallpaperState_Factory(provider, provider2, provider3);
    }

    public static DozeWallpaperState newInstance(IWallpaperManager iWallpaperManager, BiometricUnlockController biometricUnlockController, DozeParameters dozeParameters) {
        return new DozeWallpaperState(iWallpaperManager, biometricUnlockController, dozeParameters);
    }
}
