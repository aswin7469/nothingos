package com.android.systemui.biometrics;

import android.os.Bundle;
import android.view.WindowManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AuthDialog {
    public static final int ANIMATE_CREDENTIAL_INITIAL_DURATION_MS = 150;
    public static final int ANIMATE_CREDENTIAL_START_DELAY_MS = 300;
    public static final int ANIMATE_MEDIUM_TO_LARGE_DURATION_MS = 450;
    public static final int ANIMATE_SMALL_TO_MEDIUM_DURATION_MS = 150;
    public static final String KEY_BIOMETRIC_CONFIRM_VISIBILITY = "confirm_visibility";
    public static final String KEY_BIOMETRIC_DIALOG_SIZE = "size";
    public static final String KEY_BIOMETRIC_INDICATOR_ERROR_SHOWING = "error_is_temporary";
    public static final String KEY_BIOMETRIC_INDICATOR_HELP_SHOWING = "hint_is_temporary";
    public static final String KEY_BIOMETRIC_INDICATOR_STRING = "indicator_string";
    public static final String KEY_BIOMETRIC_SENSOR_PROPS = "sensor_props";
    public static final String KEY_BIOMETRIC_SENSOR_TYPE = "sensor_type";
    public static final String KEY_BIOMETRIC_SHOWING = "biometric_showing";
    public static final String KEY_BIOMETRIC_STATE = "state";
    public static final String KEY_BIOMETRIC_TRY_AGAIN_VISIBILITY = "try_agian_visibility";
    public static final String KEY_CONTAINER_ANIMATING_IN = "container_animating_in";
    public static final String KEY_CONTAINER_GOING_AWAY = "container_going_away";
    public static final String KEY_CREDENTIAL_SHOWING = "credential_showing";
    public static final int SIZE_LARGE = 3;
    public static final int SIZE_MEDIUM = 2;
    public static final int SIZE_SMALL = 1;
    public static final int SIZE_UNKNOWN = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogSize {
    }

    void animateToCredentialUI();

    void dismissFromSystemServer();

    void dismissWithoutCallback(boolean z);

    String getOpPackageName();

    long getRequestId();

    boolean isAllowDeviceCredentials();

    void onAuthenticationFailed(int i, String str);

    void onAuthenticationSucceeded(int i);

    void onError(int i, String str);

    void onHelp(int i, String str);

    void onOrientationChanged();

    void onPointerDown();

    void onSaveState(Bundle bundle);

    void show(WindowManager windowManager, Bundle bundle);

    public static class LayoutParams {
        final int mMediumHeight;
        final int mMediumWidth;

        LayoutParams(int i, int i2) {
            this.mMediumWidth = i;
            this.mMediumHeight = i2;
        }
    }
}
