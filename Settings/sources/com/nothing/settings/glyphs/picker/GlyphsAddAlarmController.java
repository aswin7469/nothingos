package com.nothing.settings.glyphs.picker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import java.io.IOException;

public class GlyphsAddAlarmController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int ADD_FILE_REQUEST_CODE = 3002;
    private static final String TAG = "AddAlarmController";
    private OnChangeListener mOnChangeListener;
    private RingtoneManager mRingtoneManager;

    public interface OnChangeListener {
        void onChange();
    }

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

    public GlyphsAddAlarmController(Context context, String str) {
        super(context, str);
    }

    public GlyphsAddAlarmController(Context context, String str, OnChangeListener onChangeListener) {
        super(context, str);
        this.mOnChangeListener = onChangeListener;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == ADD_FILE_REQUEST_CODE) {
            saveRingtones(intent);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        startAddSoundActivity();
        return true;
    }

    private void startAddSoundActivity() {
        ((Activity) this.mContext).startActivityForResult(getMediaFilePickerIntent(), ADD_FILE_REQUEST_CODE);
    }

    @SuppressLint({"InlinedApi"})
    private Intent getMediaFilePickerIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("audio/*");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"audio/*", "application/ogg"});
        return intent;
    }

    private void initRingtoneManager() {
        RingtoneManager ringtoneManager = new RingtoneManager(this.mContext, true);
        this.mRingtoneManager = ringtoneManager;
        ringtoneManager.setType(4);
    }

    private void saveRingtones(Intent intent) {
        if (this.mRingtoneManager == null) {
            initRingtoneManager();
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new GlyphsAddAlarmController$$ExternalSyntheticLambda0(this, intent));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$saveRingtones$0(Intent intent) {
        if (intent.getData() != null) {
            try {
                this.mRingtoneManager.addCustomExternalRingtone(intent.getData(), 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item itemAt = clipData.getItemAt(i);
                    Uri uri = null;
                    try {
                        uri = this.mRingtoneManager.addCustomExternalRingtone(itemAt.getUri(), 4);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    if (uri == null) {
                        Log.d(TAG, "FAIL Uri " + itemAt.getUri());
                    }
                }
            }
        }
        OnChangeListener onChangeListener = this.mOnChangeListener;
        if (onChangeListener != null) {
            onChangeListener.onChange();
        }
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }
}
