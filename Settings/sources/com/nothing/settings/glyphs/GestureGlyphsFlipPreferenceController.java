package com.nothing.settings.glyphs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;

public class GestureGlyphsFlipPreferenceController extends GlyphsFlipSwitchPreferenceController implements Preference.OnPreferenceClickListener {
    private static final String LED_EFFECT_ENABLE = "led_effect_enable";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f261ON = 1;
    private static final String TAG = "GestureGlyphsFlipPreferenceController";
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            GestureGlyphsFlipPreferenceController gestureGlyphsFlipPreferenceController = GestureGlyphsFlipPreferenceController.this;
            gestureGlyphsFlipPreferenceController.initPreferenceState(gestureGlyphsFlipPreferenceController.isChecked());
        }
    };
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
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

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean onPreferenceClick(Preference preference2) {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GestureGlyphsFlipPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.preference = findPreference;
        findPreference.setOnPreferenceClickListener(this);
    }

    public void updateState(Preference preference2) {
        super.updateState(preference2);
        initPreferenceState(isChecked());
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContentResolver, LED_EFFECT_ENABLE, 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContentResolver, LED_EFFECT_ENABLE, z ? 1 : 0);
    }

    public void onPause() {
        ContentResolver contentResolver = this.mContentResolver;
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(this.mContentObserver);
        }
    }

    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor(LED_EFFECT_ENABLE), false, this.mContentObserver, -1);
    }

    public void initPreferenceState(boolean z) {
        Preference preference2 = this.preference;
        if (preference2 != null) {
            preference2.setEnabled(z);
        }
    }

    private void refreshSummary() {
        this.preference.setSummary(isChecked() ? R$string.switch_on_text : R$string.switch_off_text);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_sound;
    }
}
