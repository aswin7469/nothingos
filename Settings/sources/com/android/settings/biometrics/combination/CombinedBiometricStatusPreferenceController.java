package com.android.settings.biometrics.combination;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricStatusPreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedPreference;

public class CombinedBiometricStatusPreferenceController extends BiometricStatusPreferenceController implements LifecycleObserver {
    private static final String KEY_BIOMETRIC_SETTINGS = "biometric_settings";
    protected final CombinedBiometricStatusUtils mCombinedBiometricStatusUtils;
    FaceManager mFaceManager;
    FingerprintManager mFingerprintManager;
    @VisibleForTesting
    RestrictedPreference mPreference;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    /* access modifiers changed from: protected */
    public boolean hasEnrolledBiometrics() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isDeviceSupported() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public CombinedBiometricStatusPreferenceController(Context context) {
        this(context, KEY_BIOMETRIC_SETTINGS, (Lifecycle) null);
    }

    public CombinedBiometricStatusPreferenceController(Context context, String str) {
        this(context, str, (Lifecycle) null);
    }

    public CombinedBiometricStatusPreferenceController(Context context, Lifecycle lifecycle) {
        this(context, KEY_BIOMETRIC_SETTINGS, lifecycle);
    }

    public CombinedBiometricStatusPreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str);
        Log.i("Security", "CombinedBiometricStatusPreferenceController::CombinedBiometricStatusPreferenceController", new RuntimeException("key:" + str));
        this.mCombinedBiometricStatusUtils = new CombinedBiometricStatusUtils(context, getUserId());
        this.mFingerprintManager = Utils.getFingerprintManagerOrNull(context);
        this.mFaceManager = Utils.getFaceManagerOrNull(context);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        updateStateInternal();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedPreference) preferenceScreen.findPreference(this.mPreferenceKey);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updateStateInternal();
    }

    private void updateStateInternal() {
        updateStateInternal(this.mCombinedBiometricStatusUtils.getDisablingAdmin());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void updateStateInternal(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        RestrictedPreference restrictedPreference;
        if (enforcedAdmin != null && (restrictedPreference = this.mPreference) != null) {
            restrictedPreference.setDisabledByAdmin(enforcedAdmin);
        }
    }

    /* access modifiers changed from: protected */
    public String getSettingsClassName() {
        return this.mCombinedBiometricStatusUtils.getSettingsClassName();
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextEnrolled() {
        return this.mContext.getString(R$string.security_settings_biometric_preference_summary_none_enrolled);
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextNoneEnrolled() {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        int size = fingerprintManager != null ? fingerprintManager.getEnrolledFingerprints(getUserId()).size() : 0;
        FaceManager faceManager = this.mFaceManager;
        boolean z = faceManager != null && faceManager.hasEnrolledTemplates(getUserId());
        if (z && size > 1) {
            return this.mContext.getString(R$string.security_settings_biometric_preference_summary_both_fp_multiple);
        }
        if (z && size == 1) {
            return this.mContext.getString(R$string.security_settings_biometric_preference_summary_both_fp_single);
        }
        if (z) {
            return this.mContext.getString(R$string.security_settings_face_preference_summary);
        }
        if (size <= 0) {
            return this.mContext.getString(R$string.security_settings_biometric_preference_summary_none_enrolled);
        }
        return this.mContext.getResources().getQuantityString(R$plurals.security_settings_fingerprint_preference_summary, size, new Object[]{Integer.valueOf(size)});
    }

    /* access modifiers changed from: protected */
    public String getEnrollClassName() {
        return Settings.CombinedBiometricSettingsActivity.class.getName();
    }
}
