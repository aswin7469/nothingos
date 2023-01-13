package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardIndication;

public class KeyguardIndicationTextView extends TextView {
    private static int sButtonStyleId = 2132017923;
    private static int sStyleId = 2132017922;
    private boolean mAnimationsEnabled = true;
    private KeyguardIndication mKeyguardIndicationInfo;
    private Animator mLastAnimator;
    private CharSequence mMessage;

    public KeyguardIndicationTextView(Context context) {
        super(context);
    }

    public KeyguardIndicationTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public KeyguardIndicationTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public KeyguardIndicationTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void clearMessages() {
        Animator animator = this.mLastAnimator;
        if (animator != null) {
            animator.cancel();
        }
        setText("");
    }

    public void switchIndication(int i) {
        switchIndication(getResources().getText(i), (KeyguardIndication) null);
    }

    public void switchIndication(KeyguardIndication keyguardIndication) {
        switchIndication(keyguardIndication == null ? null : keyguardIndication.getMessage(), keyguardIndication);
    }

    public void switchIndication(CharSequence charSequence, KeyguardIndication keyguardIndication) {
        switchIndication(charSequence, keyguardIndication, true, (Runnable) null);
    }

    public void switchIndication(CharSequence charSequence, KeyguardIndication keyguardIndication, boolean z, final Runnable runnable) {
        this.mMessage = charSequence;
        this.mKeyguardIndicationInfo = keyguardIndication;
        if (z) {
            boolean z2 = (keyguardIndication == null || keyguardIndication.getIcon() == null) ? false : true;
            AnimatorSet animatorSet = new AnimatorSet();
            if (!TextUtils.isEmpty(this.mMessage) || z2) {
                AnimatorSet inAnimator = getInAnimator();
                inAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        Runnable runnable = runnable;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
                animatorSet.playSequentially(new Animator[]{getOutAnimator(), inAnimator});
            } else {
                AnimatorSet outAnimator = getOutAnimator();
                outAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        Runnable runnable = runnable;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
                animatorSet.play(outAnimator);
            }
            Animator animator = this.mLastAnimator;
            if (animator != null) {
                animator.cancel();
            }
            this.mLastAnimator = animatorSet;
            animatorSet.start();
            return;
        }
        setAlpha(1.0f);
        setTranslationY(0.0f);
        setNextIndication();
        if (runnable != null) {
            runnable.run();
        }
        Animator animator2 = this.mLastAnimator;
        if (animator2 != null) {
            animator2.cancel();
            this.mLastAnimator = null;
        }
    }

    private AnimatorSet getOutAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, new float[]{0.0f});
        ofFloat.setDuration(getFadeOutDuration());
        ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            private boolean mCancelled = false;

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (!this.mCancelled) {
                    KeyguardIndicationTextView.this.setNextIndication();
                }
            }

            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.mCancelled = true;
                KeyguardIndicationTextView.this.setAlpha(0.0f);
            }
        });
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, new float[]{0.0f, (float) (-getYTranslationPixels())});
        ofFloat2.setDuration(getFadeOutDuration());
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        return animatorSet;
    }

    /* access modifiers changed from: private */
    public void setNextIndication() {
        KeyguardIndication keyguardIndication = this.mKeyguardIndicationInfo;
        if (keyguardIndication != null) {
            if (keyguardIndication.getBackground() != null) {
                setTextAppearance(sButtonStyleId);
            } else {
                setTextAppearance(sStyleId);
            }
            setBackground(this.mKeyguardIndicationInfo.getBackground());
            setTextColor(this.mKeyguardIndicationInfo.getTextColor());
            setOnClickListener(this.mKeyguardIndicationInfo.getClickListener());
            setClickable(this.mKeyguardIndicationInfo.getClickListener() != null);
            Drawable icon = this.mKeyguardIndicationInfo.getIcon();
            if (icon != null) {
                icon.setTint(getCurrentTextColor());
                if (icon instanceof AnimatedVectorDrawable) {
                    ((AnimatedVectorDrawable) icon).start();
                }
            }
            setCompoundDrawablesRelativeWithIntrinsicBounds(icon, (Drawable) null, (Drawable) null, (Drawable) null);
        }
        setText(this.mMessage);
    }

    private AnimatorSet getInAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, new float[]{1.0f});
        ofFloat.setStartDelay(getFadeInDelay());
        ofFloat.setDuration(getFadeInDuration());
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, new float[]{(float) getYTranslationPixels(), 0.0f});
        ofFloat2.setDuration(getYInDuration());
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                KeyguardIndicationTextView.this.setTranslationY(0.0f);
                KeyguardIndicationTextView.this.setAlpha(1.0f);
            }
        });
        animatorSet.playTogether(new Animator[]{ofFloat2, ofFloat});
        return animatorSet;
    }

    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
    }

    private long getFadeInDelay() {
        return !this.mAnimationsEnabled ? 0 : 150;
    }

    private long getFadeInDuration() {
        return !this.mAnimationsEnabled ? 0 : 317;
    }

    private long getYInDuration() {
        return !this.mAnimationsEnabled ? 0 : 600;
    }

    private long getFadeOutDuration() {
        return !this.mAnimationsEnabled ? 0 : 167;
    }

    private int getYTranslationPixels() {
        return this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.keyguard_indication_y_translation);
    }
}
