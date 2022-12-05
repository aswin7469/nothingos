package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public abstract class VibrationIntensityPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final String mEnabledKey;
    private Preference mPreference;
    private final String mSettingKey;
    private final SettingObserver mSettingsContentObserver;
    private final boolean mSupportRampingRinger;
    protected final Vibrator mVibrator;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    protected abstract int getDefaultIntensity();

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrationIntensityPreferenceController(Context context, String str, String str2, String str3, boolean z) {
        super(context, str);
        this.mVibrator = (Vibrator) this.mContext.getSystemService(Vibrator.class);
        this.mSettingKey = str2;
        this.mEnabledKey = str3;
        this.mSupportRampingRinger = z;
        this.mSettingsContentObserver = new SettingObserver(str2) { // from class: com.android.settings.accessibility.VibrationIntensityPreferenceController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z2, Uri uri) {
                VibrationIntensityPreferenceController vibrationIntensityPreferenceController = VibrationIntensityPreferenceController.this;
                vibrationIntensityPreferenceController.updateState(vibrationIntensityPreferenceController.mPreference);
            }
        };
    }

    public VibrationIntensityPreferenceController(Context context, String str, String str2, String str3) {
        this(context, str, str2, str3, false);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        SettingObserver settingObserver = this.mSettingsContentObserver;
        contentResolver.registerContentObserver(settingObserver.uri, false, settingObserver);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsContentObserver);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        int i = Settings.System.getInt(this.mContext.getContentResolver(), this.mSettingKey, getDefaultIntensity());
        boolean z = true;
        if (Settings.System.getInt(this.mContext.getContentResolver(), this.mEnabledKey, 1) != 1 && (!this.mSupportRampingRinger || !AccessibilitySettings.isRampingRingerEnabled(this.mContext))) {
            z = false;
        }
        Context context = this.mContext;
        if (!z) {
            i = 0;
        }
        return getIntensityString(context, i);
    }

    public static CharSequence getIntensityString(Context context, int i) {
        if (!context.getResources().getBoolean(R.bool.config_vibration_supports_multiple_intensities)) {
            if (i == 0) {
                return context.getString(R.string.switch_off_text);
            }
            return context.getString(R.string.switch_on_text);
        } else if (i == 0) {
            return context.getString(R.string.accessibility_vibration_intensity_off);
        } else {
            if (i == 1) {
                return context.getString(R.string.accessibility_vibration_intensity_low);
            }
            if (i == 2) {
                return context.getString(R.string.accessibility_vibration_intensity_medium);
            }
            return i != 3 ? "" : context.getString(R.string.accessibility_vibration_intensity_high);
        }
    }

    /* loaded from: classes.dex */
    private static class SettingObserver extends ContentObserver {
        public final Uri uri;

        public SettingObserver(String str) {
            super(new Handler(Looper.getMainLooper()));
            this.uri = Settings.System.getUriFor(str);
        }
    }
}
