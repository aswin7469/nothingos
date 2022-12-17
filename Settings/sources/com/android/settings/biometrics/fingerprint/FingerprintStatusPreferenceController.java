package com.android.settings.biometrics.fingerprint;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.fingerprint.FingerprintManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricStatusPreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedPreference;

public class FingerprintStatusPreferenceController extends BiometricStatusPreferenceController implements LifecycleObserver {
    private static final String KEY_FINGERPRINT_SETTINGS = "fingerprint_settings";
    protected final FingerprintManager mFingerprintManager;
    private final FingerprintStatusUtils mFingerprintStatusUtils;
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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public FingerprintStatusPreferenceController(Context context) {
        this(context, KEY_FINGERPRINT_SETTINGS);
    }

    public FingerprintStatusPreferenceController(Context context, String str) {
        this(context, str, (Lifecycle) null);
    }

    public FingerprintStatusPreferenceController(Context context, Lifecycle lifecycle) {
        this(context, KEY_FINGERPRINT_SETTINGS, lifecycle);
    }

    public FingerprintStatusPreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str);
        FingerprintManager fingerprintManagerOrNull = Utils.getFingerprintManagerOrNull(context);
        this.mFingerprintManager = fingerprintManagerOrNull;
        this.mFingerprintStatusUtils = new FingerprintStatusUtils(context, fingerprintManagerOrNull, getUserId());
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

    /* access modifiers changed from: protected */
    public boolean isDeviceSupported() {
        return this.mFingerprintStatusUtils.isAvailable();
    }

    /* access modifiers changed from: protected */
    public boolean hasEnrolledBiometrics() {
        return this.mFingerprintManager.hasEnrolledFingerprints(getUserId());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updateStateInternal();
    }

    private void updateStateInternal() {
        updateStateInternal(this.mFingerprintStatusUtils.getDisablingAdmin());
    }

    /* access modifiers changed from: protected */
    public String getSettingsClassName() {
        return this.mFingerprintStatusUtils.getSettingsClassName();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void updateStateInternal(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        RestrictedPreference restrictedPreference = this.mPreference;
        if (restrictedPreference != null) {
            restrictedPreference.setDisabledByAdmin(enforcedAdmin);
        }
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextEnrolled() {
        int size = this.mFingerprintManager.getEnrolledFingerprints(getUserId()).size();
        return this.mContext.getResources().getQuantityString(R$plurals.security_settings_fingerprint_preference_summary, size, new Object[]{Integer.valueOf(size)});
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextNoneEnrolled() {
        return this.mContext.getString(R$string.security_settings_fingerprint_preference_summary_none);
    }

    /* access modifiers changed from: protected */
    public String getEnrollClassName() {
        return FingerprintEnrollIntroduction.class.getName();
    }
}
