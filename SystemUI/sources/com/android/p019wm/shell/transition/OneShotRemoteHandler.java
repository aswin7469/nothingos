package com.android.p019wm.shell.transition;

import android.app.ActivityTaskManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.transition.OneShotRemoteHandler */
public class OneShotRemoteHandler implements Transitions.TransitionHandler {
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public final RemoteTransition mRemote;
    private IBinder mTransition = null;

    public OneShotRemoteHandler(ShellExecutor shellExecutor, RemoteTransition remoteTransition) {
        this.mMainExecutor = shellExecutor;
        this.mRemote = remoteTransition;
    }

    public void setTransition(IBinder iBinder) {
        this.mTransition = iBinder;
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final SurfaceControl.Transaction transaction2, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (this.mTransition != iBinder) {
            return false;
        }
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Using registered One-shot remote transition %s for %s.", new Object[]{this.mRemote, iBinder});
        final OneShotRemoteHandler$$ExternalSyntheticLambda0 oneShotRemoteHandler$$ExternalSyntheticLambda0 = new OneShotRemoteHandler$$ExternalSyntheticLambda0(this, transitionFinishCallback);
        C36211 r3 = new IRemoteTransitionFinishedCallback.Stub() {
            public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
                if (OneShotRemoteHandler.this.mRemote.asBinder() != null) {
                    OneShotRemoteHandler.this.mRemote.asBinder().unlinkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
                }
                OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$1$$ExternalSyntheticLambda0(transaction, transaction2, transitionFinishCallback, windowContainerTransaction));
            }

            static /* synthetic */ void lambda$onTransitionFinished$0(SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
                if (transaction != null) {
                    transaction2.merge(transaction);
                }
                transitionFinishCallback.onTransitionFinished(windowContainerTransaction, (WindowContainerTransactionCallback) null);
            }
        };
        try {
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().linkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
            }
            try {
                ActivityTaskManager.getService().setRunningRemoteTransitionDelegate(this.mRemote.getAppThread());
            } catch (SecurityException unused) {
                Slog.e("ShellTransitions", "Unable to boost animation thread. This should only happen during unit tests");
            }
            this.mRemote.getRemoteTransition().startAnimation(iBinder, transitionInfo, transaction, r3);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().unlinkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
            }
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startAnimation$1$com-android-wm-shell-transition-OneShotRemoteHandler */
    public /* synthetic */ void mo51264x2986f74a(Transitions.TransitionFinishCallback transitionFinishCallback) {
        Log.e("ShellTransitions", "Remote transition died, finishing");
        this.mMainExecutor.execute(new OneShotRemoteHandler$$ExternalSyntheticLambda1(transitionFinishCallback));
    }

    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Using registered One-shot remote transition %s for %s.", new Object[]{this.mRemote, iBinder});
        try {
            this.mRemote.getRemoteTransition().mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new IRemoteTransitionFinishedCallback.Stub() {
                public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
                    OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(transitionFinishCallback, windowContainerTransaction));
                }
            });
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error merging remote transition.", e);
        }
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        RemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if ((remoteTransition != null ? remoteTransition.getRemoteTransition() : null) != this.mRemote.getRemoteTransition()) {
            return null;
        }
        this.mTransition = iBinder;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "RemoteTransition directly requested for %s: %s", new Object[]{iBinder, remoteTransition});
        return new WindowContainerTransaction();
    }
}
