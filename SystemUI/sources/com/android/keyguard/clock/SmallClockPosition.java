package com.android.keyguard.clock;

import android.content.Context;
import android.util.MathUtils;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1894R;

class SmallClockPosition {
    private final int mBurnInOffsetY;
    private float mDarkAmount;
    private final int mKeyguardLockHeight;
    private final int mKeyguardLockPadding;
    private final int mStatusBarHeight;

    SmallClockPosition(Context context) {
        this(SystemBarUtils.getStatusBarHeight(context), context.getResources().getDimensionPixelSize(C1894R.dimen.keyguard_lock_padding), context.getResources().getDimensionPixelSize(C1894R.dimen.keyguard_lock_height), context.getResources().getDimensionPixelSize(C1894R.dimen.burn_in_prevention_offset_y));
    }

    SmallClockPosition(int i, int i2, int i3, int i4) {
        this.mStatusBarHeight = i;
        this.mKeyguardLockPadding = i2;
        this.mKeyguardLockHeight = i3;
        this.mBurnInOffsetY = i4;
    }

    /* access modifiers changed from: package-private */
    public void setDarkAmount(float f) {
        this.mDarkAmount = f;
    }

    /* access modifiers changed from: package-private */
    public int getPreferredY() {
        int i = this.mStatusBarHeight;
        int i2 = this.mKeyguardLockHeight;
        int i3 = this.mKeyguardLockPadding;
        return (int) MathUtils.lerp(i + i2 + (i3 * 2), i + i2 + (i3 * 2) + this.mBurnInOffsetY, this.mDarkAmount);
    }
}
