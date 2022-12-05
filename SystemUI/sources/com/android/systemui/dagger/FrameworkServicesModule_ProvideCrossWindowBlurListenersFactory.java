package com.android.systemui.dagger;

import android.view.CrossWindowBlurListeners;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory implements Factory<CrossWindowBlurListeners> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public CrossWindowBlurListeners mo1933get() {
        return provideCrossWindowBlurListeners();
    }

    public static FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static CrossWindowBlurListeners provideCrossWindowBlurListeners() {
        return (CrossWindowBlurListeners) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideCrossWindowBlurListeners());
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory INSTANCE = new FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory();
    }
}
