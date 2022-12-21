package com.android.keyguard;

import android.content.res.ColorStateList;
import android.view.MotionEvent;

public interface KeyguardSecurityView {
    public static final int PROMPT_REASON_AFTER_LOCKOUT = 5;
    public static final int PROMPT_REASON_DEVICE_ADMIN = 3;
    public static final int PROMPT_REASON_NONE = 0;
    public static final int PROMPT_REASON_NON_STRONG_BIOMETRIC_TIMEOUT = 7;
    public static final int PROMPT_REASON_PREPARE_FOR_UPDATE = 6;
    public static final int PROMPT_REASON_RESTART = 1;
    public static final int PROMPT_REASON_TIMEOUT = 2;
    public static final int PROMPT_REASON_USER_REQUEST = 4;
    public static final int SCREEN_ON = 1;
    public static final int VIEW_REVEALED = 2;

    boolean disallowInterceptTouch(MotionEvent motionEvent) {
        return false;
    }

    CharSequence getTitle();

    boolean needsInput();

    void onPause();

    void onResume(int i);

    void onStartingToHide() {
    }

    void reset();

    void showMessage(CharSequence charSequence, ColorStateList colorStateList);

    void showPromptReason(int i);

    void startAppearAnimation();

    boolean startDisappearAnimation(Runnable runnable);
}
