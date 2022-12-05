package com.android.systemui.classifier;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class FalsingModule_ProvidesDoubleTapTimeoutMsFactory implements Factory<Long> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public Long mo1933get() {
        return Long.valueOf(providesDoubleTapTimeoutMs());
    }

    public static FalsingModule_ProvidesDoubleTapTimeoutMsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static long providesDoubleTapTimeoutMs() {
        return FalsingModule.providesDoubleTapTimeoutMs();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final FalsingModule_ProvidesDoubleTapTimeoutMsFactory INSTANCE = new FalsingModule_ProvidesDoubleTapTimeoutMsFactory();
    }
}
