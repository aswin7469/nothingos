package com.android.p019wm.shell.transition;

import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.IWindowContainerTransactionCallback;

/* renamed from: com.android.wm.shell.transition.LegacyTransitions */
public class LegacyTransitions {

    /* renamed from: com.android.wm.shell.transition.LegacyTransitions$ILegacyTransition */
    public interface ILegacyTransition {
        void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, SurfaceControl.Transaction transaction);
    }

    /* renamed from: com.android.wm.shell.transition.LegacyTransitions$LegacyTransition */
    public static class LegacyTransition {
        private final RemoteAnimationAdapter mAdapter = new RemoteAnimationAdapter(new RemoteAnimationWrapper(), 0, 0);
        /* access modifiers changed from: private */
        public RemoteAnimationTarget[] mApps;
        /* access modifiers changed from: private */
        public boolean mCancelled = false;
        /* access modifiers changed from: private */
        public IRemoteAnimationFinishedCallback mFinishCallback = null;
        private final ILegacyTransition mLegacyTransition;
        /* access modifiers changed from: private */
        public RemoteAnimationTarget[] mNonApps;
        private final SyncCallback mSyncCallback = new SyncCallback();
        /* access modifiers changed from: private */
        public int mSyncId = -1;
        /* access modifiers changed from: private */
        public SurfaceControl.Transaction mTransaction;
        /* access modifiers changed from: private */
        public int mTransit;
        /* access modifiers changed from: private */
        public RemoteAnimationTarget[] mWallpapers;

        public LegacyTransition(int i, ILegacyTransition iLegacyTransition) {
            this.mLegacyTransition = iLegacyTransition;
            this.mTransit = i;
        }

        public int getType() {
            return this.mTransit;
        }

        public IWindowContainerTransactionCallback getSyncCallback() {
            return this.mSyncCallback;
        }

        public RemoteAnimationAdapter getAdapter() {
            return this.mAdapter;
        }

        /* renamed from: com.android.wm.shell.transition.LegacyTransitions$LegacyTransition$SyncCallback */
        private class SyncCallback extends IWindowContainerTransactionCallback.Stub {
            private SyncCallback() {
            }

            public void onTransactionReady(int i, SurfaceControl.Transaction transaction) throws RemoteException {
                int unused = LegacyTransition.this.mSyncId = i;
                SurfaceControl.Transaction unused2 = LegacyTransition.this.mTransaction = transaction;
                LegacyTransition.this.checkApply();
            }
        }

        /* renamed from: com.android.wm.shell.transition.LegacyTransitions$LegacyTransition$RemoteAnimationWrapper */
        private class RemoteAnimationWrapper extends IRemoteAnimationRunner.Stub {
            private RemoteAnimationWrapper() {
            }

            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
                int unused = LegacyTransition.this.mTransit = i;
                RemoteAnimationTarget[] unused2 = LegacyTransition.this.mApps = remoteAnimationTargetArr;
                RemoteAnimationTarget[] unused3 = LegacyTransition.this.mWallpapers = remoteAnimationTargetArr2;
                RemoteAnimationTarget[] unused4 = LegacyTransition.this.mNonApps = remoteAnimationTargetArr3;
                IRemoteAnimationFinishedCallback unused5 = LegacyTransition.this.mFinishCallback = iRemoteAnimationFinishedCallback;
                LegacyTransition.this.checkApply();
            }

            public void onAnimationCancelled(boolean z) throws RemoteException {
                boolean unused = LegacyTransition.this.mCancelled = true;
                LegacyTransition legacyTransition = LegacyTransition.this;
                RemoteAnimationTarget[] unused2 = legacyTransition.mApps = legacyTransition.mWallpapers = legacyTransition.mNonApps = null;
                LegacyTransition.this.checkApply();
            }
        }

        /* access modifiers changed from: private */
        public void checkApply() throws RemoteException {
            if (this.mSyncId >= 0) {
                IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.mFinishCallback;
                if (iRemoteAnimationFinishedCallback != null || this.mCancelled) {
                    this.mLegacyTransition.onAnimationStart(this.mTransit, this.mApps, this.mWallpapers, this.mNonApps, iRemoteAnimationFinishedCallback, this.mTransaction);
                }
            }
        }
    }
}
