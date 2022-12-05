package com.android.systemui.biometrics;

import android.content.Context;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImeAwareEditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class AuthCredentialPasswordView extends AuthCredentialView implements TextView.OnEditorActionListener {
    private final InputMethodManager mImm = (InputMethodManager) ((LinearLayout) this).mContext.getSystemService(InputMethodManager.class);
    private ImeAwareEditText mPasswordField;

    public AuthCredentialPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        ImeAwareEditText findViewById = findViewById(R$id.lockPassword);
        this.mPasswordField = findViewById;
        findViewById.setOnEditorActionListener(this);
        this.mPasswordField.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.systemui.biometrics.AuthCredentialPasswordView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view, int i, KeyEvent keyEvent) {
                boolean lambda$onFinishInflate$0;
                lambda$onFinishInflate$0 = AuthCredentialPasswordView.this.lambda$onFinishInflate$0(view, i, keyEvent);
                return lambda$onFinishInflate$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$0(View view, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        if (keyEvent.getAction() == 1) {
            this.mContainerView.sendEarlyUserCanceled();
            this.mContainerView.animateAway(1);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mPasswordField.setTextOperationUser(UserHandle.of(this.mUserId));
        if (this.mCredentialType == 1) {
            this.mPasswordField.setInputType(18);
        }
        this.mPasswordField.requestFocus();
        this.mPasswordField.scheduleShowSoftInput();
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean z = keyEvent == null && (i == 0 || i == 6 || i == 5);
        boolean z2 = keyEvent != null && KeyEvent.isConfirmKey(keyEvent.getKeyCode()) && keyEvent.getAction() == 0;
        if (z || z2) {
            checkPasswordAndUnlock();
            return true;
        }
        return false;
    }

    private void checkPasswordAndUnlock() {
        LockscreenCredential createPasswordOrNone;
        if (this.mCredentialType == 1) {
            createPasswordOrNone = LockscreenCredential.createPinOrNone(this.mPasswordField.getText());
        } else {
            createPasswordOrNone = LockscreenCredential.createPasswordOrNone(this.mPasswordField.getText());
        }
        try {
            if (!createPasswordOrNone.isNone()) {
                this.mPendingLockCheck = LockPatternChecker.verifyCredential(this.mLockPatternUtils, createPasswordOrNone, this.mEffectiveUserId, 1, new LockPatternChecker.OnVerifyCallback() { // from class: com.android.systemui.biometrics.AuthCredentialPasswordView$$ExternalSyntheticLambda1
                    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
                        AuthCredentialPasswordView.this.onCredentialVerified(verifyCredentialResponse, i);
                    }
                });
                createPasswordOrNone.close();
                return;
            }
            createPasswordOrNone.close();
        } catch (Throwable th) {
            if (createPasswordOrNone != null) {
                try {
                    createPasswordOrNone.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthCredentialView
    public void onCredentialVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        super.onCredentialVerified(verifyCredentialResponse, i);
        if (verifyCredentialResponse.isMatched()) {
            this.mImm.hideSoftInputFromWindow(getWindowToken(), 0);
        } else {
            this.mPasswordField.setText("");
        }
    }
}
