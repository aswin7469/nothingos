package com.android.settings.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class CellularFallbackPreferenceController extends TogglePreferenceController {
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

    public CellularFallbackPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return avoidBadWifiConfig() ? 3 : 0;
    }

    public boolean isChecked() {
        return avoidBadWifiCurrentSettings();
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putString(this.mContext.getContentResolver(), "network_avoid_bad_wifi", z ? "1" : null);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_network;
    }

    private boolean avoidBadWifiConfig() {
        int activeDataSubscriptionId = getActiveDataSubscriptionId();
        if (activeDataSubscriptionId == -1 || getResourcesForSubId(activeDataSubscriptionId).getInteger(17694884) == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public int getActiveDataSubscriptionId() {
        return SubscriptionManager.getActiveDataSubscriptionId();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Resources getResourcesForSubId(int i) {
        return SubscriptionManager.getResourcesForSubId(this.mContext, i);
    }

    private boolean avoidBadWifiCurrentSettings() {
        return "1".equals(Settings.Global.getString(this.mContext.getContentResolver(), "network_avoid_bad_wifi"));
    }
}
