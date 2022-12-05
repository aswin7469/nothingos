package com.nt.settings.glyphs.pick;

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
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;
import java.io.IOException;
/* loaded from: classes2.dex */
public class GlyphsAddAlarmController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int ADD_FILE_REQUEST_CODE = 3002;
    private static final String TAG = "AddAlarmController";
    private OnChangeListener mOnChangeListener;
    private RingtoneManager mRingtoneManager;

    /* loaded from: classes2.dex */
    public interface OnChangeListener {
        void onChange();
    }

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

    public GlyphsAddAlarmController(Context context, String str) {
        super(context, str);
    }

    public GlyphsAddAlarmController(Context context, String str, OnChangeListener onChangeListener) {
        super(context, str);
        this.mOnChangeListener = onChangeListener;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == ADD_FILE_REQUEST_CODE) {
            saveRingtones(intent);
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
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

    private void saveRingtones(final Intent intent) {
        if (this.mRingtoneManager == null) {
            initRingtoneManager();
        }
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsAddAlarmController.1
            @Override // java.lang.Runnable
            public void run() {
                if (intent.getData() != null) {
                    try {
                        GlyphsAddAlarmController.this.mRingtoneManager.addCustomExternalRingtone(intent.getData(), 4);
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
                                uri = GlyphsAddAlarmController.this.mRingtoneManager.addCustomExternalRingtone(itemAt.getUri(), 4);
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                            if (uri == null) {
                                Log.d(GlyphsAddAlarmController.TAG, "FAIL Uri " + itemAt.getUri());
                            }
                        }
                    }
                }
                if (GlyphsAddAlarmController.this.mOnChangeListener != null) {
                    GlyphsAddAlarmController.this.mOnChangeListener.onChange();
                }
            }
        });
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }
}
