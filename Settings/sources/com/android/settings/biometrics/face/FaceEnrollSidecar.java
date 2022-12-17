package com.android.settings.biometrics.face;

import android.app.Activity;
import android.hardware.face.FaceManager;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import java.util.Arrays;

public class FaceEnrollSidecar extends BiometricEnrollSidecar {
    private final int[] mDisabledFeatures;
    private FaceManager.EnrollmentCallback mEnrollmentCallback = new FaceManager.EnrollmentCallback() {
        public void onEnrollmentProgress(int i) {
            FaceEnrollSidecar.super.onEnrollmentProgress(i);
        }

        public void onEnrollmentHelp(int i, CharSequence charSequence) {
            FaceEnrollSidecar.super.onEnrollmentHelp(i, charSequence);
        }

        public void onEnrollmentError(int i, CharSequence charSequence) {
            FaceEnrollSidecar.super.onEnrollmentError(i, charSequence);
        }
    };
    private FaceUpdater mFaceUpdater;

    public int getMetricsCategory() {
        return 1509;
    }

    public FaceEnrollSidecar(int[] iArr) {
        this.mDisabledFeatures = Arrays.copyOf(iArr, iArr.length);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mFaceUpdater = new FaceUpdater(activity);
    }

    public void startEnrollment() {
        super.startEnrollment();
        this.mFaceUpdater.enroll(this.mUserId, this.mToken, this.mEnrollmentCancel, this.mEnrollmentCallback, this.mDisabledFeatures);
    }
}
