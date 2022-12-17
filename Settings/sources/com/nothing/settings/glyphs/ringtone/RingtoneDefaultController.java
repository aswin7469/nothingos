package com.nothing.settings.glyphs.ringtone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;

public class RingtoneDefaultController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    public static final int SELECT_RINGTONE_REQUEST_CODE = 10001;
    private static final String TAG = "RingtoneDefault";
    private Preference mPreference;

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

    public RingtoneDefaultController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
    }

    public String getDefaultRingtoneTitle() {
        try {
            Context context = this.mContext;
            return Ringtone.getTitle(context, RingtoneManager.getActualDefaultRingtoneUri(context, 1), false, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mPreference = preference;
        ThreadUtils.postOnBackgroundThread((Runnable) new RingtoneDefaultController$$ExternalSyntheticLambda1(this, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$1(Preference preference) {
        if (preference != null) {
            ThreadUtils.postOnMainThread(new RingtoneDefaultController$$ExternalSyntheticLambda0(preference, getDefaultRingtoneTitle()));
        }
    }

    private void selectRingtone() {
        Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1);
        int i = 0;
        if (actualDefaultRingtoneUri == null) {
            i = Settings.Global.getInt(this.mContext.getContentResolver(), "led_default_ringtone_sound_mode", 0);
        } else {
            String param = UrlUtil.getParam(actualDefaultRingtoneUri.toString(), "soundOnly");
            if (!TextUtils.isEmpty(param)) {
                i = Integer.parseInt(param);
            }
        }
        Context context = this.mContext;
        ResultPickHelper.startRingtoneSoundSelector((Activity) context, context.getString(R$string.nt_glyphs_select_ringtone_title), (String) null, actualDefaultRingtoneUri, 1, i, SELECT_RINGTONE_REQUEST_CODE);
    }

    public boolean onPreferenceClick(Preference preference) {
        selectRingtone();
        return true;
    }

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
