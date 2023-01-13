package com.android.p019wm.shell.dagger;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.view.IWindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.RootDisplayAreaOrganizer;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.ShellCommandHandlerImpl;
import com.android.p019wm.shell.ShellInit;
import com.android.p019wm.shell.ShellInitImpl;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.TaskViewFactory;
import com.android.p019wm.shell.TaskViewFactoryController;
import com.android.p019wm.shell.TaskViewTransitions;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.apppairs.AppPairs;
import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.back.BackAnimationController;
import com.android.p019wm.shell.bubbles.BubbleController;
import com.android.p019wm.shell.bubbles.Bubbles;
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
import com.android.p019wm.shell.common.annotations.ShellAnimationThread;
import com.android.p019wm.shell.common.annotations.ShellBackgroundThread;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.common.annotations.ShellSplashscreenThread;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelperController;
import com.android.p019wm.shell.draganddrop.DragAndDrop;
import com.android.p019wm.shell.draganddrop.DragAndDropController;
import com.android.p019wm.shell.freeform.FreeformTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.p019wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.onehanded.OneHandedController;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.phone.PipTouchHandler;
import com.android.p019wm.shell.recents.RecentTasks;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.p019wm.shell.startingsurface.phone.PhoneStartingWindowTypeAlgorithm;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldTransitionHandler;
import dagger.BindsOptionalOf;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;

@Module(includes = {WMShellConcurrencyModule.class})
/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule */
public abstract class WMShellBaseModule {
    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract AppPairsController optionalAppPairs();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract BubbleController optionalBubblesController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract DisplayImeController optionalDisplayImeController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract FreeformTaskListener optionalFreeformTaskListener();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract FullscreenTaskListener optionalFullscreenTaskListener();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract FullscreenUnfoldController optionalFullscreenUnfoldController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract LegacySplitScreenController optionalLegacySplitScreenController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract OneHandedController optionalOneHandedController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract PipTouchHandler optionalPipTouchHandler();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    public abstract ShellUnfoldProgressProvider optionalShellUnfoldProgressProvider();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract SplitScreenController optionalSplitScreenController();

    /* access modifiers changed from: package-private */
    @BindsOptionalOf
    @DynamicOverride
    public abstract StartingWindowTypeAlgorithm optionalStartingWindowTypeAlgorithm();

    @WMSingleton
    @Provides
    static DisplayController provideDisplayController(Context context, IWindowManager iWindowManager, @ShellMainThread ShellExecutor shellExecutor) {
        return new DisplayController(context, iWindowManager, shellExecutor);
    }

    @WMSingleton
    @Provides
    static DisplayInsetsController provideDisplayInsetsController(IWindowManager iWindowManager, DisplayController displayController, @ShellMainThread ShellExecutor shellExecutor) {
        return new DisplayInsetsController(iWindowManager, displayController, shellExecutor);
    }

    @WMSingleton
    @Provides
    static DisplayImeController provideDisplayImeController(@DynamicOverride Optional<DisplayImeController> optional, IWindowManager iWindowManager, DisplayController displayController, DisplayInsetsController displayInsetsController, @ShellMainThread ShellExecutor shellExecutor, TransactionPool transactionPool) {
        if (optional.isPresent()) {
            return optional.get();
        }
        return new DisplayImeController(iWindowManager, displayController, displayInsetsController, shellExecutor, transactionPool);
    }

    @WMSingleton
    @Provides
    static DisplayLayout provideDisplayLayout() {
        return new DisplayLayout();
    }

    @WMSingleton
    @Provides
    static DragAndDropController provideDragAndDropController(Context context, DisplayController displayController, UiEventLogger uiEventLogger, IconProvider iconProvider, @ShellMainThread ShellExecutor shellExecutor) {
        return new DragAndDropController(context, displayController, uiEventLogger, iconProvider, shellExecutor);
    }

    @WMSingleton
    @Provides
    static Optional<DragAndDrop> provideDragAndDrop(DragAndDropController dragAndDropController) {
        return Optional.m1751of(dragAndDropController.asDragAndDrop());
    }

    @WMSingleton
    @Provides
    static ShellTaskOrganizer provideShellTaskOrganizer(@ShellMainThread ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        return new ShellTaskOrganizer(shellExecutor, context, compatUIController, optional);
    }

