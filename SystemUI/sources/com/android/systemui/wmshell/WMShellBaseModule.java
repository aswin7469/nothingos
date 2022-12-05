package com.android.systemui.wmshell;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.IWindowManager;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.wm.shell.FullscreenTaskListener;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import java.util.Optional;
/* loaded from: classes2.dex */
public abstract class WMShellBaseModule {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayController provideDisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        return new DisplayController(context, iWindowManager, shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayLayout provideDisplayLayout() {
        return new DisplayLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DragAndDropController provideDragAndDropController(Context context, DisplayController displayController) {
        return new DragAndDropController(context, displayController);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShellTaskOrganizer provideShellTaskOrganizer(ShellExecutor shellExecutor, Context context, SizeCompatUIController sizeCompatUIController) {
        return new ShellTaskOrganizer(shellExecutor, context, sizeCompatUIController);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SizeCompatUIController provideSizeCompatUIController(Context context, DisplayController displayController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue) {
        return new SizeCompatUIController(context, displayController, displayImeController, syncTransactionQueue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SyncTransactionQueue provideSyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        return new SyncTransactionQueue(transactionPool, shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SystemWindows provideSystemWindows(DisplayController displayController, IWindowManager iWindowManager) {
        return new SystemWindows(displayController, iWindowManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskStackListenerImpl providerTaskStackListenerImpl(Handler handler) {
        return new TaskStackListenerImpl(handler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TransactionPool provideTransactionPool() {
        return new TransactionPool();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WindowManagerShellWrapper provideWindowManagerShellWrapper(ShellExecutor shellExecutor) {
        return new WindowManagerShellWrapper(shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<Bubbles> provideBubbles(Optional<BubbleController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda1.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<BubbleController> provideBubbleController(Context context, FloatingContentCoordinator floatingContentCoordinator, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, LauncherApps launcherApps, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, ShellExecutor shellExecutor, Handler handler) {
        return Optional.of(BubbleController.create(context, null, floatingContentCoordinator, iStatusBarService, windowManager, windowManagerShellWrapper, launcherApps, taskStackListenerImpl, uiEventLogger, shellTaskOrganizer, displayController, shellExecutor, handler));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FullscreenTaskListener provideFullscreenTaskListener(SyncTransactionQueue syncTransactionQueue) {
        return new FullscreenTaskListener(syncTransactionQueue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<HideDisplayCutout> provideHideDisplayCutout(Optional<HideDisplayCutoutController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda2.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<HideDisplayCutoutController> provideHideDisplayCutoutController(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        return Optional.ofNullable(HideDisplayCutoutController.create(context, displayController, shellExecutor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<OneHanded> provideOneHanded(Optional<OneHandedController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda4.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<OneHandedController> provideOneHandedController(Context context, WindowManager windowManager, DisplayController displayController, DisplayLayout displayLayout, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        return Optional.ofNullable(OneHandedController.create(context, windowManager, displayController, displayLayout, taskStackListenerImpl, uiEventLogger, shellExecutor, handler));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<TaskSurfaceHelper> provideTaskSurfaceHelper(Optional<TaskSurfaceHelperController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda6.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<TaskSurfaceHelperController> provideTaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return Optional.ofNullable(new TaskSurfaceHelperController(shellTaskOrganizer, shellExecutor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FloatingContentCoordinator provideFloatingContentCoordinator() {
        return new FloatingContentCoordinator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PipAppOpsListener providePipAppOpsListener(Context context, PipTouchHandler pipTouchHandler, ShellExecutor shellExecutor) {
        return new PipAppOpsListener(context, pipTouchHandler.getMotionHelper(), shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PipMediaController providePipMediaController(Context context, Handler handler) {
        return new PipMediaController(context, handler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PipSurfaceTransactionHelper providePipSurfaceTransactionHelper() {
        return new PipSurfaceTransactionHelper();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PipUiEventLogger providePipUiEventLogger(UiEventLogger uiEventLogger, PackageManager packageManager) {
        return new PipUiEventLogger(uiEventLogger, packageManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShellTransitions provideRemoteTransitions(Transitions transitions) {
        return transitions.asRemoteTransitions();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Transitions provideTransitions(ShellTaskOrganizer shellTaskOrganizer, TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        return new Transitions(shellTaskOrganizer, transactionPool, context, shellExecutor, shellExecutor2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RootTaskDisplayAreaOrganizer provideRootTaskDisplayAreaOrganizer(ShellExecutor shellExecutor, Context context) {
        return new RootTaskDisplayAreaOrganizer(shellExecutor, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<SplitScreen> provideSplitScreen(Optional<SplitScreenController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda5.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<SplitScreenController> provideSplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayImeController displayImeController, Transitions transitions, TransactionPool transactionPool) {
        if (ActivityTaskManager.supportsSplitScreenMultiWindow(context)) {
            return Optional.of(new SplitScreenController(shellTaskOrganizer, syncTransactionQueue, context, rootTaskDisplayAreaOrganizer, shellExecutor, displayImeController, transitions, transactionPool));
        }
        return Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<LegacySplitScreen> provideLegacySplitScreen(Optional<LegacySplitScreenController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda3.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<AppPairs> provideAppPairs(Optional<AppPairsController> optional) {
        return optional.map(WMShellBaseModule$$ExternalSyntheticLambda0.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<StartingSurface> provideStartingSurface(StartingWindowController startingWindowController) {
        return Optional.of(startingWindowController.asStartingSurface());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static StartingWindowController provideStartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, TransactionPool transactionPool) {
        return new StartingWindowController(context, shellExecutor, startingWindowTypeAlgorithm, transactionPool);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<TaskViewFactory> provideTaskViewFactory(TaskViewFactoryController taskViewFactoryController) {
        return Optional.of(taskViewFactoryController.asTaskViewFactory());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskViewFactoryController provideTaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return new TaskViewFactoryController(shellTaskOrganizer, shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShellInit provideShellInit(ShellInitImpl shellInitImpl) {
        return shellInitImpl.asShellInit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShellInitImpl provideShellInitImpl(DisplayImeController displayImeController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, Optional<BubbleController> optional, Optional<LegacySplitScreenController> optional2, Optional<SplitScreenController> optional3, Optional<AppPairsController> optional4, Optional<PipTouchHandler> optional5, FullscreenTaskListener fullscreenTaskListener, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        return new ShellInitImpl(displayImeController, dragAndDropController, shellTaskOrganizer, optional, optional2, optional3, optional4, optional5, fullscreenTaskListener, transitions, startingWindowController, shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<ShellCommandHandler> provideShellCommandHandler(ShellCommandHandlerImpl shellCommandHandlerImpl) {
        return Optional.of(shellCommandHandlerImpl.asShellCommandHandler());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShellCommandHandlerImpl provideShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, ShellExecutor shellExecutor) {
        return new ShellCommandHandlerImpl(shellTaskOrganizer, optional, optional2, optional3, optional4, optional5, optional6, shellExecutor);
    }
}
