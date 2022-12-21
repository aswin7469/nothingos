package com.android.p019wm.shell.dagger;

import android.animation.AnimationHandler;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.UserManager;
import android.view.WindowManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.TaskViewTransitions;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.bubbles.BubbleController;
import com.android.p019wm.shell.bubbles.BubbleStackView;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.FloatingContentCoordinator;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.annotations.ChoreographerSfVsync;
import com.android.p019wm.shell.common.annotations.ShellBackgroundThread;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.draganddrop.DragAndDropController;
import com.android.p019wm.shell.freeform.FreeformTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.p019wm.shell.onehanded.OneHandedController;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipBoundsAlgorithm;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransition;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipTransitionState;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.phone.PhonePipMenuController;
import com.android.p019wm.shell.pip.phone.PipController;
import com.android.p019wm.shell.pip.phone.PipMotionHelper;
import com.android.p019wm.shell.pip.phone.PipTouchHandler;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldBackgroundController;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Provider;

@Module(includes = {WMShellBaseModule.class})
/* renamed from: com.android.wm.shell.dagger.WMShellModule */
public class WMShellModule {
    @WMSingleton
    @Provides
    static BubbleController provideBubbleController(Context context, FloatingContentCoordinator floatingContentCoordinator, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, UserManager userManager, LauncherApps launcherApps, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, @DynamicOverride Optional<OneHandedController> optional, DragAndDropController dragAndDropController, @ShellMainThread ShellExecutor shellExecutor, @ShellMainThread Handler handler, @ShellBackgroundThread ShellExecutor shellExecutor2, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        return BubbleController.create(context, (BubbleStackView.SurfaceSynchronizer) null, floatingContentCoordinator, iStatusBarService, windowManager, windowManagerShellWrapper, userManager, launcherApps, taskStackListenerImpl, uiEventLogger, shellTaskOrganizer, displayController, optional, dragAndDropController, shellExecutor, handler, shellExecutor2, taskViewTransitions, syncTransactionQueue);
    }

    @WMSingleton
    @DynamicOverride
    @Provides
    static FreeformTaskListener provideFreeformTaskListener(SyncTransactionQueue syncTransactionQueue) {
        return new FreeformTaskListener(syncTransactionQueue);
    }

    @WMSingleton
    @DynamicOverride
    @Provides
    static OneHandedController provideOneHandedController(Context context, WindowManager windowManager, DisplayController displayController, DisplayLayout displayLayout, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, InteractionJankMonitor interactionJankMonitor, @ShellMainThread ShellExecutor shellExecutor, @ShellMainThread Handler handler) {
        return OneHandedController.create(context, windowManager, displayController, displayLayout, taskStackListenerImpl, interactionJankMonitor, uiEventLogger, shellExecutor, handler);
    }

