package com.android.settings.biometrics.face;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorPrivacyManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settings.R$bool;
import com.android.settings.R$id;
import com.android.settings.R$integer;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollIntroduction;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.utils.SensorPrivacyManagerHelper;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.span.LinkSpan;
import java.util.Objects;

public class FaceEnrollIntroduction extends BiometricEnrollIntroduction {
    private FaceFeatureProvider mFaceFeatureProvider;
    private FaceManager mFaceManager;
    private FooterButton mPrimaryFooterButton;
    private FooterButton mSecondaryFooterButton;
    private SensorPrivacyManager mSensorPrivacyManager;

    /* access modifiers changed from: protected */
    public boolean generateChallengeOnCreate() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getExtraKeyForBiometric() {
        return "for_face";
    }

    public int getMetricsCategory() {
        return 1506;
    }

    public int getModality() {
        return 8;
    }

    public void onClick(LinkSpan linkSpan) {
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        if (!BiometricUtils.tryStartingNextBiometricEnroll(this, 6, "skip")) {
            super.onSkipButtonClick(view);
        }
    }

    /* access modifiers changed from: protected */
    public void onEnrollmentSkipped(Intent intent) {
        if (!BiometricUtils.tryStartingNextBiometricEnroll(this, 6, "skipped")) {
            super.onEnrollmentSkipped(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishedEnrolling(Intent intent) {
        if (!BiometricUtils.tryStartingNextBiometricEnroll(this, 6, "finished")) {
            super.onFinishedEnrolling(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ((ImageView) findViewById(R$id.icon_glasses)).getBackground().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(R$id.icon_looking)).getBackground().setColorFilter(getIconColorFilter());
        ((TextView) findViewById(R$id.info_message_glasses)).setText(getInfoMessageGlasses());
        ((TextView) findViewById(R$id.info_message_looking)).setText(getInfoMessageLooking());
        ((TextView) findViewById(R$id.title_in_control)).setText(getInControlTitle());
        ((TextView) findViewById(R$id.how_message)).setText(getHowMessage());
        ((TextView) findViewById(R$id.message_in_control)).setText(getInControlMessage());
        if (getResources().getBoolean(R$bool.config_face_intro_show_less_secure)) {
            ((LinearLayout) findViewById(R$id.info_row_less_secure)).setVisibility(0);
            ((ImageView) findViewById(R$id.icon_less_secure)).getBackground().setColorFilter(getIconColorFilter());
        }
        if (getResources().getBoolean(R$bool.config_face_intro_show_require_eyes)) {
            ((LinearLayout) findViewById(R$id.info_row_require_eyes)).setVisibility(0);
            ((ImageView) findViewById(R$id.icon_require_eyes)).getBackground().setColorFilter(getIconColorFilter());
            ((TextView) findViewById(R$id.info_message_require_eyes)).setText(getInfoMessageRequireEyes());
        }
        this.mFaceManager = Utils.getFaceManagerOrNull(this);
        this.mFaceFeatureProvider = FeatureFactory.getFactory(getApplicationContext()).getFaceFeatureProvider();
        if (this.mToken == null && BiometricUtils.containsGatekeeperPasswordHandle(getIntent()) && generateChallengeOnCreate()) {
            this.mFooterBarMixin.getPrimaryButton().setEnabled(false);
            this.mFaceManager.generateChallenge(this.mUserId, new FaceEnrollIntroduction$$ExternalSyntheticLambda0(this));
        }
        this.mSensorPrivacyManager = (SensorPrivacyManager) getApplicationContext().getSystemService(SensorPrivacyManager.class);
        boolean isSensorBlocked = SensorPrivacyManagerHelper.getInstance(getApplicationContext()).isSensorBlocked(2, this.mUserId);
        Log.v("FaceEnrollIntroduction", "cameraPrivacyEnabled : " + isSensorBlocked);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(int i, int i2, long j) {
        this.mToken = BiometricUtils.requestGatekeeperHat((Context) this, getIntent(), this.mUserId, j);
        this.mSensorId = i;
        this.mChallenge = j;
        this.mFooterBarMixin.getPrimaryButton().setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageGlasses() {
        return R$string.security_settings_face_enroll_introduction_info_glasses;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageLooking() {
        return R$string.security_settings_face_enroll_introduction_info_looking;
    }

    /* access modifiers changed from: protected */
    public int getInfoMessageRequireEyes() {
        return R$string.security_settings_face_enroll_introduction_info_gaze;
    }

    /* access modifiers changed from: protected */
    public int getHowMessage() {
        return R$string.security_settings_face_enroll_introduction_how_message;
    }

    /* access modifiers changed from: protected */
    public int getInControlTitle() {
        return R$string.security_settings_face_enroll_introduction_control_title;
    }

    /* access modifiers changed from: protected */
    public int getInControlMessage() {
        return R$string.security_settings_face_enroll_introduction_control_message;
    }

    /* access modifiers changed from: protected */
    public boolean isDisabledByAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this, 128, this.mUserId) != null;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResource() {
        return R$layout.face_enroll_introduction;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDisabledByAdmin() {
        return R$string.security_settings_face_enroll_introduction_title_unlock_disabled;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return R$string.security_settings_face_enroll_introduction_title;
    }

    /* access modifiers changed from: protected */
    public String getDescriptionDisabledByAdmin() {
        return ((DevicePolicyManager) getSystemService(DevicePolicyManager.class)).getResources().getString("Settings.FACE_UNLOCK_DISABLED", new FaceEnrollIntroduction$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDescriptionDisabledByAdmin$1() {
        return getString(R$string.f150x7c35c12e);
    }

    /* access modifiers changed from: protected */
    public FooterButton getNextButton() {
        FooterBarMixin footerBarMixin = this.mFooterBarMixin;
        if (footerBarMixin != null) {
            return footerBarMixin.getPrimaryButton();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public TextView getErrorTextView() {
        return (TextView) findViewById(R$id.error_text);
    }

    private boolean maxFacesEnrolled() {
        boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
        FaceManager faceManager = this.mFaceManager;
        if (faceManager == null) {
            return false;
        }
        int i = ((FaceSensorPropertiesInternal) faceManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser;
        int size = this.mFaceManager.getEnrolledFaces(this.mUserId).size();
        int integer = getApplicationContext().getResources().getInteger(R$integer.suw_max_faces_enrollable);
        if (isAnySetupWizard) {
            if (size >= integer) {
                return true;
            }
            return false;
        } else if (size >= i) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public int checkMaxEnrolled() {
        if (this.mFaceManager == null) {
            return R$string.face_intro_error_unknown;
        }
        if (maxFacesEnrolled()) {
            return R$string.face_intro_error_max;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void getChallenge(BiometricEnrollIntroduction.GenerateChallengeCallback generateChallengeCallback) {
        FaceManager faceManagerOrNull = Utils.getFaceManagerOrNull(this);
        this.mFaceManager = faceManagerOrNull;
        if (faceManagerOrNull == null) {
            generateChallengeCallback.onChallengeGenerated(0, 0, 0);
            return;
        }
        int i = this.mUserId;
        Objects.requireNonNull(generateChallengeCallback);
        faceManagerOrNull.generateChallenge(i, new FaceEnrollIntroduction$$ExternalSyntheticLambda4(generateChallengeCallback));
    }

    /* access modifiers changed from: protected */
    public Intent getEnrollingIntent() {
        Intent intent = new Intent(this, FaceEnrollEducation.class);
        WizardManagerHelper.copyWizardManagerExtras(getIntent(), intent);
        return intent;
    }

    /* access modifiers changed from: protected */
    public int getConfirmLockTitleResId() {
        return R$string.security_settings_face_preference_title;
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        boolean z = false;
        boolean booleanExtra = getIntent().getBooleanExtra("require_consent", false);
        boolean isSensorBlocked = SensorPrivacyManagerHelper.getInstance(getApplicationContext()).isSensorBlocked(2, this.mUserId);
        if (WizardManagerHelper.isAnySetupWizard(getIntent()) || (booleanExtra && !WizardManagerHelper.isUserSetupComplete(this))) {
            z = true;
        }
        if (!isSensorBlocked || z) {
            super.onNextButtonClick(view);
            return;
        }
        if (this.mSensorPrivacyManager == null) {
            this.mSensorPrivacyManager = (SensorPrivacyManager) getApplicationContext().getSystemService(SensorPrivacyManager.class);
        }
        this.mSensorPrivacyManager.showSensorUseDialog(2);
    }

    /* access modifiers changed from: protected */
    public FooterButton getPrimaryFooterButton() {
        if (this.mPrimaryFooterButton == null) {
            this.mPrimaryFooterButton = new FooterButton.Builder(this).setText(R$string.security_settings_face_enroll_introduction_agree).setButtonType(6).setListener(new FaceEnrollIntroduction$$ExternalSyntheticLambda3(this)).setTheme(R$style.SudGlifButton_Primary).build();
        }
        return this.mPrimaryFooterButton;
    }

    /* access modifiers changed from: protected */
    public FooterButton getSecondaryFooterButton() {
        if (this.mSecondaryFooterButton == null) {
            this.mSecondaryFooterButton = new FooterButton.Builder(this).setText(R$string.security_settings_face_enroll_introduction_no_thanks).setListener(new FaceEnrollIntroduction$$ExternalSyntheticLambda2(this)).setButtonType(5).setTheme(R$style.SudGlifButton_Primary).build();
        }
        return this.mSecondaryFooterButton;
    }

    /* access modifiers changed from: protected */
    public int getAgreeButtonTextRes() {
        return R$string.security_settings_fingerprint_enroll_introduction_agree;
    }

    /* access modifiers changed from: protected */
    public int getMoreButtonTextRes() {
        return R$string.security_settings_face_enroll_introduction_more;
    }
}
