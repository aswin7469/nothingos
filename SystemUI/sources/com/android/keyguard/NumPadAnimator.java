package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.ContextThemeWrapper;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.util.ColorUtilKt;

class NumPadAnimator {
    private static final int CONTRACT_ANIMATION_DELAY_MS = 33;
    private static final int CONTRACT_ANIMATION_MS = 417;
    private static final int EXPAND_ANIMATION_MS = 100;
    private static final int EXPAND_COLOR_ANIMATION_MS = 50;
    private GradientDrawable mBackground;
    private ValueAnimator mContractAnimator;
    private AnimatorSet mContractAnimatorSet;
    private TextView mDigitTextView;
    private ValueAnimator mExpandAnimator;
    private AnimatorSet mExpandAnimatorSet;
    private Drawable mImageButton;
    private int mNormalBackgroundColor;
    private int mPressedBackgroundColor;
    private int mStyle;
    private int mTextColorPressed;
    private int mTextColorPrimary;

    NumPadAnimator(Context context, Drawable drawable, int i, Drawable drawable2) {
        this(context, drawable, i, (TextView) null, drawable2);
    }

    NumPadAnimator(Context context, Drawable drawable, int i, TextView textView, Drawable drawable2) {
        this.mStyle = i;
        this.mBackground = (GradientDrawable) drawable;
        this.mDigitTextView = textView;
        this.mImageButton = drawable2;
        reloadColors(context);
    }

    public void expand() {
        this.mExpandAnimatorSet.cancel();
        this.mContractAnimatorSet.cancel();
        this.mExpandAnimatorSet.start();
    }

    public void contract() {
        this.mExpandAnimatorSet.cancel();
        this.mContractAnimatorSet.cancel();
        this.mContractAnimatorSet.start();
    }

    /* access modifiers changed from: package-private */
    public void onLayout(int i) {
        float f = (float) i;
        float f2 = f / 2.0f;
        float f3 = f / 4.0f;
        this.mBackground.setCornerRadius(f2);
        this.mExpandAnimator.setFloatValues(new float[]{f2, f3});
        this.mContractAnimator.setFloatValues(new float[]{f3, f2});
    }

    /* access modifiers changed from: package-private */
    public void reloadColors(Context context) {
        int i;
        boolean z = this.mImageButton == null;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, this.mStyle);
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{16843817});
        this.mNormalBackgroundColor = ColorUtilKt.getPrivateAttrColorIfUnset(contextThemeWrapper, obtainStyledAttributes, 0, 0, 17956909);
        obtainStyledAttributes.recycle();
        this.mBackground.setColor(this.mNormalBackgroundColor);
        this.mPressedBackgroundColor = context.getColor(17170491);
        if (z) {
            i = Utils.getColorAttrDefaultColor(context, 16842806);
        } else {
            i = Utils.getColorAttrDefaultColor(context, 16842809);
        }
        this.mTextColorPrimary = i;
        this.mTextColorPressed = Utils.getColorAttrDefaultColor(context, 17957103);
        createAnimators();
    }

    private void createAnimators() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mExpandAnimator = ofFloat;
        ofFloat.setDuration(100);
        this.mExpandAnimator.setInterpolator(Interpolators.LINEAR);
        this.mExpandAnimator.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda0(this));
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mNormalBackgroundColor), Integer.valueOf(this.mPressedBackgroundColor)});
        ofObject.setDuration(50);
        ofObject.setInterpolator(Interpolators.LINEAR);
        ofObject.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda1(this));
        ValueAnimator ofObject2 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mTextColorPrimary), Integer.valueOf(this.mTextColorPressed)});
        ofObject2.setInterpolator(Interpolators.LINEAR);
        ofObject2.setDuration(50);
        ofObject2.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda2(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.mExpandAnimatorSet = animatorSet;
        animatorSet.playTogether(new Animator[]{this.mExpandAnimator, ofObject, ofObject2});
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.mContractAnimator = ofFloat2;
        ofFloat2.setStartDelay(33);
        this.mContractAnimator.setDuration(417);
        this.mContractAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mContractAnimator.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda3(this));
        ValueAnimator ofObject3 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mPressedBackgroundColor), Integer.valueOf(this.mNormalBackgroundColor)});
        ofObject3.setInterpolator(Interpolators.LINEAR);
        ofObject3.setStartDelay(33);
        ofObject3.setDuration(417);
        ofObject3.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda4(this));
        ValueAnimator ofObject4 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mTextColorPressed), Integer.valueOf(this.mTextColorPrimary)});
        ofObject4.setInterpolator(Interpolators.LINEAR);
        ofObject4.setStartDelay(33);
        ofObject4.setDuration(417);
        ofObject4.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda5(this));
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mContractAnimatorSet = animatorSet2;
        animatorSet2.playTogether(new Animator[]{this.mContractAnimator, ofObject3, ofObject4});
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createAnimators$0$com-android-keyguard-NumPadAnimator  reason: not valid java name */
    public /* synthetic */ void m2304lambda$createAnimators$0$comandroidkeyguardNumPadAnimator(ValueAnimator valueAnimator) {
        this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createAnimators$1$com-android-keyguard-NumPadAnimator  reason: not valid java name */
    public /* synthetic */ void m2305lambda$createAnimators$1$comandroidkeyguardNumPadAnimator(ValueAnimator valueAnimator) {
        this.mBackground.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createAnimators$2$com-android-keyguard-NumPadAnimator  reason: not valid java name */
    public /* synthetic */ void m2306lambda$createAnimators$2$comandroidkeyguardNumPadAnimator(ValueAnimator valueAnimator) {
        TextView textView = this.mDigitTextView;
        if (textView != null) {
            textView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
        Drawable drawable = this.mImageButton;
        if (drawable != null) {
            drawable.setTint(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createAnimators$3$com-android-keyguard-NumPadAnimator  reason: not valid java name */
    public /* synthetic */ void m2307lambda$createAnimators$3$comandroidkeyguardNumPadAnimator(ValueAnimator valueAnimator) {
        this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createAnimators$4$com-android-keyguard-NumPadAnimator  reason: not valid java name */
    public /* synthetic */ void m2308lambda$createAnimators$4$comandroidkeyguardNumPadAnimator(ValueAnimator valueAnimator) {
        this.mBackground.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createAnimators$5$com-android-keyguard-NumPadAnimator  reason: not valid java name */
    public /* synthetic */ void m2309lambda$createAnimators$5$comandroidkeyguardNumPadAnimator(ValueAnimator valueAnimator) {
        TextView textView = this.mDigitTextView;
        if (textView != null) {
            textView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
        Drawable drawable = this.mImageButton;
        if (drawable != null) {
            drawable.setTint(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }
}
