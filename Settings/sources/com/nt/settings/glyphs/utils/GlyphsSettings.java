package com.nt.settings.glyphs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import com.android.settings.DBReadAsyncTask;
/* loaded from: classes2.dex */
public class GlyphsSettings {
    private Context mContext;
    private boolean mShouldShowMusicItem;
    private SharedPreferences mgGlyphsPreferences;

    public GlyphsSettings(Context context) {
        this.mContext = context;
        new DBReadAsyncTask(this.mContext).execute(new Void[0]);
        this.mgGlyphsPreferences = this.mContext.getSharedPreferences("glyphs_preferences", 0);
    }

    public boolean isShowGuideSettings() {
        return this.mgGlyphsPreferences.getBoolean("show_guide", true);
    }

    public void setShowGuideSettings(boolean z) {
        this.mgGlyphsPreferences.edit().putBoolean("show_guide", z).apply();
    }

    public boolean shouldShowMusicItem() {
        boolean z = true;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "key_first_setting_led_ringtone", 1) != 0) {
            z = false;
        }
        this.mShouldShowMusicItem = z;
        return z;
    }

    public void setShowMusicItem(boolean z) {
        if (this.mShouldShowMusicItem) {
            return;
        }
        this.mShouldShowMusicItem = z;
        Settings.Global.putInt(this.mContext.getContentResolver(), "key_first_setting_led_ringtone", !z ? 1 : 0);
    }
}
