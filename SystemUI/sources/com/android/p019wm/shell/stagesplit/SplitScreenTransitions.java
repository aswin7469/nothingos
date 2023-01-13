package com.android.p019wm.shell.stagesplit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.transition.OneShotRemoteHandler;
import com.android.p019wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Objects;

/* renamed from: com.android.wm.shell.stagesplit.SplitScreenTransitions */
class SplitScreenTransitions {
    public static final int FLAG_IS_DIVIDER_BAR = 256;
    private static final String TAG = "SplitScreenTransitions";
    private IBinder mAnimatingTransition = null;
    private final ArrayList<Animator> mAnimations = new ArrayList<>();
    private Transitions.TransitionFinishCallback mFinishCallback = null;
    private SurfaceControl.Transaction mFinishTransaction;
    private final Runnable mOnFinish;
    IBinder mPendingDismiss = null;
    IBinder mPendingEnter = null;
    private Transitions.TransitionFinishCallback mRemoteFinishCB = new SplitScreenTransitions$$ExternalSyntheticLambda7(this);
    private OneShotRemoteHandler mRemoteHandler = null;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-stagesplit-SplitScreenTransitions */
    public /* synthetic */ void mo51001xf8dbd6b4(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        if (windowContainerTransaction == null && windowContainerTransactionCallback == null) {
            onFinish();
            return;
        }
        throw new UnsupportedOperationException("finish transactions not supported yet.");
    }

    SplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, Runnable runnable) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mOnFinish = runnable;
    }

    /* access modifiers changed from: package-private */
    public void playAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishCallback = transitionFinishCallback;
        this.mAnimatingTransition = iBinder;
        OneShotRemoteHandler oneShotRemoteHandler = this.mRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.startAnimation(iBinder, transitionInfo, transaction, transaction2, this.mRemoteFinishCB);
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
                    this.mFinishTransaction.setPosition(leash, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
                }
                Rect rect = new Rect(change.getStartAbsBounds());
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
                    if (transitionInfo.getType() == 18) {
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

    /* access modifiers changed from: package-private */
    public IBinder startEnterTransition(int i, WindowContainerTransaction windowContainerTransaction, RemoteTransition remoteTransition, Transitions.TransitionHandler transitionHandler) {
        if (remoteTransition != null) {
            this.mRemoteHandler = new OneShotRemoteHandler(this.mTransitions.getMainExecutor(), remoteTransition);
        }
        IBinder startTransition = this.mTransitions.startTransition(i, windowContainerTransaction, transitionHandler);
        this.mPendingEnter = startTransition;
        OneShotRemoteHandler oneShotRemoteHandler = this.mRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.setTransition(startTransition);
        }
        return startTransition;
    }

    /* access modifiers changed from: package-private */
    public IBinder startSnapToDismiss(WindowContainerTransaction windowContainerTransaction, Transitions.TransitionHandler transitionHandler) {
        IBinder startTransition = this.mTransitions.startTransition(18, windowContainerTransaction, transitionHandler);
        this.mPendingDismiss = startTransition;
        return startTransition;
    }

    /* access modifiers changed from: package-private */
    public void onFinish() {
        if (this.mAnimations.isEmpty()) {
            this.mOnFinish.run();
            SurfaceControl.Transaction transaction = this.mFinishTransaction;
            if (transaction != null) {
                transaction.apply();
                this.mTransactionPool.release(this.mFinishTransaction);
                this.mFinishTransaction = null;
            }
            this.mFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
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
    }

    private void startExampleAnimation(SurfaceControl surfaceControl, boolean z) {
        float f = z ? 1.0f : 0.0f;
        float f2 = 1.0f - f;
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f2, f});
        ofFloat.setDuration(500);
        ofFloat.addUpdateListener(new SplitScreenTransitions$$ExternalSyntheticLambda1(acquire, surfaceControl, f2, f));
        final SplitScreenTransitions$$ExternalSyntheticLambda2 splitScreenTransitions$$ExternalSyntheticLambda2 = new SplitScreenTransitions$$ExternalSyntheticLambda2(this, acquire, surfaceControl, f, ofFloat);
        ofFloat.addListener(new Animator.AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda2.run();
            }

            public void onAnimationCancel(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda2.run();
            }
        });
        this.mAnimations.add(ofFloat);
        ShellExecutor animExecutor = this.mTransitions.getAnimExecutor();
        Objects.requireNonNull(ofFloat);
        animExecutor.execute(new SplitScreenTransitions$$ExternalSyntheticLambda3(ofFloat));
    }

    static /* synthetic */ void lambda$startExampleAnimation$1(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, float f2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        transaction.setAlpha(surfaceControl, (f * (1.0f - animatedFraction)) + (f2 * animatedFraction));
        transaction.apply();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startExampleAnimation$3$com-android-wm-shell-stagesplit-SplitScreenTransitions */
    public /* synthetic */ void mo51003xf2c7c575(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, f);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new SplitScreenTransitions$$ExternalSyntheticLambda4(this, valueAnimator));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startExampleAnimation$2$com-android-wm-shell-stagesplit-SplitScreenTransitions */
    public /* synthetic */ void mo51002xf1917296(ValueAnimator valueAnimator) {
        this.mAnimations.remove((Object) valueAnimator);
        onFinish();
    }

    private void startExampleResizeAnimation(SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(500);
        ofFloat.addUpdateListener(new SplitScreenTransitions$$ExternalSyntheticLambda5(acquire, surfaceControl, rect, rect2));
        final SplitScreenTransitions$$ExternalSyntheticLambda6 splitScreenTransitions$$ExternalSyntheticLambda6 = new SplitScreenTransitions$$ExternalSyntheticLambda6(this, acquire, surfaceControl, rect2, ofFloat);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda6.run();
            }

            public void onAnimationCancel(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda6.run();
            }
        });
        this.mAnimations.add(ofFloat);
        ShellExecutor animExecutor = this.mTransitions.getAnimExecutor();
        Objects.requireNonNull(ofFloat);
        animExecutor.execute(new SplitScreenTransitions$$ExternalSyntheticLambda3(ofFloat));
    }

    static /* synthetic */ void lambda$startExampleResizeAnimation$4(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        transaction.setWindowCrop(surfaceControl, (int) ((((float) rect.width()) * f) + (((float) rect2.width()) * animatedFraction)), (int) ((((float) rect.height()) * f) + (((float) rect2.height()) * animatedFraction)));
        transaction.setPosition(surfaceControl, (((float) rect.left) * f) + (((float) rect2.left) * animatedFraction), (((float) rect.top) * f) + (((float) rect2.top) * animatedFraction));
        transaction.apply();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startExampleResizeAnimation$6$com-android-wm-shell-stagesplit-SplitScreenTransitions */
    public /* synthetic */ void mo51005x44cf1226(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, ValueAnimator valueAnimator) {
        transaction.setWindowCrop(surfaceControl, 0, 0);
        transaction.setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new SplitScreenTransitions$$ExternalSyntheticLambda0(this, valueAnimator));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startExampleResizeAnimation$5$com-android-wm-shell-stagesplit-SplitScreenTransitions */
    public /* synthetic */ void mo51004x4398bf47(ValueAnimator valueAnimator) {
        this.mAnimations.remove((Object) valueAnimator);
        onFinish();
    }
}