    @WMSingleton
    @Provides
    static KidsModeTaskOrganizer provideKidsModeTaskOrganizer(@ShellMainThread ShellExecutor shellExecutor, @ShellMainThread Handler handler, Context context, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, DisplayInsetsController displayInsetsController, Optional<RecentTasksController> optional) {
        return new KidsModeTaskOrganizer(shellExecutor, handler, context, syncTransactionQueue, displayController, displayInsetsController, optional);
    }

    @WMSingleton
    @Provides
    static Optional<CompatUI> provideCompatUI(CompatUIController compatUIController) {
        return Optional.m1751of(compatUIController.asCompatUI());
    }

    @WMSingleton
    @Provides
    static CompatUIController provideCompatUIController(Context context, DisplayController displayController, DisplayInsetsController displayInsetsController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue, @ShellMainThread ShellExecutor shellExecutor, Lazy<Transitions> lazy) {
        return new CompatUIController(context, displayController, displayInsetsController, displayImeController, syncTransactionQueue, shellExecutor, lazy);
    }

    @WMSingleton
    @Provides
    static SyncTransactionQueue provideSyncTransactionQueue(TransactionPool transactionPool, @ShellMainThread ShellExecutor shellExecutor) {
        return new SyncTransactionQueue(transactionPool, shellExecutor);
    }

    @WMSingleton
    @Provides
    static SystemWindows provideSystemWindows(DisplayController displayController, IWindowManager iWindowManager) {
        return new SystemWindows(displayController, iWindowManager);
    }

    @WMSingleton
    @Provides
    static IconProvider provideIconProvider(Context context) {
        return new IconProvider(context);
    }

    @WMSingleton
    @Provides
    static TaskStackListenerImpl providerTaskStackListenerImpl(@ShellMainThread Handler handler) {
        return new TaskStackListenerImpl(handler);
    }

    @WMSingleton
    @Provides
    static TransactionPool provideTransactionPool() {
        return new TransactionPool();
    }

    @WMSingleton
    @Provides
    static WindowManagerShellWrapper provideWindowManagerShellWrapper(@ShellMainThread ShellExecutor shellExecutor) {
        return new WindowManagerShellWrapper(shellExecutor);
    }

