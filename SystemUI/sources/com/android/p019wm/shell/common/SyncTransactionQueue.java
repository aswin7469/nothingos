package com.android.p019wm.shell.common;

import android.util.Slog;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import com.android.p019wm.shell.transition.LegacyTransitions;
import java.util.ArrayList;

/* renamed from: com.android.wm.shell.common.SyncTransactionQueue */
public final class SyncTransactionQueue {
    private static final boolean DEBUG = false;
    private static final int REPLY_TIMEOUT = 5300;
    private static final String TAG = "SyncTransactionQueue";
    /* access modifiers changed from: private */
    public SyncCallback mInFlight = null;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public final Runnable mOnReplyTimeout = new SyncTransactionQueue$$ExternalSyntheticLambda0(this);
    /* access modifiers changed from: private */
    public final ArrayList<SyncCallback> mQueue = new ArrayList<>();
    private final ArrayList<TransactionRunnable> mRunnables = new ArrayList<>();
    private final TransactionPool mTransactionPool;

    /* renamed from: com.android.wm.shell.common.SyncTransactionQueue$TransactionRunnable */
    public interface TransactionRunnable {
        void runWithTransaction(SurfaceControl.Transaction transaction);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-common-SyncTransactionQueue  reason: not valid java name */
    public /* synthetic */ void m3450lambda$new$0$comandroidwmshellcommonSyncTransactionQueue() {
        synchronized (this.mQueue) {
            SyncCallback syncCallback = this.mInFlight;
            if (syncCallback != null && this.mQueue.contains(syncCallback)) {
                Slog.w(TAG, "Sync Transaction timed-out: " + this.mInFlight.mWCT);
                SyncCallback syncCallback2 = this.mInFlight;
                syncCallback2.onTransactionReady(syncCallback2.mId, new SurfaceControl.Transaction());
            }
        }
    }

    public SyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
    }

    public void queue(WindowContainerTransaction windowContainerTransaction) {
        if (!windowContainerTransaction.isEmpty()) {
            SyncCallback syncCallback = new SyncCallback(windowContainerTransaction);
            synchronized (this.mQueue) {
                this.mQueue.add(syncCallback);
                if (this.mQueue.size() == 1) {
                    syncCallback.send();
                }
            }
        }
    }

