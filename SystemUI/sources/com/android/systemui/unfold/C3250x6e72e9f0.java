package com.android.systemui.unfold;

import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.systemui.unfold.UnfoldTransitionModule_ProvideStatusBarScopedTransitionProviderFactory */
public final class C3250x6e72e9f0 implements Factory<Optional<ScopedUnfoldTransitionProgressProvider>> {
    private final UnfoldTransitionModule module;
    private final Provider<Optional<NaturalRotationUnfoldProgressProvider>> sourceProvider;

    public C3250x6e72e9f0(UnfoldTransitionModule unfoldTransitionModule, Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider) {
        this.module = unfoldTransitionModule;
        this.sourceProvider = provider;
    }

    public Optional<ScopedUnfoldTransitionProgressProvider> get() {
        return provideStatusBarScopedTransitionProvider(this.module, this.sourceProvider.get());
    }

    public static C3250x6e72e9f0 create(UnfoldTransitionModule unfoldTransitionModule, Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider) {
        return new C3250x6e72e9f0(unfoldTransitionModule, provider);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider> provideStatusBarScopedTransitionProvider(com.android.systemui.unfold.UnfoldTransitionModule r0, java.util.Optional<com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider> r1) {
        /*
            java.util.Optional r0 = r0.provideStatusBarScopedTransitionProvider(r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.C3250x6e72e9f0.provideStatusBarScopedTransitionProvider(com.android.systemui.unfold.UnfoldTransitionModule, java.util.Optional):java.util.Optional");
    }
}
