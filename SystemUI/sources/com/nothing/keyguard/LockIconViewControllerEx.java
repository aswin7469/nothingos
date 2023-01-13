package com.nothing.keyguard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.LockIconView;
import com.android.keyguard.LockIconViewController;
import com.android.systemui.C1894R;
import com.android.systemui.biometrics.AuthController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.facerecognition.IFaceRecognitionAnimationCallback;
import com.nothing.systemui.keyguard.RepeatingVectorAnimation;

public class LockIconViewControllerEx {
    private static final String TAG = "LockIconViewControllerEx";
    private static float sDefaultDensity;
    private static final float sDistAboveKgBottomAreaPx = 0.0f;
    private static int sLockIconRadiusPx;
    private IFaceRecognitionAnimationCallback iFaceRecognitionAnimationCallback = new IFaceRecognitionAnimationCallback() {
        public void resetFaceImage() {
        }

        public void startLoadingAnimation() {
        }

        public void startSlideUpAnimation() {
        }

        public void startFailureAnimation() {
            LockIconViewControllerEx.this.mLockIconView.post(LockIconViewControllerEx.this.updateRunnable);
        }

        public void startFreezeAnimation() {
            LockIconViewControllerEx.this.mLockIconView.post(LockIconViewControllerEx.this.updateRunnable);
        }

        public void startSuccessAnimation() {
            LockIconViewControllerEx.this.mLockIconView.post(LockIconViewControllerEx.this.updateRunnable);
        }

        public void onFaceAuthenticationTimeout() {
            LockIconViewControllerEx.this.mLockIconView.post(LockIconViewControllerEx.this.updateRunnable);
        }

        public void onFaceSuccessConnect() {
            LockIconViewControllerEx.this.mLockIconView.post(LockIconViewControllerEx.this.updateRunnable);
        }
    };
    private int mBottomPadding;
    private Context mContext;
    private int mDefaultPaddingPx;
    private AnimatedVectorDrawable mFaceLoadingIcon;
    private AnimatedVectorDrawable mFpToUnlockIcon;
    private float mHeightPixels;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private Drawable mLockIcon;
    /* access modifiers changed from: private */
    public LockIconView mLockIconView;
    /* access modifiers changed from: private */
    public LockIconViewController mLockIconViewController;
    private AnimatedVectorDrawable mLockToUnlockIcon;
    private CharSequence mLockedLabel;
    private RepeatingVectorAnimation mRepeatingAnimation;
    private boolean mShowFaceLoadingIcon;
    private Drawable mUnlockIcon;
    private CharSequence mUnlockedLabel;
    private float mWidthPixels;
    /* access modifiers changed from: private */
    public Runnable updateRunnable = new Runnable() {
        public void run() {
            LockIconViewControllerEx.this.mLockIconViewController.updateVisibility();
        }
    };

    static {
        float f = ((float) DisplayMetrics.DENSITY_DEVICE_STABLE) / 160.0f;
        sDefaultDensity = f;
        sLockIconRadiusPx = (int) (f * 14.0f);
    }

    public void init(Context context, LockIconView lockIconView, Resources resources, KeyguardUpdateMonitor keyguardUpdateMonitor, LockIconViewController lockIconViewController) {
        this.mContext = context;
        this.mLockIconViewController = lockIconViewController;
        this.mLockIconView = lockIconView;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mUnlockedLabel = resources.getString(C1894R.string.accessibility_unlock_button);
        this.mLockedLabel = resources.getString(C1894R.string.accessibility_lock_icon);
        this.mFaceLoadingIcon = (AnimatedVectorDrawable) this.mLockIconView.getResources().getDrawable(C1894R.C1896drawable.nt_lock_face_loading, context.getTheme());
        this.mRepeatingAnimation = new RepeatingVectorAnimation(this.mFaceLoadingIcon);
        this.mUnlockIcon = lockIconView.getContext().getResources().getDrawable(C1894R.C1896drawable.nt_ic_unlock, lockIconView.getContext().getTheme());
        this.mLockIcon = lockIconView.getContext().getResources().getDrawable(C1894R.anim.nt_lock_to_unlock, lockIconView.getContext().getTheme());
        this.mFpToUnlockIcon = (AnimatedVectorDrawable) lockIconView.getContext().getResources().getDrawable(C1894R.anim.nt_lock_to_unlock, lockIconView.getContext().getTheme());
        this.mLockToUnlockIcon = (AnimatedVectorDrawable) lockIconView.getContext().getResources().getDrawable(C1894R.anim.nt_lock_to_unlock, lockIconView.getContext().getTheme());
    }

