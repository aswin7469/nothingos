package com.nt.settings.glyphs;

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
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.pick.GlyphsPickResultHelper;
/* loaded from: classes2.dex */
public class GlyphsNotificationSettingPreferenceController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final String KEY_PHONE_RINGTONE = "key_glyphs_notification_setting";
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1004;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_PHONE_RINGTONE;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsNotificationSettingPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        String preferenceKey = getPreferenceKey();
        if (TextUtils.isEmpty(preferenceKey)) {
            return;
        }
        updateState(preferenceScreen.findPreference(preferenceKey));
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(final Preference preference) {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsNotificationSettingPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsNotificationSettingPreferenceController.this.lambda$updateState$0(preference);
            }
        });
        preference.setOnPreferenceClickListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateSummary */
    public void lambda$updateState$0(final Preference preference) {
        try {
            final String title = Ringtone.getTitle(this.mContext, RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2), false, true);
            if (title == null) {
                return;
            }
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsNotificationSettingPreferenceController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Preference.this.setSummary(title);
                }
            });
        } catch (IllegalArgumentException unused) {
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        Context context = this.mContext;
        GlyphsPickResultHelper.startRingtoneSoundSelector((Activity) context, context.getString(R.string.nt_glyphs_default_notifications_title), "", RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2), 2, Settings.Global.getInt(this.mContext.getContentResolver(), "led_notification_mode", 0), SELECT_RINGTONE_REQUEST_CODE);
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (SELECT_RINGTONE_REQUEST_CODE == i && i2 == -1) {
            RingtoneManager.setActualDefaultRingtoneUri(this.mContext, 2, (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI"));
            Settings.Global.putInt(this.mContext.getContentResolver(), "led_notification_mode", intent.getIntExtra("key_sound_only", 0));
        }
    }
}
