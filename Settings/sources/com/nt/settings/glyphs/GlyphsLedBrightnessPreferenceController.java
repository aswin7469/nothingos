package com.nt.settings.glyphs;

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
import androidx.constraintlayout.widget.R$styleable;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.utils.LedBrightnessUtils;
import com.nt.settings.glyphs.widget.GlyphsBrightnessSliderPreference;
import com.nt.settings.glyphs.widget.GlyphsPreviewPreference;
/* loaded from: classes2.dex */
public class GlyphsLedBrightnessPreferenceController extends BasePreferenceController implements GlyphsBrightnessSliderPreference.OnSeekBarPreferenceChangeListener, LifecycleObserver, OnResume, OnPause {
    private static final String KEY_GLYPHS_PREVIEW = "glyphs_preview";
    private static final int ON = 1;
    private static final String TAG = "LedBrightness";
    private LightsManager.LightsSession mLightsSession;
    private GlyphsPreviewPreference mPreviewPreference;
    private int mProgress = 0;
    private LightsManager mLightsManager = (LightsManager) this.mContext.getSystemService(LightsManager.class);
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.nt.settings.glyphs.GlyphsLedBrightnessPreferenceController.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (!GlyphsLedBrightnessPreferenceController.this.isLedSwitchChecked()) {
                GlyphsLedBrightnessPreferenceController.this.mPreviewPreference.closeLed();
            } else {
                GlyphsLedBrightnessPreferenceController.this.mPreviewPreference.setLightness(GlyphsLedBrightnessPreferenceController.this.mProgress);
            }
        }
    };

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsLedBrightnessPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        final GlyphsBrightnessSliderPreference glyphsBrightnessSliderPreference = (GlyphsBrightnessSliderPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreviewPreference = (GlyphsPreviewPreference) preferenceScreen.findPreference(KEY_GLYPHS_PREVIEW);
        glyphsBrightnessSliderPreference.setOnSeekBarChangeListener(this);
        glyphsBrightnessSliderPreference.setMaxAndMin(LedBrightnessUtils.getLedBrightnessMin(), LedBrightnessUtils.getLedBrightnessMax());
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsLedBrightnessPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsLedBrightnessPreferenceController.this.lambda$displayPreference$0(glyphsBrightnessSliderPreference);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(final GlyphsBrightnessSliderPreference glyphsBrightnessSliderPreference) {
        this.mProgress = LedBrightnessUtils.readLedBrightness();
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsLedBrightnessPreferenceController.2
            @Override // java.lang.Runnable
            public void run() {
                glyphsBrightnessSliderPreference.setProgress(GlyphsLedBrightnessPreferenceController.this.mProgress);
                if (!GlyphsLedBrightnessPreferenceController.this.isLedSwitchChecked()) {
                    GlyphsLedBrightnessPreferenceController.this.mPreviewPreference.closeLed();
                } else {
                    GlyphsLedBrightnessPreferenceController.this.mPreviewPreference.setLightness(GlyphsLedBrightnessPreferenceController.this.mProgress);
                }
            }
        });
    }

    @Override // com.nt.settings.glyphs.widget.GlyphsBrightnessSliderPreference.OnSeekBarPreferenceChangeListener
    public void onProgressChanged(Preference preference, final int i) {
        this.mProgress = i;
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsLedBrightnessPreferenceController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsLedBrightnessPreferenceController.this.lambda$onProgressChanged$1(i);
            }
        });
        requestLights(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onProgressChanged$1(int i) {
        saveLedBrightness2Settings(i);
        LedBrightnessUtils.writeLedBrightness(i);
        if (this.mPreviewPreference != null) {
            if (!isLedSwitchChecked()) {
                this.mPreviewPreference.setLightness(0);
            } else {
                this.mPreviewPreference.setLightness(i);
            }
        }
    }

    @Override // com.nt.settings.glyphs.widget.GlyphsBrightnessSliderPreference.OnSeekBarPreferenceChangeListener
    public void onStartTrackingTouch(Preference preference, int i) {
        if (this.mLightsSession == null) {
            this.mLightsSession = this.mLightsManager.openSession();
        }
        requestLights(i);
    }

    @Override // com.nt.settings.glyphs.widget.GlyphsBrightnessSliderPreference.OnSeekBarPreferenceChangeListener
    public void onStopTrackingTouch(Preference preference) {
        LightsManager.LightsSession lightsSession = this.mLightsSession;
        if (lightsSession != null) {
            lightsSession.close();
        }
        this.mLightsSession = null;
    }

    private void requestLights(int i) {
        if (this.mLightsManager == null || this.mLightsSession == null) {
            return;
        }
        this.mLightsSession.requestLights(new LightsRequest.Builder().addLight(getLightByType(R$styleable.Constraint_transitionPathRotate), new LightState.Builder().setColor(1).setAllWhiteBr(i).build()).build());
    }

    private Light getLightByType(int i) {
        for (Light light : this.mLightsManager.getLights()) {
            if (light.getId() == i) {
                return light;
            }
        }
        Log.e(TAG, "getLightByType: can not get light for type " + i);
        return null;
    }

    private void saveLedBrightness2Settings(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "led_brightness_value", i);
    }

    public boolean isLedSwitchChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "led_effect_enable", 0) == 1;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("led_effect_enable"), false, this.mContentObserver, -1);
    }
}
