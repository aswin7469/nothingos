package com.android.settings.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class BubbleSummaryNotificationPreferenceController extends BasePreferenceController {

    /* renamed from: ON */
    static final int f209ON = 1;

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

    public BubbleSummaryNotificationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        int i;
        Context context = this.mContext;
        if (areBubblesEnabled()) {
            i = R$string.notifications_bubble_setting_on_summary;
        } else {
            i = R$string.switch_off_text;
        }
        return context.getString(i);
    }

    public int getAvailabilityStatus() {
        return ((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).isLowRamDevice() ? 3 : 0;
    }

    private boolean areBubblesEnabled() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_bubbles", 1) == 1;
    }
}
