package com.android.wm.shell.transition;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes2.dex */
public class OneShotRemoteHandler implements Transitions.TransitionHandler {
    private final ShellExecutor mMainExecutor;
    private final IRemoteTransition mRemote;
    private IBinder mTransition = null;

    public OneShotRemoteHandler(ShellExecutor shellExecutor, IRemoteTransition iRemoteTransition) {
        this.mMainExecutor = shellExecutor;
        this.mRemote = iRemoteTransition;
    }

    public void setTransition(IBinder iBinder) {
        this.mTransition = iBinder;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (this.mTransition != iBinder) {
            return false;
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", String.valueOf(this.mRemote), String.valueOf(iBinder));
        }
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                OneShotRemoteHandler.this.lambda$startAnimation$1(transitionFinishCallback);
            }
        };
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(deathRecipient, transitionFinishCallback);
        try {
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().linkToDeath(deathRecipient, 0);
            }
            this.mRemote.startAnimation(iBinder, transitionInfo, transaction, anonymousClass1);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().unlinkToDeath(deathRecipient, 0);
            }
            transitionFinishCallback.onTransitionFinished(null, null);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$1(final Transitions.TransitionFinishCallback transitionFinishCallback) {
        Log.e("ShellTransitions", "Remote transition died, finishing");
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Transitions.TransitionFinishCallback.this.onTransitionFinished(null, null);
            }
        });
    }

    /* renamed from: com.android.wm.shell.transition.OneShotRemoteHandler$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends IRemoteTransitionFinishedCallback.Stub {
        final /* synthetic */ Transitions.TransitionFinishCallback val$finishCallback;
        final /* synthetic */ IBinder.DeathRecipient val$remoteDied;

        AnonymousClass1(IBinder.DeathRecipient deathRecipient, Transitions.TransitionFinishCallback transitionFinishCallback) {
            this.val$remoteDied = deathRecipient;
            this.val$finishCallback = transitionFinishCallback;
        }

        public void onTransitionFinished(final WindowContainerTransaction windowContainerTransaction) {
            if (OneShotRemoteHandler.this.mRemote.asBinder() != null) {
                OneShotRemoteHandler.this.mRemote.asBinder().unlinkToDeath(this.val$remoteDied, 0);
            }
            ShellExecutor shellExecutor = OneShotRemoteHandler.this.mMainExecutor;
            final Transitions.TransitionFinishCallback transitionFinishCallback = this.val$finishCallback;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Transitions.TransitionFinishCallback.this.onTransitionFinished(windowContainerTransaction, null);
                }
            });
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", String.valueOf(this.mRemote), String.valueOf(iBinder));
        }
        try {
            this.mRemote.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new AnonymousClass2(transitionFinishCallback));
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error merging remote transition.", e);
        }
    }

    /* renamed from: com.android.wm.shell.transition.OneShotRemoteHandler$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends IRemoteTransitionFinishedCallback.Stub {
        final /* synthetic */ Transitions.TransitionFinishCallback val$finishCallback;

        AnonymousClass2(Transitions.TransitionFinishCallback transitionFinishCallback) {
            this.val$finishCallback = transitionFinishCallback;
        }

        public void onTransitionFinished(final WindowContainerTransaction windowContainerTransaction) {
            ShellExecutor shellExecutor = OneShotRemoteHandler.this.mMainExecutor;
            final Transitions.TransitionFinishCallback transitionFinishCallback = this.val$finishCallback;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Transitions.TransitionFinishCallback.this.onTransitionFinished(windowContainerTransaction, null);
                }
            });
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        IRemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition != this.mRemote) {
            return null;
        }
        this.mTransition = iBinder;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 967375804, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
