package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsAlgorithm;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuController;
import com.android.p019wm.shell.pip.p020tv.TvPipNotificationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidePipFactory */
public final class TvPipModule_ProvidePipFactory implements Factory<Optional<Pip>> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PipAppOpsListener> pipAppOpsListenerProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<PipParamsChangedForwarder> pipParamsChangedForwarderProvider;
    private final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;
    private final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    private final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    private final Provider<TvPipBoundsController> tvPipBoundsControllerProvider;
    private final Provider<TvPipBoundsState> tvPipBoundsStateProvider;
    private final Provider<TvPipMenuController> tvPipMenuControllerProvider;
    private final Provider<TvPipNotificationController> tvPipNotificationControllerProvider;
    private final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public TvPipModule_ProvidePipFactory(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<TvPipBoundsAlgorithm> provider3, Provider<TvPipBoundsController> provider4, Provider<PipAppOpsListener> provider5, Provider<PipTaskOrganizer> provider6, Provider<TvPipMenuController> provider7, Provider<PipMediaController> provider8, Provider<PipTransitionController> provider9, Provider<TvPipNotificationController> provider10, Provider<TaskStackListenerImpl> provider11, Provider<PipParamsChangedForwarder> provider12, Provider<DisplayController> provider13, Provider<WindowManagerShellWrapper> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.tvPipBoundsAlgorithmProvider = provider3;
        this.tvPipBoundsControllerProvider = provider4;
        this.pipAppOpsListenerProvider = provider5;
        this.pipTaskOrganizerProvider = provider6;
        this.tvPipMenuControllerProvider = provider7;
        this.pipMediaControllerProvider = provider8;
        this.pipTransitionControllerProvider = provider9;
        this.tvPipNotificationControllerProvider = provider10;
        this.taskStackListenerProvider = provider11;
        this.pipParamsChangedForwarderProvider = provider12;
        this.displayControllerProvider = provider13;
        this.windowManagerShellWrapperProvider = provider14;
        this.mainExecutorProvider = provider15;
    }

    public Optional<Pip> get() {
        return providePip(this.contextProvider.get(), this.tvPipBoundsStateProvider.get(), this.tvPipBoundsAlgorithmProvider.get(), this.tvPipBoundsControllerProvider.get(), this.pipAppOpsListenerProvider.get(), this.pipTaskOrganizerProvider.get(), this.tvPipMenuControllerProvider.get(), this.pipMediaControllerProvider.get(), this.pipTransitionControllerProvider.get(), this.tvPipNotificationControllerProvider.get(), this.taskStackListenerProvider.get(), this.pipParamsChangedForwarderProvider.get(), this.displayControllerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipFactory create(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<TvPipBoundsAlgorithm> provider3, Provider<TvPipBoundsController> provider4, Provider<PipAppOpsListener> provider5, Provider<PipTaskOrganizer> provider6, Provider<TvPipMenuController> provider7, Provider<PipMediaController> provider8, Provider<PipTransitionController> provider9, Provider<TvPipNotificationController> provider10, Provider<TaskStackListenerImpl> provider11, Provider<PipParamsChangedForwarder> provider12, Provider<DisplayController> provider13, Provider<WindowManagerShellWrapper> provider14, Provider<ShellExecutor> provider15) {
        return new TvPipModule_ProvidePipFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static Optional<Pip> providePip(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, PipTransitionController pipTransitionController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(TvPipModule.providePip(context, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipBoundsController, pipAppOpsListener, pipTaskOrganizer, tvPipMenuController, pipMediaController, pipTransitionController, tvPipNotificationController, taskStackListenerImpl, pipParamsChangedForwarder, displayController, windowManagerShellWrapper, shellExecutor));
    }
}
