package com.android.p019wm.shell;

import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.bubbles.BubbleController;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import com.android.p019wm.shell.draganddrop.DragAndDropController;
import com.android.p019wm.shell.freeform.FreeformTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.p019wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.p019wm.shell.pip.phone.PipTouchHandler;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.UnfoldTransitionHandler;
import java.util.Optional;

/* renamed from: com.android.wm.shell.ShellInitImpl */
public class ShellInitImpl {
    private static final String TAG = "ShellInitImpl";
    private final Optional<AppPairsController> mAppPairsOptional;
    private final Optional<BubbleController> mBubblesOptional;
    private final DisplayController mDisplayController;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final DragAndDropController mDragAndDropController;
    private final Optional<FreeformTaskListener> mFreeformTaskListenerOptional;
    private final FullscreenTaskListener mFullscreenTaskListener;
    private final Optional<FullscreenUnfoldController> mFullscreenUnfoldController;
    private final InitImpl mImpl = new InitImpl();
    private final KidsModeTaskOrganizer mKidsModeTaskOrganizer;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final Optional<PipTouchHandler> mPipTouchHandlerOptional;
    private final Optional<RecentTasksController> mRecentTasks;
    private final ShellTaskOrganizer mShellTaskOrganizer;
    private final Optional<SplitScreenController> mSplitScreenOptional;
    private final StartingWindowController mStartingWindow;
    private final Transitions mTransitions;
    private final Optional<UnfoldTransitionHandler> mUnfoldTransitionHandler;

    public ShellInitImpl(DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<BubbleController> optional, Optional<SplitScreenController> optional2, Optional<AppPairsController> optional3, Optional<PipTouchHandler> optional4, FullscreenTaskListener fullscreenTaskListener, Optional<FullscreenUnfoldController> optional5, Optional<UnfoldTransitionHandler> optional6, Optional<FreeformTaskListener> optional7, Optional<RecentTasksController> optional8, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mDragAndDropController = dragAndDropController;
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mKidsModeTaskOrganizer = kidsModeTaskOrganizer;
        this.mBubblesOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mAppPairsOptional = optional3;
        this.mFullscreenTaskListener = fullscreenTaskListener;
        this.mPipTouchHandlerOptional = optional4;
        this.mFullscreenUnfoldController = optional5;
        this.mUnfoldTransitionHandler = optional6;
        this.mFreeformTaskListenerOptional = optional7;
        this.mRecentTasks = optional8;
        this.mTransitions = transitions;
        this.mMainExecutor = shellExecutor;
        this.mStartingWindow = startingWindowController;
    }

    public ShellInit asShellInit() {
        return this.mImpl;
    }

    /* access modifiers changed from: private */
    public void init() {
        this.mDisplayController.initialize();
        this.mDisplayInsetsController.initialize();
        this.mDisplayImeController.startMonitorDisplays();
        this.mShellTaskOrganizer.addListenerForType(this.mFullscreenTaskListener, -2);
        this.mShellTaskOrganizer.initStartingWindow(this.mStartingWindow);
        this.mShellTaskOrganizer.registerOrganizer();
        this.mAppPairsOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda0());
        this.mSplitScreenOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda1());
        this.mBubblesOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda2());
        this.mDragAndDropController.initialize(this.mSplitScreenOptional);
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mTransitions.register(this.mShellTaskOrganizer);
            this.mUnfoldTransitionHandler.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda3());
        }
        this.mPipTouchHandlerOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda4());
        this.mFreeformTaskListenerOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda5(this));
        this.mFullscreenUnfoldController.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda6());
        this.mRecentTasks.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda7());
        this.mKidsModeTaskOrganizer.initialize(this.mStartingWindow);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$init$1$com-android-wm-shell-ShellInitImpl  reason: not valid java name */
    public /* synthetic */ void m3375lambda$init$1$comandroidwmshellShellInitImpl(FreeformTaskListener freeformTaskListener) {
        this.mShellTaskOrganizer.addListenerForType(freeformTaskListener, -5);
    }

    @ExternalThread
    /* renamed from: com.android.wm.shell.ShellInitImpl$InitImpl */
    private class InitImpl implements ShellInit {
        private InitImpl() {
        }

        public void init() {
            try {
                ShellInitImpl.this.mMainExecutor.executeBlocking(new ShellInitImpl$InitImpl$$ExternalSyntheticLambda0(this));
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to initialize the Shell in 2s", e);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$init$0$com-android-wm-shell-ShellInitImpl$InitImpl  reason: not valid java name */
        public /* synthetic */ void m3376lambda$init$0$comandroidwmshellShellInitImpl$InitImpl() {
            ShellInitImpl.this.init();
        }
    }
}
