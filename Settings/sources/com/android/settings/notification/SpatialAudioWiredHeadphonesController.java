package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import android.media.Spatializer;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class SpatialAudioWiredHeadphonesController extends TogglePreferenceController {
    private final Spatializer mSpatializer;
    final AudioDeviceAttributes mWiredHeadphones = new AudioDeviceAttributes(2, 4, "");

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SpatialAudioWiredHeadphonesController(Context context, String str) {
        super(context, str);
        this.mSpatializer = ((AudioManager) context.getSystemService(AudioManager.class)).getSpatializer();
    }

    public int getAvailabilityStatus() {
        return this.mSpatializer.isAvailableForDevice(this.mWiredHeadphones) ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mSpatializer.getCompatibleAudioDevices().contains(this.mWiredHeadphones);
    }

    public boolean setChecked(boolean z) {
        if (z) {
            this.mSpatializer.addCompatibleAudioDevice(this.mWiredHeadphones);
        } else {
            this.mSpatializer.removeCompatibleAudioDevice(this.mWiredHeadphones);
        }
        return z == isChecked();
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_sound;
    }
}
