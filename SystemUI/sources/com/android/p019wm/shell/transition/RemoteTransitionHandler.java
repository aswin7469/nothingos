package com.android.p019wm.shell.transition;

import android.app.ActivityTaskManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.transition.Transitions;
import java.util.ArrayList;

/* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler */
public class RemoteTransitionHandler implements Transitions.TransitionHandler {
    private static final String TAG = "RemoteTransitionHandler";
    private final ArrayMap<IBinder, RemoteDeathHandler> mDeathHandlers = new ArrayMap<>();
    /* access modifiers changed from: private */
    public final ArrayList<Pair<TransitionFilter, RemoteTransition>> mFilters = new ArrayList<>();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public final ArrayMap<IBinder, RemoteTransition> mRequestedRemotes = new ArrayMap<>();

    RemoteTransitionHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    /* access modifiers changed from: package-private */
    public void addFiltered(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
        handleDeath(remoteTransition.asBinder(), (Transitions.TransitionFinishCallback) null);
        this.mFilters.add(new Pair(transitionFilter, remoteTransition));
    }

    /* access modifiers changed from: package-private */
    public void removeFiltered(RemoteTransition remoteTransition) {
        boolean z = false;
        for (int size = this.mFilters.size() - 1; size >= 0; size--) {
            if (((RemoteTransition) this.mFilters.get(size).second).asBinder().equals(remoteTransition.asBinder())) {
                this.mFilters.remove(size);
                z = true;
            }
        }
        if (z) {
            unhandleDeath(remoteTransition.asBinder(), (Transitions.TransitionFinishCallback) null);
        }
    }

