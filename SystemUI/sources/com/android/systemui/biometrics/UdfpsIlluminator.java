package com.android.systemui.biometrics;

interface UdfpsIlluminator {
    void setHbmProvider(UdfpsHbmProvider udfpsHbmProvider);

    void startIllumination(Runnable runnable);

    void stopIllumination();
}
