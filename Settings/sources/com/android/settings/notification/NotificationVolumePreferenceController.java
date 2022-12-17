package com.android.settings.notification;

import android.content.Context;
import android.text.TextUtils;
import com.android.settings.R$bool;
import com.android.settings.R$drawable;
import com.android.settings.Utils;

public class NotificationVolumePreferenceController extends RingVolumePreferenceController {
    private static final String KEY_NOTIFICATION_VOLUME = "notification_volume";

    public int getAudioStream() {
        return 5;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public String getPreferenceKey() {
        return KEY_NOTIFICATION_VOLUME;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public NotificationVolumePreferenceController(Context context) {
        super(context, KEY_NOTIFICATION_VOLUME);
    }

    public int getAvailabilityStatus() {
        return (!this.mContext.getResources().getBoolean(R$bool.config_show_notification_volume) || Utils.isVoiceCapable(this.mContext) || this.mHelper.isSingleVolume()) ? 3 : 0;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), KEY_NOTIFICATION_VOLUME);
    }

    public int getMuteIcon() {
        return R$drawable.ic_notifications_off_24dp;
    }
}
