package com.android.settings.biometrics.face;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.face.FaceManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$string;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricStatusPreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedPreference;
import com.nothing.settings.face.FaceRecognitionActivity;
import com.nothing.settings.face.FaceUtils;

public class FaceStatusPreferenceController extends BiometricStatusPreferenceController implements LifecycleObserver {
    public static final String KEY_FACE_SETTINGS = "face_settings";
    protected final FaceManager mFaceManager;
    private final FaceStatusUtils mFaceStatusUtils;
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
    public boolean isDeviceSupported() {
        return true;
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

    public FaceStatusPreferenceController(Context context) {
        this(context, KEY_FACE_SETTINGS, (Lifecycle) null);
    }

    public FaceStatusPreferenceController(Context context, String str) {
        this(context, str, (Lifecycle) null);
    }

    public FaceStatusPreferenceController(Context context, Lifecycle lifecycle) {
        this(context, KEY_FACE_SETTINGS, lifecycle);
    }

    public FaceStatusPreferenceController(Context context, String str, Lifecycle lifecycle) {
        super(context, str);
        FaceManager faceManagerOrNull = Utils.getFaceManagerOrNull(context);
        this.mFaceManager = faceManagerOrNull;
        this.mFaceStatusUtils = new FaceStatusUtils(context, faceManagerOrNull, getUserId());
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
    public boolean hasEnrolledBiometrics() {
        return FaceUtils.isFaceDataExist(this.mContext);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updateStateInternal();
    }

    private void updateStateInternal() {
        updateStateInternal(this.mFaceStatusUtils.getDisablingAdmin());
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
        return this.mContext.getResources().getString(R$string.security_settings_face_preference_summary);
    }

    /* access modifiers changed from: protected */
    public String getSummaryTextNoneEnrolled() {
        return this.mContext.getResources().getString(R$string.nt_security_settings_face_preference_summary_none);
    }

    /* access modifiers changed from: protected */
    public String getSettingsClassName() {
        return Settings.FaceSettingsActivity.class.getName();
    }

    /* access modifiers changed from: protected */
    public String getEnrollClassName() {
        return FaceRecognitionActivity.class.getName();
    }
}
