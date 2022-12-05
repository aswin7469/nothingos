package com.android.systemui.wmshell;

import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public StartingWindowTypeAlgorithm mo1933get() {
        return provideStartingWindowTypeAlgorithm();
    }

    public static WMShellModule_ProvideStartingWindowTypeAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.provideStartingWindowTypeAlgorithm());
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final WMShellModule_ProvideStartingWindowTypeAlgorithmFactory INSTANCE = new WMShellModule_ProvideStartingWindowTypeAlgorithmFactory();
    }
}