    @WMSingleton
    @Provides
    static Optional<BackAnimation> provideBackAnimation(Optional<BackAnimationController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda7());
    }

    @WMSingleton
    @Provides
    static Optional<Bubbles> provideBubbles(Optional<BubbleController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda4());
    }

    @WMSingleton
    @Provides
    static FullscreenTaskListener provideFullscreenTaskListener(@DynamicOverride Optional<FullscreenTaskListener> optional, SyncTransactionQueue syncTransactionQueue, Optional<FullscreenUnfoldController> optional2, Optional<RecentTasksController> optional3) {
        if (optional.isPresent()) {
            return optional.get();
        }
        return new FullscreenTaskListener(syncTransactionQueue, optional2, optional3);
    }

    @WMSingleton
    @Provides
    static Optional<FullscreenUnfoldController> provideFullscreenUnfoldController(@DynamicOverride Optional<FullscreenUnfoldController> optional, Optional<ShellUnfoldProgressProvider> optional2) {
        if (!optional2.isPresent() || optional2.get() == ShellUnfoldProgressProvider.NO_PROVIDER) {
            return Optional.empty();
        }
        return optional;
    }

    @WMSingleton
    @Provides
    static Optional<UnfoldTransitionHandler> provideUnfoldTransitionHandler(Optional<ShellUnfoldProgressProvider> optional, TransactionPool transactionPool, Transitions transitions, @ShellMainThread ShellExecutor shellExecutor) {
        if (optional.isPresent()) {
            return Optional.m1751of(new UnfoldTransitionHandler(optional.get(), transactionPool, shellExecutor, transitions));
        }
        return Optional.empty();
    }

    @WMSingleton
    @Provides
    static Optional<FreeformTaskListener> provideFreeformTaskListener(@DynamicOverride Optional<FreeformTaskListener> optional, Context context) {
        if (FreeformTaskListener.isFreeformEnabled(context)) {
            return optional;
        }
        return Optional.empty();
    }

    @WMSingleton
    @Provides
    static Optional<HideDisplayCutout> provideHideDisplayCutout(Optional<HideDisplayCutoutController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda1());
    }

    @WMSingleton
    @Provides
    static Optional<HideDisplayCutoutController> provideHideDisplayCutoutController(Context context, DisplayController displayController, @ShellMainThread ShellExecutor shellExecutor) {
        return Optional.ofNullable(HideDisplayCutoutController.create(context, displayController, shellExecutor));
    }

    @WMSingleton
    @Provides
    static Optional<OneHanded> provideOneHanded(Optional<OneHandedController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda8());
    }

    @WMSingleton
    @Provides
    static Optional<OneHandedController> providesOneHandedController(@DynamicOverride Optional<OneHandedController> optional) {
        if (SystemProperties.getBoolean(OneHandedController.SUPPORT_ONE_HANDED_MODE, false)) {
            return optional;
        }
        return Optional.empty();
    }

    @WMSingleton
    @Provides
    static Optional<TaskSurfaceHelper> provideTaskSurfaceHelper(Optional<TaskSurfaceHelperController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda6());
    }

    @Provides
    static Optional<TaskSurfaceHelperController> provideTaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, @ShellMainThread ShellExecutor shellExecutor) {
        return Optional.ofNullable(new TaskSurfaceHelperController(shellTaskOrganizer, shellExecutor));
    }

    @WMSingleton
    @Provides
    static FloatingContentCoordinator provideFloatingContentCoordinator() {
        return new FloatingContentCoordinator();
    }

    @WMSingleton
    @Provides
    static PipMediaController providePipMediaController(Context context, @ShellMainThread Handler handler) {
        return new PipMediaController(context, handler);
    }

    @WMSingleton
    @Provides
    static PipSurfaceTransactionHelper providePipSurfaceTransactionHelper() {
        return new PipSurfaceTransactionHelper();
    }

    @WMSingleton
    @Provides
    static PipUiEventLogger providePipUiEventLogger(UiEventLogger uiEventLogger, PackageManager packageManager) {
        return new PipUiEventLogger(uiEventLogger, packageManager);
    }

    @WMSingleton
    @Provides
    static Optional<RecentTasks> provideRecentTasks(Optional<RecentTasksController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda0());
    }

    @WMSingleton
    @Provides
    static Optional<RecentTasksController> provideRecentTasksController(Context context, TaskStackListenerImpl taskStackListenerImpl, @ShellMainThread ShellExecutor shellExecutor) {
        return Optional.ofNullable(RecentTasksController.create(context, taskStackListenerImpl, shellExecutor));
    }

    @WMSingleton
    @Provides
    static ShellTransitions provideRemoteTransitions(Transitions transitions) {
        return transitions.asRemoteTransitions();
    }

    @WMSingleton
    @Provides
    static Transitions provideTransitions(ShellTaskOrganizer shellTaskOrganizer, TransactionPool transactionPool, DisplayController displayController, Context context, @ShellMainThread ShellExecutor shellExecutor, @ShellMainThread Handler handler, @ShellAnimationThread ShellExecutor shellExecutor2) {
        return new Transitions(shellTaskOrganizer, transactionPool, displayController, context, shellExecutor, handler, shellExecutor2);
    }

    @WMSingleton
    @Provides
    static TaskViewTransitions provideTaskViewTransitions(Transitions transitions) {
        return new TaskViewTransitions(transitions);
    }

    @WMSingleton
    @Provides
    static RootTaskDisplayAreaOrganizer provideRootTaskDisplayAreaOrganizer(@ShellMainThread ShellExecutor shellExecutor, Context context) {
        return new RootTaskDisplayAreaOrganizer(shellExecutor, context);
    }

    @WMSingleton
    @Provides
    static RootDisplayAreaOrganizer provideRootDisplayAreaOrganizer(@ShellMainThread ShellExecutor shellExecutor) {
        return new RootDisplayAreaOrganizer(shellExecutor);
    }

    @WMSingleton
    @Provides
    static Optional<DisplayAreaHelper> provideDisplayAreaHelper(@ShellMainThread ShellExecutor shellExecutor, RootDisplayAreaOrganizer rootDisplayAreaOrganizer) {
        return Optional.m1751of(new DisplayAreaHelperController(shellExecutor, rootDisplayAreaOrganizer));
    }

    @WMSingleton
    @Provides
    static Optional<SplitScreen> provideSplitScreen(Optional<SplitScreenController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda2());
    }

    @WMSingleton
    @Provides
    static Optional<SplitScreenController> providesSplitScreenController(@DynamicOverride Optional<SplitScreenController> optional, Context context) {
        if (ActivityTaskManager.supportsSplitScreenMultiWindow(context)) {
            return optional;
        }
        return Optional.empty();
    }

    @WMSingleton
    @Provides
    static Optional<LegacySplitScreen> provideLegacySplitScreen(Optional<LegacySplitScreenController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda5());
    }

    @WMSingleton
    @Provides
    static Optional<AppPairs> provideAppPairs(Optional<AppPairsController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda3());
    }

    @WMSingleton
    @Provides
    static Optional<StartingSurface> provideStartingSurface(StartingWindowController startingWindowController) {
        return Optional.m1751of(startingWindowController.asStartingSurface());
    }

    @WMSingleton
    @Provides
    static StartingWindowController provideStartingWindowController(Context context, @ShellSplashscreenThread ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, IconProvider iconProvider, TransactionPool transactionPool) {
        return new StartingWindowController(context, shellExecutor, startingWindowTypeAlgorithm, iconProvider, transactionPool);
    }

    @WMSingleton
    @Provides
    static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm(@DynamicOverride Optional<StartingWindowTypeAlgorithm> optional) {
        if (optional.isPresent()) {
            return optional.get();
        }
        return new PhoneStartingWindowTypeAlgorithm();
    }

    @WMSingleton
    @Provides
    static Optional<TaskViewFactory> provideTaskViewFactory(TaskViewFactoryController taskViewFactoryController) {
        return Optional.m1751of(taskViewFactoryController.asTaskViewFactory());
    }

    @WMSingleton
    @Provides
    static TaskViewFactoryController provideTaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, @ShellMainThread ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue, TaskViewTransitions taskViewTransitions) {
        return new TaskViewFactoryController(shellTaskOrganizer, shellExecutor, syncTransactionQueue, taskViewTransitions);
    }

    @WMSingleton
    @Provides
    static ShellInit provideShellInit(ShellInitImpl shellInitImpl) {
        return shellInitImpl.asShellInit();
    }

    @WMSingleton
    @Provides
    static ShellInitImpl provideShellInitImpl(DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<BubbleController> optional, Optional<SplitScreenController> optional2, Optional<AppPairsController> optional3, Optional<PipTouchHandler> optional4, FullscreenTaskListener fullscreenTaskListener, Optional<FullscreenUnfoldController> optional5, Optional<UnfoldTransitionHandler> optional6, Optional<FreeformTaskListener> optional7, Optional<RecentTasksController> optional8, Transitions transitions, StartingWindowController startingWindowController, @ShellMainThread ShellExecutor shellExecutor) {
        return new ShellInitImpl(displayController, displayImeController, displayInsetsController, dragAndDropController, shellTaskOrganizer, kidsModeTaskOrganizer, optional, optional2, optional3, optional4, fullscreenTaskListener, optional5, optional6, optional7, optional8, transitions, startingWindowController, shellExecutor);
    }

    @WMSingleton
    @Provides
    static Optional<ShellCommandHandler> provideShellCommandHandler(ShellCommandHandlerImpl shellCommandHandlerImpl) {
        return Optional.m1751of(shellCommandHandlerImpl.asShellCommandHandler());
    }

    @WMSingleton
    @Provides
    static ShellCommandHandlerImpl provideShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, Optional<RecentTasksController> optional7, @ShellMainThread ShellExecutor shellExecutor) {
        return new ShellCommandHandlerImpl(shellTaskOrganizer, kidsModeTaskOrganizer, optional, optional2, optional3, optional4, optional5, optional6, optional7, shellExecutor);
    }

    @WMSingleton
    @Provides
    static Optional<BackAnimationController> provideBackAnimationController(Context context, @ShellMainThread ShellExecutor shellExecutor, @ShellBackgroundThread Handler handler) {
        if (BackAnimationController.IS_ENABLED) {
            return Optional.m1751of(new BackAnimationController(shellExecutor, handler, context));
        }
        return Optional.empty();
    }
}
