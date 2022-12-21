package com.android.p019wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipTransitionState;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsAlgorithm;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import com.android.p019wm.shell.pip.p020tv.TvPipController;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuController;
import com.android.p019wm.shell.pip.p020tv.TvPipNotificationController;
import com.android.p019wm.shell.pip.p020tv.TvPipTaskOrganizer;
import com.android.p019wm.shell.pip.p020tv.TvPipTransition;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.transition.Transitions;
import dagger.Module;
import dagger.Provides;
import java.util.Objects;
import java.util.Optional;

@Module(includes = {WMShellBaseModule.class})
/* renamed from: com.android.wm.shell.dagger.TvPipModule */
public abstract class TvPipModule {
    @WMSingleton
    @Provides
    static Optional<Pip> providePip(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, PipTransitionController pipTransitionController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, @ShellMainThread ShellExecutor shellExecutor) {
        return Optional.m1745of(TvPipController.create(context, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipBoundsController, pipAppOpsListener, pipTaskOrganizer, pipTransitionController, tvPipMenuController, pipMediaController, tvPipNotificationController, taskStackListenerImpl, pipParamsChangedForwarder, displayController, windowManagerShellWrapper, shellExecutor));
    }

    @WMSingleton
    @Provides
    static TvPipBoundsController provideTvPipBoundsController(Context context, @ShellMainThread Handler handler, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm) {
        return new TvPipBoundsController(context, new TvPipModule$$ExternalSyntheticLambda0(), handler, tvPipBoundsState, tvPipBoundsAlgorithm);
    }

    @WMSingleton
    @Provides
    static PipSnapAlgorithm providePipSnapAlgorithm() {
        return new PipSnapAlgorithm();
    }

    @WMSingleton
    @Provides
    static TvPipBoundsAlgorithm provideTvPipBoundsAlgorithm(Context context, TvPipBoundsState tvPipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return new TvPipBoundsAlgorithm(context, tvPipBoundsState, pipSnapAlgorithm);
    }

    @WMSingleton
    @Provides
    static TvPipBoundsState provideTvPipBoundsState(Context context) {
        return new TvPipBoundsState(context);
    }

    @WMSingleton
    @Provides
    static PipTransitionController provideTvPipTransition(Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsState tvPipBoundsState, TvPipMenuController tvPipMenuController) {
        return new TvPipTransition(tvPipBoundsState, tvPipMenuController, tvPipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer);
    }

    @WMSingleton
    @Provides
    static TvPipMenuController providesTvPipMenuController(Context context, TvPipBoundsState tvPipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, @ShellMainThread Handler handler) {
        return new TvPipMenuController(context, tvPipBoundsState, systemWindows, pipMediaController, handler);
    }

    @WMSingleton
    @Provides
    static TvPipNotificationController provideTvPipNotificationController(Context context, PipMediaController pipMediaController, PipParamsChangedForwarder pipParamsChangedForwarder, TvPipBoundsState tvPipBoundsState, @ShellMainThread Handler handler) {
        return new TvPipNotificationController(context, pipMediaController, pipParamsChangedForwarder, tvPipBoundsState, handler);
    }

    @WMSingleton
    @Provides
    static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return new PipAnimationController(pipSurfaceTransactionHelper);
    }

    @WMSingleton
    @Provides
    static PipTransitionState providePipTransitionState() {
        return new PipTransitionState();
    }

    @WMSingleton
    @Provides
    static PipTaskOrganizer providePipTaskOrganizer(Context context, TvPipMenuController tvPipMenuController, SyncTransactionQueue syncTransactionQueue, TvPipBoundsState tvPipBoundsState, PipTransitionState pipTransitionState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, PipAnimationController pipAnimationController, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, @ShellMainThread ShellExecutor shellExecutor) {
        return new TvPipTaskOrganizer(context, syncTransactionQueue, pipTransitionState, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, pipParamsChangedForwarder, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor);
    }

    @WMSingleton
    @Provides
    static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return new PipParamsChangedForwarder();
    }

    @WMSingleton
    @Provides
    static PipAppOpsListener providePipAppOpsListener(Context context, PipTaskOrganizer pipTaskOrganizer, @ShellMainThread ShellExecutor shellExecutor) {
        Objects.requireNonNull(pipTaskOrganizer);
        return new PipAppOpsListener(context, new TvPipModule$$ExternalSyntheticLambda1(pipTaskOrganizer), shellExecutor);
    }
}
