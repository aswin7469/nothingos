package com.nt.settings.glyphs.ringtone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.pick.GlyphsPickResultHelper;
/* loaded from: classes2.dex */
public class GlyphsRingtoneDefaultController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    public static final int SELECT_RINGTONE_REQUEST_CODE = 10001;
    private static final String TAG = "RingtoneDefault";
    private Preference mPreference;

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

    public GlyphsRingtoneDefaultController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDefaultRingtoneTitle() {
        try {
            return Ringtone.getTitle(this.mContext, RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1), false, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(final Preference preference) {
        super.updateState(preference);
        this.mPreference = preference;
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsRingtoneDefaultController.1
            @Override // java.lang.Runnable
            public void run() {
                if (preference == null) {
                    return;
                }
                final String defaultRingtoneTitle = GlyphsRingtoneDefaultController.this.getDefaultRingtoneTitle();
                ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsRingtoneDefaultController.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        preference.setSummary(defaultRingtoneTitle);
                    }
                });
            }
        });
    }

    private void selectRingtone() {
        Context context = this.mContext;
        GlyphsPickResultHelper.startRingtoneSoundSelector((Activity) context, context.getString(R.string.nt_glyphs_select_ringtone_title), null, RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1), 1, Settings.Global.getInt(this.mContext.getContentResolver(), "led_default_ringtone_sound_mode", 0), SELECT_RINGTONE_REQUEST_CODE);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        selectRingtone();
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i2 == -1 && i == 10001 && intent != null) {
            Log.d(TAG, intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI") + "");
            Log.d(TAG, intent.getIntExtra("key_sound_only", -1) + "");
            saveRingtone((Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI"), intent.getIntExtra("key_sound_only", -1));
            updateState(this.mPreference);
        }
    }

    private void saveRingtone(Uri uri, int i) {
        RingtoneManager.setActualDefaultRingtoneUri(this.mContext, 1, uri);
        Settings.Global.putInt(this.mContext.getContentResolver(), "led_default_ringtone_sound_mode", i);
    }
}