    @WMSingleton
    @DynamicOverride
    @Provides
    static SplitScreenController provideSplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, @ShellMainThread ShellExecutor shellExecutor, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, IconProvider iconProvider, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        return new SplitScreenController(shellTaskOrganizer, syncTransactionQueue, context, rootTaskDisplayAreaOrganizer, shellExecutor, displayController, displayImeController, displayInsetsController, transitions, transactionPool, iconProvider, optional, provider);
    }

    @WMSingleton
    @Provides
    static LegacySplitScreenController provideLegacySplitScreen(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, @ShellMainThread ShellExecutor shellExecutor, @ChoreographerSfVsync AnimationHandler animationHandler) {
        return new LegacySplitScreenController(context, displayController, systemWindows, displayImeController, transactionPool, shellTaskOrganizer, syncTransactionQueue, taskStackListenerImpl, transitions, shellExecutor, animationHandler);
    }

    @WMSingleton
    @Provides
    static AppPairsController provideAppPairs(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, @ShellMainThread ShellExecutor shellExecutor, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController) {
        return new AppPairsController(shellTaskOrganizer, syncTransactionQueue, displayController, shellExecutor, displayImeController, displayInsetsController);
    }

    @WMSingleton
    @Provides
    static Optional<Pip> providePip(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<OneHandedController> optional, @ShellMainThread ShellExecutor shellExecutor) {
        return Optional.ofNullable(PipController.create(context, displayController, pipAppOpsListener, pipBoundsAlgorithm, pipBoundsState, pipMediaController, phonePipMenuController, pipTaskOrganizer, pipTouchHandler, pipTransitionController, windowManagerShellWrapper, taskStackListenerImpl, pipParamsChangedForwarder, optional, shellExecutor));
    }

    @WMSingleton
    @Provides
    static PipBoundsState providePipBoundsState(Context context) {
        return new PipBoundsState(context);
    }

    @WMSingleton
    @Provides
    static PipSnapAlgorithm providePipSnapAlgorithm() {
        return new PipSnapAlgorithm();
    }

    @WMSingleton
    @Provides
    static PipBoundsAlgorithm providesPipBoundsAlgorithm(Context context, PipBoundsState pipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return new PipBoundsAlgorithm(context, pipBoundsState, pipSnapAlgorithm);
    }

    @WMSingleton
    @Provides
    static PhonePipMenuController providesPipPhoneMenuController(Context context, PipBoundsState pipBoundsState, PipMediaController pipMediaController, SystemWindows systemWindows, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger, @ShellMainThread ShellExecutor shellExecutor, @ShellMainThread Handler handler) {
        return new PhonePipMenuController(context, pipBoundsState, pipMediaController, systemWindows, optional, pipUiEventLogger, shellExecutor, handler);
    }

    @WMSingleton
    @Provides
    static PipTouchHandler providePipTouchHandler(Context context, PhonePipMenuController phonePipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PipMotionHelper pipMotionHelper, FloatingContentCoordinator floatingContentCoordinator, PipUiEventLogger pipUiEventLogger, @ShellMainThread ShellExecutor shellExecutor) {
        return new PipTouchHandler(context, phonePipMenuController, pipBoundsAlgorithm, pipBoundsState, pipTaskOrganizer, pipMotionHelper, floatingContentCoordinator, pipUiEventLogger, shellExecutor);
    }

    @WMSingleton
    @Provides
    static PipTransitionState providePipTransitionState() {
        return new PipTransitionState();
    }

    @WMSingleton
    @Provides
    static PipTaskOrganizer providePipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipTransitionState pipTransitionState, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PhonePipMenuController phonePipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, @ShellMainThread ShellExecutor shellExecutor) {
        return new PipTaskOrganizer(context, syncTransactionQueue, pipTransitionState, pipBoundsState, pipBoundsAlgorithm, phonePipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, pipParamsChangedForwarder, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor);
    }

    @WMSingleton
    @Provides
    static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return new PipAnimationController(pipSurfaceTransactionHelper);
    }

    @WMSingleton
    @Provides
    static PipTransitionController providePipTransitionController(Context context, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipTransitionState pipTransitionState, PhonePipMenuController phonePipMenuController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<SplitScreenController> optional) {
        return new PipTransition(context, pipBoundsState, pipTransitionState, phonePipMenuController, pipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer, pipSurfaceTransactionHelper, optional);
    }

    @WMSingleton
    @Provides
    static PipAppOpsListener providePipAppOpsListener(Context context, PipTouchHandler pipTouchHandler, @ShellMainThread ShellExecutor shellExecutor) {
        return new PipAppOpsListener(context, pipTouchHandler.getMotionHelper(), shellExecutor);
    }

    @WMSingleton
    @Provides
    static PipMotionHelper providePipMotionHelper(Context context, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PhonePipMenuController phonePipMenuController, PipSnapAlgorithm pipSnapAlgorithm, PipTransitionController pipTransitionController, FloatingContentCoordinator floatingContentCoordinator) {
        return new PipMotionHelper(context, pipBoundsState, pipTaskOrganizer, phonePipMenuController, pipSnapAlgorithm, pipTransitionController, floatingContentCoordinator);
    }

    @WMSingleton
    @DynamicOverride
    @Provides
    static FullscreenUnfoldController provideFullscreenUnfoldController(Context context, Optional<ShellUnfoldProgressProvider> optional, Lazy<UnfoldBackgroundController> lazy, DisplayInsetsController displayInsetsController, @ShellMainThread ShellExecutor shellExecutor) {
        return new FullscreenUnfoldController(context, shellExecutor, lazy.get(), optional.get(), displayInsetsController);
    }

    @Provides
    static Optional<StageTaskUnfoldController> provideStageTaskUnfoldController(Optional<ShellUnfoldProgressProvider> optional, Context context, TransactionPool transactionPool, Lazy<UnfoldBackgroundController> lazy, DisplayInsetsController displayInsetsController, @ShellMainThread ShellExecutor shellExecutor) {
        return optional.map(new WMShellModule$$ExternalSyntheticLambda0(context, transactionPool, displayInsetsController, lazy, shellExecutor));
    }

    static /* synthetic */ StageTaskUnfoldController lambda$provideStageTaskUnfoldController$0(Context context, TransactionPool transactionPool, DisplayInsetsController displayInsetsController, Lazy lazy, ShellExecutor shellExecutor, ShellUnfoldProgressProvider shellUnfoldProgressProvider) {
        return new StageTaskUnfoldController(context, transactionPool, shellUnfoldProgressProvider, displayInsetsController, (UnfoldBackgroundController) lazy.get(), shellExecutor);
    }

    @WMSingleton
    @Provides
    static UnfoldBackgroundController provideUnfoldBackgroundController(RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, Context context) {
        return new UnfoldBackgroundController(context, rootTaskDisplayAreaOrganizer);
    }

    @WMSingleton
    @Provides
    static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return new PipParamsChangedForwarder();
    }
}
