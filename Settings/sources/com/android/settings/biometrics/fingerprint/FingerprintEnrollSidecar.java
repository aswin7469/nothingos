package com.android.settings.biometrics.fingerprint;

import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.biometrics.BiometricEnrollSidecar;

public class FingerprintEnrollSidecar extends BiometricEnrollSidecar {
    private int mEnrollReason;
    private FingerprintManager.EnrollmentCallback mEnrollmentCallback = new FingerprintManager.EnrollmentCallback() {
        public void onEnrollmentProgress(int i) {
            FingerprintEnrollSidecar.super.onEnrollmentProgress(i);
        }

        public void onEnrollmentHelp(int i, CharSequence charSequence) {
            FingerprintEnrollSidecar.super.onEnrollmentHelp(i, charSequence);
        }

        public void onEnrollmentError(int i, CharSequence charSequence) {
            FingerprintEnrollSidecar.super.onEnrollmentError(i, charSequence);
        }
    };
    private FingerprintUpdater mFingerprintUpdater;

    public int getMetricsCategory() {
        return 245;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mFingerprintUpdater = new FingerprintUpdater(activity);
    }

    /* access modifiers changed from: protected */
    public void startEnrollment() {
        super.startEnrollment();
        byte[] bArr = this.mToken;
        if (bArr == null) {
            Log.e("FingerprintEnrollSidecar", "Null hardware auth token for enroll");
            onEnrollmentError(1, getString(R$string.fingerprint_intro_error_unknown));
            return;
        }
        this.mFingerprintUpdater.enroll(bArr, this.mEnrollmentCancel, this.mUserId, this.mEnrollmentCallback, this.mEnrollReason);
    }

    public void setEnrollReason(int i) {
        this.mEnrollReason = i;
    }
}
