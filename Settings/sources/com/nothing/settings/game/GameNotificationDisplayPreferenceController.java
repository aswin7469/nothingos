package com.nothing.settings.game;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class GameNotificationDisplayPreferenceController extends BasePreferenceController {
    private static final String GAME_MODE_NOTIFICATION_DISPLAY_MODE = "nt_game_mode_notification_display_mode";
    private static final int KEY_NOTIFICATION_DISPLAY_DEFAULT_VALUE = 0;
    static final String PREF_KEY_NOTIFICATION_DISPLAY = "gm_setting_notification_display";

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

    public GameNotificationDisplayPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        boolean z = false;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), GAME_MODE_NOTIFICATION_DISPLAY_MODE, 0) == 0) {
            z = true;
        }
        return this.mContext.getString(z ? R$string.gm_setting_notification_display_default : R$string.gm_setting_notification_display_minimal);
    }
}
