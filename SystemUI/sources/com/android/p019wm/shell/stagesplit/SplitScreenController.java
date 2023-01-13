package com.android.p019wm.shell.stagesplit;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.RemoteTransition;
import android.window.WindowContainerTransaction;
import com.android.internal.logging.InstanceId;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ExecutorUtils;
import com.android.p019wm.shell.common.RemoteCallable;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import com.android.p019wm.shell.common.split.SplitLayout;
import com.android.p019wm.shell.draganddrop.DragAndDropPolicy;
import com.android.p019wm.shell.stagesplit.ISplitScreen;
import com.android.p019wm.shell.stagesplit.SplitScreen;
import com.android.p019wm.shell.transition.LegacyTransitions;
import com.android.p019wm.shell.transition.Transitions;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenController */
public class SplitScreenController implements DragAndDropPolicy.Starter, RemoteCallable<SplitScreenController> {
    /* access modifiers changed from: private */
    public static final String TAG = "SplitScreenController";
    private final Context mContext;
    private final DisplayImeController mDisplayImeController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final SplitScreenImpl mImpl = new SplitScreenImpl();
    private final SplitscreenEventLogger mLogger;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final RootTaskDisplayAreaOrganizer mRootTDAOrganizer;
    /* access modifiers changed from: private */
    public StageCoordinator mStageCoordinator;
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;
    private final Provider<Optional<StageTaskUnfoldController>> mUnfoldControllerProvider;

