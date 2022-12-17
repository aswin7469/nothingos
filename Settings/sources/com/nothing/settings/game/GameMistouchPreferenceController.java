package com.nothing.settings.game;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.experience.AppTracking;

public class GameMistouchPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private AppTracking mAppTracker;

    public String getPreferenceKey() {
        return "protect_touch";
    }

    public boolean isAvailable() {
        return true;
    }

    public GameMistouchPreferenceController(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        boolean z = false;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "nt_game_mode_mistouch_prevention", 0) != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        setTrackingEvent(booleanValue ? 1 : 0);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "nt_game_mode_mistouch_prevention", booleanValue);
        return true;
    }

    public void setTrackingEvent(int i) {
        if (this.mAppTracker == null) {
            this.mAppTracker = AppTracking.getInstance(this.mContext);
        }
        if (this.mAppTracker != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("game_mistouch", i);
            this.mAppTracker.logProductEvent("gamemode", bundle);
        }
    }
}
