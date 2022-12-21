package com.nothing.systemui.facerecognition;

public interface IFaceRecognitionAnimationCallback {
    void onFaceAuthenticationTimeout();

    void onFaceSuccessConnect();

    void resetFaceImage();

    void startFailureAnimation();

    void startFreezeAnimation();

    void startLoadingAnimation();

    void startSlideUpAnimation();

    void startSuccessAnimation();
}
