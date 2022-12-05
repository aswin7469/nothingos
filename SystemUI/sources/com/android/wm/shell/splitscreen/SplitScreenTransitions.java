package com.android.wm.shell.splitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2;
import com.android.wm.shell.transition.OneShotRemoteHandler;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SplitScreenTransitions {
    private SurfaceControl.Transaction mFinishTransaction;
    private final Runnable mOnFinish;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;
    IBinder mPendingDismiss = null;
    IBinder mPendingEnter = null;
    private IBinder mAnimatingTransition = null;
    private OneShotRemoteHandler mRemoteHandler = null;
    private Transitions.TransitionFinishCallback mRemoteFinishCB = new Transitions.TransitionFinishCallback() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda2
        @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
        public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
            SplitScreenTransitions.this.lambda$new$0(windowContainerTransaction, windowContainerTransactionCallback);
        }
    };
    private final ArrayList<Animator> mAnimations = new ArrayList<>();
    private Transitions.TransitionFinishCallback mFinishCallback = null;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        if (windowContainerTransaction != null || windowContainerTransactionCallback != null) {
            throw new UnsupportedOperationException("finish transactions not supported yet.");
        }
        onFinish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, Runnable runnable) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mOnFinish = runnable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishCallback = transitionFinishCallback;
        this.mAnimatingTransition = iBinder;
        OneShotRemoteHandler oneShotRemoteHandler = this.mRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.startAnimation(iBinder, transitionInfo, transaction, this.mRemoteFinishCB);
            this.mRemoteHandler = null;
            return;
        }
        playInternalAnimation(iBinder, transitionInfo, transaction, windowContainerToken, windowContainerToken2);
    }

    private void playInternalAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishTransaction = this.mTransactionPool.acquire();
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size)).getMode();
            if (mode == 6) {
                if (change.getParent() != null) {
                    TransitionInfo.Change change2 = transitionInfo.getChange(change.getParent());
                    transaction.show(change2.getLeash());
                    transaction.setAlpha(change2.getLeash(), 1.0f);
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setLayer(leash, transitionInfo.getChanges().size() - size);
                    this.mFinishTransaction.reparent(leash, change2.getLeash());
                    this.mFinishTransaction.setPosition(leash, change.getEndRelOffset().x, change.getEndRelOffset().y);
                }
                Rect rect = new Rect(change.getStartAbsBounds());
                if (transitionInfo.getType() == 11) {
                    rect.offsetTo(change.getEndAbsBounds().left, change.getEndAbsBounds().top);
                }
                Rect rect2 = new Rect(change.getEndAbsBounds());
                rect.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                rect2.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                startExampleResizeAnimation(leash, rect, rect2);
            }
            if (change.getParent() == null) {
                if (iBinder == this.mPendingEnter && (windowContainerToken.equals(change.getContainer()) || windowContainerToken2.equals(change.getContainer()))) {
                    transaction.setWindowCrop(leash, change.getStartAbsBounds().width(), change.getStartAbsBounds().height());
                }
                boolean isOpeningType = Transitions.isOpeningType(transitionInfo.getType());
                if (isOpeningType && (mode == 1 || mode == 3)) {
                    startExampleAnimation(leash, true);
                } else if (!isOpeningType && (mode == 2 || mode == 4)) {
                    if (transitionInfo.getType() == 11) {
                        transaction.setAlpha(leash, 0.0f);
                    } else {
                        startExampleAnimation(leash, false);
                    }
                }
            }
        }
        transaction.apply();
        onFinish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder startEnterTransition(int i, WindowContainerTransaction windowContainerTransaction, IRemoteTransition iRemoteTransition, Transitions.TransitionHandler transitionHandler) {
        if (iRemoteTransition != null) {
            this.mRemoteHandler = new OneShotRemoteHandler(this.mTransitions.getMainExecutor(), iRemoteTransition);
        }
        IBinder startTransition = this.mTransitions.startTransition(i, windowContainerTransaction, transitionHandler);
        this.mPendingEnter = startTransition;
        OneShotRemoteHandler oneShotRemoteHandler = this.mRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.setTransition(startTransition);
        }
        return startTransition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder startSnapToDismiss(WindowContainerTransaction windowContainerTransaction, Transitions.TransitionHandler transitionHandler) {
        IBinder startTransition = this.mTransitions.startTransition(11, windowContainerTransaction, transitionHandler);
        this.mPendingDismiss = startTransition;
        return startTransition;
    }

    void onFinish() {
        if (!this.mAnimations.isEmpty()) {
            return;
        }
        this.mOnFinish.run();
        SurfaceControl.Transaction transaction = this.mFinishTransaction;
        if (transaction != null) {
            transaction.apply();
            this.mTransactionPool.release(this.mFinishTransaction);
            this.mFinishTransaction = null;
        }
        this.mFinishCallback.onTransitionFinished(null, null);
        this.mFinishCallback = null;
        IBinder iBinder = this.mAnimatingTransition;
        if (iBinder == this.mPendingEnter) {
            this.mPendingEnter = null;
        }
        if (iBinder == this.mPendingDismiss) {
            this.mPendingDismiss = null;
        }
        this.mAnimatingTransition = null;
    }

    private void startExampleAnimation(final SurfaceControl surfaceControl, boolean z) {
        final float f = z ? 1.0f : 0.0f;
        final float f2 = 1.0f - f;
        final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SplitScreenTransitions.lambda$startExampleAnimation$1(acquire, surfaceControl, f2, f, valueAnimator);
            }
        });
        final Runnable runnable = new Runnable() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.this.lambda$startExampleAnimation$3(acquire, surfaceControl, f, ofFloat);
            }
        };
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions.1
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
    public static /* synthetic */ void lambda$startExampleAnimation$1(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, float f2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        transaction.setAlpha(surfaceControl, (f * (1.0f - animatedFraction)) + (f2 * animatedFraction));
        transaction.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$3(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, final ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, f);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.this.lambda$startExampleAnimation$2(valueAnimator);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$2(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }

    private void startExampleResizeAnimation(final SurfaceControl surfaceControl, final Rect rect, final Rect rect2) {
        final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SplitScreenTransitions.lambda$startExampleResizeAnimation$4(acquire, surfaceControl, rect, rect2, valueAnimator);
            }
        });
        final Runnable runnable = new Runnable() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.this.lambda$startExampleResizeAnimation$6(acquire, surfaceControl, rect2, ofFloat);
            }
        };
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions.2
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
    public static /* synthetic */ void lambda$startExampleResizeAnimation$4(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        transaction.setWindowCrop(surfaceControl, (int) ((rect.width() * f) + (rect2.width() * animatedFraction)), (int) ((rect.height() * f) + (rect2.height() * animatedFraction)));
        transaction.setPosition(surfaceControl, (rect.left * f) + (rect2.left * animatedFraction), (rect.top * f) + (rect2.top * animatedFraction));
        transaction.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$6(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, final ValueAnimator valueAnimator) {
        transaction.setWindowCrop(surfaceControl, 0, 0);
        transaction.setPosition(surfaceControl, rect.left, rect.top);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.this.lambda$startExampleResizeAnimation$5(valueAnimator);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$5(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }
}
