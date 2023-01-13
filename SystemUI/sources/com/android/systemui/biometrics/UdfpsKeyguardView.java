package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.nothing.systemui.util.NTLogUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;

public class UdfpsKeyguardView extends UdfpsAnimationView {
    static final int ANIMATION_BETWEEN_AOD_AND_LOCKSCREEN = 1;
    static final int ANIMATION_NONE = 0;
    static final int ANIMATION_UNLOCKED_SCREEN_OFF = 2;
    private static final String TAG = "UdfpsKeyguardView";
    private int mAlpha;
    private int mAnimationType = 0;
    /* access modifiers changed from: private */
    public LottieAnimationView mAodFp;
    private AnimatorSet mBackgroundInAnimator = new AnimatorSet();
    /* access modifiers changed from: private */
    public ImageView mBgProtection;
    private float mBurnInOffsetX;
    private float mBurnInOffsetY;
    private float mBurnInProgress;
    private UdfpsDrawable mFingerprintDrawable;
    /* access modifiers changed from: private */
    public boolean mFullyInflated;
    private float mInterpolatedDarkAmount;
    private final AsyncLayoutInflater.OnInflateFinishedListener mLayoutInflaterFinishListener = new AsyncLayoutInflater.OnInflateFinishedListener() {
        public void onInflateFinished(View view, int i, ViewGroup viewGroup) {
            boolean unused = UdfpsKeyguardView.this.mFullyInflated = true;
            LottieAnimationView unused2 = UdfpsKeyguardView.this.mAodFp = (LottieAnimationView) view.findViewById(C1894R.C1898id.udfps_aod_fp);
            LottieAnimationView unused3 = UdfpsKeyguardView.this.mLockScreenFp = (LottieAnimationView) view.findViewById(C1894R.C1898id.udfps_lockscreen_fp);
            ImageView unused4 = UdfpsKeyguardView.this.mBgProtection = (ImageView) view.findViewById(C1894R.C1898id.udfps_keyguard_fp_bg);
            UdfpsKeyguardView.this.mLockScreenFp.setVisibility(8);
            UdfpsKeyguardView.this.mBgProtection.setVisibility(8);
            UdfpsKeyguardView.this.mAodFp.setVisibility(8);
            UdfpsKeyguardView udfpsKeyguardView = UdfpsKeyguardView.this;
            LottieAnimationView unused5 = udfpsKeyguardView.mNTLockScreenFp = (LottieAnimationView) udfpsKeyguardView.findViewById(C1894R.C1898id.nt_udfps_lockscreen_fp);
            UdfpsKeyguardView.this.updatePadding();
            UdfpsKeyguardView.this.updateColor();
            UdfpsKeyguardView.this.updateAlpha("onInflateFinished");
            viewGroup.addView(view);
            UdfpsKeyguardView.this.mLockScreenFp.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER, new UdfpsKeyguardView$2$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onInflateFinished$0$com-android-systemui-biometrics-UdfpsKeyguardView$2 */
        public /* synthetic */ ColorFilter mo30984xc49f4e46(LottieFrameInfo lottieFrameInfo) {
            return new PorterDuffColorFilter(UdfpsKeyguardView.this.mTextColorPrimary, PorterDuff.Mode.SRC_ATOP);
        }
    };
    /* access modifiers changed from: private */
    public LottieAnimationView mLockScreenFp;
    private final int mMaxBurnInOffsetX;
    private final int mMaxBurnInOffsetY;
    /* access modifiers changed from: private */
    public LottieAnimationView mNTLockScreenFp;
    private float mScaleFactor = 1.0f;
    /* access modifiers changed from: private */
    public int mTextColorPrimary;
    boolean mUdfpsRequested;

    @Retention(RetentionPolicy.SOURCE)
    private @interface AnimationType {
    }

    public boolean dozeTimeTick() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onIlluminationStarting() {
    }

    /* access modifiers changed from: package-private */
    public void onIlluminationStopped() {
    }

    public UdfpsKeyguardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintDrawable = new UdfpsFpDrawable(context);
        this.mMaxBurnInOffsetX = context.getResources().getDimensionPixelSize(C1894R.dimen.udfps_burn_in_offset_x);
        this.mMaxBurnInOffsetY = context.getResources().getDimensionPixelSize(C1894R.dimen.udfps_burn_in_offset_y);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        new AsyncLayoutInflater(this.mContext).inflate(C1894R.layout.udfps_keyguard_view_internal, this, this.mLayoutInflaterFinishListener);
    }

    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    private void updateBurnInOffsets() {
        float f;
        boolean z;
        if (this.mFullyInflated) {
            if (this.mAnimationType == 2) {
                f = 1.0f;
            } else {
                f = this.mInterpolatedDarkAmount;
            }
            boolean z2 = true;
            this.mBurnInOffsetX = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX), f);
            this.mBurnInOffsetY = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY), f);
            this.mBurnInProgress = MathUtils.lerp(0.0f, BurnInHelperKt.getBurnInProgressOffset(), f);
            if (this.mAnimationType == 1 && !this.mPauseAuth) {
                this.mLockScreenFp.setTranslationX(this.mBurnInOffsetX);
                this.mLockScreenFp.setTranslationY(this.mBurnInOffsetY);
                this.mBgProtection.setAlpha(1.0f - this.mInterpolatedDarkAmount);
                this.mLockScreenFp.setAlpha(1.0f - this.mInterpolatedDarkAmount);
            } else if (f == 0.0f) {
                this.mLockScreenFp.setTranslationX(0.0f);
                this.mLockScreenFp.setTranslationY(0.0f);
                this.mBgProtection.setAlpha(((float) this.mAlpha) / 255.0f);
                this.mLockScreenFp.setAlpha(((float) this.mAlpha) / 255.0f);
            } else {
                this.mBgProtection.setAlpha(0.0f);
                this.mLockScreenFp.setAlpha(0.0f);
            }
            this.mLockScreenFp.setProgress(1.0f - this.mInterpolatedDarkAmount);
            this.mAodFp.setTranslationX(this.mBurnInOffsetX);
            this.mAodFp.setTranslationY(this.mBurnInOffsetY);
            this.mAodFp.setProgress(this.mBurnInProgress);
            this.mAodFp.setAlpha(this.mInterpolatedDarkAmount);
            int i = this.mAnimationType;
            if (i == 1) {
                float f2 = this.mInterpolatedDarkAmount;
                if (f2 == 0.0f || f2 == 1.0f) {
                    z = true;
                    if (!(i == 2 && this.mInterpolatedDarkAmount == 1.0f)) {
                        z2 = false;
                    }
                    if (!z || z2) {
                        this.mAnimationType = 0;
                    }
                    return;
                }
            }
            z = false;
            z2 = false;
            if (!z) {
            }
            this.mAnimationType = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void requestUdfps(boolean z, int i) {
        this.mUdfpsRequested = z;
    }

    /* access modifiers changed from: package-private */
    public void updateColor() {
        if (this.mFullyInflated) {
            this.mTextColorPrimary = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
            this.mBgProtection.setImageDrawable(getContext().getDrawable(C1894R.C1896drawable.fingerprint_bg));
            this.mLockScreenFp.invalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public void setScaleFactor(float f) {
        this.mScaleFactor = f;
    }

    /* access modifiers changed from: package-private */
    public void updatePadding() {
        if (this.mLockScreenFp != null && this.mAodFp != null) {
            int dimensionPixelSize = (int) (((float) getResources().getDimensionPixelSize(C1894R.dimen.lock_icon_padding)) * this.mScaleFactor);
            this.mLockScreenFp.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
            this.mAodFp.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        }
    }

    /* access modifiers changed from: package-private */
    public void setUnpausedAlpha(int i) {
        this.mAlpha = i;
        updateAlpha("setUnpausedAlpha " + this.mAlpha);
    }

    /* access modifiers changed from: package-private */
    public int getUnpausedAlpha() {
        return this.mAlpha;
    }

    /* access modifiers changed from: protected */
    public int updateAlpha(String str) {
        int updateAlpha = super.updateAlpha(str);
        NTLogUtil.m1686d(TAG, "updateAlpha: alpha = " + updateAlpha + ", mPauseAuth=" + this.mPauseAuth + ", reason=" + str);
        return updateAlpha;
    }

    /* access modifiers changed from: package-private */
    public int calculateAlpha() {
        return this.mPauseAuth ? 0 : 255;
    }

    /* access modifiers changed from: package-private */
    public void onDozeAmountChanged(float f, float f2, int i) {
        this.mAnimationType = i;
        this.mInterpolatedDarkAmount = f2;
    }

    /* access modifiers changed from: package-private */
    public void animateInUdfpsBouncer(final Runnable runnable) {
        if (!this.mBackgroundInAnimator.isRunning() && this.mFullyInflated) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mBackgroundInAnimator = animatorSet;
            animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mBgProtection, View.ALPHA, new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(this.mBgProtection, View.SCALE_X, new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(this.mBgProtection, View.SCALE_Y, new float[]{0.0f, 1.0f})});
            this.mBackgroundInAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mBackgroundInAnimator.setDuration(500);
            this.mBackgroundInAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            this.mBackgroundInAnimator.start();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("UdfpsKeyguardView (" + this + NavigationBarInflaterView.KEY_CODE_END);
        printWriter.println("    mPauseAuth=" + this.mPauseAuth);
        printWriter.println("    mUnpausedAlpha=" + getUnpausedAlpha());
        printWriter.println("    mUdfpsRequested=" + this.mUdfpsRequested);
        printWriter.println("    mInterpolatedDarkAmount=" + this.mInterpolatedDarkAmount);
        printWriter.println("    mAnimationType=" + this.mAnimationType);
    }

    public void startFpsBreatheAnim() {
        LottieAnimationView lottieAnimationView = this.mNTLockScreenFp;
        if (lottieAnimationView != null && !lottieAnimationView.isAnimating()) {
            NTLogUtil.m1688i(TAG, "startFpsBreatheAnim");
            this.mNTLockScreenFp.playAnimation();
        }
    }

    public void stopFpsBreatheAnim() {
        LottieAnimationView lottieAnimationView = this.mNTLockScreenFp;
        if (lottieAnimationView != null && lottieAnimationView.isAnimating()) {
            NTLogUtil.m1688i(TAG, "stopFpsBreatheAnim");
            this.mNTLockScreenFp.pauseAnimation();
            this.mNTLockScreenFp.setProgress(0.0f);
        }
    }
}
