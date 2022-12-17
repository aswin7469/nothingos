package com.android.settings.display;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class DarkUIPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    public static final int DIALOG_SEEN = 1;
    public static final String PREF_DARK_MODE_DIALOG_SEEN = "dark_mode_dialog_seen";
    private PowerManager mPowerManager;
    Preference mPreference;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            DarkUIPreferenceController.this.updateEnabledStateIfNeeded();
        }
    };
    private UiModeManager mUiModeManager;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DarkUIPreferenceController(Context context, String str) {
        super(context, str);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    public boolean isChecked() {
        return (this.mContext.getResources().getConfiguration().uiMode & 32) != 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updateEnabledStateIfNeeded();
    }

    public boolean setChecked(boolean z) {
        return this.mUiModeManager.setNightModeActivated(z);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }

    /* access modifiers changed from: package-private */
    public void updateEnabledStateIfNeeded() {
        int i;
        if (this.mPreference != null) {
            boolean isPowerSaveMode = isPowerSaveMode();
            this.mPreference.setEnabled(!isPowerSaveMode);
            if (isPowerSaveMode) {
                if (isChecked()) {
                    i = R$string.dark_ui_mode_disabled_summary_dark_theme_on;
                } else {
                    i = R$string.dark_ui_mode_disabled_summary_dark_theme_off;
                }
                this.mPreference.setSummary((CharSequence) this.mContext.getString(i));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isPowerSaveMode() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public void onStart() {
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
    }

    public void onStop() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }
}
