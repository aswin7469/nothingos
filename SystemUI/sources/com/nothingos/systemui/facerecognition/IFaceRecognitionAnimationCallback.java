package com.nothingos.systemui.facerecognition;
/* loaded from: classes2.dex */
public interface IFaceRecognitionAnimationCallback {
    void onFaceAuthenticationTimeout();

    void onFaceSuccessConnect();

    void resetFaceImage();

    void startFailureAnimation();

    void startFreezeAnimation();

    void startLoadingAnimation();

    void startSuccessAnimation();
}
