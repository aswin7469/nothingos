package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.Spatializer;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class SpatialAudioParentPreferenceController extends BasePreferenceController {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "SpatialAudioSetting";
    private SpatialAudioPreferenceController mSpatialAudioPreferenceController;
    private SpatialAudioWiredHeadphonesController mSpatialAudioWiredHeadphonesController;
    private final Spatializer mSpatializer;

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

    public SpatialAudioParentPreferenceController(Context context, String str) {
        super(context, str);
        this.mSpatializer = ((AudioManager) context.getSystemService(AudioManager.class)).getSpatializer();
        this.mSpatialAudioPreferenceController = new SpatialAudioPreferenceController(context, "unused");
        this.mSpatialAudioWiredHeadphonesController = new SpatialAudioWiredHeadphonesController(context, "unused");
    }

    public int getAvailabilityStatus() {
        int immersiveAudioLevel = this.mSpatializer.getImmersiveAudioLevel();
        if (DEBUG) {
            Log.d(TAG, "spatialization level: " + immersiveAudioLevel);
        }
        return immersiveAudioLevel == 0 ? 3 : 0;
    }

    public CharSequence getSummary() {
        boolean z = this.mSpatialAudioPreferenceController.isAvailable() && this.mSpatialAudioPreferenceController.isChecked();
        boolean z2 = this.mSpatialAudioWiredHeadphonesController.isAvailable() && this.mSpatialAudioWiredHeadphonesController.isChecked();
        if (z && z2) {
            Context context = this.mContext;
            return context.getString(R$string.spatial_summary_on_two, new Object[]{context.getString(R$string.spatial_audio_speaker), this.mContext.getString(R$string.spatial_audio_wired_headphones)});
        } else if (z) {
            Context context2 = this.mContext;
            return context2.getString(R$string.spatial_summary_on_one, new Object[]{context2.getString(R$string.spatial_audio_speaker)});
        } else if (!z2) {
            return this.mContext.getString(R$string.spatial_summary_off);
        } else {
            Context context3 = this.mContext;
            return context3.getString(R$string.spatial_summary_on_one, new Object[]{context3.getString(R$string.spatial_audio_wired_headphones)});
        }
    }
}
