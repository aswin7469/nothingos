package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.C1894R;

public class KeyguardPINView extends KeyguardPinBasedInputView {
    private final AppearAnimationUtils mAppearAnimationUtils;
    private ConstraintLayout mContainer;
    private final DisappearAnimationUtils mDisappearAnimationUtils;
    private final DisappearAnimationUtils mDisappearAnimationUtilsLocked;
    private int mDisappearYTranslation;
    private int mLastDevicePosture;
    private View[][] mViews;

    /* access modifiers changed from: protected */
    public int getPasswordTextViewId() {
        return C1894R.C1898id.pinEntry;
    }

    public int getWrongPasswordStringId() {
        return C1894R.string.kg_wrong_pin;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void resetState() {
    }

    public KeyguardPINView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardPINView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastDevicePosture = 0;
        this.mAppearAnimationUtils = new AppearAnimationUtils(context);
        Context context2 = context;
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context2, 125, 0.6f, 0.45f, AnimationUtils.loadInterpolator(this.mContext, AndroidResources.FAST_OUT_LINEAR_IN));
        this.mDisappearAnimationUtilsLocked = new DisappearAnimationUtils(context2, 187, 0.6f, 0.45f, AnimationUtils.loadInterpolator(this.mContext, AndroidResources.FAST_OUT_LINEAR_IN));
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(C1894R.dimen.disappear_y_translation);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        updateMargins();
    }

    /* access modifiers changed from: package-private */
    public void onDevicePostureChanged(int i) {
        this.mLastDevicePosture = i;
        updateMargins();
    }

    private void updateMargins() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.num_pad_entry_row_margin_bottom);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.num_pad_key_margin_end);
        String string = this.mContext.getResources().getString(C1894R.string.num_pad_key_ratio);
        for (int i = 1; i < 5; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                View view = this.mViews[i][i2];
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                layoutParams.dimensionRatio = string;
                if (i != 4) {
                    layoutParams.bottomMargin = dimensionPixelSize;
                }
                if (i2 != 2) {
                    layoutParams.rightMargin = dimensionPixelSize2;
                }
                view.setLayoutParams(layoutParams);
            }
        }
        float f = this.mContext.getResources().getFloat(C1894R.dimen.half_opened_bouncer_height_ratio);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mContainer);
        if (this.mLastDevicePosture != 2) {
            f = 0.0f;
        }
        constraintSet.setGuidelinePercent(C1894R.C1898id.pin_pad_top_guideline, f);
        constraintSet.applyTo(this.mContainer);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContainer = (ConstraintLayout) findViewById(C1894R.C1898id.pin_container);
        this.mViews = new View[][]{new View[]{findViewById(C1894R.C1898id.row0), null, null}, new View[]{findViewById(C1894R.C1898id.key1), findViewById(C1894R.C1898id.key2), findViewById(C1894R.C1898id.key3)}, new View[]{findViewById(C1894R.C1898id.key4), findViewById(C1894R.C1898id.key5), findViewById(C1894R.C1898id.key6)}, new View[]{findViewById(C1894R.C1898id.key7), findViewById(C1894R.C1898id.key8), findViewById(C1894R.C1898id.key9)}, new View[]{findViewById(C1894R.C1898id.delete_button), findViewById(C1894R.C1898id.key0), findViewById(C1894R.C1898id.key_enter)}, new View[]{null, this.mEcaView, null}};
    }

    public void startAppearAnimation() {
        enableClipping(false);
        setAlpha(1.0f);
        setTranslationY(this.mAppearAnimationUtils.getStartTranslation());
        AppearAnimationUtils.startTranslationYAnimation(this, 0, 500, 0.0f, this.mAppearAnimationUtils.getInterpolator(), getAnimationListener(19));
        this.mAppearAnimationUtils.startAnimation2d(this.mViews, new Runnable() {
            public void run() {
                KeyguardPINView.this.enableClipping(true);
            }
        });
    }

    public boolean startDisappearAnimation(boolean z, Runnable runnable) {
        DisappearAnimationUtils disappearAnimationUtils;
        enableClipping(false);
        setTranslationY(0.0f);
        if (z) {
            disappearAnimationUtils = this.mDisappearAnimationUtilsLocked;
        } else {
            disappearAnimationUtils = this.mDisappearAnimationUtils;
        }
        disappearAnimationUtils.createAnimation((View) this, 0, 200, (float) this.mDisappearYTranslation, false, this.mDisappearAnimationUtils.getInterpolator(), (Runnable) new KeyguardPINView$$ExternalSyntheticLambda0(this, runnable), getAnimationListener(22));
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startDisappearAnimation$0$com-android-keyguard-KeyguardPINView */
    public /* synthetic */ void mo25930xdaec1c89(Runnable runnable) {
        enableClipping(true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public void enableClipping(boolean z) {
        this.mContainer.setClipToPadding(z);
        this.mContainer.setClipChildren(z);
        setClipChildren(z);
    }
}
