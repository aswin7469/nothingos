package com.android.p019wm.shell.onehanded;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.BaseInterpolator;
import android.window.WindowContainerToken;
import com.android.p019wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.android.wm.shell.onehanded.OneHandedAnimationController */
public class OneHandedAnimationController {
    private static final float FRACTION_END = 1.0f;
    private static final float FRACTION_START = 0.0f;
    private static final String TAG = "OneHandedAnimationController";
    public static final int TRANSITION_DIRECTION_EXIT = 2;
    public static final int TRANSITION_DIRECTION_NONE = 0;
    public static final int TRANSITION_DIRECTION_TRIGGER = 1;
    private final HashMap<WindowContainerToken, OneHandedTransitionAnimator> mAnimatorMap = new HashMap<>();
    private final OneHandedInterpolator mInterpolator;
    private final OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.onehanded.OneHandedAnimationController$TransitionDirection */
    public @interface TransitionDirection {
    }

    public OneHandedAnimationController(Context context) {
        this.mSurfaceTransactionHelper = new OneHandedSurfaceTransactionHelper(context);
        this.mInterpolator = new OneHandedInterpolator();
    }

    /* access modifiers changed from: package-private */
    public OneHandedTransitionAnimator getAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
        OneHandedTransitionAnimator oneHandedTransitionAnimator = this.mAnimatorMap.get(windowContainerToken);
        if (oneHandedTransitionAnimator == null) {
            this.mAnimatorMap.put(windowContainerToken, setupOneHandedTransitionAnimator(OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f, f2, rect)));
        } else if (oneHandedTransitionAnimator.isRunning()) {
            oneHandedTransitionAnimator.updateEndValue(f2);
        } else {
            oneHandedTransitionAnimator.cancel();
            this.mAnimatorMap.put(windowContainerToken, setupOneHandedTransitionAnimator(OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f, f2, rect)));
        }
        return this.mAnimatorMap.get(windowContainerToken);
    }

    /* access modifiers changed from: package-private */
    public HashMap<WindowContainerToken, OneHandedTransitionAnimator> getAnimatorMap() {
        return this.mAnimatorMap;
    }

    /* access modifiers changed from: package-private */
    public boolean isAnimatorsConsumed() {
        return this.mAnimatorMap.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public void removeAnimator(WindowContainerToken windowContainerToken) {
        OneHandedTransitionAnimator remove = this.mAnimatorMap.remove(windowContainerToken);
        if (remove != null && remove.isRunning()) {
            remove.cancel();
        }
    }

    /* access modifiers changed from: package-private */
    public OneHandedTransitionAnimator setupOneHandedTransitionAnimator(OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        oneHandedTransitionAnimator.setSurfaceTransactionHelper(this.mSurfaceTransactionHelper);
        oneHandedTransitionAnimator.setInterpolator(this.mInterpolator);
        oneHandedTransitionAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return oneHandedTransitionAnimator;
    }

    /* renamed from: com.android.wm.shell.onehanded.OneHandedAnimationController$OneHandedTransitionAnimator */
    public static abstract class OneHandedTransitionAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private float mCurrentValue;
        private float mEndValue;
        private final SurfaceControl mLeash;
        private final List<OneHandedAnimationCallback> mOneHandedAnimationCallbacks;
        private float mStartValue;
        private OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
        private OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;
        private final WindowContainerToken mToken;
        private int mTransitionDirection;

        public static /* synthetic */ SurfaceControl.Transaction $r8$lambda$VwOlxiZ0_YlCJjS6K81dTETC6w4() {
            return new SurfaceControl.Transaction();
        }

        /* access modifiers changed from: package-private */
        public abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        public void onAnimationRepeat(Animator animator) {
        }

        /* access modifiers changed from: package-private */
        public void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        /* access modifiers changed from: package-private */
        public void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        public /* bridge */ /* synthetic */ Animator setDuration(long j) {
            return super.setDuration(j);
        }

        private OneHandedTransitionAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2) {
            this.mOneHandedAnimationCallbacks = new ArrayList();
            this.mLeash = surfaceControl;
            this.mToken = windowContainerToken;
            this.mStartValue = f;
            this.mEndValue = f2;
            addListener(this);
            addUpdateListener(this);
            this.mSurfaceControlTransactionFactory = new C3490x55a9da47();
            this.mTransitionDirection = 0;
        }

        public void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            this.mOneHandedAnimationCallbacks.forEach(new C3491x55a9da48(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAnimationStart$0$com-android-wm-shell-onehanded-OneHandedAnimationController$OneHandedTransitionAnimator */
        public /* synthetic */ void mo49836x65730abd(OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationStart(this);
        }

        public void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            onEndTransaction(this.mLeash, newSurfaceControlTransaction);
            this.mOneHandedAnimationCallbacks.forEach(new C3492x55a9da49(this, newSurfaceControlTransaction));
            this.mOneHandedAnimationCallbacks.clear();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAnimationEnd$1$com-android-wm-shell-onehanded-OneHandedAnimationController$OneHandedTransitionAnimator */
        public /* synthetic */ void mo49835xb3944cf7(SurfaceControl.Transaction transaction, OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationEnd(transaction, this);
        }

        public void onAnimationCancel(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            this.mOneHandedAnimationCallbacks.forEach(new C3493x55a9da4a(this));
            this.mOneHandedAnimationCallbacks.clear();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAnimationCancel$2$com-android-wm-shell-onehanded-OneHandedAnimationController$OneHandedTransitionAnimator */
        public /* synthetic */ void mo49834x1e131427(OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationCancel(this);
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            this.mOneHandedAnimationCallbacks.forEach(new C3494x55a9da4b(this, newSurfaceControlTransaction));
            applySurfaceControlTransaction(this.mLeash, newSurfaceControlTransaction, valueAnimator.getAnimatedFraction());
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAnimationUpdate$3$com-android-wm-shell-onehanded-OneHandedAnimationController$OneHandedTransitionAnimator */
        public /* synthetic */ void mo49837xcb9896b7(SurfaceControl.Transaction transaction, OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onAnimationUpdate(transaction, 0.0f, this.mCurrentValue);
        }

        /* access modifiers changed from: package-private */
        public OneHandedSurfaceTransactionHelper getSurfaceTransactionHelper() {
            return this.mSurfaceTransactionHelper;
        }

        /* access modifiers changed from: package-private */
        public void setSurfaceTransactionHelper(OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper) {
            this.mSurfaceTransactionHelper = oneHandedSurfaceTransactionHelper;
        }

        /* access modifiers changed from: package-private */
        public OneHandedTransitionAnimator addOneHandedAnimationCallback(OneHandedAnimationCallback oneHandedAnimationCallback) {
            if (oneHandedAnimationCallback != null) {
                this.mOneHandedAnimationCallbacks.add(oneHandedAnimationCallback);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public WindowContainerToken getToken() {
            return this.mToken;
        }

        /* access modifiers changed from: package-private */
        public float getDestinationOffset() {
            return this.mEndValue - this.mStartValue;
        }

        /* access modifiers changed from: package-private */
        public int getTransitionDirection() {
            return this.mTransitionDirection;
        }

        /* access modifiers changed from: package-private */
        public OneHandedTransitionAnimator setTransitionDirection(int i) {
            this.mTransitionDirection = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public float getStartValue() {
            return this.mStartValue;
        }

        /* access modifiers changed from: package-private */
        public float getEndValue() {
            return this.mEndValue;
        }

        /* access modifiers changed from: package-private */
        public void setCurrentValue(float f) {
            this.mCurrentValue = f;
        }

        /* access modifiers changed from: package-private */
        public void updateEndValue(float f) {
            this.mEndValue = f;
        }

        /* access modifiers changed from: package-private */
        public SurfaceControl.Transaction newSurfaceControlTransaction() {
            return this.mSurfaceControlTransactionFactory.getTransaction();
        }

        static OneHandedTransitionAnimator ofYOffset(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
            return new OneHandedTransitionAnimator(windowContainerToken, surfaceControl, f, f2, rect) {
                private final Rect mTmpRect;
                final /* synthetic */ Rect val$displayBounds;

                private float getCastedFractionValue(float f, float f2, float f3) {
                    return (f * (1.0f - f3)) + (f2 * f3) + 0.5f;
                }

                {
                    this.val$displayBounds = r11;
                    this.mTmpRect = new Rect(r11);
                }

                public /* bridge */ /* synthetic */ Animator setDuration(long j) {
                    return super.setDuration(j);
                }

                /* access modifiers changed from: package-private */
                public void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f) {
                    float castedFractionValue = getCastedFractionValue(getStartValue(), getEndValue(), f);
                    Rect rect = this.mTmpRect;
                    rect.set(rect.left, this.mTmpRect.top + Math.round(castedFractionValue), this.mTmpRect.right, this.mTmpRect.bottom + Math.round(castedFractionValue));
                    setCurrentValue(castedFractionValue);
                    getSurfaceTransactionHelper().crop(transaction, surfaceControl, this.mTmpRect).round(transaction, surfaceControl).translate(transaction, surfaceControl, castedFractionValue);
                    transaction.apply();
                }

                /* access modifiers changed from: package-private */
                public void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
                    getSurfaceTransactionHelper().crop(transaction, surfaceControl, this.mTmpRect).round(transaction, surfaceControl).translate(transaction, surfaceControl, getStartValue());
                    transaction.apply();
                }
            };
        }
    }

    /* renamed from: com.android.wm.shell.onehanded.OneHandedAnimationController$OneHandedInterpolator */
    public class OneHandedInterpolator extends BaseInterpolator {
        public OneHandedInterpolator() {
        }

        public float getInterpolation(float f) {
            return (float) ((Math.pow(2.0d, (double) (-10.0f * f)) * Math.sin((((double) ((f - 4.0f) / 4.0f)) * 6.283185307179586d) / 4.0d)) + 1.0d);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedAnimationControllerstates: ");
        printWriter.print("  mAnimatorMap=");
        printWriter.println((Object) this.mAnimatorMap);
        OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        if (oneHandedSurfaceTransactionHelper != null) {
            oneHandedSurfaceTransactionHelper.dump(printWriter);
        }
    }
}
