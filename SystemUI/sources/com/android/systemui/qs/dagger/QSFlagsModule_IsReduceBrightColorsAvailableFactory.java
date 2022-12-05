package com.android.systemui.qs.dagger;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFlagsModule_IsReduceBrightColorsAvailableFactory implements Factory<Boolean> {
    private final Provider<Context> contextProvider;

    public QSFlagsModule_IsReduceBrightColorsAvailableFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public Boolean mo1933get() {
        return Boolean.valueOf(isReduceBrightColorsAvailable(this.contextProvider.mo1933get()));
    }

    public static QSFlagsModule_IsReduceBrightColorsAvailableFactory create(Provider<Context> provider) {
        return new QSFlagsModule_IsReduceBrightColorsAvailableFactory(provider);
    }

    public static boolean isReduceBrightColorsAvailable(Context context) {
        return QSFlagsModule.isReduceBrightColorsAvailable(context);
    }
}
