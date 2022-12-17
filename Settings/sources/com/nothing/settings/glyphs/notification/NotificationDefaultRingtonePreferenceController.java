package com.nothing.settings.glyphs.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.utils.ResultPickHelper;

public class NotificationDefaultRingtonePreferenceController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1004;

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

    public NotificationDefaultRingtonePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        String preferenceKey = getPreferenceKey();
        if (!TextUtils.isEmpty(preferenceKey)) {
            updateState(preferenceScreen.findPreference(preferenceKey));
        }
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            ThreadUtils.postOnBackgroundThread((Runnable) new C2023x7b8d837f(this, preference));
            preference.setOnPreferenceClickListener(this);
        }
    }

    /* renamed from: updateStateInner */
    public void lambda$updateState$0(Preference preference) {
        try {
            Context context = this.mContext;
            String title = Ringtone.getTitle(context, RingtoneManager.getActualDefaultRingtoneUri(context, 2), false, true);
            if (title != null) {
                ThreadUtils.postOnMainThread(new C2022x7b8d837e(preference, title));
            }
        } catch (IllegalArgumentException unused) {
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        Context context = this.mContext;
        ResultPickHelper.startRingtoneSoundSelector((Activity) context, context.getString(R$string.nt_glyphs_default_notifications_title), "", RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2), 2, Settings.Global.getInt(this.mContext.getContentResolver(), "led_notification_mode", 0), SELECT_RINGTONE_REQUEST_CODE);
        return true;
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (SELECT_RINGTONE_REQUEST_CODE == i && i2 == -1) {
            RingtoneManager.setActualDefaultRingtoneUri(this.mContext, 2, (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI"));
            Settings.Global.putInt(this.mContext.getContentResolver(), "led_notification_mode", intent.getIntExtra("key_sound_only", 0));
        }
    }
}