    public void onTransitionMerged(IBinder iBinder) {
        this.mRequestedRemotes.remove(iBinder);
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        IBinder iBinder2 = iBinder;
        TransitionInfo transitionInfo2 = transitionInfo;
        Transitions.TransitionFinishCallback transitionFinishCallback2 = transitionFinishCallback;
        RemoteTransition remoteTransition = this.mRequestedRemotes.get(iBinder);
        if (remoteTransition == null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "Transition %s doesn't have explicit remote, search filters for match for %s", new Object[]{iBinder2, transitionInfo2});
            int size = this.mFilters.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " Checking filter %s", new Object[]{this.mFilters.get(size)});
                if (((TransitionFilter) this.mFilters.get(size).first).matches(transitionInfo2)) {
                    Slog.d(TAG, "Found filter" + this.mFilters.get(size));
                    remoteTransition = (RemoteTransition) this.mFilters.get(size).second;
                    this.mRequestedRemotes.put(iBinder, remoteTransition);
                    break;
                }
                size--;
            }
        }
        RemoteTransition remoteTransition2 = remoteTransition;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " Delegate animation for %s to %s", new Object[]{iBinder2, remoteTransition2});
        if (remoteTransition2 == null) {
            return false;
        }
        final RemoteTransition remoteTransition3 = remoteTransition2;
        final Transitions.TransitionFinishCallback transitionFinishCallback3 = transitionFinishCallback;
        final SurfaceControl.Transaction transaction3 = transaction2;
        final IBinder iBinder3 = iBinder;
        C36231 r1 = new IRemoteTransitionFinishedCallback.Stub() {
            public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
                RemoteTransitionHandler.this.unhandleDeath(remoteTransition3.asBinder(), transitionFinishCallback3);
                RemoteTransitionHandler.this.mMainExecutor.execute(new RemoteTransitionHandler$1$$ExternalSyntheticLambda0(this, transaction, transaction3, iBinder3, transitionFinishCallback3, windowContainerTransaction));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onTransitionFinished$0$com-android-wm-shell-transition-RemoteTransitionHandler$1 */
            public /* synthetic */ void mo51271x67d3e2b9(SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
                if (transaction != null) {
                    transaction2.merge(transaction);
                }
                RemoteTransitionHandler.this.mRequestedRemotes.remove(iBinder);
                transitionFinishCallback.onTransitionFinished(windowContainerTransaction, (WindowContainerTransactionCallback) null);
            }
        };
        try {
            handleDeath(remoteTransition2.asBinder(), transitionFinishCallback2);
            try {
                ActivityTaskManager.getService().setRunningRemoteTransitionDelegate(remoteTransition2.getAppThread());
            } catch (SecurityException unused) {
                Log.e("ShellTransitions", "Unable to boost animation thread. This should only happen during unit tests");
            }
            remoteTransition2.getRemoteTransition().startAnimation(iBinder, transitionInfo2, transaction, r1);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            unhandleDeath(remoteTransition2.asBinder(), transitionFinishCallback2);
            this.mRequestedRemotes.remove(iBinder);
            this.mMainExecutor.execute(new RemoteTransitionHandler$$ExternalSyntheticLambda0(transitionFinishCallback2));
        }
        return true;
    }

    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final IBinder iBinder2, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        IRemoteTransition remoteTransition = this.mRequestedRemotes.get(iBinder2).getRemoteTransition();
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, " Attempt merge %s into %s", new Object[]{iBinder, remoteTransition});
        if (remoteTransition != null) {
            try {
                remoteTransition.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new IRemoteTransitionFinishedCallback.Stub() {
                    public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
                        RemoteTransitionHandler.this.mMainExecutor.execute(new RemoteTransitionHandler$2$$ExternalSyntheticLambda0(this, iBinder2, transitionFinishCallback, windowContainerTransaction));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onTransitionFinished$0$com-android-wm-shell-transition-RemoteTransitionHandler$2 */
                    public /* synthetic */ void mo51273x67d3e2ba(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
                        if (!RemoteTransitionHandler.this.mRequestedRemotes.containsKey(iBinder)) {
                            Log.e(RemoteTransitionHandler.TAG, "Merged transition finished after it's mergeTarget (the transition it was supposed to merge into). This usually means that the mergeTarget's RemoteTransition impl erroneously accepted/ran the merge request after finishing the mergeTarget");
                        }
                        transitionFinishCallback.onTransitionFinished(windowContainerTransaction, (WindowContainerTransactionCallback) null);
                    }
                });
            } catch (RemoteException e) {
                Log.e("ShellTransitions", "Error attempting to merge remote transition.", e);
            }
        }
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        RemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition == null) {
            return null;
        }
        this.mRequestedRemotes.put(iBinder, remoteTransition);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, "RemoteTransition directly requested for %s: %s", new Object[]{iBinder, remoteTransition});
        return new WindowContainerTransaction();
    }

    private void handleDeath(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        synchronized (this.mDeathHandlers) {
            RemoteDeathHandler remoteDeathHandler = this.mDeathHandlers.get(iBinder);
            if (remoteDeathHandler == null) {
                remoteDeathHandler = new RemoteDeathHandler(iBinder);
                try {
                    iBinder.linkToDeath(remoteDeathHandler, 0);
                    this.mDeathHandlers.put(iBinder, remoteDeathHandler);
                } catch (RemoteException unused) {
                    Slog.e(TAG, "Failed to link to death");
                    return;
                }
            }
            remoteDeathHandler.addUser(transitionFinishCallback);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unhandleDeath(android.os.IBinder r3, com.android.p019wm.shell.transition.Transitions.TransitionFinishCallback r4) {
        /*
            r2 = this;
            android.util.ArrayMap<android.os.IBinder, com.android.wm.shell.transition.RemoteTransitionHandler$RemoteDeathHandler> r0 = r2.mDeathHandlers
            monitor-enter(r0)
            android.util.ArrayMap<android.os.IBinder, com.android.wm.shell.transition.RemoteTransitionHandler$RemoteDeathHandler> r1 = r2.mDeathHandlers     // Catch:{ all -> 0x0036 }
            java.lang.Object r1 = r1.get(r3)     // Catch:{ all -> 0x0036 }
            com.android.wm.shell.transition.RemoteTransitionHandler$RemoteDeathHandler r1 = (com.android.p019wm.shell.transition.RemoteTransitionHandler.RemoteDeathHandler) r1     // Catch:{ all -> 0x0036 }
            if (r1 != 0) goto L_0x000f
            monitor-exit(r0)     // Catch:{ all -> 0x0036 }
            return
        L_0x000f:
            r1.removeUser(r4)     // Catch:{ all -> 0x0036 }
            int r4 = r1.getUserCount()     // Catch:{ all -> 0x0036 }
            if (r4 != 0) goto L_0x0034
            java.util.ArrayList r4 = r1.mPendingFinishCallbacks     // Catch:{ all -> 0x0036 }
            boolean r4 = r4.isEmpty()     // Catch:{ all -> 0x0036 }
            if (r4 == 0) goto L_0x002c
            r4 = 0
            r3.unlinkToDeath(r1, r4)     // Catch:{ all -> 0x0036 }
            android.util.ArrayMap<android.os.IBinder, com.android.wm.shell.transition.RemoteTransitionHandler$RemoteDeathHandler> r2 = r2.mDeathHandlers     // Catch:{ all -> 0x0036 }
            r2.remove(r3)     // Catch:{ all -> 0x0036 }
            goto L_0x0034
        L_0x002c:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0036 }
            java.lang.String r3 = "Unhandling death for binder that still has pending finishCallback(s)."
            r2.<init>((java.lang.String) r3)     // Catch:{ all -> 0x0036 }
            throw r2     // Catch:{ all -> 0x0036 }
        L_0x0034:
            monitor-exit(r0)     // Catch:{ all -> 0x0036 }
            return
        L_0x0036:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0036 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.transition.RemoteTransitionHandler.unhandleDeath(android.os.IBinder, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):void");
    }

    /* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$RemoteDeathHandler */
    private class RemoteDeathHandler implements IBinder.DeathRecipient {
        /* access modifiers changed from: private */
        public final ArrayList<Transitions.TransitionFinishCallback> mPendingFinishCallbacks = new ArrayList<>();
        private final IBinder mRemote;
        private int mUsers = 0;

        RemoteDeathHandler(IBinder iBinder) {
            this.mRemote = iBinder;
        }

        /* access modifiers changed from: package-private */
        public void addUser(Transitions.TransitionFinishCallback transitionFinishCallback) {
            if (transitionFinishCallback != null) {
                this.mPendingFinishCallbacks.add(transitionFinishCallback);
            }
            this.mUsers++;
        }

        /* access modifiers changed from: package-private */
        public void removeUser(Transitions.TransitionFinishCallback transitionFinishCallback) {
            if (transitionFinishCallback != null) {
                this.mPendingFinishCallbacks.remove((Object) transitionFinishCallback);
            }
            this.mUsers--;
        }

        /* access modifiers changed from: package-private */
        public int getUserCount() {
            return this.mUsers;
        }

        public void binderDied() {
            RemoteTransitionHandler.this.mMainExecutor.execute(new C3625xe5e43da0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$binderDied$0$com-android-wm-shell-transition-RemoteTransitionHandler$RemoteDeathHandler */
        public /* synthetic */ void mo51278xe7f9796c() {
            for (int size = RemoteTransitionHandler.this.mFilters.size() - 1; size >= 0; size--) {
                if (this.mRemote.equals(((RemoteTransition) ((Pair) RemoteTransitionHandler.this.mFilters.get(size)).second).asBinder())) {
                    RemoteTransitionHandler.this.mFilters.remove(size);
                }
            }
            for (int size2 = RemoteTransitionHandler.this.mRequestedRemotes.size() - 1; size2 >= 0; size2--) {
                if (this.mRemote.equals(((RemoteTransition) RemoteTransitionHandler.this.mRequestedRemotes.valueAt(size2)).asBinder())) {
                    RemoteTransitionHandler.this.mRequestedRemotes.removeAt(size2);
                }
            }
            for (int size3 = this.mPendingFinishCallbacks.size() - 1; size3 >= 0; size3--) {
                this.mPendingFinishCallbacks.get(size3).onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
            }
            this.mPendingFinishCallbacks.clear();
        }
    }
}
