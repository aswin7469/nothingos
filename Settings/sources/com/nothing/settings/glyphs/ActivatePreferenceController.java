package com.nothing.settings.glyphs;

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
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.nothing.settings.utils.NtUtils;

public class ActivatePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause, OnMainSwitchChangeListener {
    private static final String KEY_BED_TIME_SCHEDULE = "key_scheduling_category";
    private static final String KEY_GLYPHS_ACTIVATE_SWITCH = "glyphs_activate_switch";
    private static final String KEY_GLYPHS_BRIGHTNESS = "glyphs_brightness";
    private static final String KEY_GLYPHS_BRIGHTNESS_TIP = "key_glyphs_brightness_tip";
    private static final String KEY_GLYPHS_SETTINGS_FOOTER = "glyphs_settings_footer";
    private static final String KEY_KEY_GLYPHS_NOTIFICATION_SETTING = "key_glyphs_notification_setting";
    private static final String KEY_KEY_GLYPHS_RINGTONES = "key_glyphs_ringtones";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f259ON = 1;
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            ActivatePreferenceController.this.refresh();
            ActivatePreferenceController activatePreferenceController = ActivatePreferenceController.this;
            activatePreferenceController.initAllPreferenceState(activatePreferenceController.isChecked());
        }
    };
    private final ContentResolver mContentResolver;
    private SettingsMainSwitchPreference mPreference;

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

    public ActivatePreferenceController(Context context, String str) {
        super(context, str);
        this.mContentResolver = context.getContentResolver();
    }

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

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = settingsMainSwitchPreference;
        settingsMainSwitchPreference.addOnSwitchChangeListener(this);
    }

    public void refresh() {
        SettingsMainSwitchPreference settingsMainSwitchPreference = this.mPreference;
        if (settingsMainSwitchPreference != null) {
            settingsMainSwitchPreference.setChecked(isChecked());
        }
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, "led_effect_enable", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        NtUtils.trackIntGlyph(this.mContext, "glyph", z ? 1 : 0);
        return Settings.Global.putInt(this.mContentResolver, "led_effect_enable", z);
    }

    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("led_effect_enable"), false, this.mContentObserver, -1);
        initAllPreferenceState(isChecked());
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        setChecked(z);
    }
}
