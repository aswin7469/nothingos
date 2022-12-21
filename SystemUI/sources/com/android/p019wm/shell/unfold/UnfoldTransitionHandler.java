package com.android.p019wm.shell.unfold;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.unfold.UnfoldTransitionHandler */
public class UnfoldTransitionHandler implements Transitions.TransitionHandler, ShellUnfoldProgressProvider.UnfoldListener {
    private final List<TransitionInfo.Change> mAnimatedFullscreenTasks = new ArrayList();
    private final Executor mExecutor;
    private Transitions.TransitionFinishCallback mFinishCallback;
    private final TransactionPool mTransactionPool;
    private IBinder mTransition;
    private final Transitions mTransitions;
    private final ShellUnfoldProgressProvider mUnfoldProgressProvider;

    public UnfoldTransitionHandler(ShellUnfoldProgressProvider shellUnfoldProgressProvider, TransactionPool transactionPool, Executor executor, Transitions transitions) {
        this.mUnfoldProgressProvider = shellUnfoldProgressProvider;
        this.mTransactionPool = transactionPool;
        this.mExecutor = executor;
        this.mTransitions = transitions;
    }

    public void init() {
        this.mTransitions.addHandler(this);
        this.mUnfoldProgressProvider.addListener(this.mExecutor, this);
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (iBinder != this.mTransition) {
            return false;
        }
        transaction.apply();
        this.mAnimatedFullscreenTasks.clear();
        transitionInfo.getChanges().forEach(new UnfoldTransitionHandler$$ExternalSyntheticLambda0(this));
        this.mFinishCallback = transitionFinishCallback;
        this.mTransition = null;
        return true;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0021, code lost:
        if (r4.getMode() == 6) goto L_0x0025;
     */
    /* renamed from: lambda$startAnimation$0$com-android-wm-shell-unfold-UnfoldTransitionHandler */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void mo51311xcc7846f9(android.window.TransitionInfo.Change r4) {
        /*
            r3 = this;
            android.app.ActivityManager$RunningTaskInfo r0 = r4.getTaskInfo()
            if (r0 == 0) goto L_0x0024
            android.app.ActivityManager$RunningTaskInfo r0 = r4.getTaskInfo()
            int r0 = r0.getWindowingMode()
            r1 = 1
            if (r0 != r1) goto L_0x0024
            android.app.ActivityManager$RunningTaskInfo r0 = r4.getTaskInfo()
            int r0 = r0.getActivityType()
            r2 = 2
            if (r0 == r2) goto L_0x0024
            int r0 = r4.getMode()
            r2 = 6
            if (r0 != r2) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r1 = 0
        L_0x0025:
            if (r1 == 0) goto L_0x002c
            java.util.List<android.window.TransitionInfo$Change> r3 = r3.mAnimatedFullscreenTasks
            r3.add(r4)
        L_0x002c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.unfold.UnfoldTransitionHandler.mo51311xcc7846f9(android.window.TransitionInfo$Change):void");
    }

    public void onStateChangeProgress(float f) {
        this.mAnimatedFullscreenTasks.forEach(new UnfoldTransitionHandler$$ExternalSyntheticLambda1(this, f));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStateChangeProgress$1$com-android-wm-shell-unfold-UnfoldTransitionHandler */
    public /* synthetic */ void mo51310x2f816bed(float f, TransitionInfo.Change change) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        float f2 = (f * 0.2f) + 0.8f;
        acquire.setScale(change.getLeash(), f2, f2);
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    public void onStateChangeFinished() {
        Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
        if (transitionFinishCallback != null) {
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
            this.mFinishCallback = null;
            this.mAnimatedFullscreenTasks.clear();
        }
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        if (transitionRequestInfo.getType() != 6 || transitionRequestInfo.getDisplayChange() == null || !transitionRequestInfo.getDisplayChange().isPhysicalDisplayChanged()) {
            return null;
        }
        this.mTransition = iBinder;
        return new WindowContainerTransaction();
    }
}
