package com.android.settings.biometrics;

import android.hardware.face.FaceManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MultiBiometricEnrollHelper$$ExternalSyntheticLambda1 implements FaceManager.GenerateChallengeCallback {
    public final /* synthetic */ MultiBiometricEnrollHelper f$0;

    public /* synthetic */ MultiBiometricEnrollHelper$$ExternalSyntheticLambda1(MultiBiometricEnrollHelper multiBiometricEnrollHelper) {
        this.f$0 = multiBiometricEnrollHelper;
    }

    public final void onGenerateChallengeResult(int i, int i2, long j) {
        this.f$0.lambda$launchFaceEnroll$0(i, i2, j);
    }
}