    public SplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, Provider<Optional<StageTaskUnfoldController>> provider) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mContext = context;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer;
        this.mMainExecutor = shellExecutor;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mTransitions = transitions;
        this.mTransactionPool = transactionPool;
        this.mUnfoldControllerProvider = provider;
        this.mLogger = new SplitscreenEventLogger();
    }

    public SplitScreen asSplitScreen() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void onOrganizerRegistered() {
        if (this.mStageCoordinator == null) {
            this.mStageCoordinator = new StageCoordinator(this.mContext, 0, this.mSyncQueue, this.mRootTDAOrganizer, this.mTaskOrganizer, this.mDisplayImeController, this.mDisplayInsetsController, this.mTransitions, this.mTransactionPool, this.mLogger, this.mUnfoldControllerProvider);
        }
    }

    public boolean isSplitScreenVisible() {
        return this.mStageCoordinator.isSplitScreenVisible();
    }

    public boolean moveToSideStage(int i, int i2) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        if (runningTaskInfo != null) {
            return moveToSideStage(runningTaskInfo, i2);
        }
        throw new IllegalArgumentException("Unknown taskId" + i);
    }

    public boolean moveToSideStage(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        return this.mStageCoordinator.moveToSideStage(runningTaskInfo, i);
    }

    public boolean removeFromSideStage(int i) {
        return this.mStageCoordinator.removeFromSideStage(i);
    }

    public void setSideStageOutline(boolean z) {
        this.mStageCoordinator.setSideStageOutline(z);
    }

    public void setSideStagePosition(int i) {
        this.mStageCoordinator.setSideStagePosition(i, (WindowContainerTransaction) null);
    }

    public void setSideStageVisibility(boolean z) {
        this.mStageCoordinator.setSideStageVisibility(z);
    }

    public void enterSplitScreen(int i, boolean z) {
        moveToSideStage(i, z ^ true ? 1 : 0);
    }

    public void exitSplitScreen(int i, int i2) {
        this.mStageCoordinator.exitSplitScreen(i, i2);
    }

    public void onKeyguardOccludedChanged(boolean z) {
        this.mStageCoordinator.onKeyguardOccludedChanged(z);
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        this.mStageCoordinator.onKeyguardVisibilityChanged(z);
    }

    public void exitSplitScreenOnHide(boolean z) {
        this.mStageCoordinator.exitSplitScreenOnHide(z);
    }

    public void getStageBounds(Rect rect, Rect rect2) {
        this.mStageCoordinator.getStageBounds(rect, rect2);
    }

    public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.registerSplitScreenListener(splitScreenListener);
    }

    public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.unregisterSplitScreenListener(splitScreenListener);
    }

    public void startTask(int i, int i2, Bundle bundle) {
        try {
            ActivityTaskManager.getService().startActivityFromRecents(i, this.mStageCoordinator.resolveStartStage(-1, i2, bundle, (WindowContainerTransaction) null));
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to launch task", e);
        }
    }

    public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
        try {
            ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, (Rect) null, this.mStageCoordinator.resolveStartStage(-1, i, bundle, (WindowContainerTransaction) null), userHandle);
        } catch (ActivityNotFoundException e) {
            Slog.e(TAG, "Failed to launch shortcut", e);
        }
    }

    public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            startIntentLegacy(pendingIntent, intent, i, bundle);
        } else {
            this.mStageCoordinator.startIntent(pendingIntent, intent, -1, i, bundle, (RemoteTransition) null);
        }
    }

    private void startIntentLegacy(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
        C35811 r0 = new LegacyTransitions.ILegacyTransition() {
            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, SurfaceControl.Transaction transaction) {
                SplitScreenController.this.mStageCoordinator.updateSurfaceBounds((SplitLayout) null, transaction, false);
                if (remoteAnimationTargetArr != null) {
                    for (int i2 = 0; i2 < remoteAnimationTargetArr.length; i2++) {
                        if (remoteAnimationTargetArr[i2].mode == 0) {
                            transaction.show(remoteAnimationTargetArr[i2].leash);
                        }
                    }
                }
                transaction.apply();
                if (iRemoteAnimationFinishedCallback != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        Slog.e(SplitScreenController.TAG, "Error finishing legacy transition: ", e);
                    }
                }
            }
        };
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.sendPendingIntent(pendingIntent, intent, this.mStageCoordinator.resolveStartStage(-1, i, bundle, windowContainerTransaction));
        this.mSyncQueue.queue(r0, 1, windowContainerTransaction);
    }

    /* access modifiers changed from: package-private */
    public RemoteAnimationTarget[] onGoingToRecentsLegacy(boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (!isSplitScreenVisible()) {
            return null;
        }
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("RecentsAnimationSplitTasks").setHidden(false).setCallsite("SplitScreenController#onGoingtoRecentsLegacy");
        this.mRootTDAOrganizer.attachToDisplayArea(0, callsite);
        SurfaceControl build = callsite.build();
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        Arrays.sort(remoteAnimationTargetArr, new SplitScreenController$$ExternalSyntheticLambda0());
        int length = remoteAnimationTargetArr.length;
        int i = 0;
        int i2 = 1;
        while (i < length) {
            RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[i];
            transaction.reparent(remoteAnimationTarget.leash, build);
            transaction.setPosition(remoteAnimationTarget.leash, (float) remoteAnimationTarget.screenSpaceBounds.left, (float) remoteAnimationTarget.screenSpaceBounds.top);
            transaction.setLayer(remoteAnimationTarget.leash, i2);
            i++;
            i2++;
        }
        transaction.apply();
        transaction.close();
        return new RemoteAnimationTarget[]{this.mStageCoordinator.getDividerBarLegacyTarget(), this.mStageCoordinator.getOutlineLegacyTarget()};
    }

    static /* synthetic */ int lambda$onGoingToRecentsLegacy$0(RemoteAnimationTarget remoteAnimationTarget, RemoteAnimationTarget remoteAnimationTarget2) {
        return remoteAnimationTarget.prefixOrderIndex - remoteAnimationTarget2.prefixOrderIndex;
    }

    public void logOnDroppedToSplit(int i, InstanceId instanceId) {
        this.mStageCoordinator.logOnDroppedToSplit(i, instanceId);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + TAG);
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        if (stageCoordinator != null) {
            stageCoordinator.dump(printWriter, str);
        }
    }

    @ExternalThread
    /* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$SplitScreenImpl */
    private class SplitScreenImpl implements SplitScreen {
        /* access modifiers changed from: private */
        public final ArrayMap<SplitScreen.SplitScreenListener, Executor> mExecutors;
        private ISplitScreenImpl mISplitScreen;
        private final SplitScreen.SplitScreenListener mListener;

        private SplitScreenImpl() {
            this.mExecutors = new ArrayMap<>();
            this.mListener = new SplitScreen.SplitScreenListener() {
                public void onStagePositionChanged(int i, int i2) {
                    for (int i3 = 0; i3 < SplitScreenImpl.this.mExecutors.size(); i3++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i3)).execute(new C3590xcb46d402(this, i3, i, i2));
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onStagePositionChanged$0$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl$1 */
                public /* synthetic */ void mo50999x3d0f5ebb(int i, int i2, int i3) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onStagePositionChanged(i2, i3);
                }

                public void onTaskStageChanged(int i, int i2, boolean z) {
                    for (int i3 = 0; i3 < SplitScreenImpl.this.mExecutors.size(); i3++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i3)).execute(new C3588xcb46d400(this, i3, i, i2, z));
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onTaskStageChanged$1$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl$1 */
                public /* synthetic */ void mo51000x634437ca(int i, int i2, int i3, boolean z) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onTaskStageChanged(i2, i3, z);
                }

                public void onSplitVisibilityChanged(boolean z) {
                    for (int i = 0; i < SplitScreenImpl.this.mExecutors.size(); i++) {
                        ((Executor) SplitScreenImpl.this.mExecutors.valueAt(i)).execute(new C3589xcb46d401(this, i, z));
                    }
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onSplitVisibilityChanged$2$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl$1 */
                public /* synthetic */ void mo50998xbe9080d8(int i, boolean z) {
                    ((SplitScreen.SplitScreenListener) SplitScreenImpl.this.mExecutors.keyAt(i)).onSplitVisibilityChanged(z);
                }
            };
        }

        public ISplitScreen createExternalInterface() {
            ISplitScreenImpl iSplitScreenImpl = this.mISplitScreen;
            if (iSplitScreenImpl != null) {
                iSplitScreenImpl.invalidate();
            }
            ISplitScreenImpl iSplitScreenImpl2 = new ISplitScreenImpl(SplitScreenController.this);
            this.mISplitScreen = iSplitScreenImpl2;
            return iSplitScreenImpl2;
        }

        public void onKeyguardOccludedChanged(boolean z) {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda4(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onKeyguardOccludedChanged$0$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50993xb882cd4c(boolean z) {
            SplitScreenController.this.onKeyguardOccludedChanged(z);
        }

        public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener, Executor executor) {
            if (!this.mExecutors.containsKey(splitScreenListener)) {
                SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1(this, splitScreenListener, executor));
                executor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2(this, splitScreenListener));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSplitScreenListener$1$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50995xedc5cb80(SplitScreen.SplitScreenListener splitScreenListener, Executor executor) {
            if (this.mExecutors.size() == 0) {
                SplitScreenController.this.registerSplitScreenListener(this.mListener);
            }
            this.mExecutors.put(splitScreenListener, executor);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSplitScreenListener$2$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50996xd0f17ec1(SplitScreen.SplitScreenListener splitScreenListener) {
            SplitScreenController.this.mStageCoordinator.sendStatusToListener(splitScreenListener);
        }

        public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0(this, splitScreenListener));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unregisterSplitScreenListener$3$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50997x24638009(SplitScreen.SplitScreenListener splitScreenListener) {
            this.mExecutors.remove(splitScreenListener);
            if (this.mExecutors.size() == 0) {
                SplitScreenController.this.unregisterSplitScreenListener(this.mListener);
            }
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            SplitScreenController.this.mMainExecutor.execute(new SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onKeyguardVisibilityChanged$4$com-android-wm-shell-stagesplit-SplitScreenController$SplitScreenImpl */
        public /* synthetic */ void mo50994x60185f09(boolean z) {
            SplitScreenController.this.onKeyguardVisibilityChanged(z);
        }
    }

    /* renamed from: com.android.wm.shell.stagesplit.SplitScreenController$ISplitScreenImpl */
    private static class ISplitScreenImpl extends ISplitScreen.Stub {
        /* access modifiers changed from: private */
        public SplitScreenController mController;
        /* access modifiers changed from: private */
        public ISplitScreenListener mListener;
        private final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() {
            public void binderDied() {
                SplitScreenController access$600 = ISplitScreenImpl.this.mController;
                access$600.getRemoteCallExecutor().execute(new C3587xfa6a0f5c(this, access$600));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$binderDied$0$com-android-wm-shell-stagesplit-SplitScreenController$ISplitScreenImpl$2 */
            public /* synthetic */ void mo50992x788ea341(SplitScreenController splitScreenController) {
                ISplitScreenListener unused = ISplitScreenImpl.this.mListener = null;
                splitScreenController.unregisterSplitScreenListener(ISplitScreenImpl.this.mSplitScreenListener);
            }
        };
        /* access modifiers changed from: private */
        public final SplitScreen.SplitScreenListener mSplitScreenListener = new SplitScreen.SplitScreenListener() {
            public void onStagePositionChanged(int i, int i2) {
                try {
                    if (ISplitScreenImpl.this.mListener != null) {
                        ISplitScreenImpl.this.mListener.onStagePositionChanged(i, i2);
                    }
                } catch (RemoteException e) {
                    Slog.e(SplitScreenController.TAG, "onStagePositionChanged", e);
                }
            }

            public void onTaskStageChanged(int i, int i2, boolean z) {
                try {
                    if (ISplitScreenImpl.this.mListener != null) {
                        ISplitScreenImpl.this.mListener.onTaskStageChanged(i, i2, z);
                    }
                } catch (RemoteException e) {
                    Slog.e(SplitScreenController.TAG, "onTaskStageChanged", e);
                }
            }
        };

        public ISplitScreenImpl(SplitScreenController splitScreenController) {
            this.mController = splitScreenController;
        }

        /* access modifiers changed from: package-private */
        public void invalidate() {
            this.mController = null;
        }

        public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "registerSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(this, iSplitScreenListener));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$registerSplitScreenListener$0$com-android-wm-shell-stagesplit-SplitScreenController$ISplitScreenImpl */
        public /* synthetic */ void mo50989xbf677a04(ISplitScreenListener iSplitScreenListener, SplitScreenController splitScreenController) {
            ISplitScreenListener iSplitScreenListener2 = this.mListener;
            if (iSplitScreenListener2 != null) {
                iSplitScreenListener2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            if (iSplitScreenListener != null) {
                try {
                    iSplitScreenListener.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
                } catch (RemoteException unused) {
                    Slog.e(SplitScreenController.TAG, "Failed to link to death");
                    return;
                }
            }
            this.mListener = iSplitScreenListener;
            splitScreenController.registerSplitScreenListener(this.mSplitScreenListener);
        }

        public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "unregisterSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$unregisterSplitScreenListener$1$com-android-wm-shell-stagesplit-SplitScreenController$ISplitScreenImpl */
        public /* synthetic */ void mo50990xda35a1bc(SplitScreenController splitScreenController) {
            ISplitScreenListener iSplitScreenListener = this.mListener;
            if (iSplitScreenListener != null) {
                iSplitScreenListener.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            this.mListener = null;
            splitScreenController.unregisterSplitScreenListener(this.mSplitScreenListener);
        }

        public void exitSplitScreen(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreen", new C3586xeaa4ee02(i));
        }

        public void exitSplitScreenOnHide(boolean z) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreenOnHide", new C3585xeaa4ee01(z));
        }

        public void setSideStageVisibility(boolean z) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setSideStageVisibility", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0(z));
        }

        public void removeFromSideStage(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "removeFromSideStage", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5(i));
        }

        public void startTask(int i, int i2, int i3, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTask", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6(i, i3, bundle));
        }

        public void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteAnimationAdapter remoteAnimationAdapter) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2(i, bundle, i2, bundle2, i3, remoteAnimationAdapter));
        }

        public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4(i, bundle, i2, bundle2, i3, remoteTransition));
        }

        public void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startShortcut", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(str, str2, i2, bundle, userHandle));
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startIntent", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8(pendingIntent, intent, i2, bundle));
        }

        public RemoteAnimationTarget[] onGoingToRecentsLegacy(boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr) {
            RemoteAnimationTarget[][] remoteAnimationTargetArr2 = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onGoingToRecentsLegacy", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9(remoteAnimationTargetArr2, z, remoteAnimationTargetArr), true);
            return remoteAnimationTargetArr2[0];
        }

        static /* synthetic */ void lambda$onGoingToRecentsLegacy$11(RemoteAnimationTarget[][] remoteAnimationTargetArr, boolean z, RemoteAnimationTarget[] remoteAnimationTargetArr2, SplitScreenController splitScreenController) {
            remoteAnimationTargetArr[0] = splitScreenController.onGoingToRecentsLegacy(z, remoteAnimationTargetArr2);
        }
    }
}
