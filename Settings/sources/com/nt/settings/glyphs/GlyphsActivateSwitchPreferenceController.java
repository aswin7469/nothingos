package com.nt.settings.glyphs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
/* loaded from: classes2.dex */
public class GlyphsActivateSwitchPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause, OnMainSwitchChangeListener {
    private static final String KEY_BED_TIME_SCHEDULE = "key_scheduling_category";
    private static final String KEY_GLYPHS_ACTIVATE_SWITCH = "glyphs_activate_switch";
    private static final String KEY_GLYPHS_BRIGHTNESS = "glyphs_brightness";
    private static final String KEY_GLYPHS_BRIGHTNESS_TIP = "key_glyphs_brightness_tip";
    private static final String KEY_GLYPHS_SETTINGS_FOOTER = "glyphs_settings_footer";
    private static final String KEY_KEY_GLYPHS_NOTIFICATION_SETTING = "key_glyphs_notification_setting";
    private static final String KEY_KEY_GLYPHS_RINGTONES = "key_glyphs_ringtones";
    private static final int OFF = 0;
    private static final int ON = 1;
    private SettingsMainSwitchPreference mPreference;
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.nt.settings.glyphs.GlyphsActivateSwitchPreferenceController.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            GlyphsActivateSwitchPreferenceController.this.refresh();
            GlyphsActivateSwitchPreferenceController glyphsActivateSwitchPreferenceController = GlyphsActivateSwitchPreferenceController.this;
            glyphsActivateSwitchPreferenceController.initAllPreferenceState(glyphsActivateSwitchPreferenceController.isChecked());
        }
    };

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

    public GlyphsActivateSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAllPreferenceState(boolean z) {
        PreferenceScreen preferenceScreen = this.mPreference.getPreferenceManager().getPreferenceScreen();
        int preferenceCount = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!TextUtils.equals(KEY_GLYPHS_ACTIVATE_SWITCH, preference.getKey()) && !TextUtils.equals(KEY_GLYPHS_BRIGHTNESS, preference.getKey()) && !TextUtils.equals(KEY_KEY_GLYPHS_RINGTONES, preference.getKey()) && !TextUtils.equals(KEY_GLYPHS_BRIGHTNESS_TIP, preference.getKey()) && !TextUtils.equals(KEY_KEY_GLYPHS_NOTIFICATION_SETTING, preference.getKey()) && !TextUtils.equals(KEY_BED_TIME_SCHEDULE, preference.getKey()) && !TextUtils.equals(KEY_GLYPHS_SETTINGS_FOOTER, preference.getKey())) {
                preference.setEnabled(z);
            }
        }
        preferenceScreen.findPreference(KEY_GLYPHS_BRIGHTNESS_TIP).setVisible(!z);
        preferenceScreen.findPreference(KEY_GLYPHS_BRIGHTNESS).setVisible(z);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = settingsMainSwitchPreference;
        settingsMainSwitchPreference.addOnSwitchChangeListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refresh() {
        SettingsMainSwitchPreference settingsMainSwitchPreference = this.mPreference;
        if (settingsMainSwitchPreference == null) {
            return;
        }
        settingsMainSwitchPreference.setChecked(isChecked());
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, "led_effect_enable", 0) == 1;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContentResolver, "led_effect_enable", z ? 1 : 0);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("led_effect_enable"), false, this.mContentObserver, -1);
        initAllPreferenceState(isChecked());
    }

    @Override // com.android.settingslib.widget.OnMainSwitchChangeListener
    public void onSwitchChanged(Switch r1, boolean z) {
        setChecked(z);
    }
}
