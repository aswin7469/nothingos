package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.TextViewInputDisabler;
import com.android.keyguard.KeyguardPasswordView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public class KeyguardPasswordView extends KeyguardAbsKeyInputView {
    private final int mDisappearYTranslation;
    private Interpolator mFastOutLinearInInterpolator;
    private Interpolator mLinearOutSlowInInterpolator;
    private TextView mPasswordEntry;
    private TextViewInputDisabler mPasswordEntryDisabler;

    public KeyguardPasswordView(Context context) {
        this(context, null);
    }

    public KeyguardPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(R$dimen.disappear_y_translation);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(context, 17563663);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getPasswordTextViewId() {
        return R$id.passwordEntry;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getPromptReasonStringRes(int i) {
        if (i != 0) {
            if (i == 1) {
                return R$string.kg_prompt_reason_restart_password;
            }
            if (i == 2) {
                return R$string.kg_prompt_reason_timeout_password;
            }
            if (i == 3) {
                return R$string.kg_prompt_reason_device_admin;
            }
            if (i == 4) {
                return R$string.kg_prompt_reason_user_request;
            }
            if (i == 6) {
                return R$string.kg_prompt_reason_timeout_password;
            }
            return R$string.kg_prompt_reason_timeout_password;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPasswordEntry = (TextView) findViewById(getPasswordTextViewId());
        this.mPasswordEntryDisabler = new TextViewInputDisabler(this.mPasswordEntry);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i, Rect rect) {
        return this.mPasswordEntry.requestFocus(i, rect);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public void resetPasswordText(boolean z, boolean z2) {
        this.mPasswordEntry.setText("");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public LockscreenCredential getEnteredCredential() {
        return LockscreenCredential.createPasswordOrNone(this.mPasswordEntry.getText());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public void setPasswordEntryEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public void setPasswordEntryInputEnabled(boolean z) {
        this.mPasswordEntryDisabler.setInputEnabled(z);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getWrongPasswordStringId() {
        return R$string.kg_wrong_password;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public void startAppearAnimation() {
        setAlpha(0.0f);
        animate().alpha(1.0f).setDuration(500L).setStartDelay(300L).start();
        setTranslationY(0.0f);
    }

    @Override // com.android.keyguard.KeyguardInputView
    public boolean startDisappearAnimation(Runnable runnable) {
        getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), 100L, Interpolators.LINEAR, null, new AnonymousClass1(runnable));
        return true;
    }

    /* renamed from: com.android.keyguard.KeyguardPasswordView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements WindowInsetsAnimationControlListener {
        final /* synthetic */ Runnable val$finishRunnable;

        @Override // android.view.WindowInsetsAnimationControlListener
        public void onFinished(WindowInsetsAnimationController windowInsetsAnimationController) {
        }

        AnonymousClass1(Runnable runnable) {
            this.val$finishRunnable = runnable;
        }

        @Override // android.view.WindowInsetsAnimationControlListener
        public void onReady(final WindowInsetsAnimationController windowInsetsAnimationController, int i) {
            final ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.KeyguardPasswordView$1$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    KeyguardPasswordView.AnonymousClass1.lambda$onReady$0(windowInsetsAnimationController, ofFloat, valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardPasswordView.1.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    windowInsetsAnimationController.finish(false);
                    KeyguardPasswordView.this.runOnFinishImeAnimationRunnable();
                    AnonymousClass1.this.val$finishRunnable.run();
                }
            });
            ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
            ofFloat.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onReady$0(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
            if (windowInsetsAnimationController.isCancelled()) {
                return;
            }
            Insets shownStateInsets = windowInsetsAnimationController.getShownStateInsets();
            windowInsetsAnimationController.setInsetsAndAlpha(Insets.add(shownStateInsets, Insets.of(0, 0, 0, (int) (((-shownStateInsets.bottom) / 4) * valueAnimator.getAnimatedFraction()))), ((Float) valueAnimator2.getAnimatedValue()).floatValue(), valueAnimator.getAnimatedFraction());
        }

        @Override // android.view.WindowInsetsAnimationControlListener
        public void onCancelled(WindowInsetsAnimationController windowInsetsAnimationController) {
            KeyguardPasswordView.this.runOnFinishImeAnimationRunnable();
            this.val$finishRunnable.run();
        }
    }

    @Override // com.android.keyguard.KeyguardInputView
    public void animateForIme(float f, boolean z) {
        animate().cancel();
        setAlpha(z ? Math.max(f, getAlpha()) : 1.0f - f);
    }

    @Override // com.android.keyguard.KeyguardInputView
    public CharSequence getTitle() {
        return getResources().getString(17040442);
    }
}
