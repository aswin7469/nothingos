package com.android.wm.shell.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.AttributeCache;
import com.android.internal.policy.TransitionAnimation;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class DefaultTransitionHandler implements Transitions.TransitionHandler {
    private final ShellExecutor mAnimExecutor;
    private final ShellExecutor mMainExecutor;
    private final TransactionPool mTransactionPool;
    private final TransitionAnimation mTransitionAnimation;
    private final ArrayMap<IBinder, ArrayList<Animator>> mAnimations = new ArrayMap<>();
    private final Rect mInsets = new Rect(0, 0, 0, 0);
    private float mTransitionAnimationScaleSetting = 1.0f;

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultTransitionHandler(TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
        this.mAnimExecutor = shellExecutor2;
        this.mTransitionAnimation = new TransitionAnimation(context, false, "ShellTransitions");
        AttributeCache.init(context);
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(final IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        Animation loadAnimation;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -146110597, 0, "start default transition animation, info = %s", String.valueOf(transitionInfo));
        }
        if (this.mAnimations.containsKey(iBinder)) {
            throw new IllegalStateException("Got a duplicate startAnimation call for " + iBinder);
        }
        final ArrayList<Animator> arrayList = new ArrayList<>();
        this.mAnimations.put(iBinder, arrayList);
        Runnable runnable = new Runnable() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DefaultTransitionHandler.this.lambda$startAnimation$0(arrayList, iBinder, transitionFinishCallback);
            }
        };
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change.getMode() == 6) {
                transaction.setPosition(change.getLeash(), change.getEndAbsBounds().left - change.getEndRelOffset().x, change.getEndAbsBounds().top - change.getEndRelOffset().y);
                if (change.getTaskInfo() != null) {
                    transaction.setWindowCrop(change.getLeash(), change.getEndAbsBounds().width(), change.getEndAbsBounds().height());
                }
            }
            if (TransitionInfo.isIndependent(change, transitionInfo) && (loadAnimation = loadAnimation(transitionInfo.getType(), transitionInfo.getFlags(), change)) != null) {
                startAnimInternal(arrayList, loadAnimation, change.getLeash(), runnable);
            }
        }
        transaction.apply();
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$0(ArrayList arrayList, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (!arrayList.isEmpty()) {
            return;
        }
        this.mAnimations.remove(iBinder);
        transitionFinishCallback.onTransitionFinished(null, null);
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void setAnimScaleSetting(float f) {
        this.mTransitionAnimationScaleSetting = f;
    }

    private Animation loadAnimation(int i, int i2, TransitionInfo.Change change) {
        boolean isOpeningType = Transitions.isOpeningType(i);
        int mode = change.getMode();
        int flags = change.getFlags();
        Animation animation = null;
        if (i == 5) {
            animation = this.mTransitionAnimation.createRelaunchAnimation(change.getStartAbsBounds(), this.mInsets, change.getEndAbsBounds());
        } else {
            boolean z = false;
            if (i == 7) {
                TransitionAnimation transitionAnimation = this.mTransitionAnimation;
                if ((flags & 1) != 0) {
                    z = true;
                }
                animation = transitionAnimation.loadKeyguardExitAnimation(i2, z);
            } else if (i == 9) {
                animation = this.mTransitionAnimation.loadKeyguardUnoccludeAnimation();
            } else if (mode != 1 || !isOpeningType) {
                if (mode != 3 || !isOpeningType) {
                    if (mode != 2 || isOpeningType) {
                        if (mode != 4 || isOpeningType) {
                            if (mode == 6) {
                                animation = new AlphaAnimation(1.0f, 1.0f);
                                animation.setDuration(336L);
                            }
                        } else if ((flags & 16) != 0) {
                            animation = this.mTransitionAnimation.loadVoiceActivityExitAnimation(false);
                        } else {
                            animation = this.mTransitionAnimation.loadDefaultAnimationAttr(15);
                        }
                    } else if ((flags & 16) != 0) {
                        animation = this.mTransitionAnimation.loadVoiceActivityExitAnimation(false);
                    } else if (change.getTaskInfo() != null) {
                        animation = this.mTransitionAnimation.loadDefaultAnimationAttr(11);
                    } else {
                        animation = this.mTransitionAnimation.loadDefaultAnimationRes((4 & flags) == 0 ? 17432590 : 17432593);
                    }
                } else if ((flags & 8) != 0) {
                    return null;
                } else {
                    if ((flags & 16) != 0) {
                        animation = this.mTransitionAnimation.loadVoiceActivityOpenAnimation(true);
                    } else {
                        animation = this.mTransitionAnimation.loadDefaultAnimationAttr(12);
                    }
                }
            } else if ((flags & 8) != 0) {
                return null;
            } else {
                if ((flags & 16) != 0) {
                    animation = this.mTransitionAnimation.loadVoiceActivityOpenAnimation(true);
                } else if (change.getTaskInfo() != null) {
                    animation = this.mTransitionAnimation.loadDefaultAnimationAttr(8);
                } else {
                    animation = this.mTransitionAnimation.loadDefaultAnimationRes((4 & flags) == 0 ? 17432591 : 17432594);
                }
            }
        }
        if (animation != null) {
            Rect startAbsBounds = change.getStartAbsBounds();
            Rect endAbsBounds = change.getEndAbsBounds();
            animation.restrictDuration(3000L);
            animation.initialize(endAbsBounds.width(), endAbsBounds.height(), startAbsBounds.width(), startAbsBounds.height());
            animation.scaleCurrentDuration(this.mTransitionAnimationScaleSetting);
        }
        return animation;
    }

    private void startAnimInternal(final ArrayList<Animator> arrayList, final Animation animation, final SurfaceControl surfaceControl, final Runnable runnable) {
        final SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        final Transformation transformation = new Transformation();
        final float[] fArr = new float[9];
        ofFloat.overrideDurationScale(1.0f);
        ofFloat.setDuration(animation.computeDurationHint());
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DefaultTransitionHandler.lambda$startAnimInternal$1(ofFloat, acquire, surfaceControl, animation, transformation, fArr, valueAnimator);
            }
        });
        final Runnable runnable2 = new Runnable() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DefaultTransitionHandler.this.lambda$startAnimInternal$3(ofFloat, acquire, surfaceControl, animation, transformation, fArr, arrayList, runnable);
            }
        };
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable2.run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                runnable2.run();
            }
        });
        arrayList.add(ofFloat);
        this.mAnimExecutor.execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startAnimInternal$1(ValueAnimator valueAnimator, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, ValueAnimator valueAnimator2) {
        applyTransformation(Math.min(valueAnimator.getDuration(), valueAnimator.getCurrentPlayTime()), transaction, surfaceControl, animation, transformation, fArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimInternal$3(final ValueAnimator valueAnimator, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, final ArrayList arrayList, final Runnable runnable) {
        applyTransformation(valueAnimator.getDuration(), transaction, surfaceControl, animation, transformation, fArr);
        this.mTransactionPool.release(transaction);
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DefaultTransitionHandler.lambda$startAnimInternal$2(arrayList, valueAnimator, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startAnimInternal$2(ArrayList arrayList, ValueAnimator valueAnimator, Runnable runnable) {
        arrayList.remove(valueAnimator);
        runnable.run();
    }

    private static void applyTransformation(long j, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr) {
        animation.getTransformation(j, transformation);
        transaction.setMatrix(surfaceControl, transformation.getMatrix(), fArr);
        transaction.setAlpha(surfaceControl, transformation.getAlpha());
        transaction.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        transaction.apply();
    }
}