    public void updateConfiguration() {
        DisplayMetrics displayMetrics = this.mLockIconView.getContext().getResources().getDisplayMetrics();
        this.mWidthPixels = (float) displayMetrics.widthPixels;
        Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        this.mHeightPixels = (float) point.y;
        float f = displayMetrics.density;
        int i = displayMetrics.densityDpi;
        this.mBottomPadding = (int) (this.mHeightPixels * 0.283f);
        sLockIconRadiusPx = this.mLockIconView.getContext().getResources().getDimensionPixelSize(C1894R.dimen.nt_lock_icon_radius);
        Log.i(TAG, "currentDensity=" + f + ", currentDensityDPI=" + i + ", mWidthPixels=" + this.mWidthPixels + ", mHeightPixels=" + this.mHeightPixels + ", mBottomPadding=" + this.mBottomPadding + ", sLockIconRadiusPx=" + sLockIconRadiusPx + ", old sDefaultDensity=" + sDefaultDensity);
    }

    public void updateVisibility(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        FaceRecognitionController faceRecognitionController = (FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class);
        boolean z7 = !faceRecognitionController.isFailed() && !faceRecognitionController.isTimeOut() && !faceRecognitionController.isFrozenMode() && this.mKeyguardUpdateMonitor.isFaceRecognitionEnable() && this.mKeyguardUpdateMonitor.isFaceCameraStarting();
        this.mShowFaceLoadingIcon = z7;
        if (z) {
            if (z7) {
                this.mLockIconView.setImageDrawable(this.mFaceLoadingIcon);
                this.mFaceLoadingIcon.forceAnimationOnUI();
                this.mRepeatingAnimation.start();
            } else {
                this.mRepeatingAnimation.stop();
                this.mLockIconView.setImageDrawable(this.mLockIcon);
            }
            this.mLockIconView.setVisibility(0);
            this.mLockIconView.setContentDescription(this.mLockedLabel);
        } else if (z2) {
            this.mRepeatingAnimation.stop();
            if (!z4) {
                if (z5) {
                    this.mLockIconView.setImageDrawable(this.mFpToUnlockIcon);
                    this.mFpToUnlockIcon.forceAnimationOnUI();
                    this.mFpToUnlockIcon.start();
                } else if (z3) {
                    this.mLockIconView.setImageDrawable(this.mLockToUnlockIcon);
                    this.mLockToUnlockIcon.forceAnimationOnUI();
                    this.mLockToUnlockIcon.start();
                } else {
                    this.mLockIconView.setImageDrawable(this.mUnlockIcon);
                }
            }
            this.mLockIconView.setVisibility(0);
            this.mLockIconView.setContentDescription(this.mUnlockedLabel);
        } else {
            this.mLockIconView.setVisibility(4);
            this.mLockIconView.setContentDescription((CharSequence) null);
        }
        if (z6) {
            this.mLockIconView.setVisibility(4);
        }
    }

    public void updateLockIconLocation(AuthController authController, Rect rect) {
        Log.i(TAG, "updateLockIconLocation real sLockIconRadiusPx=" + sLockIconRadiusPx + ", sDistAboveKgBottomAreaPx==0.0, real mBottomPadding=" + this.mBottomPadding + ", mHeightPixels=" + this.mHeightPixels);
        this.mLockIconView.setCenterLocation(new PointF(this.mWidthPixels / 2.0f, (this.mHeightPixels - ((float) this.mBottomPadding)) + ((float) sLockIconRadiusPx)), (float) sLockIconRadiusPx, 1);
        this.mLockIconView.getHitRect(rect);
    }

    public void onViewAttached() {
        ((FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class)).registerCallback(this.iFaceRecognitionAnimationCallback);
    }

    public void onViewDetached() {
        ((FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class)).removeCallback(this.iFaceRecognitionAnimationCallback);
    }

    public void onLockIconClick() {
        if (this.mKeyguardUpdateMonitor.isFaceRecognitionEnable() && !this.mKeyguardUpdateMonitor.isFaceCameraStarting() && !this.mKeyguardUpdateMonitor.isFaceRecognitionSucceeded() && !((FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class)).isFrozenMode()) {
            this.mKeyguardUpdateMonitor.requestFaceAuth(true);
        }
    }
}
