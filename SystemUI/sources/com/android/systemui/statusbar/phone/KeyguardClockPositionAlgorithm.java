package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.util.MathUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.statusbar.notification.NotificationUtils;
/* loaded from: classes.dex */
public class KeyguardClockPositionAlgorithm {
    private int mBurnInPreventionOffsetX;
    private int mBurnInPreventionOffsetY;
    private int mBurnInPreventionOffsetYLargeClock;
    private boolean mBypassEnabled;
    private float mClockBottom;
    private int mContainerTopPadding;
    private int mCutoutTopInset = 0;
    private float mDarkAmount;
    private boolean mHasCustomClock;
    private boolean mHasVisibleNotifs;
    private int mHeight;
    private boolean mIsClockTopAligned;
    private boolean mIsSplitShade;
    private int mKeyguardStatusHeight;
    private int mMaxShadeBottom;
    private int mMinTopMargin;
    private int mNotificationStackHeight;
    private float mOverStretchAmount;
    private float mPanelExpansion;
    private float mQsExpansion;
    private int mStatusViewBottomMargin;
    private float mUdfpsTop;
    private int mUnlockedStackScrollerPadding;
    private int mUserSwitchHeight;
    private int mUserSwitchPreferredY;

    /* loaded from: classes.dex */
    public static class Result {
        public float clockAlpha;
        public float clockScale;
        public int clockX;
        public int clockY;
        public int clockYFullyDozing;
        public int stackScrollerPadding;
        public int stackScrollerPaddingExpanded;
        public int userSwitchY;
    }

    public void loadDimens(Resources resources) {
        this.mStatusViewBottomMargin = resources.getDimensionPixelSize(R$dimen.keyguard_status_view_bottom_margin);
        this.mContainerTopPadding = resources.getDimensionPixelSize(R$dimen.keyguard_clock_top_margin);
        this.mBurnInPreventionOffsetX = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_x);
        this.mBurnInPreventionOffsetY = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_y);
        this.mBurnInPreventionOffsetYLargeClock = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_y_large_clock);
    }

    public void setup(int i, int i2, int i3, float f, int i4, int i5, int i6, int i7, boolean z, boolean z2, float f2, float f3, boolean z3, int i8, float f4, int i9, boolean z4, float f5, float f6, boolean z5) {
        this.mMinTopMargin = Math.max(this.mContainerTopPadding, i6) + i;
        this.mMaxShadeBottom = i2;
        this.mNotificationStackHeight = i3;
        this.mPanelExpansion = f;
        this.mHeight = i4;
        this.mKeyguardStatusHeight = this.mStatusViewBottomMargin + i5;
        this.mUserSwitchHeight = i6;
        this.mUserSwitchPreferredY = i7;
        this.mHasCustomClock = z;
        this.mHasVisibleNotifs = z2;
        this.mDarkAmount = f2;
        this.mOverStretchAmount = f3;
        this.mBypassEnabled = z3;
        this.mUnlockedStackScrollerPadding = i8;
        this.mQsExpansion = f4;
        this.mCutoutTopInset = i9;
        this.mIsSplitShade = z4;
        this.mUdfpsTop = f5;
        this.mClockBottom = f6;
        this.mIsClockTopAligned = z5;
    }

    public void run(Result result) {
        int clockY = getClockY(this.mPanelExpansion, this.mDarkAmount);
        result.clockY = clockY;
        result.userSwitchY = getUserSwitcherY(this.mPanelExpansion);
        result.clockYFullyDozing = getClockY(1.0f, 1.0f);
        result.clockAlpha = getClockAlpha(clockY);
        result.stackScrollerPadding = getStackScrollerPadding(clockY);
        result.stackScrollerPaddingExpanded = this.mBypassEnabled ? this.mUnlockedStackScrollerPadding : getClockY(1.0f, this.mDarkAmount) + this.mKeyguardStatusHeight;
        result.clockX = (int) NotificationUtils.interpolate(0.0f, burnInPreventionOffsetX(), this.mDarkAmount);
        result.clockScale = NotificationUtils.interpolate(BurnInHelperKt.getBurnInScale(), 1.0f, 1.0f - this.mDarkAmount);
    }

    private int getStackScrollerPadding(int i) {
        if (this.mBypassEnabled) {
            return (int) (this.mUnlockedStackScrollerPadding + this.mOverStretchAmount);
        }
        return this.mIsSplitShade ? i : i + this.mKeyguardStatusHeight;
    }

    public float getMinStackScrollerPadding() {
        return this.mBypassEnabled ? this.mUnlockedStackScrollerPadding : this.mMinTopMargin + this.mKeyguardStatusHeight;
    }

    private int getExpandedPreferredClockY() {
        return this.mMinTopMargin + this.mUserSwitchHeight;
    }

    public int getLockscreenStatusViewHeight() {
        return this.mKeyguardStatusHeight;
    }

    private int getClockY(float f, float f2) {
        float lerp = MathUtils.lerp((-this.mKeyguardStatusHeight) / 3.0f, getExpandedPreferredClockY(), Interpolators.FAST_OUT_LINEAR_IN.getInterpolation(f));
        int i = this.mBurnInPreventionOffsetYLargeClock;
        int i2 = this.mCutoutTopInset;
        float f3 = lerp - ((float) i) < ((float) i2) ? i2 - (lerp - i) : 0.0f;
        float f4 = this.mUdfpsTop;
        if ((f4 > -1.0f) && !this.mIsClockTopAligned) {
            float f5 = this.mClockBottom;
            if (f4 < f5) {
                int i3 = ((int) (lerp - i2)) / 2;
                if (i >= i3) {
                    i = i3;
                }
                f3 = -i;
            } else {
                float f6 = lerp - i2;
                float f7 = f4 - f5;
                int i4 = ((int) (f7 + f6)) / 2;
                if (i >= i4) {
                    i = i4;
                }
                f3 = (f7 - f6) / 2.0f;
            }
        }
        return (int) (MathUtils.lerp(lerp, burnInPreventionOffsetY(i) + lerp + f3, f2) + this.mOverStretchAmount);
    }

    private int getUserSwitcherY(float f) {
        return (int) (MathUtils.lerp((-this.mKeyguardStatusHeight) - this.mUserSwitchHeight, this.mUserSwitchPreferredY, Interpolators.FAST_OUT_LINEAR_IN.getInterpolation(f)) + this.mOverStretchAmount);
    }

    private float getClockAlpha(int i) {
        return MathUtils.lerp(Interpolators.ACCELERATE.getInterpolation(Math.max(0.0f, i / Math.max(1.0f, getClockY(1.0f, this.mDarkAmount))) * (1.0f - MathUtils.saturate(this.mQsExpansion / 0.3f))), 1.0f, this.mDarkAmount);
    }

    private float burnInPreventionOffsetY(int i) {
        return BurnInHelperKt.getBurnInOffset(i * 2, false) - i;
    }

    private float burnInPreventionOffsetX() {
        return BurnInHelperKt.getBurnInOffset(this.mBurnInPreventionOffsetX, true);
    }
}
