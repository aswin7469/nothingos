package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class SnoozeNotificationPreferenceController extends TogglePreferenceController {
    static final int OFF = 0;

    /* renamed from: ON */
    static final int f211ON = 1;
    private static final String TAG = "SnoozeNotifPrefContr";

    public int getAvailabilityStatus() {
        return 0;
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

    public SnoozeNotificationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "show_notification_snooze", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "show_notification_snooze", z ? 1 : 0);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_notifications;
    }
}
