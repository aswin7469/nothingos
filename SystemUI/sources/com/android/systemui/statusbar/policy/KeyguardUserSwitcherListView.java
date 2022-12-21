package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.keyguard.KeyguardConstants;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.animation.Interpolators;

public class KeyguardUserSwitcherListView extends AlphaOptimizedLinearLayout {
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardUserSwitcherListView";
    private boolean mAnimating;
    private final AppearAnimationUtils mAppearAnimationUtils;
    private final DisappearAnimationUtils mDisappearAnimationUtils;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardUserSwitcherListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAppearAnimationUtils = new AppearAnimationUtils(context, 220, -0.5f, 0.5f, Interpolators.FAST_OUT_SLOW_IN);
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 220, 0.2f, 0.2f, Interpolators.FAST_OUT_SLOW_IN_REVERSE);
    }

    /* access modifiers changed from: package-private */
    public void setDarkAmount(float f) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof KeyguardUserDetailItemView) {
                ((KeyguardUserDetailItemView) childAt).setDarkAmount(f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAnimating() {
        return this.mAnimating;
    }

    /* access modifiers changed from: package-private */
    public void updateVisibilities(boolean z, boolean z2) {
        if (DEBUG) {
            Log.d(TAG, String.format("updateVisibilities: open=%b animate=%b childCount=%d", Boolean.valueOf(z), Boolean.valueOf(z2), Integer.valueOf(getChildCount())));
        }
        this.mAnimating = false;
        int childCount = getChildCount();
        KeyguardUserDetailItemView[] keyguardUserDetailItemViewArr = new KeyguardUserDetailItemView[childCount];
        for (int i = 0; i < childCount; i++) {
            KeyguardUserDetailItemView keyguardUserDetailItemView = (KeyguardUserDetailItemView) getChildAt(i);
            keyguardUserDetailItemViewArr[i] = keyguardUserDetailItemView;
            keyguardUserDetailItemView.clearAnimation();
            if (i == 0) {
                keyguardUserDetailItemViewArr[i].updateVisibilities(true, z, z2);
                keyguardUserDetailItemViewArr[i].setClickable(true);
            } else {
                keyguardUserDetailItemViewArr[i].setClickable(z);
                if (z) {
                    keyguardUserDetailItemViewArr[i].updateVisibilities(true, true, false);
                }
            }
        }
        if (z2 && childCount > 1) {
            keyguardUserDetailItemViewArr[0] = null;
            setClipChildren(false);
            setClipToPadding(false);
            this.mAnimating = true;
            (z ? this.mAppearAnimationUtils : this.mDisappearAnimationUtils).startAnimation(keyguardUserDetailItemViewArr, new KeyguardUserSwitcherListView$$ExternalSyntheticLambda0(this, z, keyguardUserDetailItemViewArr));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateVisibilities$0$com-android-systemui-statusbar-policy-KeyguardUserSwitcherListView */
    public /* synthetic */ void mo45900xbcc43ce6(boolean z, KeyguardUserDetailItemView[] keyguardUserDetailItemViewArr) {
        setClipChildren(true);
        setClipToPadding(true);
        this.mAnimating = false;
        if (!z) {
            for (int i = 1; i < keyguardUserDetailItemViewArr.length; i++) {
                keyguardUserDetailItemViewArr[i].updateVisibilities(false, true, false);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void replaceView(KeyguardUserDetailItemView keyguardUserDetailItemView, int i) {
        removeViewAt(i);
        addView(keyguardUserDetailItemView, i);
    }

    /* access modifiers changed from: package-private */
    public void removeLastView() {
        removeViewAt(getChildCount() - 1);
    }
}
