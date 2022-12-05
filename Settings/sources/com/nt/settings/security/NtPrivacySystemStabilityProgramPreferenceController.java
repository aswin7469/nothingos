package com.nt.settings.security;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class NtPrivacySystemStabilityProgramPreferenceController extends TogglePreferenceController implements LifecycleObserver {
    private static final String KEY = "nt_system_stability_program";
    private static final String PERSIST_SERVICE_UPLOADLOG_ENABLE = "persist.service.uploadlog.enable";
    private static final String TAG = "NtSystemStabilityProg";
    private Preference mPreference;

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtPrivacySystemStabilityProgramPreferenceController(Context context) {
        super(context, KEY);
        updateConfig();
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return SystemProperties.get(PERSIST_SERVICE_UPLOADLOG_ENABLE, "1").equals("1");
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.nothing.logtool", "com.nothing.logtool.LogService"));
        if (z) {
            this.mContext.startService(intent);
            SystemProperties.set(PERSIST_SERVICE_UPLOADLOG_ENABLE, "1");
            Log.i(TAG, "onClick: startLogService");
            return true;
        }
        this.mContext.stopService(intent);
        SystemProperties.set(PERSIST_SERVICE_UPLOADLOG_ENABLE, "0");
        Log.i(TAG, "onClick: stopLogService");
        return true;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    private void updateConfig() {
        updateState(this.mPreference);
    }
}
