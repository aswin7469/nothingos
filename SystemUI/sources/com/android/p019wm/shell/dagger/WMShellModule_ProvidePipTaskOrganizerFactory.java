package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipBoundsAlgorithm;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipTransitionState;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.phone.PhonePipMenuController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidePipTaskOrganizerFactory */
public final class WMShellModule_ProvidePipTaskOrganizerFactory implements Factory<PipTaskOrganizer> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PhonePipMenuController> menuPhoneControllerProvider;
    private final Provider<PipAnimationController> pipAnimationControllerProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipParamsChangedForwarder> pipParamsChangedForwarderProvider;
    private final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;
    private final Provider<PipTransitionState> pipTransitionStateProvider;
    private final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<SplitScreenController>> splitScreenControllerOptionalProvider;
    private final Provider<SyncTransactionQueue> syncTransactionQueueProvider;

    public WMShellModule_ProvidePipTaskOrganizerFactory(Provider<Context> provider, Provider<SyncTransactionQueue> provider2, Provider<PipTransitionState> provider3, Provider<PipBoundsState> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PhonePipMenuController> provider6, Provider<PipAnimationController> provider7, Provider<PipSurfaceTransactionHelper> provider8, Provider<PipTransitionController> provider9, Provider<PipParamsChangedForwarder> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.syncTransactionQueueProvider = provider2;
        this.pipTransitionStateProvider = provider3;
        this.pipBoundsStateProvider = provider4;
        this.pipBoundsAlgorithmProvider = provider5;
        this.menuPhoneControllerProvider = provider6;
        this.pipAnimationControllerProvider = provider7;
        this.pipSurfaceTransactionHelperProvider = provider8;
        this.pipTransitionControllerProvider = provider9;
        this.pipParamsChangedForwarderProvider = provider10;
        this.splitScreenControllerOptionalProvider = provider11;
        this.displayControllerProvider = provider12;
        this.pipUiEventLoggerProvider = provider13;
        this.shellTaskOrganizerProvider = provider14;
        this.mainExecutorProvider = provider15;
    }

    public PipTaskOrganizer get() {
        return providePipTaskOrganizer(this.contextProvider.get(), this.syncTransactionQueueProvider.get(), this.pipTransitionStateProvider.get(), this.pipBoundsStateProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.menuPhoneControllerProvider.get(), this.pipAnimationControllerProvider.get(), this.pipSurfaceTransactionHelperProvider.get(), this.pipTransitionControllerProvider.get(), this.pipParamsChangedForwarderProvider.get(), this.splitScreenControllerOptionalProvider.get(), this.displayControllerProvider.get(), this.pipUiEventLoggerProvider.get(), this.shellTaskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvidePipTaskOrganizerFactory create(Provider<Context> provider, Provider<SyncTransactionQueue> provider2, Provider<PipTransitionState> provider3, Provider<PipBoundsState> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PhonePipMenuController> provider6, Provider<PipAnimationController> provider7, Provider<PipSurfaceTransactionHelper> provider8, Provider<PipTransitionController> provider9, Provider<PipParamsChangedForwarder> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        return new WMShellModule_ProvidePipTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static PipTaskOrganizer providePipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipTransitionState pipTransitionState, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PhonePipMenuController phonePipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return (PipTaskOrganizer) Preconditions.checkNotNullFromProvides(WMShellModule.providePipTaskOrganizer(context, syncTransactionQueue, pipTransitionState, pipBoundsState, pipBoundsAlgorithm, phonePipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, pipParamsChangedForwarder, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor));
    }
}
