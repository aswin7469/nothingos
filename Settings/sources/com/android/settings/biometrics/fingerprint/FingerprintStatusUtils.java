package com.android.settings.biometrics.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.biometrics.ParentalControlsUtils;
import com.android.settingslib.RestrictedLockUtils;

public class FingerprintStatusUtils {
    private final Context mContext;
    private final FingerprintManager mFingerprintManager;
    private final int mUserId;

    public FingerprintStatusUtils(Context context, FingerprintManager fingerprintManager, int i) {
        this.mContext = context;
        this.mFingerprintManager = fingerprintManager;
        this.mUserId = i;
    }

    public boolean isAvailable() {
        return Utils.hasFingerprintHardware(this.mContext);
    }

    public RestrictedLockUtils.EnforcedAdmin getDisablingAdmin() {
        return ParentalControlsUtils.parentConsentRequired(this.mContext, 2);
    }

    public String getSummary() {
        if (!hasEnrolled()) {
            return this.mContext.getString(R$string.security_settings_fingerprint_preference_summary_none);
        }
        int size = this.mFingerprintManager.getEnrolledFingerprints(this.mUserId).size();
        return this.mContext.getResources().getQuantityString(R$plurals.security_settings_fingerprint_preference_summary, size, new Object[]{Integer.valueOf(size)});
    }

    public String getSettingsClassName() {
        if (hasEnrolled()) {
            return FingerprintSettings.class.getName();
        }
        return FingerprintEnrollIntroductionInternal.class.getName();
    }

    public boolean hasEnrolled() {
        return this.mFingerprintManager.hasEnrolledFingerprints(this.mUserId);
    }
}
