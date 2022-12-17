package com.nothing.settings.game;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import com.android.settings.core.BasePreferenceController;
import com.nothing.NtFeaturesUtils;

public class GameSettingsPreferenceController extends BasePreferenceController {
    private static final String KEY_GESTURES_SETTINGS = "game_settings";
    private static final String TAG = "GameSettingsPreferenceController";

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

    public GameSettingsPreferenceController(Context context, String str) {
        super(context, KEY_GESTURES_SETTINGS);
    }

    public int getAvailabilityStatus() {
        boolean isSupport = NtFeaturesUtils.isSupport(new int[]{4});
        Log.d(TAG, "isSupport is " + isSupport);
        if (isSupport) {
            return 0;
        }
        return 2;
    }
}
