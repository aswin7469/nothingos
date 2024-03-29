package com.android.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.utils.SensorPrivacyManagerHelper;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import java.util.concurrent.Executor;

public abstract class SensorToggleController extends TogglePreferenceController {
    private final Executor mCallbackExecutor;
    protected final SensorPrivacyManagerHelper mSensorPrivacyManagerHelper;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public String getRestriction() {
        return null;
    }

    public abstract int getSensor();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SensorToggleController(Context context, String str) {
        super(context, str);
        this.mSensorPrivacyManagerHelper = SensorPrivacyManagerHelper.getInstance(context);
        this.mCallbackExecutor = context.getMainExecutor();
    }

    public boolean isChecked() {
        return !this.mSensorPrivacyManagerHelper.isSensorBlocked(getSensor());
    }

    public boolean setChecked(boolean z) {
        this.mSensorPrivacyManagerHelper.setSensorBlockedForProfileGroup(2, getSensor(), !z);
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        if (restrictedSwitchPreference != null) {
            restrictedSwitchPreference.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, getRestriction(), this.mContext.getUserId()));
        }
        this.mSensorPrivacyManagerHelper.addSensorBlockedListener(getSensor(), new SensorToggleController$$ExternalSyntheticLambda0(this, preferenceScreen), this.mCallbackExecutor);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(PreferenceScreen preferenceScreen, int i, boolean z) {
        updateState(preferenceScreen.findPreference(this.mPreferenceKey));
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_privacy;
    }
}
