package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewTreeObserver;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LockIcon extends KeyguardAffordanceView {
    static final int ERROR = 0;
    static final int LOCK = 2;
    private static final int[][] LOCK_ANIM_RES_IDS = {new int[]{C1893R.anim.lock_to_error, C1893R.anim.lock_unlock, C1893R.anim.lock_lock, C1893R.anim.lock_scanning}, new int[]{C1893R.anim.lock_to_error_circular, C1893R.anim.lock_unlock_circular, C1893R.anim.lock_lock_circular, C1893R.anim.lock_scanning_circular}, new int[]{C1893R.anim.lock_to_error_filled, C1893R.anim.lock_unlock_filled, C1893R.anim.lock_lock_filled, C1893R.anim.lock_scanning_filled}, new int[]{C1893R.anim.lock_to_error_rounded, C1893R.anim.lock_unlock_rounded, C1893R.anim.lock_lock_rounded, C1893R.anim.lock_scanning_rounded}};
    static final int SCANNING = 3;
    static final int STATE_BIOMETRICS_ERROR = 3;
    static final int STATE_LOCKED = 0;
    static final int STATE_LOCK_OPEN = 1;
    static final int STATE_SCANNING_FACE = 2;
    static final int UNLOCK = 1;
    private float mDozeAmount;
    private boolean mDozing;
    private final SparseArray<Drawable> mDrawableCache = new SparseArray<>();
    private int mIconColor = 0;
    private boolean mKeyguardJustShown;
    private int mOldState;
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        public boolean onPreDraw() {
            LockIcon.this.getViewTreeObserver().removeOnPreDrawListener(this);
            boolean unused = LockIcon.this.mPredrawRegistered = false;
            final int access$100 = LockIcon.this.mState;
            Drawable access$200 = LockIcon.this.getIcon(access$100);
            LockIcon.this.setImageDrawable(access$200, false);
            if (access$100 == 2) {
                LockIcon lockIcon = LockIcon.this;
                lockIcon.announceForAccessibility(lockIcon.getResources().getString(C1893R.string.accessibility_scanning_face));
            }
            if (!(access$200 instanceof AnimatedVectorDrawable)) {
                return true;
            }
            final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) access$200;
            animatedVectorDrawable.forceAnimationOnUI();
            animatedVectorDrawable.clearAnimationCallbacks();
            animatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
                public void onAnimationEnd(Drawable drawable) {
                    if (LockIcon.this.getDrawable() == animatedVectorDrawable && access$100 == LockIcon.this.mState && access$100 == 2) {
                        animatedVectorDrawable.start();
                    } else {
                        Trace.endAsyncSection("LockIcon#Animation", access$100);
                    }
                }
            });
            Trace.beginAsyncSection("LockIcon#Animation", access$100);
            animatedVectorDrawable.start();
            return true;
        }
    };
    /* access modifiers changed from: private */
    public boolean mPredrawRegistered;
    /* access modifiers changed from: private */
    public int mState;

    @Retention(RetentionPolicy.SOURCE)
    @interface LockAnimIndex {
    }

    private static int getAnimationIndexForTransition(int i, int i2, boolean z, boolean z2) {
        if (z) {
            return -1;
        }
        if (i2 == 3) {
            return 0;
        }
        if (i != 1 && i2 == 1) {
            return 1;
        }
        if (i == 1 && i2 == 0 && !z2) {
            return 2;
        }
        return i2 == 2 ? 3 : -1;
    }

    public LockIcon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDrawableCache.clear();
    }

    /* access modifiers changed from: package-private */
    public boolean updateIconVisibility(boolean z) {
        int i = 0;
        if (z == (getVisibility() == 0)) {
            return false;
        }
        if (!z) {
            i = 4;
        }
        setVisibility(i);
        animate().cancel();
        if (z) {
            setScaleX(0.0f);
            setScaleY(0.0f);
            animate().setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).scaleX(1.0f).scaleY(1.0f).withLayer().setDuration(233).start();
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void update(int i, boolean z, boolean z2) {
        this.mOldState = this.mState;
        this.mState = i;
        this.mDozing = z;
        this.mKeyguardJustShown = z2;
        if (!this.mPredrawRegistered) {
            this.mPredrawRegistered = true;
            getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDozeAmount(float f) {
        this.mDozeAmount = f;
        updateDarkTint();
    }

    /* access modifiers changed from: package-private */
    public void updateColor(int i) {
        if (this.mIconColor != i) {
            this.mDrawableCache.clear();
            this.mIconColor = i;
            updateDarkTint();
        }
    }

    private void updateDarkTint() {
        setImageTintList(ColorStateList.valueOf(ColorUtils.blendARGB(this.mIconColor, -1, this.mDozeAmount)));
    }

    /* access modifiers changed from: private */
    public Drawable getIcon(int i) {
        int animationIndexForTransition = getAnimationIndexForTransition(this.mOldState, i, this.mDozing, this.mKeyguardJustShown);
        int themedAnimationResId = animationIndexForTransition != -1 ? getThemedAnimationResId(animationIndexForTransition) : getIconForState(i);
        if (!this.mDrawableCache.contains(themedAnimationResId)) {
            this.mDrawableCache.put(themedAnimationResId, getContext().getDrawable(themedAnimationResId));
        }
        return this.mDrawableCache.get(themedAnimationResId);
    }

    private static int getIconForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return 17302501;
            }
            if (!(i == 2 || i == 3)) {
                throw new IllegalArgumentException();
            }
        }
        return 17302492;
    }

    private int getThemedAnimationResId(int i) {
        String emptyIfNull = TextUtils.emptyIfNull(Settings.Secure.getString(getContext().getContentResolver(), "theme_customization_overlay_packages"));
        if (emptyIfNull.contains("com.android.theme.icon_pack.circular.android")) {
            return LOCK_ANIM_RES_IDS[1][i];
        }
        if (emptyIfNull.contains("com.android.theme.icon_pack.filled.android")) {
            return LOCK_ANIM_RES_IDS[2][i];
        }
        if (emptyIfNull.contains("com.android.theme.icon_pack.rounded.android")) {
            return LOCK_ANIM_RES_IDS[3][i];
        }
        return LOCK_ANIM_RES_IDS[0][i];
    }
}
