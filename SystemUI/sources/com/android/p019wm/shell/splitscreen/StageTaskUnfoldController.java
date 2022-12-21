package com.android.p019wm.shell.splitscreen;

import android.animation.RectEvaluator;
import android.animation.TypeEvaluator;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldBackgroundController;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.splitscreen.StageTaskUnfoldController */
public class StageTaskUnfoldController implements ShellUnfoldProgressProvider.UnfoldListener, DisplayInsetsController.OnInsetsChangedListener {
    private static final float CROPPING_START_MARGIN_FRACTION = 0.05f;
    private static final TypeEvaluator<Rect> RECT_EVALUATOR = new RectEvaluator(new Rect());
    private final SparseArray<AnimationContext> mAnimationContextByTaskId = new SparseArray<>();
    private final UnfoldBackgroundController mBackgroundController;
    private boolean mBothStagesVisible;
    private final DisplayInsetsController mDisplayInsetsController;
    private final Executor mExecutor;
    /* access modifiers changed from: private */
    public final int mExpandedTaskBarHeight;
    /* access modifiers changed from: private */
    public final Rect mStageBounds = new Rect();
    /* access modifiers changed from: private */
    public InsetsSource mTaskbarInsetsSource;
    private final TransactionPool mTransactionPool;
    private final ShellUnfoldProgressProvider mUnfoldProgressProvider;
    private final float mWindowCornerRadiusPx;

    public StageTaskUnfoldController(Context context, TransactionPool transactionPool, ShellUnfoldProgressProvider shellUnfoldProgressProvider, DisplayInsetsController displayInsetsController, UnfoldBackgroundController unfoldBackgroundController, Executor executor) {
        this.mUnfoldProgressProvider = shellUnfoldProgressProvider;
        this.mTransactionPool = transactionPool;
        this.mExecutor = executor;
        this.mBackgroundController = unfoldBackgroundController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mWindowCornerRadiusPx = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mExpandedTaskBarHeight = context.getResources().getDimensionPixelSize(17105561);
    }

    public void init() {
        this.mUnfoldProgressProvider.addListener(this.mExecutor, this);
        this.mDisplayInsetsController.addInsetsChangedListener(0, this);
    }

    public void insetsChanged(InsetsState insetsState) {
        this.mTaskbarInsetsSource = insetsState.getSource(21);
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            this.mAnimationContextByTaskId.valueAt(size).update();
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (runningTaskInfo.hasParentTask()) {
            this.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new AnimationContext(surfaceControl));
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.hasParentTask()) {
            AnimationContext animationContext = this.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
            if (animationContext != null) {
                SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
                resetSurface(acquire, animationContext);
                acquire.apply();
                this.mTransactionPool.release(acquire);
            }
            this.mAnimationContextByTaskId.remove(runningTaskInfo.taskId);
        }
    }

    public void onStateChangeProgress(float f) {
        if (this.mAnimationContextByTaskId.size() != 0 && this.mBothStagesVisible) {
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            this.mBackgroundController.ensureBackground(acquire);
            for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
                AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                valueAt.mCurrentCropRect.set(RECT_EVALUATOR.evaluate(f, valueAt.mStartCropRect, valueAt.mEndCropRect));
                acquire.setWindowCrop(valueAt.mLeash, valueAt.mCurrentCropRect).setCornerRadius(valueAt.mLeash, this.mWindowCornerRadiusPx);
            }
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    public void onStateChangeFinished() {
        resetTransformations();
    }

    public void onSplitVisibilityChanged(boolean z) {
        this.mBothStagesVisible = z;
        if (!z) {
            resetTransformations();
        }
    }

    public void onLayoutChanged(Rect rect, int i, boolean z) {
        this.mStageBounds.set(rect);
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            this.mAnimationContextByTaskId.valueAt(size).update(i, z);
        }
    }

    private void resetTransformations() {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            resetSurface(acquire, this.mAnimationContextByTaskId.valueAt(size));
        }
        this.mBackgroundController.removeBackground(acquire);
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    private void resetSurface(SurfaceControl.Transaction transaction, AnimationContext animationContext) {
        transaction.setWindowCrop(animationContext.mLeash, (Rect) null).setCornerRadius(animationContext.mLeash, 0.0f);
    }

    /* renamed from: com.android.wm.shell.splitscreen.StageTaskUnfoldController$AnimationContext */
    private class AnimationContext {
        final Rect mCurrentCropRect;
        final Rect mEndCropRect;
        private boolean mIsLandscape;
        final SurfaceControl mLeash;
        private int mSplitPosition;
        final Rect mStartCropRect;

        private AnimationContext(SurfaceControl surfaceControl) {
            this.mStartCropRect = new Rect();
            this.mEndCropRect = new Rect();
            this.mCurrentCropRect = new Rect();
            this.mSplitPosition = -1;
            this.mIsLandscape = false;
            this.mLeash = surfaceControl;
            update();
        }

        /* access modifiers changed from: private */
        public void update(int i, boolean z) {
            this.mSplitPosition = i;
            this.mIsLandscape = z;
            update();
        }

        /* access modifiers changed from: private */
        public void update() {
            Insets insets;
            this.mStartCropRect.set(StageTaskUnfoldController.this.mStageBounds);
            boolean isTaskbarExpanded = isTaskbarExpanded();
            if (isTaskbarExpanded) {
                this.mStartCropRect.inset(StageTaskUnfoldController.this.mTaskbarInsetsSource.calculateVisibleInsets(this.mStartCropRect));
            }
            this.mStartCropRect.offsetTo(0, 0);
            this.mEndCropRect.set(this.mStartCropRect);
            int max = (int) (((float) Math.max(this.mEndCropRect.width(), this.mEndCropRect.height())) * 0.05f);
            if (this.mIsLandscape) {
                insets = getLandscapeMargins(max, isTaskbarExpanded);
            } else {
                insets = getPortraitMargins(max, isTaskbarExpanded);
            }
            this.mStartCropRect.inset(insets);
        }

        private Insets getLandscapeMargins(int i, boolean z) {
            int i2;
            int i3 = 0;
            int i4 = z ? 0 : i;
            if (this.mSplitPosition == 0) {
                i2 = 0;
                i3 = i;
            } else {
                i2 = i;
            }
            return Insets.of(i3, i, i2, i4);
        }

        private Insets getPortraitMargins(int i, boolean z) {
            int i2;
            int i3 = 0;
            if (this.mSplitPosition == 0) {
                i2 = 0;
                i3 = i;
            } else {
                i2 = z ? 0 : i;
            }
            return Insets.of(i, i3, i, i2);
        }

        private boolean isTaskbarExpanded() {
            return StageTaskUnfoldController.this.mTaskbarInsetsSource != null && StageTaskUnfoldController.this.mTaskbarInsetsSource.getFrame().height() >= StageTaskUnfoldController.this.mExpandedTaskBarHeight;
        }
    }
}
