package com.nt.settings.glyphs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class GestureGlyphsFlipPreferenceController extends GlyphsFlipSwitchPreferenceController implements Preference.OnPreferenceClickListener {
    private static final int OFF = 0;
    private static final int ON = 1;
    private Preference preference;
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.nt.settings.glyphs.GestureGlyphsFlipPreferenceController.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            GestureGlyphsFlipPreferenceController gestureGlyphsFlipPreferenceController = GestureGlyphsFlipPreferenceController.this;
            gestureGlyphsFlipPreferenceController.initPreferenceState(gestureGlyphsFlipPreferenceController.isChecked());
        }
    };

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GestureGlyphsFlipPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.preference = findPreference;
        findPreference.setOnPreferenceClickListener(this);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        initPreferenceState(isChecked());
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, "led_effect_enable", 0) == 1;
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContentResolver, "led_effect_enable", z ? 1 : 0);
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        ContentResolver contentResolver = this.mContentResolver;
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(this.mContentObserver);
        }
    }

    @Override // com.nt.settings.glyphs.GlyphsFlipSwitchPreferenceController, com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("led_effect_enable"), false, this.mContentObserver, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPreferenceState(boolean z) {
        Preference preference = this.preference;
        if (preference == null) {
            return;
        }
        preference.setEnabled(z);
    }
}
