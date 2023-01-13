package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import com.android.internal.widget.LockscreenCredential;
import com.android.systemui.C1894R;

public abstract class KeyguardAbsKeyInputView extends KeyguardInputView {
    protected static final int MINIMUM_PASSWORD_LENGTH_BEFORE_REPORT = 3;
    protected View mEcaView;
    private KeyDownListener mKeyDownListener;

    public interface KeyDownListener {
        boolean onKeyDown(int i, KeyEvent keyEvent);
    }

    /* access modifiers changed from: protected */
    public abstract LockscreenCredential getEnteredCredential();

    /* access modifiers changed from: protected */
    public abstract int getPasswordTextViewId();

    /* access modifiers changed from: protected */
    public abstract int getPromptReasonStringRes(int i);

    /* access modifiers changed from: protected */
    public int getWrongPasswordStringId() {
        return C1894R.string.kg_wrong_password;
    }

    /* access modifiers changed from: protected */
    public abstract void resetPasswordText(boolean z, boolean z2);

    /* access modifiers changed from: protected */
    public abstract void resetState();

    /* access modifiers changed from: protected */
    public abstract void setPasswordEntryEnabled(boolean z);

    /* access modifiers changed from: protected */
    public abstract void setPasswordEntryInputEnabled(boolean z);

    public KeyguardAbsKeyInputView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardAbsKeyInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mEcaView = findViewById(C1894R.C1898id.keyguard_selector_fade_container);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        KeyDownListener keyDownListener = this.mKeyDownListener;
        return keyDownListener != null && keyDownListener.onKeyDown(i, keyEvent);
    }

    public void doHapticKeyClick() {
        performHapticFeedback(1, 1);
    }

    public void setKeyDownListener(KeyDownListener keyDownListener) {
        this.mKeyDownListener = keyDownListener;
    }
}
