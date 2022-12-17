package com.android.settings.biometrics.face;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.settings.R$string;

public class FaceEnrollParentalConsent extends FaceEnrollIntroduction {
    public static final int[] CONSENT_STRING_RESOURCES = {R$string.security_settings_face_enroll_consent_introduction_title, R$string.security_settings_face_enroll_introduction_consent_message, R$string.security_settings_face_enroll_introduction_info_consent_glasses, R$string.security_settings_face_enroll_introduction_info_consent_looking, R$string.security_settings_face_enroll_introduction_info_consent_gaze, R$string.security_settings_face_enroll_introduction_how_consent_message, R$string.security_settings_face_enroll_introduction_control_consent_title, R$string.f149xf37cee4f};

    /* access modifiers changed from: protected */
    public boolean generateChallengeOnCreate() {
        return false;
    }

    public int getMetricsCategory() {
        return 1893;
    }

    /* access modifiers changed from: protected */
    public boolean onSetOrConfirmCredentials(Intent intent) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setDescriptionText(R$string.security_settings_face_enroll_introduction_consent_message);
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        onConsentResult(true);
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        onConsentResult(false);
    }

    /* access modifiers changed from: protected */
    public void onEnrollmentSkipped(Intent intent) {
        onConsentResult(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishedEnrolling(Intent intent) {
        onConsentResult(true);
    }

    private void onConsentResult(boolean z) {
        Intent intent = new Intent();
        intent.putExtra("sensor_modality", 8);
        setResult(z ? 4 : 5, intent);
        finish();
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageGlasses() {
        return R$string.security_settings_face_enroll_introduction_info_consent_glasses;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageLooking() {
        return R$string.security_settings_face_enroll_introduction_info_consent_looking;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageRequireEyes() {
        return R$string.security_settings_face_enroll_introduction_info_consent_gaze;
    }

    /* access modifiers changed from: protected */
    public int getHowMessage() {
        return R$string.security_settings_face_enroll_introduction_how_consent_message;
    }

    /* access modifiers changed from: protected */
    public int getInControlTitle() {
        return R$string.security_settings_face_enroll_introduction_control_consent_title;
    }

    /* access modifiers changed from: protected */
    public int getInControlMessage() {
        return R$string.f149xf37cee4f;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return R$string.security_settings_face_enroll_consent_introduction_title;
    }
}
