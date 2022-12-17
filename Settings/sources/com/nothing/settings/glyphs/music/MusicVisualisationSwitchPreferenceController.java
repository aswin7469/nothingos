package com.nothing.settings.glyphs.music;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.nothing.settings.glyphs.utils.GlyphsSettings;

public class MusicVisualisationSwitchPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private final GlyphsSettings mGlyphsSettings;
    private Preference preference;

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

    public void onPause() {
    }

    public boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MusicVisualisationSwitchPreferenceController(Context context, String str) {
        super(context, str);
        this.mGlyphsSettings = new GlyphsSettings(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.preference = preferenceScreen.findPreference(getPreferenceKey());
    }

    private void refreshSummary() {
        this.preference.setVisible(shouldShowMusicItem());
        this.preference.setSummary(isChecked() ? R$string.switch_on_text : R$string.switch_off_text);
    }

    private boolean shouldShowMusicItem() {
        GlyphsSettings glyphsSettings = this.mGlyphsSettings;
        return glyphsSettings != null && glyphsSettings.shouldShowMusicItem();
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, "led_effect_music_enalbe", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContentResolver, "led_effect_music_enalbe", z ? 1 : 0);
    }

    public void onResume() {
        refreshSummary();
    }
}
