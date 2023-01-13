package com.android.systemui.unfold;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.systemui.unfold.UnfoldTransitionModule_ProvideNaturalRotationProgressProviderFactory */
public final class C3249xd9485442 implements Factory<Optional<NaturalRotationUnfoldProgressProvider>> {
    private final Provider<Context> contextProvider;
    private final UnfoldTransitionModule module;
    private final Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;
    private final Provider<IWindowManager> windowManagerProvider;

    public C3249xd9485442(UnfoldTransitionModule unfoldTransitionModule, Provider<Context> provider, Provider<IWindowManager> provider2, Provider<Optional<UnfoldTransitionProgressProvider>> provider3) {
        this.module = unfoldTransitionModule;
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.unfoldTransitionProgressProvider = provider3;
    }

    public Optional<NaturalRotationUnfoldProgressProvider> get() {
        return provideNaturalRotationProgressProvider(this.module, this.contextProvider.get(), this.windowManagerProvider.get(), this.unfoldTransitionProgressProvider.get());
    }

    public static C3249xd9485442 create(UnfoldTransitionModule unfoldTransitionModule, Provider<Context> provider, Provider<IWindowManager> provider2, Provider<Optional<UnfoldTransitionProgressProvider>> provider3) {
        return new C3249xd9485442(unfoldTransitionModule, provider, provider2, provider3);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.util.Optional<com.android.systemui.unfold.UnfoldTransitionProgressProvider>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider> provideNaturalRotationProgressProvider(com.android.systemui.unfold.UnfoldTransitionModule r0, android.content.Context r1, android.view.IWindowManager r2, java.util.Optional<com.android.systemui.unfold.UnfoldTransitionProgressProvider> r3) {
        /*
            java.util.Optional r0 = r0.provideNaturalRotationProgressProvider(r1, r2, r3)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.C3249xd9485442.provideNaturalRotationProgressProvider(com.android.systemui.unfold.UnfoldTransitionModule, android.content.Context, android.view.IWindowManager, java.util.Optional):java.util.Optional");
    }
}
