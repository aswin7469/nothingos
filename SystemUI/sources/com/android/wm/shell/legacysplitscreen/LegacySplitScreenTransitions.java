package com.android.wm.shell.legacysplitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class LegacySplitScreenTransitions implements Transitions.TransitionHandler {
    private SurfaceControl.Transaction mFinishTransaction;
    private final LegacySplitScreenTaskListener mListener;
    private final LegacySplitScreenController mSplitScreen;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;
    private IBinder mPendingDismiss = null;
    private boolean mDismissFromSnap = false;
    private IBinder mPendingEnter = null;
    private IBinder mAnimatingTransition = null;
    private final ArrayList<Animator> mAnimations = new ArrayList<>();
    private Transitions.TransitionFinishCallback mFinishCallback = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LegacySplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, LegacySplitScreenController legacySplitScreenController, LegacySplitScreenTaskListener legacySplitScreenTaskListener) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mSplitScreen = legacySplitScreenController;
        this.mListener = legacySplitScreenTaskListener;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        ActivityManager.RunningTaskInfo triggerTask = transitionRequestInfo.getTriggerTask();
        int type = transitionRequestInfo.getType();
        if (this.mSplitScreen.isDividerVisible()) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            if (triggerTask == null) {
                return windowContainerTransaction;
            }
            if (!(((type == 2 || type == 4) && triggerTask.parentTaskId == this.mListener.mPrimary.taskId) || ((type == 1 || type == 3) && !triggerTask.supportsMultiWindow))) {
                return windowContainerTransaction;
            }
            WindowManagerProxy.buildDismissSplit(windowContainerTransaction, this.mListener, this.mSplitScreen.getSplitLayout(), true);
            if (type == 1 || type == 3) {
                windowContainerTransaction.reorder(triggerTask.token, true);
            }
            this.mPendingDismiss = iBinder;
            return windowContainerTransaction;
        } else if (triggerTask == null || !((type == 1 || type == 3) && triggerTask.configuration.windowConfiguration.getWindowingMode() == 3)) {
            return null;
        } else {
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            this.mSplitScreen.prepareEnterSplitTransition(windowContainerTransaction2);
            this.mPendingEnter = iBinder;
            return windowContainerTransaction2;
        }
    }

    private void startExampleAnimation(final SurfaceControl surfaceControl, boolean z) {
        final float f = z ? 1.0f : 0.0f;
        final float f2 = 1.0f - f;
        final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LegacySplitScreenTransitions.lambda$startExampleAnimation$0(acquire, surfaceControl, f2, f, valueAnimator);
            }
        });
        final Runnable runnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$startExampleAnimation$2(acquire, surfaceControl, f, ofFloat);
            }
        };
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                runnable.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startExampleAnimation$0(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, float f2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        transaction.setAlpha(surfaceControl, (f * (1.0f - animatedFraction)) + (f2 * animatedFraction));
        transaction.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$2(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, final ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, f);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$startExampleAnimation$1(valueAnimator);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$1(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }

    private void startExampleResizeAnimation(final SurfaceControl surfaceControl, final Rect rect, final Rect rect2) {
        final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LegacySplitScreenTransitions.lambda$startExampleResizeAnimation$3(acquire, surfaceControl, rect, rect2, valueAnimator);
            }
        });
        final Runnable runnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$startExampleResizeAnimation$5(acquire, surfaceControl, rect2, ofFloat);
            }
        };
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                runnable.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startExampleResizeAnimation$3(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        transaction.setWindowCrop(surfaceControl, (int) ((rect.width() * f) + (rect2.width() * animatedFraction)), (int) ((rect.height() * f) + (rect2.height() * animatedFraction)));
        transaction.setPosition(surfaceControl, (rect.left * f) + (rect2.left * animatedFraction), (rect.top * f) + (rect2.top * animatedFraction));
        transaction.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$5(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, final ValueAnimator valueAnimator) {
        transaction.setWindowCrop(surfaceControl, 0, 0);
        transaction.setPosition(surfaceControl, rect.left, rect.top);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$startExampleResizeAnimation$4(valueAnimator);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$4(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenController] */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [int] */
    /* JADX WARN: Type inference failed for: r7v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v47 */
    /* JADX WARN: Type inference failed for: r9v7, types: [android.graphics.Rect] */
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, Transitions.TransitionFinishCallback transitionFinishCallback) {
        boolean z;
        boolean z2;
        ?? r7 = 0;
        if (iBinder != this.mPendingDismiss && iBinder != this.mPendingEnter) {
            if (!this.mSplitScreen.isDividerVisible()) {
                return false;
            }
            for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
                if (change.getTaskInfo() != null && change.getTaskInfo().getActivityType() == 2) {
                    if (change.getMode() == 1 || change.getMode() == 3) {
                        this.mSplitScreen.ensureMinimizedSplit();
                    } else if (change.getMode() == 2 || change.getMode() == 4) {
                        this.mSplitScreen.ensureNormalSplit();
                    }
                }
            }
            return false;
        }
        this.mFinishCallback = transitionFinishCallback;
        this.mFinishTransaction = this.mTransactionPool.acquire();
        this.mAnimatingTransition = iBinder;
        int size2 = transitionInfo.getChanges().size() - 1;
        while (size2 >= 0) {
            TransitionInfo.Change change2 = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
            SurfaceControl leash = change2.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size2)).getMode();
            if (mode == 6) {
                if (change2.getParent() != null) {
                    TransitionInfo.Change change3 = transitionInfo.getChange(change2.getParent());
                    transaction.show(change3.getLeash());
                    transaction.setAlpha(change3.getLeash(), 1.0f);
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setLayer(leash, transitionInfo.getChanges().size() - size2);
                    this.mFinishTransaction.reparent(leash, change3.getLeash());
                    this.mFinishTransaction.setPosition(leash, change2.getEndRelOffset().x, change2.getEndRelOffset().y);
                }
                ?? rect = new Rect(change2.getStartAbsBounds());
                if (change2.getTaskInfo() == null || change2.getTaskInfo().getActivityType() != 2) {
                    boolean z3 = r7 == true ? 1 : 0;
                    boolean z4 = r7 == true ? 1 : 0;
                    boolean z5 = r7 == true ? 1 : 0;
                    boolean z6 = r7 == true ? 1 : 0;
                    boolean z7 = r7 == true ? 1 : 0;
                    z2 = z3;
                } else {
                    z2 = true;
                }
                if (this.mPendingDismiss == iBinder && this.mDismissFromSnap && !z2) {
                    int i = r7 == true ? 1 : 0;
                    int i2 = r7 == true ? 1 : 0;
                    int i3 = r7 == true ? 1 : 0;
                    int i4 = r7 == true ? 1 : 0;
                    int i5 = r7 == true ? 1 : 0;
                    rect.offsetTo(i, r7);
                }
                Rect rect2 = new Rect(change2.getEndAbsBounds());
                rect.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                rect2.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                startExampleResizeAnimation(leash, rect, rect2);
            }
            if (change2.getParent() == null) {
                if ((iBinder == this.mPendingEnter && this.mListener.mPrimary.token.equals(change2.getContainer())) || this.mListener.mSecondary.token.equals(change2.getContainer())) {
                    transaction.setWindowCrop(leash, change2.getStartAbsBounds().width(), change2.getStartAbsBounds().height());
                    if (this.mListener.mPrimary.token.equals(change2.getContainer())) {
                        transaction.setLayer(leash, transitionInfo.getChanges().size() + 1);
                    }
                }
                boolean isOpeningType = Transitions.isOpeningType(transitionInfo.getType());
                if (isOpeningType && (mode == 1 || mode == 3)) {
                    startExampleAnimation(leash, true);
                } else if (!isOpeningType && (mode == 2 || mode == 4)) {
                    if (iBinder == this.mPendingDismiss && this.mDismissFromSnap) {
                        transaction.setAlpha(leash, 0.0f);
                    } else {
                        z = false;
                        startExampleAnimation(leash, false);
                        size2--;
                        r7 = z;
                    }
                }
            }
            z = false;
            size2--;
            r7 = z;
        }
        if (iBinder == this.mPendingEnter) {
            int size3 = transitionInfo.getChanges().size() - 1;
            while (true) {
                if (size3 < 0) {
                    break;
                }
                TransitionInfo.Change change4 = (TransitionInfo.Change) transitionInfo.getChanges().get(size3);
                if (change4.getTaskInfo() == null || change4.getTaskInfo().getActivityType() != 2) {
                    size3--;
                } else if (change4.getMode() == 1 || change4.getMode() == 3 || change4.getMode() == 6) {
                    r7 = 1;
                }
            }
            this.mSplitScreen.finishEnterSplitTransition(r7);
        }
        transaction.apply();
        onFinish();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dismissSplit(LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, boolean z, final boolean z2) {
        final WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        WindowManagerProxy.buildDismissSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout, z);
        this.mTransitions.getMainExecutor().execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$dismissSplit$6(z2, windowContainerTransaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissSplit$6(boolean z, WindowContainerTransaction windowContainerTransaction) {
        this.mDismissFromSnap = z;
        this.mPendingDismiss = this.mTransitions.startTransition(20, windowContainerTransaction, this);
    }

    private void onFinish() {
        if (!this.mAnimations.isEmpty()) {
            return;
        }
        this.mFinishTransaction.apply();
        this.mTransactionPool.release(this.mFinishTransaction);
        this.mFinishTransaction = null;
        this.mFinishCallback.onTransitionFinished(null, null);
        this.mFinishCallback = null;
        IBinder iBinder = this.mAnimatingTransition;
        if (iBinder == this.mPendingEnter) {
            this.mPendingEnter = null;
        }
        if (iBinder == this.mPendingDismiss) {
            this.mSplitScreen.onDismissSplit();
            this.mPendingDismiss = null;
        }
        this.mDismissFromSnap = false;
        this.mAnimatingTransition = null;
    }
}
