package com.android.keyguard;

import com.android.keyguard.KeyguardSecurityModel;

public interface KeyguardSecurityCallback {
    void dismiss(boolean z, int i, KeyguardSecurityModel.SecurityMode securityMode);

    void dismiss(boolean z, int i, boolean z2, KeyguardSecurityModel.SecurityMode securityMode);

    boolean isVerifyUnlockOnly();

    void onCancelClicked() {
    }

    void onUserInput();

    void reportUnlockAttempt(int i, boolean z, int i2);

    void reset();

    void userActivity();
}
