package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.CancellationSignal;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.TextViewInputDisabler;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.Interpolators;

public class KeyguardPasswordView extends KeyguardAbsKeyInputView {
    private static final long IME_DISAPPEAR_DURATION_MS = 125;
    private final int mDisappearYTranslation;
    private Interpolator mFastOutLinearInInterpolator;
    private Interpolator mLinearOutSlowInInterpolator;
    private TextView mPasswordEntry;
    private TextViewInputDisabler mPasswordEntryDisabler;

    /* access modifiers changed from: protected */
    public int getPasswordTextViewId() {
        return C1893R.C1897id.passwordEntry;
    }

    /* access modifiers changed from: protected */
    public int getPromptReasonStringRes(int i) {
        if (i != 0) {
            return i != 1 ? i != 3 ? i != 4 ? C1893R.string.kg_prompt_reason_timeout_password : C1893R.string.kg_prompt_reason_user_request : C1893R.string.kg_prompt_reason_device_admin : C1893R.string.kg_prompt_reason_restart_password;
        }
        return 0;
    }

    public int getWrongPasswordStringId() {
        return C1893R.string.kg_wrong_password;
    }

    /* access modifiers changed from: protected */
    public void resetState() {
    }

    public KeyguardPasswordView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(C1893R.dimen.disappear_y_translation);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, AndroidResources.LINEAR_OUT_SLOW_IN);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(context, AndroidResources.FAST_OUT_LINEAR_IN);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPasswordEntry = (TextView) findViewById(getPasswordTextViewId());
        this.mPasswordEntryDisabler = new TextViewInputDisabler(this.mPasswordEntry);
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        return this.mPasswordEntry.requestFocus(i, rect);
    }

    /* access modifiers changed from: protected */
    public void resetPasswordText(boolean z, boolean z2) {
        this.mPasswordEntry.setText("");
    }

    /* access modifiers changed from: protected */
    public LockscreenCredential getEnteredCredential() {
        return LockscreenCredential.createPasswordOrNone(this.mPasswordEntry.getText());
    }

    /* access modifiers changed from: protected */
    public void setPasswordEntryEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
    }

    /* access modifiers changed from: protected */
    public void setPasswordEntryInputEnabled(boolean z) {
        this.mPasswordEntryDisabler.setInputEnabled(z);
    }

    public void startAppearAnimation() {
        setAlpha(0.0f);
        animate().alpha(1.0f).setDuration(300).start();
        setTranslationY(0.0f);
    }

    public boolean startDisappearAnimation(final Runnable runnable) {
        getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), 100, Interpolators.LINEAR, (CancellationSignal) null, new WindowInsetsAnimationControlListener() {
            public void onFinished(WindowInsetsAnimationController windowInsetsAnimationController) {
            }

            public void onReady(final WindowInsetsAnimationController windowInsetsAnimationController, int i) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                ofFloat.addUpdateListener(new KeyguardPasswordView$1$$ExternalSyntheticLambda0(windowInsetsAnimationController, ofFloat));
                ofFloat.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                    }

                    public void onAnimationEnd(Animator animator) {
                        DejankUtils.postAfterTraversal(new KeyguardPasswordView$1$1$$ExternalSyntheticLambda0(this, windowInsetsAnimationController, runnable));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onAnimationEnd$0$com-android-keyguard-KeyguardPasswordView$1$1 */
                    public /* synthetic */ void mo25938x84419558(WindowInsetsAnimationController windowInsetsAnimationController, Runnable runnable) {
                        Trace.beginSection("KeyguardPasswordView#onAnimationEnd");
                        windowInsetsAnimationController.finish(false);
                        KeyguardPasswordView.this.runOnFinishImeAnimationRunnable();
                        runnable.run();
                        Trace.endSection();
                    }
                });
                ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                ofFloat.start();
            }

            static /* synthetic */ void lambda$onReady$0(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
                if (!windowInsetsAnimationController.isCancelled()) {
                    Insets shownStateInsets = windowInsetsAnimationController.getShownStateInsets();
                    windowInsetsAnimationController.setInsetsAndAlpha(Insets.add(shownStateInsets, Insets.of(0, 0, 0, (int) (((float) ((-shownStateInsets.bottom) / 4)) * valueAnimator.getAnimatedFraction()))), ((Float) valueAnimator2.getAnimatedValue()).floatValue(), valueAnimator.getAnimatedFraction());
                }
            }

            public void onCancelled(WindowInsetsAnimationController windowInsetsAnimationController) {
                KeyguardPasswordView.this.runOnFinishImeAnimationRunnable();
                runnable.run();
            }
        });
        return true;
    }

    public CharSequence getTitle() {
        return getResources().getString(17040518);
    }
}
