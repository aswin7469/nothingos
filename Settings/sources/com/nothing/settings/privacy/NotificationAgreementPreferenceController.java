package com.nothing.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.core.TogglePreferenceController;

public class NotificationAgreementPreferenceController extends TogglePreferenceController {
    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NotificationAgreementPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "nt_system_notification", 1) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.System.putInt(this.mContext.getContentResolver(), "nt_system_notification", z ? 1 : 0);
    }
}
