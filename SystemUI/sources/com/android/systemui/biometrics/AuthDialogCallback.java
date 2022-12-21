package com.android.systemui.biometrics;

public interface AuthDialogCallback {
    public static final int DISMISSED_BIOMETRIC_AUTHENTICATED = 4;
    public static final int DISMISSED_BUTTON_NEGATIVE = 2;
    public static final int DISMISSED_BUTTON_POSITIVE = 3;
    public static final int DISMISSED_BY_SYSTEM_SERVER = 6;
    public static final int DISMISSED_CREDENTIAL_AUTHENTICATED = 7;
    public static final int DISMISSED_ERROR = 5;
    public static final int DISMISSED_USER_CANCELED = 1;

    public @interface DismissedReason {
    }

    void onDeviceCredentialPressed();

    void onDialogAnimatedIn();

    void onDismissed(int i, byte[] bArr);

    void onSystemEvent(int i);

    void onTryAgainPressed();
}
