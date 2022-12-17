package com.android.settings.biometrics.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.settings.R$string;

public class FingerprintEnrollParentalConsent extends FingerprintEnrollIntroduction {
    public static final int[] CONSENT_STRING_RESOURCES = {R$string.security_settings_fingerprint_enroll_consent_introduction_title, R$string.f155xb68b81de, R$string.f156x4f718905, R$string.f164x9550dc28, R$string.f165x9550dc29, R$string.f166x9550dc2a, R$string.f167x9550dc2b, R$string.f168x9550dc2c};

    public int getMetricsCategory() {
        return 1892;
    }

    /* access modifiers changed from: protected */
    public boolean onSetOrConfirmCredentials(Intent intent) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setDescriptionText(R$string.f155xb68b81de);
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
        intent.putExtra("sensor_modality", 2);
        setResult(z ? 4 : 5, intent);
        finish();
    }

    /* access modifiers changed from: protected */
    public int getFooterTitle1() {
        return R$string.f156x4f718905;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage2() {
        return R$string.f164x9550dc28;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage3() {
        return R$string.f165x9550dc29;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage4() {
        return R$string.f166x9550dc2a;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage5() {
        return R$string.f167x9550dc2b;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage6() {
        return R$string.f168x9550dc2c;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return R$string.security_settings_fingerprint_enroll_consent_introduction_title;
    }
}
