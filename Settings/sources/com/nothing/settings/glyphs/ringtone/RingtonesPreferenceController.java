package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.content.Intent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.notification.RingtonePreferenceControllerBase;
import com.nothing.settings.glyphs.utils.GlyphsSettings;

public class RingtonesPreferenceController extends RingtonePreferenceControllerBase implements Preference.OnPreferenceClickListener {
    public String getPreferenceKey() {
        return "key_glyphs_ringtones";
    }

    public int getRingtoneType() {
        return 1;
    }

    public RingtonesPreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        return Utils.isVoiceCapable(this.mContext);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey()).setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference) {
        if (!new GlyphsSettings(this.mContext).isShowGuideSettings()) {
            return false;
        }
        startGuideSettingsActivity();
        return true;
    }

    private void startGuideSettingsActivity() {
        this.mContext.startActivity(new Intent("android.settings.ACTION_GUIDE_SETTINGS"));
    }
}
