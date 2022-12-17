package com.nothing.settings.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.nothing.experience.AppTracking;

public class WifiAssistantPreferenceController extends TogglePreferenceController {
    private static final String APPTRACKING_EVENT = "Settings_networkassistant";
    private static final String APPTRACKING_LABEL = "wifiqualityreminder_switch";
    private static final String KEY_WIFI_ASSISTANT = "wifi_assistant";
    private static final String TAG = "WifiAssistantPreferenceController";
    private static final String WIFI_ASSISTANT_STATE = "wifi_assistant_state";
    private AppTracking mAppTracker;

    public int getAvailabilityStatus() {
        return 2;
    }

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

    public WifiAssistantPreferenceController(Context context) {
        super(context, KEY_WIFI_ASSISTANT);
    }

    private boolean readWifiAssistantState() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), WIFI_ASSISTANT_STATE, 1) != 0) {
            return true;
        }
        return false;
    }

    private boolean setWifiAssistantState(int i) {
        return Settings.System.putInt(this.mContext.getContentResolver(), WIFI_ASSISTANT_STATE, i);
    }

    public boolean isChecked() {
        return readWifiAssistantState();
    }

    public boolean setChecked(boolean z) {
        setTrackingEvent(z);
        return setWifiAssistantState(z ? 1 : 0);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_network;
    }

    public void setTrackingEvent(boolean z) {
        if (this.mAppTracker == null) {
            this.mAppTracker = AppTracking.getInstance(this.mContext);
        }
        if (this.mAppTracker != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(APPTRACKING_LABEL, z ? 1 : 0);
            this.mAppTracker.logProductEvent(APPTRACKING_EVENT, bundle);
        }
    }
}