    public void queue(LegacyTransitions.ILegacyTransition iLegacyTransition, int i, WindowContainerTransaction windowContainerTransaction) {
        if (!windowContainerTransaction.isEmpty()) {
            SyncCallback syncCallback = new SyncCallback(iLegacyTransition, i, windowContainerTransaction);
            synchronized (this.mQueue) {
                this.mQueue.add(syncCallback);
                if (this.mQueue.size() == 1) {
                    syncCallback.send();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean queueIfWaiting(android.window.WindowContainerTransaction r4) {
        /*
            r3 = this;
            boolean r0 = r4.isEmpty()
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r0 = r3.mQueue
            monitor-enter(r0)
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r2 = r3.mQueue     // Catch:{ all -> 0x002d }
            boolean r2 = r2.isEmpty()     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x0015
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r1
        L_0x0015:
            com.android.wm.shell.common.SyncTransactionQueue$SyncCallback r1 = new com.android.wm.shell.common.SyncTransactionQueue$SyncCallback     // Catch:{ all -> 0x002d }
            r1.<init>(r4)     // Catch:{ all -> 0x002d }
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r4 = r3.mQueue     // Catch:{ all -> 0x002d }
            r4.add(r1)     // Catch:{ all -> 0x002d }
            java.util.ArrayList<com.android.wm.shell.common.SyncTransactionQueue$SyncCallback> r3 = r3.mQueue     // Catch:{ all -> 0x002d }
            int r3 = r3.size()     // Catch:{ all -> 0x002d }
            r4 = 1
            if (r3 != r4) goto L_0x002b
            r1.send()     // Catch:{ all -> 0x002d }
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r4
        L_0x002d:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.common.SyncTransactionQueue.queueIfWaiting(android.window.WindowContainerTransaction):boolean");
    }

    public void runInSync(TransactionRunnable transactionRunnable) {
        synchronized (this.mQueue) {
            if (this.mInFlight != null) {
                this.mRunnables.add(transactionRunnable);
                return;
            }
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            transactionRunnable.runWithTransaction(acquire);
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    /* access modifiers changed from: private */
    public void onTransactionReceived(SurfaceControl.Transaction transaction) {
        int size = this.mRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mRunnables.get(i).runWithTransaction(transaction);
        }
        this.mRunnables.subList(0, size).clear();
    }

    /* renamed from: com.android.wm.shell.common.SyncTransactionQueue$SyncCallback */
    private class SyncCallback extends WindowContainerTransactionCallback {
        int mId = -1;
        final LegacyTransitions.LegacyTransition mLegacyTransition;
        final WindowContainerTransaction mWCT;

        SyncCallback(WindowContainerTransaction windowContainerTransaction) {
            this.mWCT = windowContainerTransaction;
            this.mLegacyTransition = null;
        }

        SyncCallback(LegacyTransitions.ILegacyTransition iLegacyTransition, int i, WindowContainerTransaction windowContainerTransaction) {
            this.mWCT = windowContainerTransaction;
            this.mLegacyTransition = new LegacyTransitions.LegacyTransition(i, iLegacyTransition);
        }

        /* access modifiers changed from: package-private */
        public void send() {
            if (SyncTransactionQueue.this.mInFlight != this) {
                if (SyncTransactionQueue.this.mInFlight == null) {
                    SyncCallback unused = SyncTransactionQueue.this.mInFlight = this;
                    if (this.mLegacyTransition != null) {
                        this.mId = new WindowOrganizer().startLegacyTransition(this.mLegacyTransition.getType(), this.mLegacyTransition.getAdapter(), this, this.mWCT);
                    } else {
                        this.mId = new WindowOrganizer().applySyncTransaction(this.mWCT, this);
                    }
                    SyncTransactionQueue.this.mMainExecutor.executeDelayed(SyncTransactionQueue.this.mOnReplyTimeout, 5300);
                    return;
                }
                throw new IllegalStateException("Sync Transactions must be serialized. In Flight: " + SyncTransactionQueue.this.mInFlight.mId + " - " + SyncTransactionQueue.this.mInFlight.mWCT);
            }
        }

        public void onTransactionReady(int i, SurfaceControl.Transaction transaction) {
            SyncTransactionQueue.this.mMainExecutor.execute(new SyncTransactionQueue$SyncCallback$$ExternalSyntheticLambda0(this, i, transaction));
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0099, code lost:
            return;
         */
        /* renamed from: lambda$onTransactionReady$0$com-android-wm-shell-common-SyncTransactionQueue$SyncCallback */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public /* synthetic */ void mo49126x2b2040e7(int r5, android.view.SurfaceControl.Transaction r6) {
            /*
                r4 = this;
                java.lang.String r0 = "Error sending callback to legacy transition: "
                java.lang.String r1 = "Got an unexpected onTransactionReady. Expected "
                com.android.wm.shell.common.SyncTransactionQueue r2 = com.android.p019wm.shell.common.SyncTransactionQueue.this
                java.util.ArrayList r2 = r2.mQueue
                monitor-enter(r2)
                int r3 = r4.mId     // Catch:{ all -> 0x009a }
                if (r3 == r5) goto L_0x002f
                java.lang.String r6 = "SyncTransactionQueue"
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x009a }
                r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x009a }
                int r4 = r4.mId     // Catch:{ all -> 0x009a }
                java.lang.StringBuilder r4 = r0.append((int) r4)     // Catch:{ all -> 0x009a }
                java.lang.String r0 = " but got "
                java.lang.StringBuilder r4 = r4.append((java.lang.String) r0)     // Catch:{ all -> 0x009a }
                java.lang.StringBuilder r4 = r4.append((int) r5)     // Catch:{ all -> 0x009a }
                java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x009a }
                android.util.Slog.e(r6, r4)     // Catch:{ all -> 0x009a }
                monitor-exit(r2)     // Catch:{ all -> 0x009a }
                return
            L_0x002f:
                com.android.wm.shell.common.SyncTransactionQueue r5 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                r1 = 0
                com.android.p019wm.shell.common.SyncTransactionQueue.SyncCallback unused = r5.mInFlight = r1     // Catch:{ all -> 0x009a }
                com.android.wm.shell.common.SyncTransactionQueue r5 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                com.android.wm.shell.common.ShellExecutor r5 = r5.mMainExecutor     // Catch:{ all -> 0x009a }
                com.android.wm.shell.common.SyncTransactionQueue r1 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                java.lang.Runnable r1 = r1.mOnReplyTimeout     // Catch:{ all -> 0x009a }
                r5.removeCallbacks(r1)     // Catch:{ all -> 0x009a }
                com.android.wm.shell.common.SyncTransactionQueue r5 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                java.util.ArrayList r5 = r5.mQueue     // Catch:{ all -> 0x009a }
                r5.remove((java.lang.Object) r4)     // Catch:{ all -> 0x009a }
                com.android.wm.shell.common.SyncTransactionQueue r5 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                r5.onTransactionReceived(r6)     // Catch:{ all -> 0x009a }
                com.android.wm.shell.transition.LegacyTransitions$LegacyTransition r5 = r4.mLegacyTransition     // Catch:{ all -> 0x009a }
                if (r5 == 0) goto L_0x0076
                android.window.IWindowContainerTransactionCallback r5 = r5.getSyncCallback()     // Catch:{ RemoteException -> 0x0060 }
                int r1 = r4.mId     // Catch:{ RemoteException -> 0x0060 }
                r5.onTransactionReady(r1, r6)     // Catch:{ RemoteException -> 0x0060 }
                goto L_0x007c
            L_0x0060:
                r5 = move-exception
                java.lang.String r6 = "SyncTransactionQueue"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x009a }
                r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x009a }
                int r0 = r4.mId     // Catch:{ all -> 0x009a }
                java.lang.StringBuilder r0 = r1.append((int) r0)     // Catch:{ all -> 0x009a }
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x009a }
                android.util.Slog.e(r6, r0, r5)     // Catch:{ all -> 0x009a }
                goto L_0x007c
            L_0x0076:
                r6.apply()     // Catch:{ all -> 0x009a }
                r6.close()     // Catch:{ all -> 0x009a }
            L_0x007c:
                com.android.wm.shell.common.SyncTransactionQueue r5 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                java.util.ArrayList r5 = r5.mQueue     // Catch:{ all -> 0x009a }
                boolean r5 = r5.isEmpty()     // Catch:{ all -> 0x009a }
                if (r5 != 0) goto L_0x0098
                com.android.wm.shell.common.SyncTransactionQueue r4 = com.android.p019wm.shell.common.SyncTransactionQueue.this     // Catch:{ all -> 0x009a }
                java.util.ArrayList r4 = r4.mQueue     // Catch:{ all -> 0x009a }
                r5 = 0
                java.lang.Object r4 = r4.get(r5)     // Catch:{ all -> 0x009a }
                com.android.wm.shell.common.SyncTransactionQueue$SyncCallback r4 = (com.android.p019wm.shell.common.SyncTransactionQueue.SyncCallback) r4     // Catch:{ all -> 0x009a }
                r4.send()     // Catch:{ all -> 0x009a }
            L_0x0098:
                monitor-exit(r2)     // Catch:{ all -> 0x009a }
                return
            L_0x009a:
                r4 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x009a }
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.common.SyncTransactionQueue.SyncCallback.mo49126x2b2040e7(int, android.view.SurfaceControl$Transaction):void");
        }
    }
}
