package com.android.settings.accessibility;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settingslib.core.AbstractPreferenceController;

public abstract class VibrationPreferenceConfig {
    private static final VibrationEffect PREVIEW_VIBRATION_EFFECT = VibrationEffect.createPredefined(0);
    private final AudioManager mAudioManager;
    protected final ContentResolver mContentResolver;
    private final int mDefaultIntensity;
    private final VibrationAttributes mPreviewVibrationAttributes;
    private final String mRingerModeSilentSummary;
    private final String mSettingKey;
    private final Vibrator mVibrator;

    public boolean isRestrictedByRingerModeSilent() {
        return false;
    }

    public static boolean isMainVibrationSwitchEnabled(ContentResolver contentResolver) {
        return Settings.System.getInt(contentResolver, "vibrate_on", 1) == 1;
    }

    public static void playVibrationPreview(Vibrator vibrator, int i) {
        vibrator.vibrate(PREVIEW_VIBRATION_EFFECT, createPreviewVibrationAttributes(i));
    }

    public VibrationPreferenceConfig(Context context, String str, int i) {
        this.mContentResolver = context.getContentResolver();
        Vibrator vibrator = (Vibrator) context.getSystemService(Vibrator.class);
        this.mVibrator = vibrator;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        this.mRingerModeSilentSummary = context.getString(R$string.accessibility_vibration_setting_disabled_for_silent_mode_summary);
        this.mSettingKey = str;
        this.mDefaultIntensity = vibrator.getDefaultVibrationIntensity(i);
        this.mPreviewVibrationAttributes = createPreviewVibrationAttributes(i);
    }

    public String getSettingKey() {
        return this.mSettingKey;
    }

    public CharSequence getSummary() {
        if (!isRestrictedByRingerModeSilent() || !isRingerModeSilent()) {
            return null;
        }
        return this.mRingerModeSilentSummary;
    }

    public boolean isPreferenceEnabled() {
        return isMainVibrationSwitchEnabled(this.mContentResolver) && (!isRestrictedByRingerModeSilent() || !isRingerModeSilent());
    }

    public int getDefaultIntensity() {
        return this.mDefaultIntensity;
    }

    public int readIntensity() {
        return Settings.System.getInt(this.mContentResolver, this.mSettingKey, this.mDefaultIntensity);
    }

    public boolean updateIntensity(int i) {
        return Settings.System.putInt(this.mContentResolver, this.mSettingKey, i);
    }

    public void playVibrationPreview() {
        this.mVibrator.vibrate(PREVIEW_VIBRATION_EFFECT, this.mPreviewVibrationAttributes);
    }

    private boolean isRingerModeSilent() {
        return this.mAudioManager.getRingerModeInternal() == 0;
    }

    private static VibrationAttributes createPreviewVibrationAttributes(int i) {
        return new VibrationAttributes.Builder().setUsage(i).setFlags(3).build();
    }

    public static final class SettingObserver extends ContentObserver {
        private static final IntentFilter INTERNAL_RINGER_MODE_CHANGED_INTENT_FILTER = new IntentFilter("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION");
        private static final Uri MAIN_SWITCH_SETTING_URI = Settings.System.getUriFor("vibrate_on");
        private Preference mPreference;
        private AbstractPreferenceController mPreferenceController;
        private final BroadcastReceiver mRingerModeChangeReceiver;
        private final Uri mUri;

        public SettingObserver(VibrationPreferenceConfig vibrationPreferenceConfig) {
            super(new Handler(true));
            this.mUri = Settings.System.getUriFor(vibrationPreferenceConfig.getSettingKey());
            if (vibrationPreferenceConfig.isRestrictedByRingerModeSilent()) {
                this.mRingerModeChangeReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        if ("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION".equals(intent.getAction())) {
                            SettingObserver.this.notifyChange();
                        }
                    }
                };
            } else {
                this.mRingerModeChangeReceiver = null;
            }
        }

        public void onChange(boolean z, Uri uri) {
            if (this.mUri.equals(uri) || MAIN_SWITCH_SETTING_URI.equals(uri)) {
                notifyChange();
            }
        }

        /* access modifiers changed from: private */
        public void notifyChange() {
            Preference preference;
            AbstractPreferenceController abstractPreferenceController = this.mPreferenceController;
            if (abstractPreferenceController != null && (preference = this.mPreference) != null) {
                abstractPreferenceController.updateState(preference);
            }
        }

        public void register(Context context) {
            BroadcastReceiver broadcastReceiver = this.mRingerModeChangeReceiver;
            if (broadcastReceiver != null) {
                context.registerReceiver(broadcastReceiver, INTERNAL_RINGER_MODE_CHANGED_INTENT_FILTER);
            }
            context.getContentResolver().registerContentObserver(this.mUri, false, this);
            context.getContentResolver().registerContentObserver(MAIN_SWITCH_SETTING_URI, false, this);
        }

        public void unregister(Context context) {
            BroadcastReceiver broadcastReceiver = this.mRingerModeChangeReceiver;
            if (broadcastReceiver != null) {
                context.unregisterReceiver(broadcastReceiver);
            }
            context.getContentResolver().unregisterContentObserver(this);
        }

        public void onDisplayPreference(AbstractPreferenceController abstractPreferenceController, Preference preference) {
            this.mPreferenceController = abstractPreferenceController;
            this.mPreference = preference;
        }
    }
}
