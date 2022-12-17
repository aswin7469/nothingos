package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import android.media.Spatializer;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class SpatialAudioPreferenceController extends TogglePreferenceController {
    private final Spatializer mSpatializer;
    final AudioDeviceAttributes mSpeaker = new AudioDeviceAttributes(2, 2, "");

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

    public SpatialAudioPreferenceController(Context context, String str) {
        super(context, str);
        this.mSpatializer = ((AudioManager) context.getSystemService(AudioManager.class)).getSpatializer();
    }

    public int getAvailabilityStatus() {
        return this.mSpatializer.isAvailableForDevice(this.mSpeaker) ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mSpatializer.getCompatibleAudioDevices().contains(this.mSpeaker);
    }

    public boolean setChecked(boolean z) {
        if (z) {
            this.mSpatializer.addCompatibleAudioDevice(this.mSpeaker);
        } else {
            this.mSpatializer.removeCompatibleAudioDevice(this.mSpeaker);
        }
        return z == isChecked();
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_sound;
    }
}
