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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardIndication;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class KeyguardIndicationTextView extends TextView {
    private static int sStyleId = R$style.TextAppearance_Keyguard_BottomArea;
    private static int sButtonStyleId = R$style.TextAppearance_Keyguard_BottomArea_Button;
    private long mNextAnimationTime = 0;
    private boolean mAnimationsEnabled = true;
    private LinkedList<CharSequence> mMessages = new LinkedList<>();
    private LinkedList<KeyguardIndication> mKeyguardIndicationInfo = new LinkedList<>();
    private LinkedList<AnimatorSet> mAnimSet = new LinkedList<>();

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
        this.mMessages.clear();
        this.mKeyguardIndicationInfo.clear();
        cancelAnimatorSet();
        setNextAnimationTime(System.currentTimeMillis());
    }

    public void switchIndication(KeyguardIndication keyguardIndication) {
        switchIndication(keyguardIndication == null ? null : keyguardIndication.getMessage(), keyguardIndication);
    }

    public void switchIndication(CharSequence charSequence, KeyguardIndication keyguardIndication) {
        if (charSequence == null) {
            charSequence = "";
        }
        CharSequence peekLast = this.mMessages.peekLast();
        if (!TextUtils.equals(peekLast, charSequence)) {
            if (peekLast == null && TextUtils.equals(charSequence, getText())) {
                return;
            }
            this.mMessages.add(charSequence);
            this.mKeyguardIndicationInfo.add(keyguardIndication);
            boolean z = (keyguardIndication == null || keyguardIndication.getIcon() == null) ? false : true;
            AnimatorSet animatorSet = new AnimatorSet();
            AnimatorSet.Builder play = animatorSet.play(getOutAnimator());
            long currentTimeMillis = System.currentTimeMillis();
            long max = Math.max(0L, this.mNextAnimationTime - currentTimeMillis);
            setNextAnimationTime(currentTimeMillis + max + getFadeOutDuration());
            long longValue = (keyguardIndication == null || keyguardIndication.getMinVisibilityMillis() == null) ? 1500L : keyguardIndication.getMinVisibilityMillis().longValue();
            if (!charSequence.equals("") || z) {
                setNextAnimationTime(this.mNextAnimationTime + longValue);
                play.before(getInAnimator());
            }
            animatorSet.setStartDelay(max);
            animatorSet.start();
            this.mAnimSet.add(animatorSet);
        }
    }

    private AnimatorSet getOutAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f);
        ofFloat.setDuration(getFadeOutDuration());
        ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardIndicationTextView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                KeyguardIndication keyguardIndication = (KeyguardIndication) KeyguardIndicationTextView.this.mKeyguardIndicationInfo.poll();
                if (keyguardIndication != null) {
                    if (keyguardIndication.getBackground() != null) {
                        KeyguardIndicationTextView.this.setTextAppearance(KeyguardIndicationTextView.sButtonStyleId);
                    } else {
                        KeyguardIndicationTextView.this.setTextAppearance(KeyguardIndicationTextView.sStyleId);
                    }
                    KeyguardIndicationTextView.this.setBackground(keyguardIndication.getBackground());
                    KeyguardIndicationTextView.this.setTextColor(keyguardIndication.getTextColor());
                    KeyguardIndicationTextView.this.setOnClickListener(keyguardIndication.getClickListener());
                    KeyguardIndicationTextView.this.setClickable(keyguardIndication.getClickListener() != null);
                    Drawable icon = keyguardIndication.getIcon();
                    if (icon != null) {
                        icon.setTint(KeyguardIndicationTextView.this.getCurrentTextColor());
                        if (icon instanceof AnimatedVectorDrawable) {
                            ((AnimatedVectorDrawable) icon).start();
                        }
                    }
                    KeyguardIndicationTextView.this.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, (Drawable) null, (Drawable) null, (Drawable) null);
                }
                KeyguardIndicationTextView keyguardIndicationTextView = KeyguardIndicationTextView.this;
                keyguardIndicationTextView.setText((CharSequence) keyguardIndicationTextView.mMessages.poll());
                KeyguardIndicationTextView.this.mAnimSet.poll();
                Log.d("IndicationTextView", "ACTION_BATTERY_CHANGED Indication showing text=" + ((Object) KeyguardIndicationTextView.this.getText()) + ", time=" + System.currentTimeMillis());
            }
        });
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0.0f, -getYTranslationPixels());
        ofFloat2.setDuration(getFadeOutDuration());
        animatorSet.playTogether(ofFloat, ofFloat2);
        return animatorSet;
    }

    private AnimatorSet getInAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f);
        ofFloat.setStartDelay(getFadeInDelay());
        ofFloat.setDuration(getFadeInDuration());
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getYTranslationPixels(), 0.0f);
        ofFloat2.setDuration(getYInDuration());
        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardIndicationTextView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                KeyguardIndicationTextView.this.setTranslationY(0.0f);
            }
        });
        animatorSet.playTogether(ofFloat2, ofFloat);
        return animatorSet;
    }

    @VisibleForTesting
    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
    }

    private long getFadeInDelay() {
        return !this.mAnimationsEnabled ? 0L : 150L;
    }

    private long getFadeInDuration() {
        return !this.mAnimationsEnabled ? 0L : 317L;
    }

    private long getYInDuration() {
        return !this.mAnimationsEnabled ? 0L : 600L;
    }

    private long getFadeOutDuration() {
        return !this.mAnimationsEnabled ? 0L : 167L;
    }

    private void setNextAnimationTime(long j) {
        if (this.mAnimationsEnabled) {
            this.mNextAnimationTime = j;
        } else {
            this.mNextAnimationTime = 0L;
        }
    }

    private int getYTranslationPixels() {
        return ((TextView) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_indication_y_translation);
    }

    private void cancelAnimatorSet() {
        for (int i = 0; i < this.mAnimSet.size(); i++) {
            this.mAnimSet.get(i).cancel();
        }
        this.mAnimSet.clear();
        setText("");
    }
}
