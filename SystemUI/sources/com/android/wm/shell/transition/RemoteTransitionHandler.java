package com.android.wm.shell.transition;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.RemoteTransitionHandler;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class RemoteTransitionHandler implements Transitions.TransitionHandler {
    private final ShellExecutor mMainExecutor;
    private final ArrayMap<IBinder, IRemoteTransition> mRequestedRemotes = new ArrayMap<>();
    private final ArrayList<Pair<TransitionFilter, IRemoteTransition>> mFilters = new ArrayList<>();
    private final IBinder.DeathRecipient mTransitionDeathRecipient = new AnonymousClass1();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IBinder.DeathRecipient {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$binderDied$0() {
            RemoteTransitionHandler.this.mFilters.clear();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            RemoteTransitionHandler.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionHandler.AnonymousClass1.this.lambda$binderDied$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteTransitionHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFiltered(TransitionFilter transitionFilter, IRemoteTransition iRemoteTransition) {
        try {
            iRemoteTransition.asBinder().linkToDeath(this.mTransitionDeathRecipient, 0);
            this.mFilters.add(new Pair<>(transitionFilter, iRemoteTransition));
        } catch (RemoteException unused) {
            Slog.e("RemoteTransitionHandler", "Failed to link to death");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeFiltered(IRemoteTransition iRemoteTransition) {
        boolean z = false;
        for (int size = this.mFilters.size() - 1; size >= 0; size--) {
            if (this.mFilters.get(size).second == iRemoteTransition) {
                this.mFilters.remove(size);
                z = true;
            }
        }
        if (z) {
            iRemoteTransition.asBinder().unlinkToDeath(this.mTransitionDeathRecipient, 0);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void onTransitionMerged(IBinder iBinder) {
        this.mRequestedRemotes.remove(iBinder);
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(final IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        IRemoteTransition iRemoteTransition = this.mRequestedRemotes.get(iBinder);
        if (iRemoteTransition == null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1269886472, 0, "Transition %s doesn't have explicit remote, search filters for match for %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
            }
            int size = this.mFilters.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 990371881, 0, " Checking filter %s", String.valueOf(this.mFilters.get(size)));
                }
                if (((TransitionFilter) this.mFilters.get(size).first).matches(transitionInfo)) {
                    iRemoteTransition = (IRemoteTransition) this.mFilters.get(size).second;
                    this.mRequestedRemotes.put(iBinder, iRemoteTransition);
                    break;
                }
                size--;
            }
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1671119352, 0, " Delegate animation for %s to %s", String.valueOf(iBinder), String.valueOf(iRemoteTransition));
        }
        if (iRemoteTransition == null) {
            return false;
        }
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                RemoteTransitionHandler.this.lambda$startAnimation$1(iBinder, transitionFinishCallback);
            }
        };
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(iRemoteTransition, deathRecipient, iBinder, transitionFinishCallback);
        try {
            if (iRemoteTransition.asBinder() != null) {
                iRemoteTransition.asBinder().linkToDeath(deathRecipient, 0);
            }
            iRemoteTransition.startAnimation(iBinder, transitionInfo, transaction, anonymousClass2);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            if (iRemoteTransition.asBinder() != null) {
                iRemoteTransition.asBinder().unlinkToDeath(deathRecipient, 0);
            }
            this.mRequestedRemotes.remove(iBinder);
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Transitions.TransitionFinishCallback.this.onTransitionFinished(null, null);
                }
            });
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$1(final IBinder iBinder, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        Log.e("ShellTransitions", "Remote transition died, finishing");
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RemoteTransitionHandler.this.lambda$startAnimation$0(iBinder, transitionFinishCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$0(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.mRequestedRemotes.remove(iBinder);
        transitionFinishCallback.onTransitionFinished(null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends IRemoteTransitionFinishedCallback.Stub {
        final /* synthetic */ Transitions.TransitionFinishCallback val$finishCallback;
        final /* synthetic */ IRemoteTransition val$remote;
        final /* synthetic */ IBinder.DeathRecipient val$remoteDied;
        final /* synthetic */ IBinder val$transition;

        AnonymousClass2(IRemoteTransition iRemoteTransition, IBinder.DeathRecipient deathRecipient, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
            this.val$remote = iRemoteTransition;
            this.val$remoteDied = deathRecipient;
            this.val$transition = iBinder;
            this.val$finishCallback = transitionFinishCallback;
        }

        public void onTransitionFinished(final WindowContainerTransaction windowContainerTransaction) {
            if (this.val$remote.asBinder() != null) {
                this.val$remote.asBinder().unlinkToDeath(this.val$remoteDied, 0);
            }
            ShellExecutor shellExecutor = RemoteTransitionHandler.this.mMainExecutor;
            final IBinder iBinder = this.val$transition;
            final Transitions.TransitionFinishCallback transitionFinishCallback = this.val$finishCallback;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionHandler.AnonymousClass2.this.lambda$onTransitionFinished$0(iBinder, transitionFinishCallback, windowContainerTransaction);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransitionFinished$0(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
            RemoteTransitionHandler.this.mRequestedRemotes.remove(iBinder);
            transitionFinishCallback.onTransitionFinished(windowContainerTransaction, null);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        IRemoteTransition iRemoteTransition = this.mRequestedRemotes.get(iBinder2);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -114556030, 0, " Attempt merge %s into %s", String.valueOf(iBinder), String.valueOf(iRemoteTransition));
        }
        if (iRemoteTransition == null) {
            return;
        }
        try {
            iRemoteTransition.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new AnonymousClass3(iBinder2, transitionFinishCallback));
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error attempting to merge remote transition.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends IRemoteTransitionFinishedCallback.Stub {
        final /* synthetic */ Transitions.TransitionFinishCallback val$finishCallback;
        final /* synthetic */ IBinder val$mergeTarget;

        AnonymousClass3(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
            this.val$mergeTarget = iBinder;
            this.val$finishCallback = transitionFinishCallback;
        }

        public void onTransitionFinished(final WindowContainerTransaction windowContainerTransaction) {
            ShellExecutor shellExecutor = RemoteTransitionHandler.this.mMainExecutor;
            final IBinder iBinder = this.val$mergeTarget;
            final Transitions.TransitionFinishCallback transitionFinishCallback = this.val$finishCallback;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionHandler.AnonymousClass3.this.lambda$onTransitionFinished$0(iBinder, transitionFinishCallback, windowContainerTransaction);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransitionFinished$0(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
            if (!RemoteTransitionHandler.this.mRequestedRemotes.containsKey(iBinder)) {
                Log.e("RemoteTransitionHandler", "Merged transition finished after it's mergeTarget (the transition it was supposed to merge into). This usually means that the mergeTarget's RemoteTransition impl erroneously accepted/ran the merge request after finishing the mergeTarget");
            }
            transitionFinishCallback.onTransitionFinished(windowContainerTransaction, null);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        IRemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition == null) {
            return null;
        }
        this.mRequestedRemotes.put(iBinder, remoteTransition);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 214412327, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
