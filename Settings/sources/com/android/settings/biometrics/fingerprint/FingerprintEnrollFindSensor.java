package com.android.settings.biometrics.fingerprint;

import android.app.ActionBar;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.view.View;
import com.android.settings.R;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricEnrollSidecar;
import com.android.settings.biometrics.BiometricUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import java.util.List;
/* loaded from: classes.dex */
public class FingerprintEnrollFindSensor extends BiometricEnrollBase implements BiometricEnrollSidecar.Listener {
    private FingerprintFindSensorAnimation mAnimation;
    private boolean mCanAssumeUdfps;
    private boolean mNextClicked;
    private FingerprintEnrollSidecar mSidecar;

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 241;
    }

    @Override // com.android.settings.biometrics.BiometricEnrollSidecar.Listener
    public void onEnrollmentHelp(int i, CharSequence charSequence) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase, com.android.settings.core.InstrumentedActivity, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        List sensorPropertiesInternal = ((FingerprintManager) getSystemService(FingerprintManager.class)).getSensorPropertiesInternal();
        boolean z = true;
        if (sensorPropertiesInternal == null || sensorPropertiesInternal.size() != 1 || !((FingerprintSensorPropertiesInternal) sensorPropertiesInternal.get(0)).isAnyUdfpsType()) {
            z = false;
        }
        this.mCanAssumeUdfps = z;
        setContentView(getContentView());
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_fingerprint_enroll_enrolling_skip).setListener(new View.OnClickListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollFindSensor$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FingerprintEnrollFindSensor.this.onSkipButtonClick(view);
            }
        }).setButtonType(7).setTheme(R.style.SudGlifButton_Secondary).build());
        if (this.mCanAssumeUdfps) {
            startActivityForResult(getFingerprintEnrollingIntent(), 5);
            setHeaderText(R.string.security_settings_udfps_enroll_find_sensor_title);
            setDescriptionText(R.string.security_settings_udfps_enroll_find_sensor_message);
            this.mFooterBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.security_settings_udfps_enroll_find_sensor_start_button).setListener(new View.OnClickListener() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollFindSensor$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    FingerprintEnrollFindSensor.this.onStartButtonClick(view);
                }
            }).setButtonType(5).setTheme(R.style.SudGlifButton_Primary).build());
        } else {
            setHeaderText(R.string.security_settings_fingerprint_enroll_find_sensor_title);
            setDescriptionText(R.string.security_settings_fingerprint_enroll_find_sensor_message);
        }
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (this.mToken == null && BiometricUtils.containsGatekeeperPasswordHandle(getIntent())) {
            ((FingerprintManager) getSystemService(FingerprintManager.class)).generateChallenge(this.mUserId, new FingerprintManager.GenerateChallengeCallback() { // from class: com.android.settings.biometrics.fingerprint.FingerprintEnrollFindSensor$$ExternalSyntheticLambda0
                public final void onChallengeGenerated(int i, int i2, long j) {
                    FingerprintEnrollFindSensor.this.lambda$onCreate$0(i, i2, j);
                }
            });
        } else if (this.mToken != null) {
            startLookingForFingerprint();
        } else {
            throw new IllegalStateException("HAT and GkPwHandle both missing...");
        }
        this.mAnimation = null;
        if (!this.mCanAssumeUdfps) {
            View findViewById = findViewById(R.id.fingerprint_sensor_location_animation);
            if (!(findViewById instanceof FingerprintFindSensorAnimation)) {
                return;
            }
            this.mAnimation = (FingerprintFindSensorAnimation) findViewById;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(int i, int i2, long j) {
        this.mChallenge = j;
        this.mSensorId = i;
        this.mToken = BiometricUtils.requestGatekeeperHat(this, getIntent(), this.mUserId, j);
        getIntent().putExtra("hw_auth_token", this.mToken);
        startLookingForFingerprint();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        stopLookingForFingerprint();
        super.onBackPressed();
    }

    protected int getContentView() {
        if (this.mCanAssumeUdfps) {
            return R.layout.udfps_enroll_find_sensor_layout;
        }
        return R.layout.fingerprint_enroll_find_sensor;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        FingerprintFindSensorAnimation fingerprintFindSensorAnimation = this.mAnimation;
        if (fingerprintFindSensorAnimation != null) {
            fingerprintFindSensorAnimation.startAnimation();
        }
    }

    private void stopLookingForFingerprint() {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = this.mSidecar;
        if (fingerprintEnrollSidecar != null) {
            fingerprintEnrollSidecar.setListener(null);
            this.mSidecar.cancelEnrollment();
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
        }
    }

    private void startLookingForFingerprint() {
        if (this.mCanAssumeUdfps) {
            return;
        }
        FingerprintEnrollSidecar fingerprintEnrollSidecar = (FingerprintEnrollSidecar) getSupportFragmentManager().findFragmentByTag("sidecar");
        this.mSidecar = fingerprintEnrollSidecar;
        if (fingerprintEnrollSidecar == null) {
            FingerprintEnrollSidecar fingerprintEnrollSidecar2 = new FingerprintEnrollSidecar();
            this.mSidecar = fingerprintEnrollSidecar2;
            fingerprintEnrollSidecar2.setEnrollReason(1);
            getSupportFragmentManager().beginTransaction().add(this.mSidecar, "sidecar").commitAllowingStateLoss();
        }
        this.mSidecar.setListener(this);
    }

    @Override // com.android.settings.biometrics.BiometricEnrollSidecar.Listener
    public void onEnrollmentProgressChange(int i, int i2) {
        this.mNextClicked = true;
        proceedToEnrolling(true);
    }

    @Override // com.android.settings.biometrics.BiometricEnrollSidecar.Listener
    public void onEnrollmentError(int i, CharSequence charSequence) {
        if (this.mNextClicked && i == 5) {
            this.mNextClicked = false;
            proceedToEnrolling(false);
            return;
        }
        FingerprintErrorDialog.showErrorDialog(this, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        FingerprintFindSensorAnimation fingerprintFindSensorAnimation = this.mAnimation;
        if (fingerprintFindSensorAnimation != null) {
            fingerprintFindSensorAnimation.pauseAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.biometrics.BiometricEnrollBase
    public boolean shouldFinishWhenBackgrounded() {
        return super.shouldFinishWhenBackgrounded() && !this.mNextClicked;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        FingerprintFindSensorAnimation fingerprintFindSensorAnimation = this.mAnimation;
        if (fingerprintFindSensorAnimation != null) {
            fingerprintFindSensorAnimation.stopAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStartButtonClick(View view) {
        startActivityForResult(getFingerprintEnrollingIntent(), 5);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSkipButtonClick(View view) {
        stopLookingForFingerprint();
        setResult(2);
        finish();
    }

    private void proceedToEnrolling(boolean z) {
        FingerprintEnrollSidecar fingerprintEnrollSidecar = this.mSidecar;
        if (fingerprintEnrollSidecar != null) {
            if (z && fingerprintEnrollSidecar.cancelEnrollment()) {
                return;
            }
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
            startActivityForResult(getFingerprintEnrollingIntent(), 5);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4) {
            if (i2 == -1 && intent != null) {
                throw new IllegalStateException("Pretty sure this is dead code");
            }
            finish();
        } else if (i == 5) {
            setResult(i2);
            finish();
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }
}
