package com.android.p019wm.shell.fullscreen;

import android.animation.RectEvaluator;
import android.animation.TypeEvaluator;
import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.MathUtils;
import android.util.SparseArray;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldBackgroundController;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.fullscreen.FullscreenUnfoldController */
public final class FullscreenUnfoldController implements ShellUnfoldProgressProvider.UnfoldListener, DisplayInsetsController.OnInsetsChangedListener {
    private static final float END_SCALE = 1.0f;
    private static final float[] FLOAT_9 = new float[9];
    private static final float HORIZONTAL_START_MARGIN = 0.08f;
    private static final TypeEvaluator<Rect> RECT_EVALUATOR = new RectEvaluator(new Rect());
    private static final float START_SCALE = 0.94f;
    private static final float VERTICAL_START_MARGIN = 0.03f;
    private final SparseArray<AnimationContext> mAnimationContextByTaskId = new SparseArray<>();
    private final UnfoldBackgroundController mBackgroundController;
    private final DisplayInsetsController mDisplayInsetsController;
    private final Executor mExecutor;
    /* access modifiers changed from: private */
    public final float mExpandedTaskBarHeight;
    private final ShellUnfoldProgressProvider mProgressProvider;
    private InsetsSource mTaskbarInsetsSource;
    private final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();
    private final float mWindowCornerRadiusPx;

    public FullscreenUnfoldController(Context context, Executor executor, UnfoldBackgroundController unfoldBackgroundController, ShellUnfoldProgressProvider shellUnfoldProgressProvider, DisplayInsetsController displayInsetsController) {
        this.mExecutor = executor;
        this.mProgressProvider = shellUnfoldProgressProvider;
        this.mDisplayInsetsController = displayInsetsController;
        this.mWindowCornerRadiusPx = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mExpandedTaskBarHeight = (float) context.getResources().getDimensionPixelSize(17105561);
        this.mBackgroundController = unfoldBackgroundController;
    }

    public void init() {
        this.mProgressProvider.addListener(this.mExecutor, this);
        this.mDisplayInsetsController.addInsetsChangedListener(0, this);
    }

    public void onStateChangeProgress(float f) {
        if (this.mAnimationContextByTaskId.size() != 0) {
            this.mBackgroundController.ensureBackground(this.mTransaction);
            for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
                AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
                valueAt.mCurrentCropRect.set(RECT_EVALUATOR.evaluate(f, valueAt.mStartCropRect, valueAt.mEndCropRect));
                float lerp = MathUtils.lerp(START_SCALE, 1.0f, f);
                valueAt.mMatrix.setScale(lerp, lerp, valueAt.mCurrentCropRect.exactCenterX(), valueAt.mCurrentCropRect.exactCenterY());
                this.mTransaction.setWindowCrop(valueAt.mLeash, valueAt.mCurrentCropRect).setMatrix(valueAt.mLeash, valueAt.mMatrix, FLOAT_9).setCornerRadius(valueAt.mLeash, this.mWindowCornerRadiusPx);
            }
            this.mTransaction.apply();
        }
    }

    public void onStateChangeFinished() {
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            resetSurface(this.mAnimationContextByTaskId.valueAt(size));
        }
        this.mBackgroundController.removeBackground(this.mTransaction);
        this.mTransaction.apply();
    }

    public void insetsChanged(InsetsState insetsState) {
        this.mTaskbarInsetsSource = insetsState.getSource(21);
        for (int size = this.mAnimationContextByTaskId.size() - 1; size >= 0; size--) {
            AnimationContext valueAt = this.mAnimationContextByTaskId.valueAt(size);
            valueAt.update(this.mTaskbarInsetsSource, valueAt.mTaskInfo);
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        this.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new AnimationContext(surfaceControl, this.mTaskbarInsetsSource, runningTaskInfo));
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        AnimationContext animationContext = this.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
        if (animationContext != null) {
            animationContext.update(this.mTaskbarInsetsSource, runningTaskInfo);
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        AnimationContext animationContext = this.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
        if (animationContext != null) {
            if (runningTaskInfo.getWindowingMode() != 2) {
                resetSurface(animationContext);
            }
            this.mAnimationContextByTaskId.remove(runningTaskInfo.taskId);
        }
        if (this.mAnimationContextByTaskId.size() == 0) {
            this.mBackgroundController.removeBackground(this.mTransaction);
        }
        this.mTransaction.apply();
    }

    private void resetSurface(AnimationContext animationContext) {
        this.mTransaction.setWindowCrop(animationContext.mLeash, (Rect) null).setCornerRadius(animationContext.mLeash, 0.0f).setMatrix(animationContext.mLeash, 1.0f, 0.0f, 0.0f, 1.0f).setPosition(animationContext.mLeash, (float) animationContext.mTaskInfo.positionInParent.x, (float) animationContext.mTaskInfo.positionInParent.y);
    }

    /* renamed from: com.android.wm.shell.fullscreen.FullscreenUnfoldController$AnimationContext */
    private class AnimationContext {
        final Rect mCurrentCropRect;
        final Rect mEndCropRect;
        final SurfaceControl mLeash;
        final Matrix mMatrix;
        final Rect mStartCropRect;
        TaskInfo mTaskInfo;

        private AnimationContext(SurfaceControl surfaceControl, InsetsSource insetsSource, TaskInfo taskInfo) {
            this.mStartCropRect = new Rect();
            this.mEndCropRect = new Rect();
            this.mCurrentCropRect = new Rect();
            this.mMatrix = new Matrix();
            this.mLeash = surfaceControl;
            update(insetsSource, taskInfo);
        }

        /* access modifiers changed from: private */
        public void update(InsetsSource insetsSource, TaskInfo taskInfo) {
            this.mTaskInfo = taskInfo;
            this.mStartCropRect.set(taskInfo.getConfiguration().windowConfiguration.getBounds());
            if (insetsSource != null && ((float) insetsSource.getFrame().height()) >= FullscreenUnfoldController.this.mExpandedTaskBarHeight) {
                Rect rect = this.mStartCropRect;
                rect.inset(insetsSource.calculateVisibleInsets(rect));
            }
            this.mEndCropRect.set(this.mStartCropRect);
            int width = (int) (((float) this.mEndCropRect.width()) * FullscreenUnfoldController.HORIZONTAL_START_MARGIN);
            this.mStartCropRect.left = this.mEndCropRect.left + width;
            this.mStartCropRect.right = this.mEndCropRect.right - width;
            int height = (int) (((float) this.mEndCropRect.height()) * FullscreenUnfoldController.VERTICAL_START_MARGIN);
            this.mStartCropRect.top = this.mEndCropRect.top + height;
            this.mStartCropRect.bottom = this.mEndCropRect.bottom - height;
        }
    }
}
