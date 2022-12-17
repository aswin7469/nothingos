package com.android.settings.biometrics.fingerprint;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricEnrollIntroduction;
import com.android.settings.biometrics.BiometricUtils;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.span.LinkSpan;
import java.util.Objects;

public class FingerprintEnrollIntroduction extends BiometricEnrollIntroduction {
    private DevicePolicyManager mDevicePolicyManager;
    @VisibleForTesting
    private FingerprintManager mFingerprintManager;
    private FooterButton mPrimaryFooterButton;
    private FooterButton mSecondaryFooterButton;

    /* access modifiers changed from: protected */
    public String getExtraKeyForBiometric() {
        return "for_fingerprint";
    }

    public int getMetricsCategory() {
        return 243;
    }

    public int getModality() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        this.mFingerprintManager = fingerprintManagerOrNull;
        if (fingerprintManagerOrNull == null) {
            Log.e("FingerprintIntro", "Null FingerprintManager");
            finish();
            return;
        }
        super.onCreate(bundle);
        this.mDevicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
        ((ImageView) findViewById(R$id.icon_fingerprint)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(R$id.icon_device_locked)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(R$id.icon_trash_can)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(R$id.icon_info)).getDrawable().setColorFilter(getIconColorFilter());
        ((ImageView) findViewById(R$id.icon_link)).getDrawable().setColorFilter(getIconColorFilter());
        ((TextView) findViewById(R$id.footer_message_2)).setText(getFooterMessage2());
        ((TextView) findViewById(R$id.footer_message_3)).setText(getFooterMessage3());
        ((TextView) findViewById(R$id.footer_message_4)).setText(getFooterMessage4());
        ((TextView) findViewById(R$id.footer_message_5)).setText(getFooterMessage5());
        ((TextView) findViewById(R$id.footer_message_6)).setText(getFooterMessage6());
        ((TextView) findViewById(R$id.footer_title_1)).setText(getFooterTitle1());
        ((TextView) findViewById(R$id.footer_title_2)).setText(getFooterTitle2());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0));
        findViewById(R$id.sud_layout_header).setVisibility(8);
        findViewById(R$id.sud_content_frame).setVisibility(8);
        findViewById(R$id.setup_wizard_layout).setBackgroundColor(getBackgroundColor());
        ((TextView) findViewById(R$id.introduction_title)).setTypeface(Typeface.create("NDot57", 0));
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        boolean z = false;
        boolean z2 = i == 2 || i == 6;
        if (i2 == 2 || i2 == 11 || i2 == 1) {
            z = true;
        }
        if (z2 && z) {
            intent = setSkipPendingEnroll(intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: protected */
    public void onCancelButtonClick(View view) {
        setResult(2, setSkipPendingEnroll(new Intent()));
        finish();
    }

    /* access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        onCancelButtonClick(view);
    }

    /* access modifiers changed from: package-private */
    public int getNegativeButtonTextId() {
        return R$string.nt_skip;
    }

    /* access modifiers changed from: protected */
    public int getFooterTitle1() {
        return R$string.security_settings_fingerprint_enroll_introduction_footer_title_1;
    }

    /* access modifiers changed from: protected */
    public int getFooterTitle2() {
        return R$string.security_settings_fingerprint_enroll_introduction_footer_title_2;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage2() {
        return R$string.f159xdb87ba0d;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage3() {
        return R$string.f160xdb87ba0e;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage4() {
        return R$string.f161xdb87ba0f;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage5() {
        return R$string.f162xdb87ba10;
    }

    /* access modifiers changed from: protected */
    public int getFooterMessage6() {
        return R$string.f163xdb87ba11;
    }

    /* access modifiers changed from: protected */
    public boolean isDisabledByAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this, 32, this.mUserId) != null;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResource() {
        return R$layout.fingerprint_enroll_introduction;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDisabledByAdmin() {
        return R$string.f158x2980692c;
    }

    /* access modifiers changed from: protected */
    public int getHeaderResDefault() {
        return R$string.security_settings_fingerprint_enroll_introduction_title;
    }

    /* access modifiers changed from: protected */
    public String getDescriptionDisabledByAdmin() {
        return getResources().getString(R$string.f157x4ab1f9db);
    }

    /* access modifiers changed from: protected */
    public FooterButton getCancelButton() {
        FooterBarMixin footerBarMixin = this.mFooterBarMixin;
        if (footerBarMixin != null) {
            return footerBarMixin.getSecondaryButton();
        }
        return null;
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

    /* access modifiers changed from: protected */
    public int checkMaxEnrolled() {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager == null) {
            return R$string.fingerprint_intro_error_unknown;
        }
        if (this.mFingerprintManager.getEnrolledFingerprints(this.mUserId).size() >= ((FingerprintSensorPropertiesInternal) fingerprintManager.getSensorPropertiesInternal().get(0)).maxEnrollmentsPerUser) {
            return R$string.fingerprint_intro_error_max;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void getChallenge(BiometricEnrollIntroduction.GenerateChallengeCallback generateChallengeCallback) {
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(this);
        this.mFingerprintManager = fingerprintManagerOrNull;
        if (fingerprintManagerOrNull == null) {
            generateChallengeCallback.onChallengeGenerated(0, 0, 0);
            return;
        }
        int i = this.mUserId;
        Objects.requireNonNull(generateChallengeCallback);
        fingerprintManagerOrNull.generateChallenge(i, new FingerprintEnrollIntroduction$$ExternalSyntheticLambda1(generateChallengeCallback));
    }

    /* access modifiers changed from: protected */
    public Intent getEnrollingIntent() {
        Intent intent = new Intent(this, FingerprintEnrollFindSensor.class);
        if (BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            intent.putExtra("gk_pw_handle", BiometricUtils.getGatekeeperPasswordHandle(getIntent()));
        }
        return intent;
    }

    /* access modifiers changed from: protected */
    public int getConfirmLockTitleResId() {
        return R$string.security_settings_fingerprint_preference_title;
    }

    public void onClick(LinkSpan linkSpan) {
        if ("url".equals(linkSpan.getId())) {
            Intent helpIntent = HelpUtils.getHelpIntent(this, getString(R$string.help_url_fingerprint), getClass().getName());
            if (helpIntent == null) {
                Log.w("FingerprintIntro", "Null help intent.");
                return;
            }
            try {
                startActivityForResult(helpIntent, 3);
            } catch (ActivityNotFoundException e) {
                Log.w("FingerprintIntro", "Activity was not found for intent, " + e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public FooterButton getPrimaryFooterButton() {
        if (this.mPrimaryFooterButton == null) {
            this.mPrimaryFooterButton = new FooterButton.Builder(this).setText(R$string.nt_setup).setListener(new FingerprintEnrollIntroduction$$ExternalSyntheticLambda0(this)).setButtonType(6).setTheme(R$style.SudGlifButton_PrimaryButton).build();
        }
        return this.mPrimaryFooterButton;
    }

    /* access modifiers changed from: protected */
    public FooterButton getSecondaryFooterButton() {
        if (this.mSecondaryFooterButton == null) {
            this.mSecondaryFooterButton = new FooterButton.Builder(this).setText(getNegativeButtonTextId()).setListener(new FingerprintEnrollIntroduction$$ExternalSyntheticLambda2(this)).setButtonType(5).setTheme(R$style.SudGlifButton_SecondaryButton).build();
        }
        return this.mSecondaryFooterButton;
    }

    /* access modifiers changed from: protected */
    public int getAgreeButtonTextRes() {
        return R$string.nt_setup;
    }

    /* access modifiers changed from: protected */
    public int getMoreButtonTextRes() {
        return R$string.security_settings_face_enroll_introduction_more;
    }

    protected static Intent setSkipPendingEnroll(Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.putExtra("skip_pending_enroll", true);
        return intent;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }
}
