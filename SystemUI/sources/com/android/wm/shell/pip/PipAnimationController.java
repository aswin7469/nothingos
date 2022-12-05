package com.android.wm.shell.pip;

import android.animation.AnimationHandler;
import android.animation.Animator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.app.TaskInfo;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.RotationUtils;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import java.util.Objects;
/* loaded from: classes2.dex */
public class PipAnimationController {
    private PipTransitionAnimator mCurrentAnimator;
    private final ThreadLocal<AnimationHandler> mSfAnimationHandlerThreadLocal = ThreadLocal.withInitial(PipAnimationController$$ExternalSyntheticLambda0.INSTANCE);
    private final PipSurfaceTransactionHelper mSurfaceTransactionHelper;

    /* loaded from: classes2.dex */
    public static class PipAnimationCallback {
        public void onPipAnimationCancel(TaskInfo taskInfo, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }

        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }

        public void onPipAnimationStart(TaskInfo taskInfo, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }
    }

    /* loaded from: classes2.dex */
    public static class PipTransactionHandler {
        public boolean handlePipTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
            throw null;
        }
    }

    public static boolean isInPipDirection(int i) {
        return i == 2;
    }

    public static boolean isOutPipDirection(int i) {
        return i == 3 || i == 4;
    }

    public static boolean isRemovePipDirection(int i) {
        return i == 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ AnimationHandler lambda$new$0() {
        AnimationHandler animationHandler = new AnimationHandler();
        animationHandler.setProvider(new SfVsyncFrameCallbackProvider());
        return animationHandler;
    }

    public PipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
    }

    @VisibleForTesting
    public PipTransitionAnimator getAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, float f, float f2) {
        PipTransitionAnimator pipTransitionAnimator = this.mCurrentAnimator;
        if (pipTransitionAnimator == null) {
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofAlpha(taskInfo, surfaceControl, rect, f, f2));
        } else if (pipTransitionAnimator.getAnimationType() == 1 && Objects.equals(rect, this.mCurrentAnimator.getDestinationBounds()) && this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.updateEndValue(Float.valueOf(f2));
        } else {
            this.mCurrentAnimator.cancel();
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofAlpha(taskInfo, surfaceControl, rect, f, f2));
        }
        return this.mCurrentAnimator;
    }

    @VisibleForTesting
    public PipTransitionAnimator getAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, Rect rect4, int i, float f, int i2) {
        PipTransitionAnimator pipTransitionAnimator = this.mCurrentAnimator;
        if (pipTransitionAnimator == null) {
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofBounds(taskInfo, surfaceControl, rect2, rect2, rect3, rect4, i, 0.0f, i2));
        } else if (pipTransitionAnimator.getAnimationType() == 1 && this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.setDestinationBounds(rect3);
        } else if (this.mCurrentAnimator.getAnimationType() == 0 && this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.setDestinationBounds(rect3);
            this.mCurrentAnimator.updateEndValue(new Rect(rect3));
        } else {
            this.mCurrentAnimator.cancel();
            this.mCurrentAnimator = setupPipTransitionAnimator(PipTransitionAnimator.ofBounds(taskInfo, surfaceControl, rect, rect2, rect3, rect4, i, f, i2));
        }
        return this.mCurrentAnimator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PipTransitionAnimator getCurrentAnimator() {
        return this.mCurrentAnimator;
    }

    private PipTransitionAnimator setupPipTransitionAnimator(PipTransitionAnimator pipTransitionAnimator) {
        pipTransitionAnimator.setSurfaceTransactionHelper(this.mSurfaceTransactionHelper);
        pipTransitionAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        pipTransitionAnimator.setFloatValues(0.0f, 1.0f);
        pipTransitionAnimator.setAnimationHandler(this.mSfAnimationHandlerThreadLocal.get());
        return pipTransitionAnimator;
    }

    /* loaded from: classes2.dex */
    public static abstract class PipTransitionAnimator<T> extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private final int mAnimationType;
        private T mBaseValue;
        protected SurfaceControl mContentOverlay;
        protected T mCurrentValue;
        private final Rect mDestinationBounds;
        private T mEndValue;
        private final SurfaceControl mLeash;
        private PipAnimationCallback mPipAnimationCallback;
        private PipTransactionHandler mPipTransactionHandler;
        protected T mStartValue;
        private float mStartingAngle;
        private PipSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
        private PipSurfaceTransactionHelper mSurfaceTransactionHelper;
        private final TaskInfo mTaskInfo;
        private int mTransitionDirection;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i) {
        }

        void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        private PipTransitionAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, int i, Rect rect, T t, T t2, T t3, float f) {
            Rect rect2 = new Rect();
            this.mDestinationBounds = rect2;
            this.mTaskInfo = taskInfo;
            this.mLeash = surfaceControl;
            this.mAnimationType = i;
            rect2.set(rect);
            this.mBaseValue = t;
            this.mStartValue = t2;
            this.mEndValue = t3;
            this.mStartingAngle = f;
            addListener(this);
            addUpdateListener(this);
            this.mSurfaceControlTransactionFactory = PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0.INSTANCE;
            this.mTransitionDirection = 0;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            onStartTransaction(this.mLeash, newSurfaceControlTransaction());
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationStart(this.mTaskInfo, this);
            }
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            applySurfaceControlTransaction(this.mLeash, newSurfaceControlTransaction(), valueAnimator.getAnimatedFraction());
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            onEndTransaction(this.mLeash, newSurfaceControlTransaction, this.mTransitionDirection);
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationEnd(this.mTaskInfo, newSurfaceControlTransaction, this);
            }
            this.mTransitionDirection = 0;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationCancel(this.mTaskInfo, this);
            }
            this.mTransitionDirection = 0;
        }

        @VisibleForTesting
        public int getAnimationType() {
            return this.mAnimationType;
        }

        @VisibleForTesting
        public PipTransitionAnimator<T> setPipAnimationCallback(PipAnimationCallback pipAnimationCallback) {
            this.mPipAnimationCallback = pipAnimationCallback;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PipTransitionAnimator<T> setPipTransactionHandler(PipTransactionHandler pipTransactionHandler) {
            this.mPipTransactionHandler = pipTransactionHandler;
            return this;
        }

        boolean handlePipTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
            PipTransactionHandler pipTransactionHandler = this.mPipTransactionHandler;
            if (pipTransactionHandler != null) {
                return pipTransactionHandler.handlePipTransaction(surfaceControl, transaction, rect);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SurfaceControl getContentOverlay() {
            return this.mContentOverlay;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PipTransitionAnimator<T> setUseContentOverlay(Context context) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            SurfaceControl surfaceControl = this.mContentOverlay;
            if (surfaceControl != null) {
                newSurfaceControlTransaction.remove(surfaceControl);
                newSurfaceControlTransaction.apply();
            }
            SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setCallsite("PipAnimation").setName("PipContentOverlay").setColorLayer().build();
            this.mContentOverlay = build;
            newSurfaceControlTransaction.show(build);
            newSurfaceControlTransaction.setLayer(this.mContentOverlay, Integer.MAX_VALUE);
            newSurfaceControlTransaction.setColor(this.mContentOverlay, getContentOverlayColor(context));
            newSurfaceControlTransaction.setAlpha(this.mContentOverlay, 0.0f);
            newSurfaceControlTransaction.reparent(this.mContentOverlay, this.mLeash);
            newSurfaceControlTransaction.apply();
            return this;
        }

        private float[] getContentOverlayColor(Context context) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842801});
            try {
                int color = obtainStyledAttributes.getColor(0, 0);
                return new float[]{Color.red(color) / 255.0f, Color.green(color) / 255.0f, Color.blue(color) / 255.0f};
            } finally {
                obtainStyledAttributes.recycle();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void clearContentOverlay() {
            this.mContentOverlay = null;
        }

        @VisibleForTesting
        public int getTransitionDirection() {
            return this.mTransitionDirection;
        }

        @VisibleForTesting
        public PipTransitionAnimator<T> setTransitionDirection(int i) {
            if (i != 1) {
                this.mTransitionDirection = i;
            }
            return this;
        }

        T getStartValue() {
            return this.mStartValue;
        }

        T getBaseValue() {
            return this.mBaseValue;
        }

        @VisibleForTesting
        public T getEndValue() {
            return this.mEndValue;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Rect getDestinationBounds() {
            return this.mDestinationBounds;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setDestinationBounds(Rect rect) {
            this.mDestinationBounds.set(rect);
            if (this.mAnimationType == 1) {
                onStartTransaction(this.mLeash, newSurfaceControlTransaction());
            }
        }

        void setCurrentValue(T t) {
            this.mCurrentValue = t;
        }

        boolean shouldApplyCornerRadius() {
            return !PipAnimationController.isOutPipDirection(this.mTransitionDirection);
        }

        boolean inScaleTransition() {
            if (this.mAnimationType != 0) {
                return false;
            }
            int transitionDirection = getTransitionDirection();
            return !PipAnimationController.isInPipDirection(transitionDirection) && !PipAnimationController.isOutPipDirection(transitionDirection);
        }

        public void updateEndValue(T t) {
            this.mEndValue = t;
        }

        protected SurfaceControl.Transaction newSurfaceControlTransaction() {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
            return transaction;
        }

        @VisibleForTesting
        public void setSurfaceControlTransactionFactory(PipSurfaceTransactionHelper.SurfaceControlTransactionFactory surfaceControlTransactionFactory) {
            this.mSurfaceControlTransactionFactory = surfaceControlTransactionFactory;
        }

        PipSurfaceTransactionHelper getSurfaceTransactionHelper() {
            return this.mSurfaceTransactionHelper;
        }

        void setSurfaceTransactionHelper(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
            this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        }

        static PipTransitionAnimator<Float> ofAlpha(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, float f, float f2) {
            return new PipTransitionAnimator<Float>(taskInfo, surfaceControl, 1, rect, Float.valueOf(f), Float.valueOf(f), Float.valueOf(f2), 0.0f) { // from class: com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator.1
                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                void applySurfaceControlTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, float f3) {
                    float floatValue = (((Float) getStartValue()).floatValue() * (1.0f - f3)) + (((Float) getEndValue()).floatValue() * f3);
                    setCurrentValue(Float.valueOf(floatValue));
                    getSurfaceTransactionHelper().alpha(transaction, surfaceControl2, floatValue).round(transaction, surfaceControl2, shouldApplyCornerRadius());
                    transaction.apply();
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                void onStartTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction) {
                    if (getTransitionDirection() == 5) {
                        return;
                    }
                    getSurfaceTransactionHelper().resetScale(transaction, surfaceControl2, getDestinationBounds()).crop(transaction, surfaceControl2, getDestinationBounds()).round(transaction, surfaceControl2, shouldApplyCornerRadius());
                    transaction.show(surfaceControl2);
                    transaction.apply();
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public void updateEndValue(Float f3) {
                    super.updateEndValue((AnonymousClass1) f3);
                    this.mStartValue = this.mCurrentValue;
                }
            };
        }

        static PipTransitionAnimator<Rect> ofBounds(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, Rect rect2, final Rect rect3, final Rect rect4, final int i, final float f, final int i2) {
            Rect rect5;
            final Rect rect6;
            final Rect rect7;
            Rect rect8;
            final boolean isOutPipDirection = PipAnimationController.isOutPipDirection(i);
            if (isOutPipDirection) {
                rect5 = new Rect(rect3);
            } else {
                rect5 = new Rect(rect);
            }
            final Rect rect9 = rect5;
            if (i2 == 1 || i2 == 3) {
                Rect rect10 = new Rect(rect3);
                Rect rect11 = new Rect(rect3);
                RotationUtils.rotateBounds(rect11, rect9, i2);
                rect6 = rect10;
                rect7 = rect11;
                rect8 = isOutPipDirection ? rect11 : rect9;
            } else {
                rect7 = null;
                rect6 = null;
                rect8 = rect9;
            }
            final Rect rect12 = rect4 == null ? null : new Rect(rect4.left - rect8.left, rect4.top - rect8.top, rect8.right - rect4.right, rect8.bottom - rect4.bottom);
            final Rect rect13 = new Rect(0, 0, 0, 0);
            final Rect rect14 = rect8;
            return new PipTransitionAnimator<Rect>(taskInfo, surfaceControl, 0, rect3, new Rect(rect), new Rect(rect2), new Rect(rect3), f) { // from class: com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator.2
                private final RectEvaluator mRectEvaluator = new RectEvaluator(new Rect());
                private final RectEvaluator mInsetsEvaluator = new RectEvaluator(new Rect());

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                void applySurfaceControlTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, float f2) {
                    Rect baseValue = getBaseValue();
                    Rect startValue = getStartValue();
                    Rect endValue = getEndValue();
                    SurfaceControl surfaceControl3 = this.mContentOverlay;
                    if (surfaceControl3 != null) {
                        transaction.setAlpha(surfaceControl3, f2 < 0.5f ? 0.0f : (f2 - 0.5f) * 2.0f);
                    }
                    if (rect7 != null) {
                        applyRotation(transaction, surfaceControl2, f2, startValue, endValue);
                        return;
                    }
                    Rect evaluate = this.mRectEvaluator.evaluate(f2, startValue, endValue);
                    float f3 = (1.0f - f2) * f;
                    setCurrentValue(evaluate);
                    if (inScaleTransition() || rect4 == null) {
                        if (isOutPipDirection) {
                            getSurfaceTransactionHelper().scale(transaction, surfaceControl2, endValue, evaluate);
                        } else {
                            getSurfaceTransactionHelper().scale(transaction, surfaceControl2, baseValue, evaluate, f3).round(transaction, surfaceControl2, baseValue, evaluate);
                        }
                    } else {
                        Rect computeInsets = computeInsets(f2);
                        getSurfaceTransactionHelper().scaleAndCrop(transaction, surfaceControl2, rect9, evaluate, computeInsets);
                        if (shouldApplyCornerRadius()) {
                            Rect rect15 = new Rect(evaluate);
                            rect15.inset(computeInsets);
                            getSurfaceTransactionHelper().round(transaction, surfaceControl2, rect14, rect15);
                        }
                    }
                    if (handlePipTransaction(surfaceControl2, transaction, evaluate)) {
                        return;
                    }
                    transaction.apply();
                }

                private void applyRotation(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl2, float f2, Rect rect15, Rect rect16) {
                    float f3;
                    float f4;
                    int i3;
                    int i4;
                    if (!rect16.equals(rect6)) {
                        rect7.set(rect3);
                        RotationUtils.rotateBounds(rect7, rect9, i2);
                        rect6.set(rect16);
                    }
                    Rect evaluate = this.mRectEvaluator.evaluate(f2, rect15, rect7);
                    setCurrentValue(evaluate);
                    Rect computeInsets = computeInsets(f2);
                    if (i2 == 1) {
                        f3 = 90.0f * f2;
                        int i5 = rect16.right;
                        int i6 = rect15.left;
                        f4 = ((i5 - i6) * f2) + i6;
                        i3 = rect16.top;
                        i4 = rect15.top;
                    } else {
                        f3 = (-90.0f) * f2;
                        int i7 = rect16.left;
                        int i8 = rect15.left;
                        f4 = ((i7 - i8) * f2) + i8;
                        i3 = rect16.bottom;
                        i4 = rect15.top;
                    }
                    getSurfaceTransactionHelper().rotateAndScaleWithCrop(transaction, surfaceControl2, rect14, evaluate, computeInsets, f3, f4, (f2 * (i3 - i4)) + i4, isOutPipDirection, i2 == 3).round(transaction, surfaceControl2, rect14, evaluate);
                    transaction.apply();
                }

                private Rect computeInsets(float f2) {
                    Rect rect15 = rect12;
                    if (rect15 == null) {
                        return rect13;
                    }
                    boolean z = isOutPipDirection;
                    Rect rect16 = z ? rect15 : rect13;
                    if (z) {
                        rect15 = rect13;
                    }
                    return this.mInsetsEvaluator.evaluate(f2, rect16, rect15);
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                void onStartTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction) {
                    getSurfaceTransactionHelper().alpha(transaction, surfaceControl2, 1.0f).round(transaction, surfaceControl2, shouldApplyCornerRadius());
                    if (PipAnimationController.isInPipDirection(i)) {
                        transaction.setWindowCrop(surfaceControl2, getStartValue());
                    }
                    transaction.show(surfaceControl2);
                    transaction.apply();
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                void onEndTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, int i3) {
                    Rect destinationBounds = getDestinationBounds();
                    getSurfaceTransactionHelper().resetScale(transaction, surfaceControl2, destinationBounds);
                    if (PipAnimationController.isOutPipDirection(i3)) {
                        transaction.setMatrix(surfaceControl2, 1.0f, 0.0f, 0.0f, 1.0f);
                        transaction.setPosition(surfaceControl2, 0.0f, 0.0f);
                        transaction.setWindowCrop(surfaceControl2, 0, 0);
                        return;
                    }
                    getSurfaceTransactionHelper().crop(transaction, surfaceControl2, destinationBounds);
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public void updateEndValue(Rect rect15) {
                    T t;
                    super.updateEndValue((AnonymousClass2) rect15);
                    T t2 = this.mStartValue;
                    if (t2 == null || (t = this.mCurrentValue) == null) {
                        return;
                    }
                    ((Rect) t2).set((Rect) t);
                }
            };
        }
    }
}
