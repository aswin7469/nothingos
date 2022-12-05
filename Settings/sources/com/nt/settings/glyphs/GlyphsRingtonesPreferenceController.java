package com.nt.settings.glyphs;

import android.content.Context;
import android.content.Intent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.notification.RingtonePreferenceControllerBase;
import com.nt.settings.glyphs.utils.GlyphsSettings;
/* loaded from: classes2.dex */
public class GlyphsRingtonesPreferenceController extends RingtonePreferenceControllerBase implements Preference.OnPreferenceClickListener {
    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "key_glyphs_ringtones";
    }

    @Override // com.android.settings.notification.RingtonePreferenceControllerBase
    public int getRingtoneType() {
        return 1;
    }

    public GlyphsRingtonesPreferenceController(Context context) {
        super(context);
    }

    @Override // com.android.settings.notification.RingtonePreferenceControllerBase, com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return Utils.isVoiceCapable(this.mContext);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey()).setOnPreferenceClickListener(this);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (new GlyphsSettings(this.mContext).isShowGuideSettings()) {
            startGuideSettingsActivity();
            return true;
        }
        return false;
    }

    private void startGuideSettingsActivity() {
        this.mContext.startActivity(new Intent("android.settings.ACTION_GUIDE_SETTINGS"));
    }
}
