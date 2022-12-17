package com.nothing.settings.glyphs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.lights.Light;
import android.hardware.lights.LightState;
import android.hardware.lights.LightsManager;
import android.hardware.lights.LightsRequest;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.BrightnessSliderPreference;
import com.nothing.settings.glyphs.preference.PreviewPreference;
import com.nothing.settings.glyphs.utils.BrightnessUtils;

public class BrightnessPreferenceController extends BasePreferenceController implements BrightnessSliderPreference.OnSeekBarPreferenceChangeListener, LifecycleObserver, OnResume, OnPause {
    private static final String KEY_GLYPHS_PREVIEW = "glyphs_preview";

    /* renamed from: ON */
    private static final int f260ON = 1;
    private static final String TAG = "LedBrightness";
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            if (!BrightnessPreferenceController.this.isLedSwitchChecked()) {
                BrightnessPreferenceController.this.mPreviewPreference.closeLed();
            } else {
                BrightnessPreferenceController.this.mPreviewPreference.setLightness(BrightnessPreferenceController.this.mProgress);
            }
        }
    };
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private LightsManager mLightsManager = ((LightsManager) this.mContext.getSystemService(LightsManager.class));
    private LightsManager.LightsSession mLightsSession;
    /* access modifiers changed from: private */
    public PreviewPreference mPreviewPreference;
    /* access modifiers changed from: private */
    public int mProgress = 0;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
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

    public BrightnessPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        BrightnessSliderPreference brightnessSliderPreference = (BrightnessSliderPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreviewPreference = (PreviewPreference) preferenceScreen.findPreference(KEY_GLYPHS_PREVIEW);
        brightnessSliderPreference.setOnSeekBarChangeListener(this);
        brightnessSliderPreference.setMaxAndMin(BrightnessUtils.getLedBrightnessMin(), BrightnessUtils.getLedBrightnessMax());
        ThreadUtils.postOnBackgroundThread((Runnable) new BrightnessPreferenceController$$ExternalSyntheticLambda0(this, brightnessSliderPreference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1(BrightnessSliderPreference brightnessSliderPreference) {
        this.mProgress = getLedBrightness2Settings();
        Log.d(TAG, "mProgress:" + this.mProgress);
        ThreadUtils.postOnMainThread(new BrightnessPreferenceController$$ExternalSyntheticLambda2(this, brightnessSliderPreference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(BrightnessSliderPreference brightnessSliderPreference) {
        brightnessSliderPreference.setProgress(this.mProgress);
        if (!isLedSwitchChecked()) {
            this.mPreviewPreference.closeLed();
        } else {
            this.mPreviewPreference.setLightness(this.mProgress);
        }
    }

    public void onProgressChanged(Preference preference, int i) {
        this.mProgress = i;
        if (this.mPreviewPreference != null) {
            if (!isLedSwitchChecked()) {
                this.mPreviewPreference.setLightness(0);
                return;
            }
            this.mPreviewPreference.setLightness(i);
        }
        requestLights(i);
    }

    public void onStartTrackingTouch(Preference preference, int i) {
        Log.d(TAG, "onStartTrackingTouch progress:" + i);
        if (this.mLightsSession == null) {
            this.mLightsSession = this.mLightsManager.openSession();
        }
        requestLights(i);
    }

    public void onStopTrackingTouch(Preference preference) {
        Log.d(TAG, "onStopTrackingTouch mProgress:" + this.mProgress);
        LightsManager.LightsSession lightsSession = this.mLightsSession;
        if (lightsSession != null) {
            lightsSession.close();
        }
        this.mLightsSession = null;
        ThreadUtils.postOnBackgroundThread((Runnable) new BrightnessPreferenceController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStopTrackingTouch$2() {
        saveLedBrightness2Settings(this.mProgress);
        BrightnessUtils.writeLedBrightness(this.mProgress);
    }

    private void requestLights(int i) {
        LightsManager.LightsSession lightsSession;
        if (this.mLightsManager != null && (lightsSession = this.mLightsSession) != null) {
            lightsSession.requestLights(new LightsRequest.Builder().addLight(getLightByType(109), new LightState.Builder().setColor((i << 2) | 1).build()).build());
        }
    }

    private Light getLightByType(int i) {
        for (Light light : this.mLightsManager.getLights()) {
            if (light.getId() == i) {
                return light;
            }
        }
        return null;
    }

    private void saveLedBrightness2Settings(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "led_brightness_value", i);
    }

    private int getLedBrightness2Settings() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "led_brightness_value", 0);
    }

    public boolean isLedSwitchChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "led_effect_enable", 0) == 1;
    }

    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("led_effect_enable"), false, this.mContentObserver, -1);
    }
}
