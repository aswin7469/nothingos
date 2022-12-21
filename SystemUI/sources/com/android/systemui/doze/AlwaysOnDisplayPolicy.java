package com.android.systemui.doze;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;

@SysUISingleton
public class AlwaysOnDisplayPolicy {
    private static final long DEFAULT_PROX_COOLDOWN_PERIOD_MS = 5000;
    private static final long DEFAULT_PROX_COOLDOWN_TRIGGER_MS = 2000;
    private static final long DEFAULT_PROX_SCREEN_OFF_DELAY_MS = 1000;
    private static final long DEFAULT_WALLPAPER_FADE_OUT_MS = 400;
    private static final long DEFAULT_WALLPAPER_VISIBILITY_MS = 60000;
    static final String KEY_DIMMING_SCRIM_ARRAY = "dimming_scrim_array";
    static final String KEY_PROX_COOLDOWN_PERIOD_MS = "prox_cooldown_period";
    static final String KEY_PROX_COOLDOWN_TRIGGER_MS = "prox_cooldown_trigger";
    static final String KEY_PROX_SCREEN_OFF_DELAY_MS = "prox_screen_off_delay";
    static final String KEY_SCREEN_BRIGHTNESS_ARRAY = "screen_brightness_array";
    static final String KEY_WALLPAPER_FADE_OUT_MS = "wallpaper_fade_out_duration";
    static final String KEY_WALLPAPER_VISIBILITY_MS = "wallpaper_visibility_timeout";
    public static final String TAG = "AlwaysOnDisplayPolicy";
    public int defaultDozeBrightness;
    public int dimBrightness;
    public int[] dimmingScrimArray;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final KeyValueListParser mParser = new KeyValueListParser(',');
    private SettingsObserver mSettingsObserver;
    public long proxCooldownPeriodMs;
    public long proxCooldownTriggerMs;
    public long proxScreenOffDelayMs;
    public int[] screenBrightnessArray;
    public long wallpaperFadeOutDuration;
    public long wallpaperVisibilityDuration;

    @Inject
    public AlwaysOnDisplayPolicy(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        SettingsObserver settingsObserver = new SettingsObserver(applicationContext.getMainThreadHandler());
        this.mSettingsObserver = settingsObserver;
        settingsObserver.observe();
    }

    private final class SettingsObserver extends ContentObserver {
        private final Uri ALWAYS_ON_DISPLAY_CONSTANTS_URI = Settings.Global.getUriFor("always_on_display_constants");

        SettingsObserver(Handler handler) {
            super(handler);
        }

        /* access modifiers changed from: package-private */
        public void observe() {
            AlwaysOnDisplayPolicy.this.mContext.getContentResolver().registerContentObserver(this.ALWAYS_ON_DISPLAY_CONSTANTS_URI, false, this, -1);
            update((Uri) null);
        }

        public void onChange(boolean z, Uri uri) {
            update(uri);
        }

        public void update(Uri uri) {
            if (uri == null || this.ALWAYS_ON_DISPLAY_CONSTANTS_URI.equals(uri)) {
                Resources resources = AlwaysOnDisplayPolicy.this.mContext.getResources();
                try {
                    AlwaysOnDisplayPolicy.this.mParser.setString(Settings.Global.getString(AlwaysOnDisplayPolicy.this.mContext.getContentResolver(), "always_on_display_constants"));
                } catch (IllegalArgumentException unused) {
                    Log.e(AlwaysOnDisplayPolicy.TAG, "Bad AOD constants");
                }
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy.proxScreenOffDelayMs = alwaysOnDisplayPolicy.mParser.getLong(AlwaysOnDisplayPolicy.KEY_PROX_SCREEN_OFF_DELAY_MS, 1000);
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy2 = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy2.proxCooldownTriggerMs = alwaysOnDisplayPolicy2.mParser.getLong(AlwaysOnDisplayPolicy.KEY_PROX_COOLDOWN_TRIGGER_MS, 2000);
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy3 = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy3.proxCooldownPeriodMs = alwaysOnDisplayPolicy3.mParser.getLong(AlwaysOnDisplayPolicy.KEY_PROX_COOLDOWN_PERIOD_MS, 5000);
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy4 = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy4.wallpaperFadeOutDuration = alwaysOnDisplayPolicy4.mParser.getLong(AlwaysOnDisplayPolicy.KEY_WALLPAPER_FADE_OUT_MS, AlwaysOnDisplayPolicy.DEFAULT_WALLPAPER_FADE_OUT_MS);
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy5 = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy5.wallpaperVisibilityDuration = alwaysOnDisplayPolicy5.mParser.getLong(AlwaysOnDisplayPolicy.KEY_WALLPAPER_VISIBILITY_MS, AlwaysOnDisplayPolicy.DEFAULT_WALLPAPER_VISIBILITY_MS);
                AlwaysOnDisplayPolicy.this.defaultDozeBrightness = resources.getInteger(17694923);
                AlwaysOnDisplayPolicy.this.dimBrightness = resources.getInteger(17694922);
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy6 = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy6.screenBrightnessArray = alwaysOnDisplayPolicy6.mParser.getIntArray(AlwaysOnDisplayPolicy.KEY_SCREEN_BRIGHTNESS_ARRAY, resources.getIntArray(C1893R.array.config_doze_brightness_sensor_to_brightness));
                AlwaysOnDisplayPolicy alwaysOnDisplayPolicy7 = AlwaysOnDisplayPolicy.this;
                alwaysOnDisplayPolicy7.dimmingScrimArray = alwaysOnDisplayPolicy7.mParser.getIntArray(AlwaysOnDisplayPolicy.KEY_DIMMING_SCRIM_ARRAY, resources.getIntArray(C1893R.array.config_doze_brightness_sensor_to_scrim_opacity));
            }
        }
    }
}
