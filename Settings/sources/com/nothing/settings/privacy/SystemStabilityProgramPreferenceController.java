package com.nothing.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;

public class SystemStabilityProgramPreferenceController extends TogglePreferenceController implements LifecycleObserver {
    private static final String KEY = "nt_system_stability_program";
    private static final String PERSIST_SERVICE_UPLOADLOG_ENABLE = "persist.service.uploadlog.enable";
    private static final String TAG = "NtSystemStabilityProg";
    private Preference mPreference;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SystemStabilityProgramPreferenceController(Context context) {
        super(context, KEY);
        updateConfig();
    }

    public boolean isChecked() {
        return SystemProperties.get(PERSIST_SERVICE_UPLOADLOG_ENABLE, "1").equals("1");
    }

    public boolean setChecked(boolean z) {
        if (z) {
            SystemProperties.set(PERSIST_SERVICE_UPLOADLOG_ENABLE, "1");
            Log.i(TAG, "onClick: startLogService");
            return true;
        }
        SystemProperties.set(PERSIST_SERVICE_UPLOADLOG_ENABLE, "0");
        Log.i(TAG, "onClick: stopLogService");
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    private void updateConfig() {
        updateState(this.mPreference);
    }
}
