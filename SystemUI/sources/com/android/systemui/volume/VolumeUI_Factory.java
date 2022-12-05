package com.android.systemui.volume;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class VolumeUI_Factory implements Factory<VolumeUI> {
    private final Provider<Context> contextProvider;
    private final Provider<VolumeDialogComponent> volumeDialogComponentProvider;

    public VolumeUI_Factory(Provider<Context> provider, Provider<VolumeDialogComponent> provider2) {
        this.contextProvider = provider;
        this.volumeDialogComponentProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public VolumeUI mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.volumeDialogComponentProvider.mo1933get());
    }

    public static VolumeUI_Factory create(Provider<Context> provider, Provider<VolumeDialogComponent> provider2) {
        return new VolumeUI_Factory(provider, provider2);
    }

    public static VolumeUI newInstance(Context context, VolumeDialogComponent volumeDialogComponent) {
        return new VolumeUI(context, volumeDialogComponent);
    }
}
