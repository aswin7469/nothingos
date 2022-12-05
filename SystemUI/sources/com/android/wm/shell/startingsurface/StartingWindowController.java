package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Slog;
import android.util.SparseIntArray;
import android.view.SurfaceControl;
import android.window.StartingWindowInfo;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.TriConsumer;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.IStartingWindow;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class StartingWindowController implements RemoteCallable<StartingWindowController> {
    public static final boolean DEBUG_SPLASH_SCREEN = Build.isDebuggable();
    private static final String TAG = "StartingWindowController";
    private final Context mContext;
    private final ShellExecutor mSplashScreenExecutor;
    private final StartingSurfaceDrawer mStartingSurfaceDrawer;
    private final StartingWindowTypeAlgorithm mStartingWindowTypeAlgorithm;
    private TriConsumer<Integer, Integer, Integer> mTaskLaunchingCallback;
    private final StartingSurfaceImpl mImpl = new StartingSurfaceImpl();
    @GuardedBy({"mTaskBackgroundColors"})
    private final SparseIntArray mTaskBackgroundColors = new SparseIntArray();

    private static boolean isSplashScreenType(@StartingWindowInfo.StartingWindowType int i) {
        return i == 1 || i == 3 || i == 4;
    }

    public StartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, TransactionPool transactionPool) {
        this.mContext = context;
        this.mStartingSurfaceDrawer = new StartingSurfaceDrawer(context, shellExecutor, transactionPool);
        this.mStartingWindowTypeAlgorithm = startingWindowTypeAlgorithm;
        this.mSplashScreenExecutor = shellExecutor;
    }

    public StartingSurface asStartingSurface() {
        return this.mImpl;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public ShellExecutor getRemoteCallExecutor() {
        return this.mSplashScreenExecutor;
    }

    void setStartingWindowListener(TriConsumer<Integer, Integer, Integer> triConsumer) {
        this.mTaskLaunchingCallback = triConsumer;
    }

    public void addStartingWindow(final StartingWindowInfo startingWindowInfo, final IBinder iBinder) {
        this.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$addStartingWindow$0(startingWindowInfo, iBinder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addStartingWindow$0(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        Trace.traceBegin(32L, "addStartingWindow");
        int suggestedWindowType = this.mStartingWindowTypeAlgorithm.getSuggestedWindowType(startingWindowInfo);
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo.taskInfo;
        if (isSplashScreenType(suggestedWindowType)) {
            this.mStartingSurfaceDrawer.addSplashScreenStartingWindow(startingWindowInfo, iBinder, suggestedWindowType);
        } else if (suggestedWindowType == 2) {
            this.mStartingSurfaceDrawer.makeTaskSnapshotWindow(startingWindowInfo, iBinder, startingWindowInfo.mTaskSnapshot);
        }
        if (suggestedWindowType != 0) {
            int i = runningTaskInfo.taskId;
            int startingWindowBackgroundColorForTask = this.mStartingSurfaceDrawer.getStartingWindowBackgroundColorForTask(i);
            if (startingWindowBackgroundColorForTask != 0) {
                synchronized (this.mTaskBackgroundColors) {
                    this.mTaskBackgroundColors.append(i, startingWindowBackgroundColorForTask);
                }
            }
            if (this.mTaskLaunchingCallback != null && isSplashScreenType(suggestedWindowType)) {
                this.mTaskLaunchingCallback.accept(Integer.valueOf(i), Integer.valueOf(suggestedWindowType), Integer.valueOf(startingWindowBackgroundColorForTask));
            }
        }
        Trace.traceEnd(32L);
    }

    public void copySplashScreenView(final int i) {
        this.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$copySplashScreenView$1(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$1(int i) {
        this.mStartingSurfaceDrawer.copySplashScreenView(i);
    }

    public void onAppSplashScreenViewRemoved(final int i) {
        this.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$onAppSplashScreenViewRemoved$2(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAppSplashScreenViewRemoved$2(int i) {
        this.mStartingSurfaceDrawer.onAppSplashScreenViewRemoved(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onImeDrawnOnTask$3(int i) {
        this.mStartingSurfaceDrawer.onImeDrawnOnTask(i);
    }

    public void onImeDrawnOnTask(final int i) {
        this.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$onImeDrawnOnTask$3(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStartingWindow$4(int i, SurfaceControl surfaceControl, Rect rect, boolean z) {
        this.mStartingSurfaceDrawer.removeStartingWindow(i, surfaceControl, rect, z);
    }

    public void removeStartingWindow(final int i, final SurfaceControl surfaceControl, final Rect rect, final boolean z) {
        this.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$removeStartingWindow$4(i, surfaceControl, rect, z);
            }
        });
        this.mSplashScreenExecutor.executeDelayed(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$removeStartingWindow$5(i);
            }
        }, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStartingWindow$5(int i) {
        synchronized (this.mTaskBackgroundColors) {
            this.mTaskBackgroundColors.delete(i);
        }
    }

    /* loaded from: classes2.dex */
    private class StartingSurfaceImpl implements StartingSurface {
        private IStartingWindowImpl mIStartingWindow;

        private StartingSurfaceImpl() {
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        /* renamed from: createExternalInterface  reason: collision with other method in class */
        public IStartingWindowImpl mo1783createExternalInterface() {
            IStartingWindowImpl iStartingWindowImpl = this.mIStartingWindow;
            if (iStartingWindowImpl != null) {
                iStartingWindowImpl.invalidate();
            }
            IStartingWindowImpl iStartingWindowImpl2 = new IStartingWindowImpl(StartingWindowController.this);
            this.mIStartingWindow = iStartingWindowImpl2;
            return iStartingWindowImpl2;
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        public int getBackgroundColor(TaskInfo taskInfo) {
            synchronized (StartingWindowController.this.mTaskBackgroundColors) {
                int indexOfKey = StartingWindowController.this.mTaskBackgroundColors.indexOfKey(taskInfo.taskId);
                if (indexOfKey >= 0) {
                    return StartingWindowController.this.mTaskBackgroundColors.valueAt(indexOfKey);
                }
                int estimateTaskBackgroundColor = StartingWindowController.this.mStartingSurfaceDrawer.estimateTaskBackgroundColor(taskInfo);
                return estimateTaskBackgroundColor != 0 ? estimateTaskBackgroundColor : SplashscreenContentDrawer.getSystemBGColor();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class IStartingWindowImpl extends IStartingWindow.Stub {
        private StartingWindowController mController;
        private IStartingWindowListener mListener;
        private final TriConsumer<Integer, Integer, Integer> mStartingWindowListener = new TriConsumer() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartingWindowController.IStartingWindowImpl.this.notifyIStartingWindowListener(((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue());
            }
        };
        private final IBinder.DeathRecipient mListenerDeathRecipient = new AnonymousClass1();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements IBinder.DeathRecipient {
            AnonymousClass1() {
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                final StartingWindowController startingWindowController = IStartingWindowImpl.this.mController;
                startingWindowController.getRemoteCallExecutor().execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        StartingWindowController.IStartingWindowImpl.AnonymousClass1.this.lambda$binderDied$0(startingWindowController);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$binderDied$0(StartingWindowController startingWindowController) {
                IStartingWindowImpl.this.mListener = null;
                startingWindowController.setStartingWindowListener(null);
            }
        }

        public IStartingWindowImpl(StartingWindowController startingWindowController) {
            this.mController = startingWindowController;
        }

        void invalidate() {
            this.mController = null;
        }

        @Override // com.android.wm.shell.startingsurface.IStartingWindow
        public void setStartingWindowListener(final IStartingWindowListener iStartingWindowListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setStartingWindowListener", new Consumer() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    StartingWindowController.IStartingWindowImpl.this.lambda$setStartingWindowListener$0(iStartingWindowListener, (StartingWindowController) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setStartingWindowListener$0(IStartingWindowListener iStartingWindowListener, StartingWindowController startingWindowController) {
            IStartingWindowListener iStartingWindowListener2 = this.mListener;
            if (iStartingWindowListener2 != null) {
                iStartingWindowListener2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            if (iStartingWindowListener != null) {
                try {
                    iStartingWindowListener.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
                } catch (RemoteException unused) {
                    Slog.e(StartingWindowController.TAG, "Failed to link to death");
                    return;
                }
            }
            this.mListener = iStartingWindowListener;
            startingWindowController.setStartingWindowListener(this.mStartingWindowListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyIStartingWindowListener(int i, int i2, int i3) {
            IStartingWindowListener iStartingWindowListener = this.mListener;
            if (iStartingWindowListener == null) {
                return;
            }
            try {
                iStartingWindowListener.onTaskLaunching(i, i2, i3);
            } catch (RemoteException e) {
                Slog.e(StartingWindowController.TAG, "Failed to notify task launching", e);
            }
        }
    }
}
