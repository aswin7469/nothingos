package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsAlgorithm;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuController;
import com.android.p019wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvideTvPipTransitionFactory */
public final class TvPipModule_ProvideTvPipTransitionFactory implements Factory<PipTransitionController> {
    private final Provider<PipAnimationController> pipAnimationControllerProvider;
    private final Provider<TvPipMenuController> pipMenuControllerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Transitions> transitionsProvider;
    private final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    private final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvideTvPipTransitionFactory(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<TvPipBoundsAlgorithm> provider4, Provider<TvPipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        this.transitionsProvider = provider;
        this.shellTaskOrganizerProvider = provider2;
        this.pipAnimationControllerProvider = provider3;
        this.tvPipBoundsAlgorithmProvider = provider4;
        this.tvPipBoundsStateProvider = provider5;
        this.pipMenuControllerProvider = provider6;
    }

    public PipTransitionController get() {
        return provideTvPipTransition(this.transitionsProvider.get(), this.shellTaskOrganizerProvider.get(), this.pipAnimationControllerProvider.get(), this.tvPipBoundsAlgorithmProvider.get(), this.tvPipBoundsStateProvider.get(), this.pipMenuControllerProvider.get());
    }

    public static TvPipModule_ProvideTvPipTransitionFactory create(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<TvPipBoundsAlgorithm> provider4, Provider<TvPipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        return new TvPipModule_ProvideTvPipTransitionFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static PipTransitionController provideTvPipTransition(Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsState tvPipBoundsState, TvPipMenuController tvPipMenuController) {
        return (PipTransitionController) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipTransition(transitions, shellTaskOrganizer, pipAnimationController, tvPipBoundsAlgorithm, tvPipBoundsState, tvPipMenuController));
    }
}
