package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import com.android.internal.widget.LockscreenCredential;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import java.util.ArrayList;

public abstract class KeyguardPinBasedInputView extends KeyguardAbsKeyInputView {
    private NumPadKey[] mButtons;
    private NumPadButton mDeleteButton;
    private NumPadButton mOkButton;
    protected PasswordTextView mPasswordEntry;

    /* access modifiers changed from: protected */
    public int getPromptReasonStringRes(int i) {
        if (i != 0) {
            return i != 1 ? i != 3 ? i != 4 ? C1893R.string.kg_prompt_reason_timeout_pin : C1893R.string.kg_prompt_reason_user_request : C1893R.string.kg_prompt_reason_device_admin : C1893R.string.kg_prompt_reason_restart_pin;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void resetState() {
    }

    public KeyguardPinBasedInputView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardPinBasedInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mButtons = new NumPadKey[10];
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        return this.mPasswordEntry.requestFocus(i, rect);
    }

    /* access modifiers changed from: protected */
    public void setPasswordEntryEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
        this.mOkButton.setEnabled(z);
        if (z && !this.mPasswordEntry.hasFocus()) {
            this.mPasswordEntry.requestFocus();
        }
    }

    /* access modifiers changed from: protected */
    public void setPasswordEntryInputEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
        this.mOkButton.setEnabled(z);
        if (z && !this.mPasswordEntry.hasFocus()) {
            this.mPasswordEntry.requestFocus();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (KeyEvent.isConfirmKey(i)) {
            this.mOkButton.performClick();
            return true;
        } else if (i == 67) {
            this.mDeleteButton.performClick();
            return true;
        } else if (i >= 7 && i <= 16) {
            performNumberClick(i - 7);
            return true;
        } else if (i < 144 || i > 153) {
            return super.onKeyDown(i, keyEvent);
        } else {
            performNumberClick(i - 144);
            return true;
        }
    }

    private void performNumberClick(int i) {
        if (i >= 0 && i <= 9) {
            this.mButtons[i].performClick();
        }
    }

    /* access modifiers changed from: protected */
    public void resetPasswordText(boolean z, boolean z2) {
        this.mPasswordEntry.reset(z, z2);
    }

    /* access modifiers changed from: protected */
    public LockscreenCredential getEnteredCredential() {
        return LockscreenCredential.createPinOrNone(this.mPasswordEntry.getText());
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        PasswordTextView passwordTextView = (PasswordTextView) findViewById(getPasswordTextViewId());
        this.mPasswordEntry = passwordTextView;
        passwordTextView.setSelected(true);
        this.mOkButton = (NumPadButton) findViewById(C1893R.C1897id.key_enter);
        NumPadButton numPadButton = (NumPadButton) findViewById(C1893R.C1897id.delete_button);
        this.mDeleteButton = numPadButton;
        numPadButton.setVisibility(0);
        this.mButtons[0] = (NumPadKey) findViewById(C1893R.C1897id.key0);
        this.mButtons[1] = (NumPadKey) findViewById(C1893R.C1897id.key1);
        this.mButtons[2] = (NumPadKey) findViewById(C1893R.C1897id.key2);
        this.mButtons[3] = (NumPadKey) findViewById(C1893R.C1897id.key3);
        this.mButtons[4] = (NumPadKey) findViewById(C1893R.C1897id.key4);
        this.mButtons[5] = (NumPadKey) findViewById(C1893R.C1897id.key5);
        this.mButtons[6] = (NumPadKey) findViewById(C1893R.C1897id.key6);
        this.mButtons[7] = (NumPadKey) findViewById(C1893R.C1897id.key7);
        this.mButtons[8] = (NumPadKey) findViewById(C1893R.C1897id.key8);
        this.mButtons[9] = (NumPadKey) findViewById(C1893R.C1897id.key9);
        this.mPasswordEntry.requestFocus();
        super.onFinishInflate();
        reloadColors();
    }

    /* access modifiers changed from: package-private */
    public NumPadKey[] getButtons() {
        return this.mButtons;
    }

    public void reloadColors() {
        for (NumPadKey reloadColors : this.mButtons) {
            reloadColors.reloadColors();
        }
        this.mPasswordEntry.reloadColors();
        this.mDeleteButton.reloadColors();
        this.mOkButton.reloadColors();
    }

    public CharSequence getTitle() {
        return getContext().getString(17040521);
    }

    public void startErrorAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 1; i <= 9; i++) {
            arrayList2.add(this.mButtons[i]);
        }
        arrayList2.add(this.mDeleteButton);
        arrayList2.add(this.mButtons[0]);
        arrayList2.add(this.mOkButton);
        int i2 = 0;
        for (int i3 = 0; i3 < arrayList2.size(); i3++) {
            View view = (View) arrayList2.get(i3);
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.setStartDelay((long) i2);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.8f});
            ofFloat.setInterpolator(Interpolators.STANDARD);
            ofFloat.addUpdateListener(new KeyguardPinBasedInputView$$ExternalSyntheticLambda0(view));
            ofFloat.setDuration(50);
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.8f, 1.0f});
            ofFloat2.setInterpolator(Interpolators.STANDARD);
            ofFloat2.addUpdateListener(new KeyguardPinBasedInputView$$ExternalSyntheticLambda1(view));
            ofFloat2.setDuration(617);
            animatorSet2.playSequentially(new Animator[]{ofFloat, ofFloat2});
            arrayList.add(animatorSet2);
            i2 += 33;
        }
        animatorSet.playTogether(arrayList);
        animatorSet.start();
    }

    static /* synthetic */ void lambda$startErrorAnimation$0(View view, ValueAnimator valueAnimator) {
        view.setScaleX(((Float) valueAnimator.getAnimatedValue()).floatValue());
        view.setScaleY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    static /* synthetic */ void lambda$startErrorAnimation$1(View view, ValueAnimator valueAnimator) {
        view.setScaleX(((Float) valueAnimator.getAnimatedValue()).floatValue());
        view.setScaleY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
